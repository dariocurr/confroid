package fr.uge.confroid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import fr.uge.confroid.utlis.ConfroidManagerUtils;
import fr.uge.confroid.utlis.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class ConfroidManager {

    private static ConfroidManager confroidManager;

    private ConfroidManager() {}

    public static ConfroidManager getConfroidManager(Context context) {
        if (confroidManager == null) {
            confroidManager = new ConfroidManager();
        }
        return confroidManager;
    }

    public static boolean saveConfiguration(Context context, Bundle bundle) {
        /* SAVE TO JSON FILE */
        File file = new File(context.getFilesDir(), bundle.getString("name").replaceAll("\\.", "_") + ".json");
        try {
            if (!file.exists()) {
                FileUtils.writeFile(file, ConfroidManagerUtils.fromBundleToJson(bundle).toString());
            } else {
                JSONObject oldJsonObject = new JSONObject(FileUtils.readFile(file));
                FileUtils.writeFile(file, ConfroidManagerUtils.addVersionFromBundleToJson(oldJsonObject, bundle).toString());
            }
            return true;
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return false;
        }
    }

    public static boolean saveConfiguration(Context context, JSONObject newVersionJsonObject) {
        /* SAVE TO JSON FILE */
        try {
            File file = new File(context.getFilesDir(), newVersionJsonObject.getString("name").replaceAll("\\.", "_") + ".json");
            JSONObject oldJsonObject = new JSONObject(FileUtils.readFile(file));
            FileUtils.writeFile(file, ConfroidManagerUtils.addVersionFromJsonToJson(oldJsonObject, newVersionJsonObject).toString());
            return true;
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return false;
        }
    }

    public static Bundle loadConfiguration(Context context, String name, Object version) {
        /* LOAD FROM JSON FILE */
        File file = new File(context.getFilesDir(), name.replaceAll("\\.", "_") + ".json");
        try {
            JSONObject jsonObject = new JSONObject(FileUtils.readFile(file));
            return ConfroidManagerUtils.getVersionFromJsonToBundle(jsonObject, version);
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }

    public static Bundle loadAllVersionsBundle(Context context, String name) {
        //***** LOAD FROM JSON FILE *****/
        try {
            return ConfroidManagerUtils.getAllVersionsFromJsonToBundle(loadAllVersionsJson(context, name));
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }

    public static JSONObject loadAllVersionsJson(Context context, String name) {
        //***** LOAD FROM JSON FILE *****/
        File file = new File(context.getFilesDir(), name.replaceAll("\\.", "_") + ".json");
        try {
            return new JSONObject(FileUtils.readFile(file));
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }

    public static boolean updateTag(Context context, String name, String newTag, Integer latestVersion) {
        File file = new File(context.getFilesDir(), name.replaceAll("\\.", "_") + ".json");
        try {
            if (file.exists()) {
                JSONObject jsonObject = new JSONObject(FileUtils.readFile(file));
                FileUtils.writeFile(file,
                        ConfroidManagerUtils.updateTagFromStringToJson(jsonObject, newTag, latestVersion.toString()).toString());
                return true;
            }
            return false;
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return false;
        }
    }

    public static boolean updateContent(Context context, Bundle bundle, String contentToEdit) {
        File file = new File(context.getFilesDir(), bundle.getString("name").replaceAll("\\.", "_") + ".json");
        try {
            if (file.exists()) {
                JSONObject jsonObject = new JSONObject(FileUtils.readFile(file));
                FileUtils.writeFile(file, ConfroidManagerUtils.updateContentFromStringToJson(jsonObject, bundle, contentToEdit).toString());
                return true;
            }
            return false;
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return false;
        }
    }

    public static List<String> loadAllConfigurationsNames(Context context) {
        //***** LOAD FROM JSON FILE *****/
        List<String> names = new ArrayList<>();
        for (String strFile : context.getFilesDir().list()) {
            names.add(strFile.substring(0, strFile.length()-5));
        }
        Log.i("names", names.toString());
        return names;
    }

    public static JSONObject getAllConfigurations(Context context) {
        JSONObject configurations = new JSONObject();
        for (String strFile : context.getFilesDir().list()) {
            File file = new File(context.getFilesDir(), strFile);
            try {
                JSONObject jsonObject = new JSONObject(FileUtils.readFile(file));
                configurations.put(strFile, jsonObject);
            } catch (JSONException e) {
                Log.e("JSONException", "");
                return null;
            }
        }
        return configurations;
    }

}
