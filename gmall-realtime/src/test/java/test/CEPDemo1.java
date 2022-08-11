package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.realtime.util.KafkaUtil;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import test.bean.LoginEvent;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternFlatSelectFunction;
import org.apache.flink.cep.PatternFlatTimeoutFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description:
 * Created by 铁盾 on 2021/12/21
 */
public class CEPDemo1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

//        SingleOutputStreamOperator<LoginEvent> source = env
//                        .fromElements(
//                        new LoginEvent("user-1", "fail", 1000L),
//                        new LoginEvent("user-1", "fail", 1100L),
//                        new LoginEvent("user-1", "fail", 2000L),
//                        new LoginEvent("user-2", "success", 2500L),
//                        new LoginEvent("user-1", "fail", 3000L)
//                )
        DataStreamSource<String> source = env.addSource(KafkaUtil.getKafkaConsumer("test_cep", "cepDemo1"));
        SingleOutputStreamOperator<LoginEvent> mappedStream = source.map(
                r -> {
                    JSONObject jsonObj = JSON.parseObject(r);
                    return new LoginEvent(jsonObj.getString("a"), jsonObj.getString("b"), jsonObj.getLong("c"));
                }
        );
//        mappedStream.print("mappedStream >>> ");
        SingleOutputStreamOperator<LoginEvent> withWatermarkDS = mappedStream.assignTimestampsAndWatermarks(
                WatermarkStrategy.<LoginEvent>forMonotonousTimestamps()
                        .withTimestampAssigner(
                                new SerializableTimestampAssigner<LoginEvent>() {
                                    @Override
                                    public long extractTimestamp(LoginEvent element, long recordTimestamp) {
//                                        System.out.println(element.ts);
                                        return element.ts;
                                    }
                                }
                        )
        );

        Pattern<LoginEvent, LoginEvent> pattern = Pattern
                .<LoginEvent>begin("first")
                .where(
                        new SimpleCondition<LoginEvent>() {
                            @Override
                            public boolean filter(LoginEvent value) {
                                return value.type.equals("fail");
                            }
                        }
                )
//                .next("second")
                .followedBy("second")
                .where(
                        new SimpleCondition<LoginEvent>() {
                            @Override
                            public boolean filter(LoginEvent ele) {
                                return !ele.type.equals("fail");
//                                return ele.type.equals("fail");
                            }
                        }
                )
                .within(Time.of(10, TimeUnit.SECONDS));

        PatternStream<LoginEvent> cepStream = CEP.pattern(withWatermarkDS.keyBy(r -> r.userId), pattern);
        OutputTag<LoginEvent> outputTag = new OutputTag<LoginEvent>("timeout") {
        };

        SingleOutputStreamOperator<LoginEvent> resultStream = cepStream
                .flatSelect(
                        outputTag,
                        new PatternFlatTimeoutFunction<LoginEvent, LoginEvent>() {
                            @Override
                            public void timeout(Map<String, List<LoginEvent>> pattern, long timeoutTimestamp, Collector<LoginEvent> out) throws Exception {
                                LoginEvent first = pattern.get("first").get(0);
                                    out.collect(first);


                            }
                        },
                        new PatternFlatSelectFunction<LoginEvent, LoginEvent>() {
                            @Override
                            public void flatSelect(Map<String, List<LoginEvent>> pattern, Collector<LoginEvent> out) throws Exception {
                                LoginEvent first = pattern.get("first").get(0);
                                out.collect(first);
                            }
                        }
                );
        DataStream<LoginEvent> timeoutStream = resultStream.getSideOutput(outputTag);
        resultStream.print("resultStream >>> ");
        timeoutStream

                .print("time_out >>> ");


        env.execute();
    }
}
