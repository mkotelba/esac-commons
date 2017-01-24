package com.esacinc.commons.transform.saxon.impl;

import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.lib.ParseOptions;

public class EsacPipelineConfiguration extends PipelineConfiguration {
    public EsacPipelineConfiguration(EsacSaxonConfiguration config) {
        super(config);

        this.setParseOptions(new ParseOptions(config.getParseOptions()));
    }

    @Override
    public EsacSaxonConfiguration getConfiguration() {
        return ((EsacSaxonConfiguration) super.getConfiguration());
    }
}
