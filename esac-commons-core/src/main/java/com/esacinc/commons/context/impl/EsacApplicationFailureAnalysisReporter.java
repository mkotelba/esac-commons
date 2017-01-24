package com.esacinc.commons.context.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalysisReporter;

public class EsacApplicationFailureAnalysisReporter implements FailureAnalysisReporter {
    private final static Logger LOGGER = LoggerFactory.getLogger(EsacApplicationFailureAnalysisReporter.class);

    @Override
    public void report(FailureAnalysis failureAnalysis) {
        LOGGER.error(String.format("Application failed to start: %s", failureAnalysis.getDescription()), failureAnalysis.getCause());
    }
}
