package fr.uge.shopping;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.shopping.model.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
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
        try {
            Log.e("Bundle", fromBundleToJson(toBundle(prefs, 1)).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        Prova prova = new Prova();
        prova.addio = 1;
        prova.apapa = 5;
        prova.ciao = "CCC";
        Log.e("Bundle", toBundle(prova, 0).toString());
         */
    }

    public static Bundle toBundle(Object object, int nextId) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", nextId);
        Class objectClass = object.getClass();
        bundle.putString("class", objectClass.getName());
        try {
            for (Field field : objectClass.getDeclaredFields()) {
                String fieldName = field.getName();
                Class fieldType = field.getType();
                Object currentObject = field.get(object);
                if (field.getType().isPrimitive() || isPrimitiveWrapper(currentObject.getClass())) {
                    bundle.putString(fieldName, currentObject.toString());
                } else if (fieldType.equals(Map.class) || fieldType.equals(List.class) || fieldType.equals(Set.class)) {
                    bundle.putBundle(fieldName, fromCollectionToBundle(currentObject, nextId));
                } else {
                    bundle.putBundle(fieldName, toBundle(currentObject, ++nextId));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    public static Bundle fromCollectionToBundle(Object object, int nextId) {
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
                    bundle.putBundle(list.indexOf(elem) + "", fromCollectionToBundle(elem, nextId));
                } else if (elem instanceof Bundle) {
                    bundle.putBundle(list.indexOf(elem) + "", (Bundle) elem);
                } else {
                    bundle.putBundle(list.indexOf(elem) + "", toBundle(elem, ++nextId));
                }
            }
        } else if (object instanceof Map) {
            Map map = (Map) object;
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                if (value instanceof String) {
                    bundle.putString(key.toString(), (String) value);
                } else if (value instanceof Byte) {
                    bundle.putByte(key.toString(), (byte) value);
                } else if (value instanceof Integer) {
                    bundle.putInt(key.toString(), (int) value);
                } else if (value instanceof Float) {
                    bundle.putFloat(key.toString(), (float) value);
                } else if (value instanceof Boolean) {
                    bundle.putBoolean(key.toString(), (boolean) value);
                } else if (value instanceof List || value instanceof Map) {
                    bundle.putBundle(key.toString(), fromCollectionToBundle(value, nextId));
                } else if (value instanceof Bundle) {
                    bundle.putBundle(key.toString(), (Bundle) value);
                } else {
                    bundle.putBundle(key.toString(), toBundle(value, ++nextId));
                }
            }
        } else if (object instanceof Set) {
            // TODO
        }
        return bundle;
    }

    private static final Set<Class> PRIMITIVE_WRAPPER_TYPES = new HashSet(Arrays.asList(
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Void.class,
            String.class));

    public static boolean isPrimitiveWrapper(Class clazz) {
        return PRIMITIVE_WRAPPER_TYPES.contains(clazz);
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
