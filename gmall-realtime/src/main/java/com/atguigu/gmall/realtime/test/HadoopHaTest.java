package com.atguigu.gmall.realtime.test;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * description:
 * Created by 铁盾 on 2022/7/15
 */
public class HadoopHaTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStateBackend(new HashMapStateBackend());
        env.getCheckpointConfig().setCheckpointStorage("hdfs://mycluster/test/ck");

        System.setProperty("HADOOP_USER_NAME", "atguigu");

        DataStreamSource<String> source = env.fromElements(
                "hello world",
                "hello hi"
        );

        source.flatMap(
                (FlatMapFunction<String, String>) (value, out) -> {
                    String[] arr = value.split(" ");
                    out.collect(arr[0]);
                    out.collect(arr[1]);
                }
        )
                .returns(TypeInformation.of(String.class))
                .map(r -> Tuple2.of(r, 1))
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(r -> r.f0)
                .reduce(
                        new ReduceFunction<Tuple2<String, Integer>>() {
                            @Override
                            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                                return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                            }
                        }
                )
                .print();




        env.execute();
    }
}
