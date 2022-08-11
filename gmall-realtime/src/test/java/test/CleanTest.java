package test;

import com.atguigu.gmall.realtime.util.KafkaUtil;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * description:
 * Created by 铁盾 on 2022/5/9
 */
public class CleanTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        tableEnv.executeSql("create table l_r_t_tst(\n" +
                "l_id string,\n" +
                "tag_left string,\n" +
                "tag_right string,\n" +
                "`type` string)" + KafkaUtil.getKafkaDDL("l_r_t_tst", "clean_test"));

        Table resultTable = tableEnv.sqlQuery("select * from l_r_t_tst where 1 = 1");
        tableEnv.createTemporaryView("result_table", resultTable);

        tableEnv.executeSql("create table l_r_clean_tst(\n" +
                "l_id string,\n" +
                "tag_left string,\n" +
                "tag_right string,\n" +
                "`type` string,\n" +
                "primary key(l_id) not enforced)" + KafkaUtil.getUpsertKafkaDDL("l_r_clean_tst"));

        tableEnv.executeSql("insert into l_r_clean_tst select * from result_table");

//        env.execute();

        


    }


}
