package com.atguigu.gmall.realtime.common;

/**
 * description:
 * Created by 铁盾 on 2022/3/15
 */
public class GmallConfig {

    // Phoenix库名
    public static final String HBASE_SCHEMA = "GMALL2022_REALTIME";

    // Phoenix驱动
    public static final String PHOENIX_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";

    // Phoenix连接参数
    public static final String PHOENIX_SERVER = "jdbc:phoenix:hadoop102,hadoop103,hadoop104:2181";

    // ClickHouse 驱动
    public static final String CLICKHOUSE_DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";

    // ClickHouse 连接 URL
    public static final String CLICKHOUSE_URL = "jdbc:clickhouse://hadoop102:8123/gmall_rebuild";

    // Doris 数据库名
    public static final String DORIS_SCHEMA = "gmall_realtime";

    // Doris 驱动
    public static final String DORIS_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Doris URL
    public static final String DORIS_URL =
            "jdbc:mysql://hadoop102:9030/gmall_realtime?user=root&password=000000";
}

