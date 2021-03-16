package fr.uge.confroid.utlis;

import android.os.Bundle;
import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ConfroidManagerUtils {

    /**
     * @param object
     * @return bundle
     */
    public static Bundle toBundle(Object object) {
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
                    bundle.putBundle(list.indexOf(elem) + "", toBundle(elem));
                } else if (elem instanceof Bundle) {
                    bundle.putBundle(list.indexOf(elem)+ "", (Bundle) elem);
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
                    bundle.putBundle(key + "", toBundle(value));
                } else if (value instanceof Bundle) {
                    bundle.putBundle(key+ "", (Bundle) value);
                }
            }
        }
        return bundle;
    }

    /**
     * @param bundle
     * @return string bundle
     */
    public static String fromBundleToString(Bundle bundle) {
        return fromBundleToString(bundle, 0);
    }

    /**
     * @param bundle
     * @param tabNumber
     * @return String content
     */
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


    /**
     * @param bundle
     * @return json
     * @throws JSONException
     */
    public static JSONObject fromBundleToJson(Bundle bundle) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", bundle.getString("name"));
        jsonObject.put("token", bundle.getString("token"));
        JSONObject contentJsonObject = new JSONObject();
        contentJsonObject.put(String.valueOf(bundle.getInt("version")), extractBundleContent(bundle));
        jsonObject.put("configurations", contentJsonObject);
        return jsonObject;
    }

    /**
     * @param jsonObject
     * @param newVersionBundle
     * @return json
     * @throws JSONException
     */
    public static JSONObject addVersionFromBundleToJson(JSONObject jsonObject, Bundle newVersionBundle) throws JSONException {
        String newVersion = String.valueOf(newVersionBundle.getInt("version"));
        JSONObject configurationsJsonObject = jsonObject.getJSONObject("configurations");
        Iterator<String> iter = configurationsJsonObject.keys();
        if (newVersionBundle.containsKey("tag")) {
            while (iter.hasNext()) {
                JSONObject oldVersion = configurationsJsonObject.getJSONObject(iter.next());
                if (oldVersion.has("tag")
                        && oldVersion.get("tag").toString().equalsIgnoreCase(newVersionBundle.get("tag").toString())) {
                    oldVersion.remove("tag");
                }
            }
        }
        configurationsJsonObject.put(newVersion, extractBundleContent(newVersionBundle));
        return jsonObject;
    }

    /**
     * @param jsonObject
     * @param newVersionJsonObject
     * @return json
     * @throws JSONException
     */
    public static JSONObject addVersionFromJsonToJson(JSONObject jsonObject, JSONObject newVersionJsonObject) throws JSONException {
        JSONObject configurationsJsonObject = jsonObject.getJSONObject("configurations");
        JSONObject contentJsonObject = new JSONObject();
        contentJsonObject.put("date", new Date());
        contentJsonObject.put("content", newVersionJsonObject.getJSONObject("content"));
        configurationsJsonObject.put(newVersionJsonObject.getInt("version") + "", contentJsonObject);
        return jsonObject;
    }

    /**
     * @param bundle
     * @return json version
     * @throws JSONException
     */
    private static JSONObject extractBundleContent(Bundle bundle) throws JSONException {
        JSONObject versionJSONObject = new JSONObject();
        if (bundle.containsKey("tag")) {
            versionJSONObject.put("tag", bundle.get("tag"));
        }
        Bundle contentBundle = bundle.getBundle("content");
        versionJSONObject.put("date", new Date());
        versionJSONObject.put("content", extractContent(contentBundle));
        return versionJSONObject;
    }

    /**
     * @param contentBundle
     * @return json content
     * @throws JSONException
     */
    private static JSONObject extractContent(Bundle contentBundle) throws JSONException {
        JSONObject contentJSONObject = new JSONObject();
        for (String key : contentBundle.keySet()) {
            Object object = contentBundle.get(key);
            if (object instanceof Bundle) {
                contentJSONObject.put(key, extractContent((Bundle) object));
            } else {
                contentJSONObject.put(key, object.toString());
            }
        }
        return contentJSONObject;
    }

    /**
     * @param jsonObject
     * @param version
     * @return null
     * @throws JSONException
     */
    public static Bundle getVersionFromJsonToBundle(JSONObject jsonObject, Object version) throws JSONException {
        if (version instanceof Integer)
            return getVersionFromJsonToBundle(jsonObject.getJSONObject("configurations").getJSONObject(String.valueOf(version)));
        else if (version instanceof String) {
            JSONObject versionsJsonObject = jsonObject.getJSONObject("configurations");
            for (Iterator<String> it = versionsJsonObject.keys(); it.hasNext(); ) {
                String versionNum = it.next();
                if (versionsJsonObject.getJSONObject(versionNum).has("tag") && versionsJsonObject.getJSONObject(versionNum).getString("tag").equals(version)) {
                    return getVersionFromJsonToBundle(jsonObject.getJSONObject("configurations").getJSONObject(versionNum));
                }
            }
        }
        return null;
    }

    /**
     * @param jsonObject
     * @return bundle
     * @throws JSONException
     */
    private static Bundle getVersionFromJsonToBundle(JSONObject jsonObject) throws JSONException {
        Iterator iter = jsonObject.keys();
        Bundle bundle = new Bundle();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object value = jsonObject.get(key);
            if(value instanceof JSONObject) {
                bundle.putBundle(key, getVersionFromJsonToBundle((JSONObject) value));
            } else {
                bundle.putString(key, value.toString());
            }
        }
        return bundle;
    }

    /**
     * @param jsonObject
     * @return all version of json bundle
     * @throws JSONException
     */
    public static Bundle getAllVersionsFromJsonToBundle(JSONObject jsonObject) throws JSONException {
        return getVersionFromJsonToBundle(jsonObject.getJSONObject("configurations"));
    }

    /**
     * @param string
     * @return name package
     */
    public static String getPackageName(String string) {
        String[] fullName = string.split("\\.");
        String lastName = fullName[2];
        if (lastName.contains("/")) {
            fullName[2] = lastName.substring(0, lastName.indexOf("/"));
        }
        return TextUtils.join(".", Arrays.copyOfRange(fullName, 0, 3));
    }








































    /**
     * @param jsonObject
     * @param newTag
     * @param latestVersion
     * @return json object
     * @throws JSONException
     */
    public static JSONObject updateTagFromStringToJson(JSONObject jsonObject, String newTag, String latestVersion) throws JSONException {
        jsonObject.getJSONObject("configurations").getJSONObject(latestVersion).put("tag", newTag);
        return jsonObject;
    }

    /**
     * @param jsonObject
     * @param bundle
     * @param contentToEdit
     * @return json object
     * @throws JSONException
     */
    public static JSONObject updateContentFromStringToJson(JSONObject jsonObject, Bundle bundle, String contentToEdit) throws JSONException {
        String[] pathToContent = contentToEdit.split("/");
        String lastKey = pathToContent[pathToContent.length - 1];
        JSONObject jsonObjectToEdit = jsonObject.getJSONObject("configurations").getJSONObject(pathToContent[0]).getJSONObject("content");
        if (pathToContent.length > 2) {
            String[] innerPath = Arrays.copyOfRange(pathToContent, 0, pathToContent.length - 1);
            for (String key : innerPath) {
                jsonObjectToEdit = jsonObjectToEdit.getJSONObject(key);
            }
        }
        Bundle contentBundle = bundle.getBundle("content");
        jsonObjectToEdit.put(lastKey, extractContent(contentBundle));
        return jsonObject;
    }























































































































































































}
