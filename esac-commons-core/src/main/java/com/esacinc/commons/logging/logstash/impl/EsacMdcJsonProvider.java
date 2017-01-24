package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.utils.EsacStreamUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class EsacMdcJsonProvider extends AbstractEsacJsonProvider implements InitializingBean {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private AbstractEsacApplication app;

    private String appPropNamePrefix;

    @Override
    protected void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) {
        Map<String, String> mdcProps = event.getMDCPropertyMap();

        if (mdcProps.isEmpty()) {
            return;
        }

        Stream<Entry<String, String>> mdcPropEntries =
            mdcProps.entrySet().stream().filter(mdcPropEntry -> StringUtils.startsWith(mdcPropEntry.getKey(), this.appPropNamePrefix));
        int numMdcPropEntries = ((int) mdcPropEntries.count());

        if (numMdcPropEntries == 0) {
            return;
        }

        fields.putAll(mdcPropEntries.map(
            mdcPropEntryItem -> new ImmutablePair<>(StringUtils.removeStart(mdcPropEntryItem.getKey(), this.appPropNamePrefix), mdcPropEntryItem.getValue()))
            .collect(EsacStreamUtils.toMap(() -> new HashMap<>(numMdcPropEntries))));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.appPropNamePrefix = this.app.getPropertyNamePrefix();
    }
}
