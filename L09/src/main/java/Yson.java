import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Yson {
    private static Logger logger = LoggerFactory.getLogger(Yson.class);

    public static String toJson(Object o) {
        return JSONValue.toJSONString(toValue(o));
    }

    public static Object toValue(Object o) {
        if (o == null) {
            return null;
        }

        Class type = o.getClass();

        List<Class<? extends Object>> list = new ArrayList<>();
        Collections.addAll(list, Integer.class, Double.class, Long.class, Short.class, Byte.class, String.class,Character.class);

        if(list.contains(type)){
            return o;
        }
       
        if (o.getClass().isArray()) {
            return getJsonArrayFromCollection(getListFromArr(o));
        }

        if (o instanceof Map) {
            return o;
        }

        if (o instanceof Iterable) {
            return getJsonArrayFromCollection(o);
        }

        if ((o.getClass().isEnum())) {
            return o;
        }

        return getJsonObjectFromObject(o);
    }

    public static JSONArray getJsonArrayFromCollection(Object iterable) {
        JSONArray jsonAr = new JSONArray();
        for (Object fieldVal : (Iterable) iterable) {
            jsonAr.add(toValue(fieldVal));
        }

        return jsonAr;
    }

    public static JSONObject getJsonObjectFromObject(Object o) {
        Class type = o.getClass();
        List<Field> fields = getObjectFields(new ArrayList<>(), type);
        JSONObject jsonObject = new JSONObject();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                jsonObject.put(field.getName(), toValue(field.get(o)));
            }

        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }

        return jsonObject;
    }


    public static List<Field> getObjectFields(List<Field> fields, Class type) {
        Field[] currentFields = type.getDeclaredFields();
        for (int i = 0; i < currentFields.length; i++) {
                fields.add(currentFields[i]);
        }
        if (type.getSuperclass() != null) {
            getObjectFields(fields, type.getSuperclass());
        }
        return fields;
    }

    private static List getListFromArr(Object arr) {
        List list = new ArrayList<>();
        for (int i = 0; i <  Array.getLength(arr); i++) {
            list.add(Array.get(arr, i));
        }
        return list;
    }

}
