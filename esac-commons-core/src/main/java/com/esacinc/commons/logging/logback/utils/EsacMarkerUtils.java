package com.esacinc.commons.logging.logback.utils;

import com.esacinc.commons.utils.EsacStreamUtils;
import com.esacinc.commons.logging.EsacLoggingEvent;
import com.esacinc.commons.logging.LoggingMessages;
import com.esacinc.commons.logging.logback.impl.FieldsMarker;
import com.esacinc.commons.logging.logback.impl.LoggingEventMarker;
import com.esacinc.commons.logging.logback.impl.MessageMarker;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.builder.Builder;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.helpers.BasicMarker;

public final class EsacMarkerUtils {
    public static class MarkerBuilder implements Builder<Marker> {
        private Marker marker;

        public MarkerBuilder(String ... tags) {
            this.appendTags(tags);
        }

        public MarkerBuilder setEvent(EsacLoggingEvent event) {
            EsacMarkerUtils.setEvent(this::getMarker, this::setMarker, event);

            return this;
        }

        public MarkerBuilder setFields(Map<String, Object> fields) {
            EsacMarkerUtils.setFields(this::getMarker, this::setMarker, fields);

            return this;
        }

        public MarkerBuilder setField(String fieldName, Object fieldValue) {
            EsacMarkerUtils.setField(this::getMarker, this::setMarker, fieldName, fieldValue);

            return this;
        }

        public MarkerBuilder setMessage(String msg) {
            return this.setMessages(msg, null);
        }

        public MarkerBuilder setMessages(String fullMsg, @Nullable String shortMsg) {
            EsacMarkerUtils.setMessages(this::getMarker, this::setMarker, fullMsg, shortMsg);

            return this;
        }

        public MarkerBuilder appendTags(String ... tags) {
            EsacMarkerUtils.appendTags(this::getMarker, this::setMarker, tags);

            return this;
        }

        public MarkerBuilder appendMarker(Marker marker) {
            EsacMarkerUtils.appendMarker(this::getMarker, this::setMarker, marker);

            return this;
        }

        @Nullable
        @Override
        public Marker build() {
            return this.marker;
        }

        @Nullable
        public Marker getMarker() {
            return this.marker;
        }

        public void setMarker(@Nullable Marker marker) {
            this.marker = marker;
        }
    }

    private EsacMarkerUtils() {
    }

    public static void setEvent(ch.qos.logback.classic.spi.LoggingEvent parentEvent, EsacLoggingEvent event) {
        setEvent(parentEvent::getMarker, parentEvent::setMarker, event);
    }

    public static void setFields(ch.qos.logback.classic.spi.LoggingEvent event, Map<String, Object> fields) {
        setFields(event::getMarker, event::setMarker, fields);
    }

    public static void setField(ch.qos.logback.classic.spi.LoggingEvent event, String fieldName, Object fieldValue) {
        setField(event::getMarker, event::setMarker, fieldName, fieldValue);
    }

    @Nullable
    public static Map<String, Object> getFields(ch.qos.logback.classic.spi.LoggingEvent event) {
        FieldsMarker fieldsMarker = EsacStreamUtils.findInstance(stream(event.getMarker()), FieldsMarker.class);

        return ((fieldsMarker != null) ? fieldsMarker.getFields() : null);
    }

    public static void setMessages(ch.qos.logback.classic.spi.LoggingEvent event, @Nullable String fullMsg) {
        setMessages(event, fullMsg, null);
    }

    public static void setMessages(ch.qos.logback.classic.spi.LoggingEvent event, @Nullable String fullMsg, @Nullable String shortMsg) {
        setMessages(event::getMarker, event::setMarker, fullMsg, shortMsg);
    }

    @Nullable
    public static LoggingMessages getMessages(ch.qos.logback.classic.spi.LoggingEvent event) {
        MessageMarker msgMarker = EsacStreamUtils.findInstance(stream(event.getMarker()), MessageMarker.class);

        return ((msgMarker != null) ? msgMarker.getMessages() : null);
    }

