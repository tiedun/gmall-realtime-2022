package test;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.realtime.common.GmallConfig;
import com.atguigu.gmall.realtime.util.DruidDSUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("a", "1");
//        jsonObj.put("b", "2");
//        filter(jsonObj, "a");
//        System.out.println(jsonObj);

//        DruidPooledConnection conn = DruidDSUtil.createDataSource().getConnection();
//
//        conn.prepareStatement("upsert into EDU_REALTIME.test(id,name) values('3', '哈呗')").executeUpdate();

        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        Connection conn = DriverManager.getConnection(GmallConfig.PHOENIX_DRIVER);
        conn.prepareStatement("upsert into EDU_REALTIME.test(id,name) values('4', '哈呗')").executeUpdate();
//
    }

    private static void filter(JSONObject jsonObj, String sinkColumns) {
//        Set<Map.Entry<String, Object>> entrySet = jsonObj.entrySet();
//        entrySet.removeIf(r -> !sinkColumns.contains(r.getKey()));
    }
}