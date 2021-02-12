package fr.uge.confroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import fr.uge.confroid.utlis.ConfroidUtils;
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

    public static void saveConfiguration(Context context, Bundle bundle) {
        /* SAVE TO JSON FILE */
        File file = new File(context.getFilesDir(), bundle.getString("name").replaceAll("\\.", "_") + ".json");
        try {
            if (!file.exists()) {
                writeFile(file, ConfroidUtils.fromBundleToJson(bundle).toString());
            } else {
                JSONObject oldJsonObject = new JSONObject(readFile(file));
                writeFile(file, ConfroidUtils.addVersionFromBundleToJson(oldJsonObject, bundle).toString());
            }
        } catch (JSONException e) {
            Log.e("JSONException", "");
        }
    }

    private static void writeFile(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            Log.e("IOException", "");
        }
    }

    private static String readFile(File file) {
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

    public static Bundle loadConfiguration(Context context, String name, Object version) {
        /* LOAD FROM JSON FILE */
        File file = new File(context.getFilesDir(), name.replaceAll("\\.", "_") + ".json");
        try {
            JSONObject jsonObject = new JSONObject(readFile(file));
            return ConfroidUtils.getVersionFromJsonToBundle(jsonObject, version);
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

        String response = readFile(file);

        JSONObject jsonObject = null;
        try {
           jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Bundle versionsBundle = ConfroidUtils.getAllVersionsFromJsonToBundle(jsonObject);


        Log.i("loadVersions", name);
        Log.i("loadVersions", versionsBundle.toString());

        return versionsBundle;
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
