package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.constants;

import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;

public interface DatasourceName extends ShardingjdbcDatasourceNames {

    String LOG = "datasource-log";
    String ORDER_MASTER = "shardingordermaster";
    String ORDER_SLAVE = "shardingorderslave";

}