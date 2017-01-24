package com.esacinc.commons.ws.logging.impl;

import com.esacinc.commons.net.logging.RestEventType;
import com.esacinc.commons.net.logging.impl.AbstractRestLoggingEvent;
import com.esacinc.commons.ws.WsDirection;
import com.esacinc.commons.ws.logging.WsLoggingEvent;
import javax.annotation.Nullable;

public abstract class AbstractWsLoggingEvent extends AbstractRestLoggingEvent implements WsLoggingEvent {
    protected String bindingName;
    protected WsDirection direction;
    protected String endpointAddr;
    protected String endpointName;
    protected String opName;
    protected String payload;
    protected String portName;
    protected String portTypeName;
    protected String prettyPayload;
    protected String serviceName;

    protected AbstractWsLoggingEvent(RestEventType eventType) {
        super(eventType);
    }

    @Override
    public boolean hasBindingName() {
        return (this.bindingName != null);
    }

    @Nullable
    @Override
    public String getBindingName() {
        return this.bindingName;
    }

    @Override
    public void setBindingName(@Nullable String bindingName) {
        this.bindingName = bindingName;
    }

    @Override
    public WsDirection getDirection() {
        return this.direction;
    }

    @Override
    public void setDirection(WsDirection direction) {
        this.direction = direction;
    }

    @Override
    public String getEndpointAddress() {
        return this.endpointAddr;
    }

    @Override
    public void setEndpointAddress(String endpointAddr) {
        this.endpointAddr = endpointAddr;
    }

    @Override
    public boolean hasEndpointName() {
        return (this.endpointName != null);
    }

    @Nullable
    @Override
    public String getEndpointName() {
        return this.endpointName;
    }

    @Override
    public void setEndpointName(@Nullable String endpointName) {
        this.endpointName = endpointName;
    }

    @Override
    public boolean hasOperationName() {
        return (this.opName != null);
    }

    @Nullable
    @Override
    public String getOperationName() {
        return this.opName;
    }

    @Override
    public void setOperationName(@Nullable String opName) {
        this.opName = opName;
    }

    @Override
    public String getPayload() {
        return this.payload;
    }

    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public boolean hasPortName() {
        return (this.portName != null);
    }

    @Nullable
    @Override
    public String getPortName() {
        return this.portName;
    }

    @Override
    public void setPortName(@Nullable String portName) {
        this.portName = portName;
    }

    @Override
    public boolean hasPortTypeName() {
        return (this.portTypeName != null);
    }

    @Nullable
    @Override
    public String getPortTypeName() {
        return this.portTypeName;
    }

    @Override
    public void setPortTypeName(@Nullable String portTypeName) {
        this.portTypeName = portTypeName;
    }

    @Override
    public boolean hasPrettyPayload() {
        return (this.prettyPayload != null);
    }

    @Nullable
    @Override
    public String getPrettyPayload() {
        return this.prettyPayload;
    }

    @Override
    public void setPrettyPayload(@Nullable String prettyPayload) {
        this.prettyPayload = prettyPayload;
    }

    @Override
    public boolean hasServiceName() {
        return (this.serviceName != null);
    }

    @Nullable
    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public void setServiceName(@Nullable String serviceName) {
        this.serviceName = serviceName;
    }
}
