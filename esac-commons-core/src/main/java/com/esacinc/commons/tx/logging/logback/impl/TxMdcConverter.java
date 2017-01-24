package com.esacinc.commons.tx.logging.logback.impl;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.utils.EsacStringUtils;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class TxMdcConverter extends ClassicConverter {
    public final static String WORD = "xTx";

    private final static String SECTION_SUFFIX = "} ";

    private final static String MDC_PROP_DELIM = ", ";

    private boolean enabled;
    private Map<String, String> mappings;

    @Override
    public String convert(ILoggingEvent event) {
        if (!this.enabled) {
            return StringUtils.EMPTY;
        }

        final Map<String, String> mdcProps = event.getMDCPropertyMap();

        return (!mdcProps.isEmpty()
            ? this.mappings.entrySet().stream().filter(mappingEntry -> mdcProps.containsKey(mappingEntry.getKey()))
                .map(mappingEntry -> (mappingEntry.getValue() + EsacStringUtils.EQUALS_CHAR + mdcProps.get(mappingEntry.getKey())))
                .collect(Collectors.joining(MDC_PROP_DELIM, EsacStringUtils.L_BRACE, SECTION_SUFFIX))
            : StringUtils.EMPTY);
    }

    @Override
    public void start() {
        AbstractEsacApplication app = ((AbstractEsacApplication) this.getContext().getObject(AbstractEsacApplication.BEAN_NAME));

        this.enabled = (!BooleanUtils.toBoolean(this.getFirstOption()) || app.isLoggingConsoleTty());

        this.mappings = app.getLoggingTxMdcMappings();

        super.start();
    }
}
