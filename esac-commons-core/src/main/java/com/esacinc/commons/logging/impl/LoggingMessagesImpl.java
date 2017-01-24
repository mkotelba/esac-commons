package com.esacinc.commons.logging.impl;

import com.esacinc.commons.logging.LoggingMessages;
import javax.annotation.Nullable;

public class LoggingMessagesImpl implements LoggingMessages {
    private String fullMsg;
    private String shortMsg;

    @Override
    public boolean hasMessage(boolean full) {
        return (this.getMessage(full) != null);
    }

    @Nullable
    @Override
    public String getMessage(boolean full) {
        return ((full || (this.shortMsg == null)) ? this.fullMsg : this.shortMsg);
    }

    @Override
    public void setMessage(boolean full, @Nullable String msg) {
        if (full) {
            this.fullMsg = msg;
        } else {
            this.shortMsg = msg;
        }
    }
}
