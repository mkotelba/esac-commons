package com.esacinc.commons.web.tomcat.impl;

import com.esacinc.commons.context.EsacPropertyNames;
import com.esacinc.commons.tx.impl.TxIdGenerator;
import com.esacinc.commons.web.tomcat.crypto.impl.EsacJsseImplementation;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnegative;
import javax.net.ssl.SSLEngine;
import org.apache.commons.collections4.map.AbstractReferenceMap.ReferenceStrength;
import org.apache.commons.collections4.map.ReferenceIdentityMap;
import org.apache.tomcat.util.net.NioChannel;
import org.apache.tomcat.util.net.NioEndpoint;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfig.Type;
import org.apache.tomcat.util.net.SocketEvent;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.net.openssl.ciphers.Cipher;
import org.apache.tomcat.util.threads.TaskQueue;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.MDC;

public class EsacNioEndpoint extends NioEndpoint {
    public class EsacSocketProcessor extends SocketProcessor {
        public EsacSocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent event) {
            super(socketWrapper, event);
        }

        @Override
        public void doRun() {
            super.doRun();
        }

        public SocketWrapperBase<NioChannel> getSocketWrapper() {
            return this.socketWrapper;
        }
    }

    public class EsacSocketProcessorTxTask implements Runnable {
        private EsacSocketProcessor proc;

        public EsacSocketProcessorTxTask(EsacSocketProcessor proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            final SocketWrapperBase<NioChannel> socketWrapper = this.proc.getSocketWrapper();
            String txId;

            // noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (socketWrapper) {
                if (socketWrapper.isClosed()) {
                    return;
                }

                synchronized (EsacNioEndpoint.this.socketTxIdLock) {
                    Socket socket = socketWrapper.getSocket().getIOChannel().socket();

                    if (EsacNioEndpoint.this.socketTxIds.containsKey(socket)) {
                        txId = EsacNioEndpoint.this.socketTxIds.get(socket);
                    } else {
                        EsacNioEndpoint.this.socketTxIds.put(socket, (txId = EsacNioEndpoint.this.txIdGen.generateId().toString()));
                    }
                }
            }

            try {
                MDC.put(EsacPropertyNames.HTTP_SERVER_TX_ID, txId);

                this.proc.doRun();
            } finally {
                MDC.remove(EsacPropertyNames.HTTP_SERVER_TX_ID);
            }
        }
    }

    public class EsacThreadPoolExecutor extends ThreadPoolExecutor {
        public EsacThreadPoolExecutor() {
            super(EsacNioEndpoint.this.getMinSpareThreads(), EsacNioEndpoint.this.getMaxThreads(), EsacNioEndpoint.this.getKeepAliveTimeout(), TimeUnit.SECONDS,
                new TaskQueue(), new TaskThreadFactory((EsacNioEndpoint.this.getName() + CONN_EXEC_THREAD_NAME_PREFIX), true, Thread.NORM_PRIORITY));

            ((TaskQueue) this.getQueue()).setParent(this);
        }

        @Override
        public void execute(Runnable task, @Nonnegative long timeout, TimeUnit unit) {
            if (task instanceof EsacSocketProcessor) {
                task = new EsacSocketProcessorTxTask(((EsacSocketProcessor) task));
            }

            super.execute(task, timeout, unit);
        }
    }

    private final static String CONN_EXEC_THREAD_NAME_PREFIX = "-exec-";

    private final static float SOCKET_TX_IDS_LOAD_FACTOR = 0.75F;

    private EsacJsseImplementation sslImpl;
    private TxIdGenerator txIdGen;
    private final Object socketTxIdLock = new Object();
    private ReferenceIdentityMap<Socket, String> socketTxIds;

    @Override
    protected EsacSocketProcessor createSocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent event) {
        return new EsacSocketProcessor(socketWrapper, event);
    }

    @Override
    protected SSLEngine createSSLEngine(String sniHostName, List<Cipher> clientRequestedCiphers) {
        SSLEngine engine = this.sslImpl.getContext().createSSLEngine();
        engine.setUseClientMode(false);

        return engine;
    }

    @Override
    protected void initialiseSsl() throws Exception {
        SSLHostConfig sslHostConfig = new SSLHostConfig();
        sslHostConfig.setHostName(this.getDefaultSSLHostConfigName());
        this.addSslHostConfig(sslHostConfig);

        this.createSSLContext(sslHostConfig);
    }

    @Override
    protected void createSSLContext(SSLHostConfig sslHostConfig) throws IllegalArgumentException {
        this.sslImpl.getSSLUtil(sslHostConfig.getCertificates(true).iterator().next()).configureServerSessionContext();
    }

    @Override
    public void startInternal() throws Exception {
        if (!this.running) {
            this.socketTxIds =
                new ReferenceIdentityMap<>(ReferenceStrength.WEAK, ReferenceStrength.HARD, this.getMinSpareThreads(), SOCKET_TX_IDS_LOAD_FACTOR, true);
            this.setExecutor(new EsacThreadPoolExecutor());
        }

        super.startInternal();
    }

    @Override
    protected Type getSslConfigType() {
        return Type.JSSE;
    }

    @Override
    public EsacJsseImplementation getSslImplementation() {
        return this.sslImpl;
    }

    public void setSslImplementation(EsacJsseImplementation sslImpl) {
        this.sslImpl = sslImpl;
    }

    @Override
    public String getSslImplementationName() {
        return EsacJsseImplementation.NAME;
    }

    public TxIdGenerator getTxIdGenerator() {
        return this.txIdGen;
    }

    public void setTxIdGenerator(TxIdGenerator txIdGen) {
        this.txIdGen = txIdGen;
    }
}
