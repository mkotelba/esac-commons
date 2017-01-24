package com.esacinc.commons.transform.saxon.impl;

import java.util.HashMap;
import java.util.Map;
import net.sf.saxon.Controller;
import net.sf.saxon.expr.instruct.Executable;

public class EsacSaxonController extends Controller {
    private Map<Object, Object> contextData = new HashMap<>();

    public EsacSaxonController(EsacSaxonConfiguration config) {
        super(config);
    }

    public EsacSaxonController(EsacSaxonConfiguration config, Executable exec) {
        super(config, exec);
    }

    @Override
    public EsacSaxonConfiguration getConfiguration() {
        return ((EsacSaxonConfiguration) super.getConfiguration());
    }

    public Map<Object, Object> getContextData() {
        return this.contextData;
    }
}
