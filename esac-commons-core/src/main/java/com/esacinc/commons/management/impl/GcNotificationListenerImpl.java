package com.esacinc.commons.management.impl;

import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import com.esacinc.commons.management.EsacManagementFactory;
import com.esacinc.commons.management.GcNotificationListener;
import com.esacinc.commons.management.logging.impl.GcLoggingEventImpl;
import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;
import javax.management.Notification;
import javax.management.openmbean.CompositeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GcNotificationListenerImpl implements GcNotificationListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(GcNotificationListenerImpl.class);

    @Override
    public void handleNotification(Notification notification, Object handback) {
        if (!notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
            return;
        }

        long jvmStartTimestamp = ((EsacManagementFactory) handback).buildDataSource(false).getJvmSnapshot().getStartTime();
        GarbageCollectionNotificationInfo notificationInfo = GarbageCollectionNotificationInfo.from(((CompositeData) notification.getUserData()));
        GcInfo gcInfo = notificationInfo.getGcInfo();

        LOGGER.info(
            new MarkerBuilder().setEvent(new GcLoggingEventImpl(notificationInfo.getGcName(), notificationInfo.getGcAction(), notificationInfo.getGcCause(),
                (jvmStartTimestamp + gcInfo.getStartTime()), (jvmStartTimestamp + gcInfo.getEndTime()), gcInfo.getDuration())).build(),
            null);
    }
}
