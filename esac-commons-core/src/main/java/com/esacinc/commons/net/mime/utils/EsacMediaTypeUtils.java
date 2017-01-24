package com.esacinc.commons.net.mime.utils;

import com.esacinc.commons.beans.ContentTypeBean;
import com.esacinc.commons.utils.EsacEnumUtils;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.MediaType;

public final class EsacMediaTypeUtils {
    private EsacMediaTypeUtils() {
    }

    @Nullable
    public static <T extends Enum<T> & ContentTypeBean> T findCompatible(Class<T> enumClass, MediaType mediaType) {
        return mapCompatible(enumClass, mediaType).findFirst().map(Entry::getValue).orElse(null);
    }

    public static <T extends Enum<T> & ContentTypeBean> Stream<Entry<MediaType, T>> mapCompatible(Class<T> enumClass, MediaType mediaType) {
        return Stream.of(enumClass.getEnumConstants())
            .flatMap(enumItem -> enumItem.getCompatibleMediaTypes().stream().filter(compatMediaType -> compatMediaType.isCompatibleWith(mediaType))
                .map(compatMediaType -> ((Entry<MediaType, T>) new ImmutablePair<>(compatMediaType, enumItem))))
            .sorted(Comparator.comparing(Entry::getKey, MediaType.SPECIFICITY_COMPARATOR.thenComparing(MediaType.QUALITY_VALUE_COMPARATOR)));
    }

    public static <T extends Enum<T> & ContentTypeBean> boolean isCompatible(Class<T> enumClass, MediaType mediaType) {
        return (EsacEnumUtils.findByPredicate(enumClass,
            enumItem -> enumItem.getCompatibleMediaTypes().stream().anyMatch(compatMediaType -> compatMediaType.isCompatibleWith(mediaType))) != null);
    }
}
