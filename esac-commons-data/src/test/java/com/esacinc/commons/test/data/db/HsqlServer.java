package com.esacinc.commons.test.data.db;

import com.esacinc.commons.data.db.jdbc.EsacDataSourceInitializer;
import com.esacinc.commons.data.db.jdbc.impl.EsacDataSourceConfig;
import com.esacinc.commons.test.beans.EsacServer;
import com.esacinc.commons.transform.impl.ResourceSource;
import java.io.File;
import org.springframework.context.EmbeddedValueResolverAware;

public interface HsqlServer extends EmbeddedValueResolverAware, EsacDataSourceInitializer, EsacServer {
    public String getDatabaseName();

    public void setDatabaseName(String dbName);

    public EsacDataSourceConfig getDataSourceConfiguration();

    public void setDataSourceConfiguration(EsacDataSourceConfig dataSrcConfig);

    public File getDirectory();

    public void setDirectory(File dir);

    public ResourceSource[] getInitializationScriptSources();

    public void setInitializationScriptSources(ResourceSource ... initScriptSrcs);
}
