//
//package com.atguigu.gmall.realtime.test;
//import blink.beans.GasNo3Singal;
//import blink.beans.NewIbatterQuiescent;
//import blink.utils.MyDatahubUtil;
//import com.aliyun.datahub.client.model.*;
//import org.apache.flink.api.common.functions.*;
//import org.apache.flink.api.common.restartstrategy.RestartStrategies;
//import org.apache.flink.api.common.state.MapState;
//import org.apache.flink.api.common.state.ValueState;
//import org.apache.flink.api.common.state.ValueStateDescriptor;
//import org.apache.flink.api.common.time.Time;
//import org.apache.flink.api.common.typeinfo.Types;
//import org.apache.flink.api.java.tuple.Tuple11;
//import org.apache.flink.api.java.tuple.Tuple8;
//import org.apache.flink.configuration.Configuration;
//import org.apache.flink.streaming.api.TimeCharacteristic;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
//import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
//import org.apache.flink.streaming.api.watermark.Watermark;
//import org.apache.flink.util.Collector;
//import scala.Tuple7;
//import javax.annotation.Nullable;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//// hev v51 v61 静态电流大新逻辑
//public class newNo2_IbatterQuiescent {
//    private static final Long datahubStartInMs = 1657814400000L;//设置消费的启动位点对应的时间
//    public static void main(String[] args) throws Exception {
//// TODO 1.获取flink环境
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
///*env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
//0, // 最多重启3次数
//Time.of(3, TimeUnit.SECONDS) // 重启时间间隔
//));*/
//// TODO 2.获取数据流
//        DataStreamSource<List<RecordEntry>> sourceDS = env.addSource(MyDatahubUtil.getDatahubSource("tb_tsp_t5_base_signal_gas_no3_data", datahubStartInMs)).setParallelism(3);
//// TODO 3.转换数据格式
//        SingleOutputStreamOperator<Tuple8<String, String, String, String, String, String, String, String>> flatMapDS = sourceDS.flatMap(new FlatMapFunction<List<RecordEntry>, Tuple8<String, String, String, String, String, String, String, String>>() {
//            @Override
//            public void flatMap(List<RecordEntry> value, Collector<Tuple8<String, String, String, String, String, String, String, String>> out) throws Exception {
//                for (RecordEntry recordEntry : value) {
//                    out.collect(getTuple8(recordEntry));
//                }
//            }
//        });
//// TODO 4.过滤脏数据 时间戳小于启动时间的不要
//        SingleOutputStreamOperator<Tuple8<String, String, String, String, String, String, String, String>> filter
//                = flatMapDS.filter(r -> r != null && !r.f1.equals("error") && !"".equals(r.f0) && !"".equals(r.f2) && Long.parseLong(r.f2) >= datahubStartInMs);
//// TODO 5.数据过滤 筛出拿铁和玛奇朵车型系统电源模式不为空或者null的数据，q_discharge信号10秒上传一条 要筛出不为null的数据 否则无法计算
//        SingleOutputStreamOperator<Tuple8<String, String, String, String, String, String, String, String>> filterDS
//                = filter.filter(value -> value != null && ("WEY 拿铁".equals(value.f0) || "WEY 玛奇朵".equals(value.f0)) &&
//                !("-".equals(value.f3) || value.f3.equals("")) && !("-".equals(value.f4) || value.f4.equals("")));
//// TODO 6.筛选数据
//        filterDS.keyBy(r -> r.f1)
//                .process(new KeyedProcessFunction<String, Tuple8<String, String, String, String, String, String, String, String>, NewIbatterQuiescent>() {
//                    private ValueState<Tuple8<String, String, String, String, String, String, String, String>> preState;
//                    @Override
//                    public void open(Configuration parameters) throws Exception {
//                        super.open(parameters);
//                        preState = getRuntimeContext().getState(
//                                new ValueStateDescriptor<Tuple8<String, String, String, String, String, String, String, String>>(
//                                        "pre", Types.TUPLE(Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.STRING, Types.STRING)
//                                )
//                        );
//                    }
//                    @Override
//                    public void processElement(Tuple8<String, String, String, String, String, String, String, String> value, Context ctx, Collector<NewIbatterQuiescent> out) throws Exception {
//                        if (value.f3 != "" && !"-".equals(value.f3) && value.f3.length() > 0) {
//                            if (preState.value() != null && preState.value().f1.length() > 0) {
////确保不过来乱序数据
//                                if (Long.parseLong(value.f2) > Long.parseLong(preState.value().f2)) {
//                                    long timeDiff = Long.parseLong(value.f2) - Long.parseLong(preState.value().f2);
//                                    if (timeDiff > 1000 * 60 * 60 * 4) {
//                                        double chaZhi = Double.parseDouble(value.f4) - Double.parseDouble(preState.value().f4);
////这是小时 四舍五入保留两位小数
//                                        double l = (Double.parseDouble(value.f2) - Double.parseDouble(preState.value().f2)) / (1000 * 60 * 60);
//                                        BigDecimal bg = new BigDecimal(l);
//                                        double hour = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                                        Tuple8<String, String, String, String, String, String, String, String> pre = preState.value();
//                                        if (chaZhi >= 0) {
//                                            double v = chaZhi / hour;
//                                            if (v > 0.15) {
//                                                out.collect(new NewIbatterQuiescent(
//                                                                pre.f1,
//                                                                pre.f2,//休眠前时间
//                                                                value.f2,
//                                                                chaZhi + "",
//                                                                hour + "",
//                                                                v + "",
//                                                                pre.f4,//休眠前q_discharge
//                                                                value.f4,
//                                                                pre.f7,//休眠前u_batt
//                                                                value.f7,
//                                                                pre.f5,//休眠前soc
//                                                                value.f5,
//                                                                pre.f6,//休眠前soc_state
//                                                                value.f6
//                                                        )
//                                                );
//                                            }
//                                        }
//                                    }
//                                    preState.update(value);
//                                }
//                            } else {
//                                preState.update(value);
//                            }
//                        }
//                    }
//                })
//                .map(new RichMapFunction<NewIbatterQuiescent, NewIbatterQuiescent>() {
////时间转换 日期格式化转换对象
//                    private SimpleDateFormat dateFormat;
//                    RecordSchema schema = null;
//                    @Override
//                    public void open(Configuration parameters) throws Exception {
//                        super.open(parameters);
//                        super.open(parameters);
//                        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    }
//                    @Override
//                    public NewIbatterQuiescent map(NewIbatterQuiescent value) throws Exception {
//                        return new NewIbatterQuiescent(
//                                value.vin,
//                                dateFormat.format(new Date(Long.parseLong(value.start_time))),
//                                dateFormat.format(new Date(Long.parseLong(value.end_time))),
//                                value.q_discharge_diff,
//                                value.hour_diff,
//                                value.ratio,
//                                value.start_q_discharge,
//                                value.end_q_discharge,
//                                value.start_u_batt,
//                                value.end_u_batt,
//                                value.start_soc,
//                                value.end_soc,
//                                value.start_soc_state,
//                                value.end_soc_state
//                        );
//                    }
//                })
//                .print();
///*
//.map(new RichMapFunction<NewIbatterQuiescent, RecordEntry>() {
////时间转换 日期格式化转换对象
//private SimpleDateFormat dateFormat;
//RecordSchema schema = null;
//@Override
//public void open(Configuration parameters) throws Exception {
//super.open(parameters);
//dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//schema = new RecordSchema();
//schema.addField(new Field("vin", FieldType.STRING));
//schema.addField(new Field("start_time", FieldType.STRING));
//schema.addField(new Field("end_time", FieldType.STRING));
//schema.addField(new Field("q_discharge_diff", FieldType.STRING));
//schema.addField(new Field("hour_diff", FieldType.STRING));
//schema.addField(new Field("ratio", FieldType.STRING));
//schema.addField(new Field("start_q_discharge", FieldType.STRING));
//schema.addField(new Field("end_q_discharge", FieldType.STRING));
//schema.addField(new Field("start_u_batt", FieldType.STRING));
//schema.addField(new Field("end_u_batt", FieldType.STRING));
//schema.addField(new Field("start_soc", FieldType.STRING));
//schema.addField(new Field("end_soc", FieldType.STRING));
//schema.addField(new Field("start_soc_state", FieldType.STRING));
//schema.addField(new Field("end_soc_state", FieldType.STRING));
//}
//@Override
//public RecordEntry map(NewIbatterQuiescent value) throws Exception {
//RecordEntry entry = new RecordEntry();
//TupleRecordData tupleRecordData = new TupleRecordData(schema);
//tupleRecordData.setField("vin", value.vin);
//tupleRecordData.setField("start_time", dateFormat.format(new Date(Long.parseLong(value.start_time))));
//tupleRecordData.setField("end_time", dateFormat.format(new Date(Long.parseLong(value.end_time))));
//tupleRecordData.setField("q_discharge_diff", value.q_discharge_diff);
//tupleRecordData.setField("hour_diff", value.hour_diff);
//tupleRecordData.setField("ratio", value.ratio);
//tupleRecordData.setField("start_q_discharge", value.start_q_discharge);
//tupleRecordData.setField("end_q_discharge", value.end_q_discharge);
//tupleRecordData.setField("start_u_batt", value.start_u_batt);
//tupleRecordData.setField("end_u_batt", value.end_u_batt);
//tupleRecordData.setField("start_soc", value.start_soc);
//tupleRecordData.setField("end_soc", value.end_soc);
//tupleRecordData.setField("start_soc_state", value.start_soc_state);
//tupleRecordData.setField("end_soc_state", value.end_soc_state);
//entry.setRecordData(tupleRecordData);
//return entry;
//}
//})
//.addSink(MyDatahubUtil.getDatahubSink("datahub_ti_tsp_gas_no3_ibatter_quiescent_data"));*/
//        env.execute();
//    }
//    public static Tuple8<String, String, String, String, String, String, String, String> getTuple8(RecordEntry recordEntry) {
//        TupleRecordData recordData = (TupleRecordData) (recordEntry.getRecordData());
//        if (recordData != null) {
//            String system_power_mode = "";
//            String q_discharge = "0";
//            String state_of_charge = "";
//            String soc_state = "";
//            String u_batt = "";
//            String vin = "error";
//            String item_time = "0";
//            String model_name = "";
//            if (recordData.getField("system_power_mode") != null) {
//                system_power_mode = (String) recordData.getField("system_power_mode");
//            }
//            if (recordData.getField("q_discharge") != null) {
//                q_discharge = (String) recordData.getField("q_discharge");
//            }
//            if (recordData.getField("state_of_charge") != null) {
//                state_of_charge = (String) recordData.getField("state_of_charge");
//            }
//            if (recordData.getField("soc_state") != null) {
//                soc_state = (String) recordData.getField("soc_state");
//            }
//            if (recordData.getField("u_batt") != null) {
//                u_batt = (String) recordData.getField("u_batt");
//            }
//            if (recordData.getField("vin") != null) {
//                vin = (String) recordData.getField("vin");
//            }
//            if (recordData.getField("item_time") != null) {
//                item_time = (String) recordData.getField("item_time");
//            }
//            if (recordData.getField("model_name") != null) {
//                model_name = (String) recordData.getField("model_name");
//            }
//            return new Tuple8<String, String, String, String, String, String, String, String>(
//                    model_name,
//                    vin,
//                    item_time,
//                    system_power_mode,
//                    q_discharge,
//                    state_of_charge,
//                    soc_state,
//                    u_batt
//            );
//        } else {
//            return new Tuple8<String, String, String, String, String, String, String, String>(
//                    "",
//                    "error",
//                    "0",
//                    "",
//                    "0",
//                    "",
//                    "",
//                    ""
//            );
//        }
//    }
//}