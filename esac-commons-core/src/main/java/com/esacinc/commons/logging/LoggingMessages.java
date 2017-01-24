package com.esacinc.commons.logging;

import javax.annotation.Nullable;

public interface LoggingMessages {
    public boolean hasMessage(boolean full);

    @Nullable
    public String getMessage(boolean full);

    public void setMessage(boolean full, @Nullable String msg);
}
