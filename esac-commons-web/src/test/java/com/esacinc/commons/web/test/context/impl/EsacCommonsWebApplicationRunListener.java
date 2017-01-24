package com.esacinc.commons.web.test.context.impl;

import com.esacinc.commons.test.context.impl.EsacCommonsApplication;
import com.esacinc.commons.web.context.impl.AbstractWebApplicationRunListener;
import org.springframework.boot.SpringApplication;

public class EsacCommonsWebApplicationRunListener extends AbstractWebApplicationRunListener<EsacCommonsApplication> {
    public EsacCommonsWebApplicationRunListener(SpringApplication app, String[] args) {
        super(app, args);
    }
}
