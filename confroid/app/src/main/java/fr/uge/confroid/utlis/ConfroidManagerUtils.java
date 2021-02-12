package fr.uge.confroid.utlis;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class ConfroidManagerUtils {

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

    public static JSONObject fromBundleToJson(Bundle bundle) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", bundle.getString("name"));
            jsonObject.put("token", bundle.getString("token"));
            JSONObject contentJsonObject = new JSONObject();
            contentJsonObject.put(String.valueOf(bundle.getInt("version")), extractVersionContent(bundle));
            jsonObject.put("configurations", contentJsonObject);
            Log.e("JSON", jsonObject.toString());
            return jsonObject;
        } catch(JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }

    public static JSONObject addVersionFromBundleToJson(JSONObject jsonObject, Bundle newVersionBundle) {
        String newVersion = String.valueOf(newVersionBundle.getInt("version"));
        try {
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
            configurationsJsonObject.put(newVersion, extractVersionContent(newVersionBundle));
            Log.e("JSON", jsonObject.toString());
            return jsonObject;
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }

    private static JSONObject extractVersionContent(Bundle bundle) {
        JSONObject versionJSONObject = new JSONObject();
        try {
            if (bundle.containsKey("tag")) {
                versionJSONObject.put("tag", bundle.get("tag"));
            }
            Bundle contentBundle = bundle.getBundle("content");
            JSONObject contentJSONObject = new JSONObject();
            for (String key : contentBundle.keySet()) {
                contentJSONObject.put(key, contentBundle.get(key));
            }
            versionJSONObject.put("date", new Date());
            versionJSONObject.put("content", contentJSONObject);
        } catch (JSONException e) {
            Log.e("JSONException", "");
        }
        return versionJSONObject;
    }

    public static Bundle getVersionFromJsonToBundle(JSONObject jsonObject, Object version) {
        try {
            if(version instanceof Integer)
                return getVersionFromJsonToBundle(jsonObject.getJSONObject("configurations").getJSONObject(String.valueOf(version)));
            else if(version instanceof String){
                JSONObject versionsJsonObject = jsonObject.getJSONObject("configurations");
                for (Iterator<String> it = versionsJsonObject.keys(); it.hasNext(); ) {
                    String versionNum = it.next();
                    if(versionsJsonObject.getJSONObject(versionNum).getString("tag").equals(version))
                        return getVersionFromJsonToBundle(jsonObject.getJSONObject("configurations").getJSONObject(versionNum));
                }
            }
            return null;
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }

    private static Bundle getVersionFromJsonToBundle(JSONObject jsonObject) {
        try {
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
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }

    public static Bundle getAllVersionsFromJsonToBundle(JSONObject jsonObject){
        try {
            return getVersionFromJsonToBundle(jsonObject.getJSONObject("configurations"));
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }

    public static void writeFile(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            Log.e("IOException", "");
        }
    }

    public static String readFile(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder content = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                content.append(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            return content.toString();
        } catch (IOException e) {
            Log.e("IOException", "");
            return null;
        }
    }

    public static String getPackageName(String string) {
        List<String> fullName = Arrays.asList(string.split("\\."));
        return TextUtils.join(".", fullName.subList(0, 3));
    }

}
