package fr.uge.confroidutils.converters;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.*;

public class FromBundleToObjectConverter {

    private static Map<Integer, Map.Entry<Field, Object>> referencedObjects;
    private static Map<Integer, Object> alreadyCreatedObjects;

    public static Object convert(Bundle bundle) {
        try {
            alreadyCreatedObjects = new HashMap<>();
            referencedObjects = new HashMap<>();

            Object object = fromBundleToObject(bundle.getBundle("content"));
            for (Integer reference : referencedObjects.keySet()) {
                Map.Entry<Field, Object> entry = referencedObjects.get(reference);
                Log.e("ENTRY", entry.getKey().toString());
                Log.e("ENTRY", entry.getValue().toString());
                entry.getKey().set(entry.getValue(), alreadyCreatedObjects.get(reference));
            }
            return object;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object fromBundleToObject(Bundle bundle) {
        try {
            Class<?> objectClass = Class.forName(bundle.getString("class"));
            Object object = objectClass.newInstance();
            for (Field field : objectClass.getDeclaredFields()) {
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                if (fieldType.equals(List.class) || fieldType.equals(Map.class) || fieldType.equals(Set.class)) {
                    field.set(object, fromBundleToCollection(bundle.getBundle(fieldName), fieldType));
                } else if (bundle.get(field.getName()) instanceof Bundle) {
                    Bundle innerBundle = bundle.getBundle(fieldName);
                    if (innerBundle.containsKey("ref")) {
                        referencedObjects.put(innerBundle.getInt("ref"), new AbstractMap.SimpleEntry<>(field, object));
                    } else {
                        field.set(object, fromBundleToObject(innerBundle));
                    }
                } else if(fieldType.equals(boolean.class)){
                    field.set(object, Boolean.getBoolean((String) bundle.get(fieldName)));
                } else if(fieldType.equals(int.class))
                    field.set(object, Integer.parseInt((String) bundle.get(fieldName)));
                else
                    field.set(object,bundle.get(fieldName));
            }
            alreadyCreatedObjects.put(bundle.getInt("id"), object);
            return object;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object fromBundleToCollection(Bundle bundle, Class<?> type) {
        Log.e("QUI", fromBundleToString(bundle));
        if (type.equals(List.class)) {
            List<Object> list = new ArrayList<>();
            for (String key : bundle.keySet()) {
                Object object =  bundle.get(key);
                if (object instanceof Bundle) {
                    list.add(fromBundleToObject((Bundle) object));
                } else {
                    list.add(bundle.get(key));
                }
            }
            return list;
        } else if (type.equals(Set.class)) {
            Set<Object> set = new HashSet<>();
            for (String key : bundle.keySet()) {
                Object object =  bundle.get(key);
                if (object instanceof Bundle) {
                    set.add(fromBundleToObject((Bundle) object));
                } else {
                    set.add(bundle.get(key));
                }
            }
        } else if (type.equals(Map.class)) {
            Map<Object, Object> map = new HashMap<>();
            for (String key : bundle.keySet()) {
                Object object =  bundle.get(key);
                if (object instanceof Bundle) {
                    map.put(key, fromBundleToObject((Bundle) object));
                } else {
                    map.put(key, object);
                }
            }
            return map;
        }
        return null;
    }

    public static String fromBundleToString(Bundle bundle) {
        return fromBundleToString(bundle, 0);
    }

    private static String fromBundleToString(Bundle bundle, int tabNumber) {
        String content = "";
        for (String key : bundle.keySet()) {
            for (int i = 0; i < tabNumber; i++) {
                content += "\t";
            }
            content += key + ": ";
            Object contentObject = bundle.get(key);
            if (contentObject instanceof Bundle) {
                content += "\n" + fromBundleToString((Bundle) contentObject, tabNumber + 1);
            } else {
                content += contentObject.toString();
            }
            content += "\n";
        }
        return content;
    }

}
