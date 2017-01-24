package com.esacinc.commons.test.beans;

import javax.annotation.Nonnegative;

public interface EsacHttpServer extends EsacChannelServer {
    @Nonnegative
    public int getMaxContentLength();

    public void setMaxContentLength(@Nonnegative int maxContentLen);
}
