package fr.uge.confroidutils.converters;

import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.*;

public class FromBundleToObjectConverter {

    private static List<Map.Entry<String, Object>> referencedObjects;
    private static Map<String, Object> alreadyCreatedObjects;

    public static Object convert(Bundle bundle) {
        try {
            alreadyCreatedObjects = new HashMap<>();
            referencedObjects = new ArrayList<>();
            Object object = fromBundleToObject(bundle.getBundle("content"));
            for (Map.Entry<String, Object> reference : referencedObjects) {
                String referencedId = reference.getKey();
                Object value = reference.getValue();
                if (value instanceof Map.Entry) {
                    Map.Entry<Object, Object> entry = (Map.Entry) value;
                    if (entry.getKey().getClass().equals(Field.class)) {
                        ((Field) entry.getKey()).set(entry.getValue(), alreadyCreatedObjects.get(referencedId));
                    } else {
                        ((Map<Object, Object>) entry.getKey()).put(entry.getValue(), alreadyCreatedObjects.get(referencedId));
                    }
                } else if (value instanceof List) {
                    ((List<Object>) value).add(alreadyCreatedObjects.get(referencedId));
                } else if (value instanceof Set) {
                    ((Set<Object>) value).add(alreadyCreatedObjects.get(referencedId));
                }
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
                putObjectInField(object, field, bundle);
            }
            alreadyCreatedObjects.put(bundle.getString("id"), object);
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

    private static void putObjectInField(Object object, Field field, Bundle bundle) {
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();
        try {
            if (fieldType.equals(String.class)) {
                field.set(object, bundle.get(fieldName));
            } else if (fieldType.equals(byte.class)) {
                field.setByte(object, Byte.parseByte(bundle.getString(fieldName)));
            } else if (fieldType.equals(short.class)) {
                field.setShort(object, Short.parseShort(bundle.getString(fieldName)));
            } else if (fieldType.equals(int.class)) {
                field.setInt(object, Integer.parseInt(bundle.getString(fieldName)));
            } else if (fieldType.equals(float.class)) {
                field.setFloat(object, Float.parseFloat(bundle.getString(fieldName)));
            } else if (fieldType.equals(double.class)) {
                field.setDouble(object, Double.parseDouble(bundle.getString(fieldName)));
            } else if (fieldType.equals(boolean.class)) {
                field.setBoolean(object, Boolean.parseBoolean(bundle.getString(fieldName)));
            } else if (fieldType.equals(List.class) || fieldType.equals(Map.class) || fieldType.equals(Set.class)) {
                field.set(object, fromBundleToCollection(bundle.getBundle(fieldName), fieldType));
            } else if (bundle.get(field.getName()) instanceof Bundle) {
                Bundle innerBundle = bundle.getBundle(fieldName);
                if (innerBundle.containsKey("ref")) {
                    referencedObjects.add(new AbstractMap.SimpleEntry<>(innerBundle.getString("ref"), new AbstractMap.SimpleEntry<>(field, object)));
                } else {
                    field.set(object, fromBundleToObject(innerBundle));
                }
            } else {
                field.set(object, bundle.get(fieldName));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static Object fromBundleToCollection(Bundle bundle, Class<?> type) {
        if (type.equals(List.class)) {
            List<Object> list = new ArrayList<>();
            for (String key : bundle.keySet()) {
                Object object =  bundle.get(key);
                if (object instanceof Bundle) {
                    Bundle innerBundle = (Bundle) object;
                    if (innerBundle.containsKey("ref")) {
                        referencedObjects.add(new AbstractMap.SimpleEntry<>(innerBundle.getString("ref"), list));
                    } else {
                        list.add(fromBundleToObject((Bundle) object));
                    }
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
                    Bundle innerBundle = (Bundle) object;
                    if (innerBundle.containsKey("ref")) {
                        referencedObjects.add(new AbstractMap.SimpleEntry<>(innerBundle.getString("ref"), set));
                    } else {
                        set.add(fromBundleToObject((Bundle) object));
                    }
                } else {
                    set.add(bundle.get(key));
                }
            }
            return set;
        } else if (type.equals(Map.class)) {
            Map<Object, Object> map = new HashMap<>();
            for (String key : bundle.keySet()) {
                Object object =  bundle.get(key);
                if (object instanceof Bundle) {
                    Bundle innerBundle = (Bundle) object;
                    if (innerBundle.containsKey("ref")) {
                        referencedObjects.add(new AbstractMap.SimpleEntry<>(innerBundle.getString("ref"), new AbstractMap.SimpleEntry<>(map, key)));
                    } else {
                        map.put(key, fromBundleToObject((Bundle) object));
                    }
                } else {
                    map.put(key, object);
                }
            }
            return map;
        }
        return null;
    }
}
