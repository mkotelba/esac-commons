package com.esacinc.commons.config;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Map;
import java.util.SortedMap;
import javax.annotation.Nullable;

public interface EsacOptions<T extends EsacOptions<T>> extends Cloneable, SortedMap<String, Object> {
    public T setOptions(Map<EsacOption<Object>, ?> opts);

    public <U> T setOption(EsacOption<U> opt, @Nullable U optValue);

    @Nullable
    public <U> U getOption(EsacOption<U> opt);

    @Nullable
    public <U> U getOption(EsacOption<U> opt, @Nullable U defaultOptValue);

    public boolean hasOption(EsacOption<?> opt);

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T merge(@Nullable T ... mergeOpts);

    public T clone();
}
