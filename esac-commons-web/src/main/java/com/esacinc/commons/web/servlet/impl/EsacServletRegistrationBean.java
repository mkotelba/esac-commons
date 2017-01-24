package com.esacinc.commons.web.servlet.impl;

import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletSecurityElement;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

public class EsacServletRegistrationBean extends ServletRegistrationBean {
    private ServletSecurityElement servletSec;

    @Override
    protected void configure(Dynamic reg) {
        super.configure(reg);

        if (this.servletSec != null) {
            reg.setServletSecurity(this.servletSec);
        }
    }

    public ServletSecurityElement getServletSecurity() {
        return this.servletSec;
    }

    public void setServletSecurity(ServletSecurityElement servletSec) {
        this.servletSec = servletSec;
    }
}
