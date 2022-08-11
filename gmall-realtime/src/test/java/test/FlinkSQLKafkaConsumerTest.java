package test;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * description:
 * Created by 铁盾 on 2022/4/12
 */
public class FlinkSQLKafkaConsumerTest {
    public static void main(String[] args) throws Exception {
        // TODO 1. 环境准备
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // TODO 2. 启用状态后端
//        env.enableCheckpointing(3000L, CheckpointingMode.EXACTLY_ONCE);
//        env.getCheckpointConfig().setCheckpointTimeout(60 * 1000L);
//        env.getCheckpointConfig().enableExternalizedCheckpoints(
//                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION
//        );
//        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(3000L);
//        env.setRestartStrategy(
//                RestartStrategies.failureRateRestart(3, Time.days(1L), Time.minutes(3L))
//        );
//        env.setStateBackend(new HashMapStateBackend());
//        env.getCheckpointConfig().setCheckpointStorage("hdfs://hadoop102:8020/ck");
//        System.setProperty("HADOOP_USER_NAME", "atguigu");
//
//        tableEnv.getConfig().setLocalTimeZone(ZoneId.of("GMT+8"));

        // TODO 3. 从 Kafka 读取业务数据，封装为 Flink SQL 表
//        tableEnv.executeSql("create table topic_db(" +
//                "`database` String,\n" +
//                "`table` String,\n" +
//                "`type` String,\n" +
//                "`data` map<String, String>,\n" +
//                "`proc_time` as PROCTIME(),\n" +
//                "`ts` string\n" +
//                ")" + KafkaUtil.getKafkaDDL("topic_db", "dwd_trade_order_detail"));
//
//        tableEnv.sqlQuery("select * from topic_db")
//                .execute()
//                .print();

//        tableEnv.executeSql("create table test(id string)");
//        tableEnv.executeSql("insert into test values('哈哈哈')");

        tableEnv.sqlQuery("select localtimestamp," +
                "current_timestamp," +
                "now()," +
                "current_row_timestamp()")
                .execute()
                .print();
//        env.execute();
    }
}
