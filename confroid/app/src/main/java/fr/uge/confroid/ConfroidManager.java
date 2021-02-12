package fr.uge.confroid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import fr.uge.confroid.utlis.ConfroidManagerUtils;
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
                ConfroidManagerUtils.writeFile(file, ConfroidManagerUtils.fromBundleToJson(bundle).toString());
            } else {
                JSONObject oldJsonObject = new JSONObject(ConfroidManagerUtils.readFile(file));
                ConfroidManagerUtils.writeFile(file, ConfroidManagerUtils.addVersionFromBundleToJson(oldJsonObject, bundle).toString());
            }
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
            JSONObject jsonObject = new JSONObject(ConfroidManagerUtils.readFile(file));
            return ConfroidManagerUtils.getVersionFromJsonToBundle(jsonObject, version);
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
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

    public static Bundle loadAllConfigurations(Context context, String name) {
        //***** LOAD FROM JSON FILE *****/
        File file = new File(context.getFilesDir(), name.replaceAll("\\.", "_") + ".json");
        try {
            JSONObject jsonObject = new JSONObject(ConfroidManagerUtils.readFile(file));
            Bundle versionsBundle = ConfroidManagerUtils.getAllVersionsFromJsonToBundle(jsonObject);
            Log.i("loadVersions", name);
            Log.i("loadVersions", versionsBundle.toString());
            return versionsBundle;
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }

    /*public static Bundle loadConfigurationByTag(Context context, String name, String tag) {
        File file = new File(context.getFilesDir(), name.replaceAll("\\.", "_") + ".json");
        try {
            JSONObject jsonObject = new JSONObject(readFile(file));
            return ConfroidUtils.getVersionFromJsonToBundle(jsonObject, version);
        } catch (JSONException e) {
            Log.e("JSONException", "");
            return null;
        }
    }*/
}
