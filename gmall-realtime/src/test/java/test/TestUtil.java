package test;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * description:
 * Created by 铁盾 on 2022/4/9
 */
public class TestUtil {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        tableEnv.sqlQuery("select PROCTIME() proc_time")
                .execute()
                .print();


        Table joinTable = tableEnv.sqlQuery(
                "select \n" +
                        "\t\tl.id l_id,\n" +
                        "\t\tl.tag l_tag,\n" +
                        "\t\tr.tag r_tag\n" +
                        "\tfrom left l \n" +
                        "\tleft join \n" +
                        "\tright r \n" +
                        "\ton l.id = r.id"
        );

//        changelogStream.print("changelogStream>>>");
//
//        retractS.print("retractS");

        env.execute();
    }
}