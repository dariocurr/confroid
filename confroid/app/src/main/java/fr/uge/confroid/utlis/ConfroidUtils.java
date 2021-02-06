package fr.uge.confroid.utlis;

import android.content.Context;
import android.os.Bundle;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface ConfroidUtils {

    default Bundle toBundle(Object object) {
        Bundle bundle = new Bundle();
        if (object instanceof List) {
            List list = (List) object;
            for (Object elem : list) {
                if (elem instanceof String) {
                    bundle.putString(list.indexOf(elem) + "", (String) elem);
                } else if (elem instanceof Byte) {
                    bundle.putByte(list.indexOf(elem) + "", (byte) elem);
                } else if (elem instanceof Integer) {
                    bundle.putInt(list.indexOf(elem) + "", (int) elem);
                } else if (elem instanceof Float) {
                    bundle.putFloat(list.indexOf(elem) + "", (float) elem);
                } else if (elem instanceof Boolean) {
                    bundle.putBoolean(list.indexOf(elem) + "", (boolean) elem);
                } else if (elem instanceof List || elem instanceof Map) {
                    bundle.putBundle(list.indexOf(elem) + "", this.toBundle(elem));
                }
            }
        } else if (object instanceof Map) {
            Map map = (Map) object;
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                if (value instanceof String) {
                    bundle.putString(key + "", (String) value);
                } else if (value instanceof Byte) {
                    bundle.putByte(key + "" + "", (byte) value);
                } else if (value instanceof Integer) {
                    bundle.putInt(key + "" + "", (int) value);
                } else if (value instanceof Float) {
                    bundle.putFloat(key + "" + "", (float) value);
                } else if (value instanceof Boolean) {
                    bundle.putBoolean(key + "" + "", (boolean) value);
                } else if (value instanceof List || value instanceof Map) {
                    bundle.putBundle(key + "", this.toBundle(value));
                }
            }
        }
        return bundle;
    }

    default void saveConfiguration (Context context, String name, Object value, String versionName) {

    }

    default <T> void loadConfiguration (Context context, String name, String version, Consumer<T> callback) {

    }

    default <T> void subscribeConfiguration (Context context, String name, Consumer <T> callback) {

    }

    default <T> void cancelConfigurationSubscription (Context context, Consumer <T> callback) {

    }

    default void getConfigurationVersions (Context context, String name, Consumer <List <Version>> callback) {
        
    }

}
