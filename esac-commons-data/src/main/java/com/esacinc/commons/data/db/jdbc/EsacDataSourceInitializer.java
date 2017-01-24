package com.esacinc.commons.data.db.jdbc;

import com.esacinc.commons.data.db.jdbc.impl.EsacDataSource;

public interface EsacDataSourceInitializer {
    public void initializeDataSource(EsacDataSource dataSrc);
}
