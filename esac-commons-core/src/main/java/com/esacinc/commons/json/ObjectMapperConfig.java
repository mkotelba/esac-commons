package com.esacinc.commons.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.annotation.Nullable;

public interface ObjectMapperConfig {
    public void configure(ObjectMapper objMapper);

    public void merge(ObjectMapperConfig config);

    public Include getContentInclusion();

    public void setContentInclusion(Include contentInclusion);

    public boolean hasDateFormat();

    @Nullable
    public DateFormat getDateFormat();

    public void setDateFormat(@Nullable DateFormat dateFormat);

    public boolean hasFeatures();

    public Map<Object, Boolean> getFeatures();

    public void setFeatures(@Nullable Map<Object, Boolean> features);

    public boolean hasFilterProvider();

    @Nullable
    public FilterProvider getFilterProvider();

    public void setFilterProvider(@Nullable FilterProvider filterProv);

    public boolean hasHandlerInstantiator();

    @Nullable
    public HandlerInstantiator getHandlerInstantiator();

    public void setHandlerInstantiator(@Nullable HandlerInstantiator handlerInstantiator);

    public boolean hasLocale();

    @Nullable
    public Locale getLocale();

    public void setLocale(@Nullable Locale locale);

    public boolean hasModules();

    public List<Module> getModules();

    public void setModules(@Nullable List<Module> modules);

    public boolean hasPrettyPrinter();

    @Nullable
    public PrettyPrinter getPrettyPrinter();

    public void setPrettyPrinter(@Nullable PrettyPrinter prettyPrinter);

    public Value getPropertyInclusion();

    public boolean hasTimeZone();

    @Nullable
    public TimeZone getTimeZone();

    public void setTimeZone(@Nullable TimeZone timeZone);

    public Include getValueInclusion();

    public void setValueInclusion(Include valueInclusion);
}
