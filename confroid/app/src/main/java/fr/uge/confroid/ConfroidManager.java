package fr.uge.confroid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import fr.uge.confroid.sqlite.ConfroidContract;
import fr.uge.confroid.sqlite.ConfroidDbHelper;
import fr.uge.confroid.utlis.ConfroidUtils;

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

    public static void saveConfiguration(Context context, String name, Bundle bundle, String tag) {
        ContentValues values = new ContentValues();
        values.put(ConfroidContract.ConfigurationEntry.NAME, name);
        values.put(ConfroidContract.ConfigurationEntry.TAG, tag);
        values.put(ConfroidContract.ConfigurationEntry.CONTENT, bundle.toString());
        values.put(ConfroidContract.ConfigurationEntry.DATE, String.valueOf(new Date()));
        values.put(ConfroidContract.ConfigurationEntry.VERSION, "2");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = new ConfroidDbHelper(context).getWritableDatabase().insert(ConfroidContract.ConfigurationEntry.TABLE_NAME,null, values);
        Log.i("id", String.valueOf(newRowId));

    }

    public static Intent loadConfiguration(Context context, String name, String requestID, String version) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ConfroidContract.ConfigurationEntry.NAME,
                ConfroidContract.ConfigurationEntry.CONTENT
        };

        String selection = ConfroidContract.ConfigurationEntry.NAME + " = ?";
        String[] selectionArgs = {name};

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

        Intent intent = new Intent();
        intent.putExtra("requestId", requestID);
        intent.putExtra("name", name);
        intent.putExtra("version", version);
        intent.putExtra("content", contentBundle);

        return intent;
    }

    public static List<String> loadAllConfigurationNames(Context context) {
        String[] projection = {
                ConfroidContract.ConfigurationEntry.NAME
        };

        String sortOrder = ConfroidContract.ConfigurationEntry.DATE + " DESC";

        Cursor cursor = new ConfroidDbHelper(context).getReadableDatabase().query(
                ConfroidContract.ConfigurationEntry.TABLE_NAME,     // The table to query
                projection,                                         // The array of columns to return (pass null to get all)
                null,                                          // The columns for the WHERE clause
                null,                                      // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                sortOrder                                           // The sort order
        );
        List<String> names = new ArrayList<>();
        while(cursor.moveToNext()) {
           names.add(cursor.getString(cursor.getColumnIndexOrThrow(ConfroidContract.ConfigurationEntry.NAME)));
        }
        cursor.close();
        return names;
    }

    public static Intent loadAllConfigurationVersions(Context context, String name, String requestId) {
        String[] projection = {
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

}
