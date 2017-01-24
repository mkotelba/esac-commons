package com.esacinc.commons.crypto.ssl.logging;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.crypto.beans.TaggedBean;
import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.springframework.core.PriorityOrdered;

public enum SslDebugPrintStreamType implements NamedBean, PriorityOrdered, TaggedBean {
    OUT(1, () -> System.out, System::setOut), ERR(2, () -> System.err, System::setErr);

    private final int tag;
    private final Supplier<PrintStream> getter;
    private final Consumer<PrintStream> setter;
    private final String name;

    private SslDebugPrintStreamType(int tag, Supplier<PrintStream> getter, Consumer<PrintStream> setter) {
        this.tag = tag;
        this.getter = getter;
        this.setter = setter;
        this.name = this.name().toLowerCase();
    }

    public Supplier<PrintStream> getGetter() {
        return this.getter;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getOrder() {
        return this.tag;
    }

    public Consumer<PrintStream> getSetter() {
        return this.setter;
    }

    @Override
    public int getTag() {
        return this.tag;
    }
}
