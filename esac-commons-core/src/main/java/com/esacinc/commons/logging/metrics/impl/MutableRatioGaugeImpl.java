package com.esacinc.commons.logging.metrics.impl;

import com.esacinc.commons.logging.metrics.MutableRatioGauge;
import javax.annotation.Nullable;

public class MutableRatioGaugeImpl extends MutableGaugeImpl<Double> implements MutableRatioGauge {
    public MutableRatioGaugeImpl() {
        this(null);
    }

    public MutableRatioGaugeImpl(@Nullable Number numerator, @Nullable Number denominator) {
        this(null);

        this.setValue(numerator, denominator);
    }

    public MutableRatioGaugeImpl(@Nullable Double value) {
        super(value);
    }

    @Override
    public void setValue(@Nullable Number numerator, @Nullable Number denominator) {
        if ((numerator != null) && (denominator != null)) {
            double doubleNumerator = numerator.doubleValue(), doubleDenominator = denominator.doubleValue();

            if (Double.isFinite(doubleNumerator) && (doubleNumerator >= 0) && Double.isFinite(doubleDenominator) && (doubleDenominator > 0)) {
                this.value = (doubleNumerator / doubleDenominator);
            }
        }
    }
}
