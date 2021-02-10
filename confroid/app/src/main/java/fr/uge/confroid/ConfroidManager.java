package fr.uge.confroid;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import androidx.annotation.RequiresApi;
import fr.uge.confroid.receivers.TokenDispenser;
import fr.uge.confroid.sqlite.ConfroidContract;
import fr.uge.confroid.sqlite.ConfroidDbHelper;
import fr.uge.confroid.utlis.ConfroidUtils;
import org.json.JSONArray;
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

    /*
    * OLD VERSION
    public static boolean addConfiguration(String name, Bundle bundle, String tag) {
        if (!configurations.containsKey(name)) {
            configurations.put(name, new ArrayList<>());
        }
        List<Configuration> configurationsList = configurations.get(name);
        return configurationsList.add(new Configuration(configurationsList.size() + "", bundle, tag));
    }
    */

    /*
    public static Intent loadAllVersions(Context context, String name, String requestId) {
        ConfroidDbHelper dbHelper = new ConfroidDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ConfroidContract.ConfigurationEntry.NAME,
                ConfroidContract.ConfigurationEntry.VERSION,
                ConfroidContract.ConfigurationEntry.CONTENT
        };

        String selection = ConfroidContract.ConfigurationEntry.NAME + " = ?";
        String[] selectionArgs = {name};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = ConfroidContract.ConfigurationEntry.DATE + " DESC";

        Cursor cursor = db.query(
                ConfroidContract.ConfigurationEntry.TABLE_NAME,     // The table to query
                projection,                                         // The array of columns to return (pass null to get all)
                selection,                                          // The columns for the WHERE clause
                selectionArgs,                                      // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                sortOrder                                           // The sort order
        );

        List<Object> content = new ArrayList<>();
        while(cursor.moveToNext()) {
            content.add("Version: " + cursor.getString(cursor.getColumnIndexOrThrow(ConfroidContract.ConfigurationEntry.VERSION)) + " Content: " + cursor.getString(cursor.getColumnIndexOrThrow(ConfroidContract.ConfigurationEntry.CONTENT)));
        }
        cursor.close();

        Bundle contentBundle = ConfroidUtils.toBundle(content);

        Intent intent = new Intent();
        intent.putExtra("requestId", requestId);
        intent.putExtra("name", name);
        intent.putExtra("content", contentBundle);

        return intent;
    }
     */

    public static void saveConfiguration(Context context, Bundle bundle) throws JSONException {
        /**** SAVE IN SQLITE DB *****/

        /*ContentValues values = new ContentValues();
        values.put(ConfroidContract.ConfigurationEntry.NAME, name);
        values.put(ConfroidContract.ConfigurationEntry.TAG, tag);
        values.put(ConfroidContract.ConfigurationEntry.CONTENT, bundle.toString());
        values.put(ConfroidContract.ConfigurationEntry.DATE, String.valueOf(new Date()));
        values.put(ConfroidContract.ConfigurationEntry.VERSION, "1");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = new ConfroidDbHelper(context).getWritableDatabase().insert(ConfroidContract.ConfigurationEntry.TABLE_NAME,null, values);
        Log.i("id", String.valueOf(newRowId));*/

        /**** SAVE IN JSON FILE *****/
        /*try {
            jsonObject.put("name", name);
            jsonObject.put("tag", tag);
            jsonObject.put("content", bundle);
            jsonObject.put("date", new Date());
            jsonObject.put("version", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        JSONObject jsonObject = ConfroidUtils.fromBundleToJson(bundle);

        Bundle bundleObj = ConfroidUtils.jsonToBundle(jsonObject);

        Log.i("dirName", String.valueOf(context.getFilesDir()));
        File file = new File(context.getFilesDir(),bundleObj.getString("name") + ".json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonObject.toString());
            Log.i("jsonObj", jsonObject.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfiguration(Context context, String name, String requestID, String version, String receiver) throws JSONException {
        /***** LOAD FROM SQLITE DB *****/
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        /*String[] projection = {
                ConfroidContract.ConfigurationEntry.NAME,
                ConfroidContract.ConfigurationEntry.CONTENT
        };

        String selection = ConfroidContract.ConfigurationEntry.NAME + " =? " + "AND " + ConfroidContract.ConfigurationEntry.VERSION + " = ?";
        String[] selectionArgs = {name, version};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = ConfroidContract.ConfigurationEntry.DATE + " DESC";

        Cursor cursor = new ConfroidDbHelper(context).getReadableDatabase().query(
                ConfroidContract.ConfigurationEntry.TABLE_NAME,     // The table to query
                projection,                                         // The array of columns to return (pass null to get all)
                selection,                                          // The columns for the WHERE clause
                selectionArgs,                                      // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                sortOrder                                           // The sort order
        );

        List<Object> content = new ArrayList<>();
        while(cursor.moveToNext()) {
            content.add(cursor.getString(cursor.getColumnIndexOrThrow(ConfroidContract.ConfigurationEntry.CONTENT)));
        }
        cursor.close();

        Bundle contentBundle = ConfroidUtils.toBundle(content);
        Class classReceiver = null;

        try {
            classReceiver = Class.forName("fr.uge.client.services.PullService");
            Log.i("classe", "class found");
        } catch (ClassNotFoundException e) {
            Log.i("classe", "classe not found");
            e.printStackTrace();
        }*/

        Log.i("loadConfig", "Ciao");

        /**** LOAD FROM JSON FILE *****/

        File file = new File(context.getFilesDir(),"config.json");
        FileReader fileReader = null;
        String response = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            response = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject  = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent();
        intent.putExtra("requestId", requestID);
        intent.putExtra("name", name);
        intent.putExtra("version", version);
        intent.putExtra("content", jsonObject.get("content").toString());
        intent.setAction("fr.uge.client.services.PULL");

        Log.i("loadConfig", name);
        Log.i("loadConfig", version);
        Log.i("loadConfig", jsonObject.get("content").toString());


        context.startService(intent);
    }

    public static List<String> loadAllConfigurationNames(Context context) throws JSONException {
        /****** LOAD FROM SQLITE DB ******/

        /*String[] projection = {
                ConfroidContract.ConfigurationEntry.NAME
        };

        String sortOrder = ConfroidContract.ConfigurationEntry.DATE + " DESC";

        Cursor cursor = new ConfroidDbHelper(context).getReadableDatabase().query(
                true,
                ConfroidContract.ConfigurationEntry.TABLE_NAME,     // The table to query
                projection,                                         // The array of columns to return (pass null to get all)
                null,
                null,                                    // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                        // don't group the rows
                null,                                        // don't filter by row groups
                sortOrder                                           // The sort order
        );
        List<String> names = new ArrayList<>();
        while(cursor.moveToNext()) {
           names.add(cursor.getString(cursor.getColumnIndexOrThrow(ConfroidContract.ConfigurationEntry.NAME)));
        }
        cursor.close();
        return names;

        */

        /***** LOAD FROM JSON FILE *****/

        File file = new File(context.getFilesDir(),".json");
        FileReader fileReader = null;
        String response = null;
        JSONArray jsonArray = new JSONArray();
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                jsonArray.put(new JSONObject(line));
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            response = stringBuilder.toString();
            Log.i("responsee", response);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject(response);
        JSONArray key = jsonObject.names();
        for (int i = 0; i < key.length(); ++i) {
            String keys = key.getString(i);
            String value = jsonObject.getString(keys);

            Log.i("valueee", value);
        }



        List<String> names = new ArrayList<>();
        //Log.i("lengthArr", String.valueOf(jsonArray.length()));

        /*try {
            //Log.i("lengthArr", String.valueOf(jsonArray.length()));
            jsonObject = new JSONObject(response);
            Iterator<String> keys = jsonObject.keys();

            Log.i("jsonObjL", String.valueOf(jsonObject.length()));

            while(keys.hasNext()) {
                String key = keys.next();
                Log.i("namesKey", (String) jsonObject.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        Log.i("loadNames", names.toString());

        return names;

    }

    public static Intent loadAllConfigurationVersions(Context context, String name, String requestId) {
        /**** LOAD FROM SQLITE DB ****/
        /*String[] projection = {
                ConfroidContract.ConfigurationEntry.VERSION,
                ConfroidContract.ConfigurationEntry.TAG,
                ConfroidContract.ConfigurationEntry.DATE,
                ConfroidContract.ConfigurationEntry.CONTENT,
        };

        String selection = ConfroidContract.ConfigurationEntry.NAME + " = ?";
        String[] selectionArgs = {name};

        String sortOrder = ConfroidContract.ConfigurationEntry.DATE + " DESC";

        Cursor cursor = new ConfroidDbHelper(context).getReadableDatabase().query(
                ConfroidContract.ConfigurationEntry.TABLE_NAME,     // The table to query
                projection,                                         // The array of columns to return (pass null to get all)
                selection,                                          // The columns for the WHERE clause
                selectionArgs,                                      // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                sortOrder                                           // The sort order
        );
        Map<String, Bundle> versions = new HashMap<>();
        while(cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("tag", cursor.getString(cursor.getColumnIndexOrThrow(ConfroidContract.ConfigurationEntry.TAG)));
            bundle.putString("date", cursor.getString(cursor.getColumnIndexOrThrow(ConfroidContract.ConfigurationEntry.DATE)));
            bundle.putString("content", cursor.getString(cursor.getColumnIndexOrThrow(ConfroidContract.ConfigurationEntry.CONTENT)));
            versions.put(cursor.getString(cursor.getColumnIndexOrThrow(ConfroidContract.ConfigurationEntry.VERSION)), bundle);
        }
        cursor.close();
        Bundle contentBundle = ConfroidUtils.toBundle(versions);

        Intent intent = new Intent();
        intent.putExtra("requestId", requestId);
        intent.putExtra("name", name);
        intent.putExtra("content", contentBundle);
        return intent;
    }
    */

        /***** LOAD FROM JSON FILE *****/

        File file = new File(context.getFilesDir(), "config.json");
        FileReader fileReader = null;
        String response = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            response = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        Map<String, Bundle> versions = new HashMap<>();

        try {
            jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if(obj.get("name").equals(name)){
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", obj.getString("tag"));
                    bundle.putString("date", obj.get("date").toString());
                    bundle.putString("content", obj.get("content").toString());
                    versions.put(obj.getString("version"), bundle);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Bundle contentBundle = ConfroidUtils.toBundle(versions);

        Intent intent = new Intent();
        intent.putExtra("requestId", requestId);
        intent.putExtra("name", name);
        intent.putExtra("content", contentBundle);

        Log.i("loadVersions", requestId);
        Log.i("loadVersions", name);
        Log.i("loadVersions", contentBundle.toString());

        return intent;
    }
}
