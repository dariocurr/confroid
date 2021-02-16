package fr.uge.shopping;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.shopping.model.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShoppingPreferences prefs = new ShoppingPreferences ();
        ShippingAddress address1 = new ShippingAddress ("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
        ShippingAddress address2 = new ShippingAddress ("Bugdroid", "Rue des tares au nougat", "Lollipop City", "Oreo Country");
        BillingDetails billingDetail = new BillingDetails ("Bugdroid", "123456789", 12, 2021, 123);
        prefs.shoppingInfo.put ("home", new ShoppingInfo(address1,  billingDetail, true));
        prefs.shoppingInfo.put ("work", new ShoppingInfo(address2,  billingDetail, false));
        Bundle bundle = fromObjectToBundle(prefs, 1);
        try {
            Log.e("Bundle", fromBundleToJson(bundle).toString());
            Log.e("Object", fromBundleToObject(bundle).equals(prefs) + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static Object fromBundleToObject(Bundle bundle) {
        try {
            Class objectClass = Class.forName(bundle.getString("class"));
            Object object = objectClass.newInstance();
            for (Field field : objectClass.getDeclaredFields()) {
                if (field.getType().equals(List.class) || field.getType().equals(Map.class)) {
                    field.set(object, fromBundleToCollection(bundle.getBundle(field.getName()), field.getType()));
                } else if (bundle.get(field.getName()) instanceof Bundle) {
                    field.set(object, fromBundleToObject(bundle.getBundle(field.getName())));
                } else {
                    field.set(object, bundle.get(field.getName()));
                }
            }
            return object;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bundle fromObjectToBundle(Object object, int nextId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("id", nextId);
            Class objectClass = object.getClass();
            bundle.putString("class", objectClass.getName());
            for (Field field : objectClass.getDeclaredFields()) {
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    for (Method method : annotation.annotationType().getDeclaredMethods()) {
                        Log.e("METHOD", method.getName());
                    }
                }
                putObjectInBundle(bundle, field.get(object), field.getName(), nextId);
            }
            return bundle;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bundle fromCollectionToBundle(Object object, int nextId) {
        Bundle bundle = new Bundle();
        if (object instanceof List) {
            List list = (List) object;
            for (Object elem : list) {
                putObjectInBundle(bundle, elem, list.indexOf(elem) + "", nextId);
            }
        } else if (object instanceof Map) {
            Map map = (Map) object;
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                putObjectInBundle(bundle, value, key.toString(), nextId);
            }
        }
        return bundle;
    }

    private static Object fromBundleToCollection(Bundle bundle, Class<?> type) {
        if (type.equals(List.class)) {
            List list = new ArrayList();
            for (String key : bundle.keySet()) {
                Object object =  bundle.get(key);
                if (object instanceof Bundle) {
                    list.add(fromBundleToObject((Bundle) object));
                } else {
                    list.add(bundle.get(key));
                }
            }
            return list;
        } else if (type.equals(Map.class)) {
            Map map = new HashMap();
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

    private static void putObjectInBundle(Bundle bundle, Object object, String key, int nextId) {
        if (object instanceof String) {
            bundle.putString(key, (String) object);
        } else if (object instanceof Byte) {
            bundle.putByte(key, (byte) object);
        } else if (object instanceof Integer) {
            bundle.putInt(key, (int) object);
        } else if (object instanceof Float) {
            bundle.putFloat(key, (float) object);
        } else if (object instanceof Boolean) {
            bundle.putBoolean(key, (boolean) object);
        } else if (object instanceof List || object instanceof Map) {
            bundle.putBundle(key, fromCollectionToBundle(object, nextId));
        } else if (object instanceof Bundle) {
            bundle.putBundle(key, (Bundle) object);
        } else {
            bundle.putBundle(key, fromObjectToBundle(object, ++nextId));
        }
    }

    private static JSONObject fromBundleToJson(Bundle bundle) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (String key : bundle.keySet()) {
            Object object = bundle.get(key);
            if (object instanceof Bundle) {
                jsonObject.put(key, fromBundleToJson((Bundle) object));
            } else {
                jsonObject.put(key, object.toString());
            }
        }
        return jsonObject;
    }

}
