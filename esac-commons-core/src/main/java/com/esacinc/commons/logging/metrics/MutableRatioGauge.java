package com.esacinc.commons.logging.metrics;

import javax.annotation.Nullable;

public interface MutableRatioGauge extends MutableGauge<Double> {
    public void setValue(@Nullable Number numerator, @Nullable Number denominator);
}