    public static void appendTags(ch.qos.logback.classic.spi.LoggingEvent event, String ... tags) {
        appendTags(event::getMarker, event::setMarker, tags);
    }

    public static void appendMarker(ch.qos.logback.classic.spi.LoggingEvent event, Marker marker) {
        appendMarker(event::getMarker, event::setMarker, marker);
    }

    public static Stream<Marker> stream(@Nullable Marker marker) {
        return EsacStreamUtils.traverse(Marker::iterator, true, marker);
    }

    private static void setEvent(Supplier<Marker> getter, Consumer<Marker> setter, EsacLoggingEvent event) {
        Marker marker = getter.get();

        if (marker != null) {
            LoggingEventMarker eventMarker = EsacStreamUtils.findInstance(stream(marker), LoggingEventMarker.class);

            if (eventMarker != null) {
                eventMarker.setEvent(event);
            } else {
                marker.add(new LoggingEventMarker(event));
            }
        } else {
            setter.accept(new LoggingEventMarker(event));
        }
    }

    private static void setFields(Supplier<Marker> getter, Consumer<Marker> setter, Map<String, Object> fields) {
        Marker marker = getter.get();
        FieldsMarker fieldsMarker;

        if (marker != null) {
            if ((fieldsMarker = EsacStreamUtils.findInstance(stream(marker), FieldsMarker.class)) == null) {
                marker.add((fieldsMarker = new FieldsMarker()));
            }
        } else {
            setter.accept((fieldsMarker = new FieldsMarker()));
        }

        // noinspection ConstantConditions
        fieldsMarker.getFields().putAll(fields);
    }

    private static void setField(Supplier<Marker> getter, Consumer<Marker> setter, String fieldName, Object fieldValue) {
        Marker marker = getter.get();
        FieldsMarker fieldsMarker;

        if (marker != null) {
            fieldsMarker = EsacStreamUtils.findInstance(stream(marker), FieldsMarker.class);
        } else {
            setter.accept((fieldsMarker = new FieldsMarker()));
        }

        // noinspection ConstantConditions
        fieldsMarker.getFields().put(fieldName, fieldValue);
    }

    private static void setMessages(Supplier<Marker> getter, Consumer<Marker> setter, @Nullable String fullMsg, @Nullable String shortMsg) {
        Marker marker = getter.get();

        if (marker != null) {
            MessageMarker msgMarker = EsacStreamUtils.findInstance(stream(marker), MessageMarker.class);

            if (msgMarker != null) {
                LoggingMessages msgs = msgMarker.getMessages();
                msgs.setMessage(true, fullMsg);
                msgs.setMessage(false, shortMsg);
            } else {
                marker.add(new MessageMarker(fullMsg, shortMsg));
            }
        } else {
            setter.accept(new MessageMarker(fullMsg, shortMsg));
        }
    }

    private static void appendTags(Supplier<Marker> getter, Consumer<Marker> setter, String ... tags) {
        if (tags.length == 0) {
            return;
        }

        Marker marker = getter.get();

        tags = Stream.of(tags).distinct().toArray(String[]::new);

        if (marker != null) {
            Set<String> existingTags =
                EsacStreamUtils.asInstances(stream(marker).filter(markerItem -> markerItem.getClass().equals(BasicMarker.class)), BasicMarker.class)
                    .map(Marker::getName).collect(Collectors.toSet());

            for (String tag : tags) {
                if (!existingTags.contains(tag)) {
                    marker.add(MarkerFactory.getDetachedMarker(tag));
                }
            }
        } else {
            setter.accept((marker = MarkerFactory.getDetachedMarker(tags[0])));

            for (int a = 1; a < tags.length; a++) {
                marker.add(MarkerFactory.getDetachedMarker(tags[a]));
            }
        }
    }

    private static void appendMarker(Supplier<Marker> getter, Consumer<Marker> setter, Marker marker) {
        Marker existingMarker = getter.get();

        if (existingMarker != null) {
            existingMarker.add(marker);
        } else {
            setter.accept(marker);
        }
    }
}
