package test;



import org.apache.flink.api.java.tuple.Tuple3;

import java.lang.reflect.Field;

/**
 * description:
 * Created by 铁盾 on 2022/4/14
 */
public class TupleTest {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Class<Tuple3> tuple3Class = Tuple3.class;
        Tuple3 tuple3 = tuple3Class.newInstance();
        Field[] declaredFields = tuple3Class.getDeclaredFields();
        for (int i = 1; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            declaredField.setAccessible(true);
            declaredField.set(tuple3, (char)('a' + i));
        }
        System.out.println(tuple3);
    }
}
