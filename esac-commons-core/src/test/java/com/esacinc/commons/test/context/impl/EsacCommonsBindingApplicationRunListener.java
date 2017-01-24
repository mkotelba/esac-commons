package com.esacinc.commons.test.context.impl;

import com.esacinc.commons.context.impl.AbstractBindingApplicationRunListener;
import org.springframework.boot.SpringApplication;

public class EsacCommonsBindingApplicationRunListener extends AbstractBindingApplicationRunListener<EsacCommonsApplication> {
    public EsacCommonsBindingApplicationRunListener(SpringApplication app, String[] args) {
        super(app, args);
    }
}
