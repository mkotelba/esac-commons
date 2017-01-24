package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils;
import com.esacinc.commons.utils.EsacStreamUtils;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.logstash.logback.composite.loggingevent.TagsJsonProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarker;

public class EsacTagsJsonProvider extends AbstractEsacJsonProvider {
    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(TagsJsonProvider.FIELD_TAGS, ElasticsearchDatatype.TEXT));

    @Override
    protected void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) {
        Stream<BasicMarker> markers = EsacStreamUtils.asInstances(EsacMarkerUtils.stream(event.getMarker()), BasicMarker.class);

        if (markers.count() == 0) {
            return;
        }

        fields.put(TagsJsonProvider.FIELD_TAGS, markers.map(Marker::getName).toArray(String[]::new));
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
