package fr.uge.confroidutils.converters;

import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FromObjectToBundleConverter {

    private static int id = 0;
    private static Map<Object, Integer> alreadyCreatedBundles;

    public static Bundle convert(Object object) {
        id = 0;
        alreadyCreatedBundles = new HashMap<>();
        return fromObjectToBundle(object);
    }

    private static Bundle fromObjectToBundle(Object object) {
        try {
            Bundle bundle = new Bundle();
            if (alreadyCreatedBundles.containsKey(object)) {
                bundle.putInt("ref", alreadyCreatedBundles.get(object));
            } else {
                alreadyCreatedBundles.put(object, ++id);
                bundle.putInt("id", id);
                Class<?> objectClass = object.getClass();
                bundle.putString("class", objectClass.getName());
                for (Field field : objectClass.getDeclaredFields()) {
                    putObjectInBundle(bundle, field.get(object), field.getName());
                }
            }
            return bundle;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bundle fromCollectionToBundle(Object object) {
        Bundle bundle = new Bundle();
        if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            for (Object elem : list) {
                putObjectInBundle(bundle, elem, list.indexOf(elem) + "");
            }
        } else if (object instanceof Set) {
            Set<Object> set = (Set<Object>) object;
            int index = 0;
            for (Object elem : set) {
                putObjectInBundle(bundle, elem, index++ + "");
            }
        } else if (object instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) object;
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                putObjectInBundle(bundle, value, key.toString());
            }
        }
        return bundle;
    }

    private static void putObjectInBundle(Bundle bundle, Object object, String key) {
        if (object instanceof String) {
            bundle.putString(key, (String) object);
        } else if (object instanceof Byte) {
            bundle.putByte(key, (byte) object);
        } else if (object instanceof Short) {
            bundle.putShort(key, (short) object);
        } else if (object instanceof Integer) {
            bundle.putInt(key, (int) object);
        } else if (object instanceof Float) {
            bundle.putFloat(key, (float) object);
        } else if (object instanceof Double) {
            bundle.putDouble(key, (double) object);
        } else if (object instanceof Boolean) {
            bundle.putBoolean(key, (boolean) object);
        } else if (object instanceof List || object instanceof Map || object instanceof Set) {
            bundle.putBundle(key, fromCollectionToBundle(object));
        } else if (object instanceof Bundle) {
            bundle.putBundle(key, (Bundle) object);
        } else {
            bundle.putBundle(key, fromObjectToBundle(object));
        }
    }

}
