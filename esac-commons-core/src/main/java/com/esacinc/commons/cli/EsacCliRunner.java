package com.esacinc.commons.cli;

import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContextAware;

public interface EsacCliRunner<T extends AbstractEsacApplication, U extends EsacCliCommand<?>> extends ApplicationContextAware, CommandLineRunner {
    public Map<String, U> getCommands();

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setCommands(U ... cmds);
}
