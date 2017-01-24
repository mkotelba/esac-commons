package com.esacinc.commons.management.impl;

import com.esacinc.commons.logging.metrics.impl.AbstractMetricDataSourceProvider;
import com.esacinc.commons.management.BufferPoolMxBeanSnapshot;
import com.esacinc.commons.management.EsacManagementException;
import com.esacinc.commons.management.EsacManagementFactory;
import com.esacinc.commons.management.EsacThreadInfo;
import com.esacinc.commons.management.GcNotificationListener;
import com.esacinc.commons.management.ManagementSnapshot;
import com.esacinc.commons.management.MemoryMxBeanSnapshot;
import com.esacinc.commons.management.MemoryPoolMxBeanSnapshot;
import com.esacinc.commons.management.SystemMxBeanSnapshot;
import com.esacinc.commons.management.utils.EsacManagementUtils;
import com.esacinc.commons.utils.EsacStreamUtils;
import com.sun.management.ThreadMXBean;
import com.sun.management.UnixOperatingSystemMXBean;
import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class EsacManagementFactoryImpl extends AbstractMetricDataSourceProvider<ManagementSnapshot> implements EsacManagementFactory {
    @Autowired
    private GcNotificationListener gcNotificationListener;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReadLock readLock = this.lock.readLock();
    private final WriteLock writeLock = this.lock.writeLock();
    private MBeanServer beanServer;
    private Map<ObjectName, String> memPoolNames;
    private int numMemPools;
    private Map<ObjectName, String> bufferPoolNames;
    private int numBufferPools;
    private boolean unixOs;
    private ThreadMXBean threadMxBean;
    private volatile ManagementSnapshot snapshot;

    public EsacManagementFactoryImpl() {
        super(ManagementSnapshot.class);
    }

    @Override
    public ManagementSnapshot buildDataSource(boolean refresh) {
        if (refresh) {
            return this.buildSnapshot();
        } else {
            this.readLock.lock();

            try {
                return this.snapshot;
            } finally {
                this.readLock.unlock();
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.beanServer = ManagementFactory.getPlatformMBeanServer();

        this.numMemPools = (this.memPoolNames = this.buildPoolNames(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE)).size();
        this.numBufferPools = (this.bufferPoolNames = this.buildPoolNames(BufferPoolMxBeanSnapshotImpl.OBJ_NAME_DOMAIN_TYPE)).size();

        this.unixOs = (ManagementFactory.getOperatingSystemMXBean() instanceof UnixOperatingSystemMXBean);

        this.threadMxBean = ((ThreadMXBean) ManagementFactory.getThreadMXBean());

        ManagementFactory.getGarbageCollectorMXBeans()
            .forEach(gcMxBean -> ((NotificationEmitter) gcMxBean).addNotificationListener(this.gcNotificationListener, null, this));

        this.buildSnapshot();
    }

    private ManagementSnapshot buildSnapshot() {
        this.writeLock.lock();

        try {
            SystemMxBeanSnapshot sysSnapshot = new SystemMxBeanSnapshotImpl(this.beanServer.getAttributes(SystemMxBeanSnapshotImpl.OBJ_NAME,
                (this.unixOs ? SystemMxBeanSnapshotImpl.UNIX_ATTR_NAMES : SystemMxBeanSnapshotImpl.ATTR_NAMES)));

            AttributeList attrs = this.beanServer.getAttributes(MemoryMxBeanSnapshotImpl.OBJ_NAME, MemoryMxBeanSnapshotImpl.ATTR_NAMES);
            List<MemoryPoolMxBeanSnapshot> memPoolSnapshots = new ArrayList<>(this.numMemPools);

            for (ObjectName memPoolObjName : this.memPoolNames.keySet()) {
                memPoolSnapshots.add(new MemoryPoolMxBeanSnapshotImpl(this.memPoolNames.get(memPoolObjName), memPoolObjName,
                    this.beanServer.getAttributes(memPoolObjName, MemoryPoolMxBeanSnapshotImpl.ATTR_NAMES)));
            }

            List<BufferPoolMxBeanSnapshot> bufferPoolSnapshots = new ArrayList<>(this.numBufferPools);

            for (ObjectName bufferPoolObjName : this.bufferPoolNames.keySet()) {
                bufferPoolSnapshots.add(new BufferPoolMxBeanSnapshotImpl(this.bufferPoolNames.get(bufferPoolObjName), bufferPoolObjName,
                    this.beanServer.getAttributes(bufferPoolObjName, BufferPoolMxBeanSnapshotImpl.ATTR_NAMES)));
            }

            MemoryMxBeanSnapshot memSnapshot = new MemoryMxBeanSnapshotImpl(attrs, memPoolSnapshots, bufferPoolSnapshots);

            attrs = this.beanServer.getAttributes(ThreadMxBeanSnapshotImpl.OBJ_NAME, ThreadMxBeanSnapshotImpl.ATTR_NAMES);

            long[] deadlockedThreadIdArr = Optional.ofNullable(this.threadMxBean.findDeadlockedThreads()).orElse(ArrayUtils.EMPTY_LONG_ARRAY),
                threadIdArr = this.threadMxBean.getAllThreadIds();

            Set<Long> deadlockedThreadIds =
                LongStream.of(deadlockedThreadIdArr).boxed().collect(Collectors.toCollection(() -> new LinkedHashSet<>(deadlockedThreadIdArr.length))),
                threadIds = LongStream.of(threadIdArr).boxed()
                    .collect(Collectors.toCollection(() -> new LinkedHashSet<>((threadIdArr.length + deadlockedThreadIdArr.length))));
            threadIds.addAll(deadlockedThreadIds);

            int numThreadIds = threadIds.size();
            Map<Long, EsacThreadInfo> threadInfos = new LinkedHashMap<>(numThreadIds);
            Map<State, Integer> stateThreadCounts = new EnumMap<>(State.class);
            int nativeThreadCount = 0, deadlockedThreadCount = 0, monitorDeadlockedThreadCount = 0, synchronizerDeadlockedThreadCount = 0;
            long threadId;
            boolean threadNative, threadDeadlocked, threadMonitorDeadlocked, threadSynchronizerDeadlocked;
            State threadState;

            Stream.of(State.class.getEnumConstants()).forEach(stateEnumItem -> stateThreadCounts.put(stateEnumItem, 0));

            for (ThreadInfo threadInfo : this.threadMxBean.getThreadInfo(ArrayUtils.toPrimitive(threadIds.toArray(new Long[numThreadIds])), true, true)) {
                if (threadInfo == null) {
                    continue;
                }

                threadInfos.put((threadId = threadInfo.getThreadId()),
                    new EsacThreadInfoImpl(threadId, threadInfo.getThreadName(), (threadNative = threadInfo.isInNative()),
                        (threadMonitorDeadlocked =
                            ((threadDeadlocked = deadlockedThreadIds.contains(threadId)) && (threadInfo.getLockedMonitors().length > 0))),
                        (threadSynchronizerDeadlocked = (threadDeadlocked && (threadInfo.getLockedSynchronizers().length > 0))),
                        (threadState = threadInfo.getThreadState())));

                if (threadNative) {
                    nativeThreadCount++;
                }

                if (threadDeadlocked) {
                    deadlockedThreadCount++;

                    if (threadMonitorDeadlocked) {
                        monitorDeadlockedThreadCount++;
                    }

                    if (threadSynchronizerDeadlocked) {
                        synchronizerDeadlockedThreadCount++;
                    }
                }

                stateThreadCounts.put(threadState, (stateThreadCounts.get(threadState) + 1));
            }

            return (this.snapshot = new ManagementSnapshotImpl(
                new ClassLoadingMxBeanSnapshotImpl(
                    this.beanServer.getAttributes(ClassLoadingMxBeanSnapshotImpl.OBJ_NAME, ClassLoadingMxBeanSnapshotImpl.ATTR_NAMES)),
                new JvmMxBeanSnapshotImpl(this.beanServer.getAttributes(JvmMxBeanSnapshotImpl.OBJ_NAME, JvmMxBeanSnapshotImpl.ATTR_NAMES)), memSnapshot,
                sysSnapshot, new ThreadMxBeanSnapshotImpl(attrs, threadInfos, stateThreadCounts, nativeThreadCount, deadlockedThreadCount,
                    monitorDeadlockedThreadCount, synchronizerDeadlockedThreadCount)));
        } catch (InstanceNotFoundException | ReflectionException e) {
            throw new EsacManagementException("Unable to refresh management factory.", e);
        } finally {
            this.writeLock.unlock();
        }
    }

    private Map<ObjectName, String> buildPoolNames(String objNameDomainType) throws EsacManagementException {
        Set<ObjectName> memPoolObjNames = this.beanServer.queryNames(EsacManagementUtils.buildObjectName(objNameDomainType), null);

        return memPoolObjNames.stream().collect(EsacStreamUtils.toMap(Function.identity(),
            memPoolObjName -> memPoolObjName.getKeyProperty(EsacManagementUtils.NAME_OBJ_NAME_PROP_NAME), () -> new LinkedHashMap<>(memPoolObjNames.size())));
    }
}
