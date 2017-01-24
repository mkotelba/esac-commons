package com.esacinc.commons.ws.logging;

import com.esacinc.commons.net.logging.RestLoggingEvent;
import com.esacinc.commons.ws.WsDirection;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;

public interface WsLoggingEvent extends RestLoggingEvent {
    public boolean hasBindingName();

    @JsonProperty
    @Nullable
    public String getBindingName();

    public void setBindingName(@Nullable String bindingName);

    @JsonProperty
    public WsDirection getDirection();

    public void setDirection(WsDirection direction);

    @JsonProperty
    public String getEndpointAddress();

    public void setEndpointAddress(String endpointAddr);

    public boolean hasEndpointName();

    @JsonProperty
    @Nullable
    public String getEndpointName();

    public void setEndpointName(@Nullable String endpointName);

    public boolean hasOperationName();

    @JsonProperty
    @Nullable
    public String getOperationName();

    public void setOperationName(@Nullable String opName);

    @JsonProperty
    public String getPayload();

    public void setPayload(String payload);

    public boolean hasPortName();

    @JsonProperty
    @Nullable
    public String getPortName();

    public void setPortName(@Nullable String portName);

    public boolean hasPortTypeName();

    @JsonProperty
    @Nullable
    public String getPortTypeName();

    public void setPortTypeName(@Nullable String portTypeName);

    public boolean hasPrettyPayload();

    @JsonProperty
    @Nullable
    public String getPrettyPayload();

    public void setPrettyPayload(@Nullable String prettyPayload);

    public boolean hasServiceName();

    @JsonProperty
    @Nullable
    public String getServiceName();

    public void setServiceName(@Nullable String serviceName);
}
