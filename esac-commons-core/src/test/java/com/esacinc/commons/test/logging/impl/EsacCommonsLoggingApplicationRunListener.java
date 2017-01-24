package com.esacinc.commons.test.logging.impl;

import com.esacinc.commons.logging.impl.AbstractLoggingApplicationRunListener;
import com.esacinc.commons.test.context.impl.EsacCommonsApplication;
import org.springframework.boot.SpringApplication;

public class EsacCommonsLoggingApplicationRunListener extends AbstractLoggingApplicationRunListener<EsacCommonsApplication> {
    public EsacCommonsLoggingApplicationRunListener(SpringApplication app, String[] args) {
        super(app, args);
    }
}
