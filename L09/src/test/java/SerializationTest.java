import com.google.gson.Gson;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testobjects.CustomEnum;
import testobjects.CustomObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class SerializationTest {
    private static Logger logger = LoggerFactory.getLogger(SerializationTest.class);
    Gson gson = new Gson();
    Yson yjson = new Yson();


    @DataProvider
    public static Object[] dataProvider() {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, "apple", "pear");
        return new Object[]{new CustomObject(), 1.2, "qwe", CustomEnum.ENUM3,  list, 12000L, 'a', 12};
    }


    @Test
    @UseDataProvider("dataProvider")
    public void serializationTest(Object src) {
        String resultStr = yjson.toJson(src);
        logger.info("Сериализованная строка : {}", resultStr);
        assertTrue(src.equals(gson.fromJson(resultStr, src.getClass())));
    }

    @Test
    public void arraySerializationTest() {
        String[] src = new String[]{"first", "second"};
        String resultStr = yjson.toJson(src);
        logger.info("Сериализованная строка : {}", resultStr);
        Arrays.equals(src, gson.fromJson(resultStr, src.getClass()));
    }
}
