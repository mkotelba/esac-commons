package com.esacinc.commons.cli.impl;

import com.esacinc.commons.beans.NamedBean;
import com.esacinc.commons.cli.EsacCliCommand;
import com.esacinc.commons.cli.EsacCliCommandOptions;
import com.esacinc.commons.cli.EsacCliRunner;
import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.utils.EsacStreamUtils;
import com.esacinc.commons.utils.EsacStringUtils;
import com.github.sebhoss.warnings.CompilerWarnings;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class AbstractEsacCliRunner<T extends AbstractEsacApplication, U extends EsacCliCommand<?>> implements EsacCliRunner<T, U> {
    @Autowired
    protected T app;

    protected ConfigurableApplicationContext appContext;
    protected Map<String, U> cmds;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractEsacCliRunner.class);

    @Override
    public void run(String ... args) throws Exception {
        String cmdName = this.app.getCommand();

        if (StringUtils.isBlank(cmdName)) {
            LOGGER.debug("No CLI command specified.");

            return;
        } else if (!this.cmds.containsKey(cmdName)) {
            LOGGER.error(String.format("Unknown CLI command (name=%s).", cmdName));

            SpringApplication.exit(this.appContext, () -> 1);
        }

        EsacCliCommand<?> cmd = this.cmds.get(cmdName);
        EsacCliCommandOptions cmdOpts = cmd.getOptions();
        CmdLineParser parser = new CmdLineParser(cmdOpts);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            LOGGER.error(String.format("Unable to parse CLI command (name=%s) option(s).", cmdName), e);

            SpringApplication.exit(this.appContext, () -> 1);
        }

        if (cmdOpts.isHelp()) {
            StringWriter usageWriter = new StringWriter();

            parser.printUsage(usageWriter, null);

            LOGGER.info(usageWriter.toString());

            SpringApplication.exit(this.appContext, () -> 0);
        } else {
            LOGGER.debug(
                String.format("Executing CLI command (name=%s, args=[%s]).", cmdName, Stream.of(args).collect(Collectors.joining(EsacStringUtils.ITEM_DELIM))));

            cmd.execute();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((ConfigurableApplicationContext) appContext);
    }

    @Override
    public Map<String, U> getCommands() {
        return this.cmds;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setCommands(U ... cmds) {
        this.cmds = Stream.of(cmds).collect(EsacStreamUtils.toMap(NamedBean::getName, Function.identity(), TreeMap::new));
    }
}
