package test;

import com.atguigu.gmall.realtime.util.KafkaUtil;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * description:
 * Created by 铁盾 on 2022/5/9
 */
public class LeftJoinTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        tableEnv.executeSql("" +
                "create table left_table(" +
                "id string, \n" +
                "tag string, \n" +
                "`type` string)" + KafkaUtil.getKafkaDDL("left_table", "left"));
        tableEnv.executeSql("" +
                "create table right_table(" +
                "id string, \n" +
                "tag string, \n" +
                "`type` string)" + KafkaUtil.getKafkaDDL("right_table", "right"));

        Table joinTable = tableEnv.sqlQuery("select l.id l_id, l.tag tag_left, " +
                "r.tag tag_right,  l.`type` `type`\n" +
                "from left_table l left join right_table r on l.id=r.id");

        tableEnv.createTemporaryView("joinTable", joinTable);

//        joinTable.execute().print();

        tableEnv.executeSql("create table l_r_t_tst(\n" +
                "l_id string,\n" +
                "tag_left string,\n" +
                "tag_right string,\n" +
                "`type` string,\n" +
                "primary key(l_id) not enforced)" + KafkaUtil.getUpsertKafkaDDL("l_r_t_tst"));

        tableEnv.executeSql("insert into l_r_t_tst\n" +
                "select * from joinTable");
//        env.execute();
    }
}
