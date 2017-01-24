package com.esacinc.commons.ws.utils;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.xml.ws.Holder;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;

public final class EsacWsUtils {
    private EsacWsUtils() {
    }

    @Nullable
    @SuppressWarnings({ "unchecked" })
    public static <T> T getMessageContentPart(Message msg, Class<T> msgContentPartClass) {
        return msgContentPartClass.cast(getMessageContentPart(msg, (msgContentPart) -> msgContentPartClass
            .isAssignableFrom(((msgContentPart instanceof Holder<?>) ? ((Holder<?>) msgContentPart) : msgContentPart).getClass())));
    }

    @Nullable
    public static Object getMessageContentPart(Message msg, Predicate<? super Object> msgContentPartPredicate) {
        return getMessageContents(msg).stream().filter(msgContentPartPredicate).findFirst().orElse(null);
    }

    public static MessageContentsList getMessageContents(Message msg) {
        MessageContentsList msgContents = MessageContentsList.getContentsList(msg);

        if (msgContents == null) {
            msg.setContent(List.class, (msgContents = new MessageContentsList()));
        }

        return msgContents;
    }
}
