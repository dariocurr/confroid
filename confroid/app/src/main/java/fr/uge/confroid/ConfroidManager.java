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
    private static Map<String, List<Configuration>> configurations;

    private ConfroidManager() {
        this.configurations = new HashMap<>();
    }

    public static ConfroidManager getConfroidManager() {
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

    public static void addConfiguration(Context context, String name, Bundle bundle, String tag) {

        ConfroidDbHelper dbHelper = new ConfroidDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ConfroidContract.ConfigurationEntry.NAME, name);
        values.put(ConfroidContract.ConfigurationEntry.TAG, tag);
        values.put(ConfroidContract.ConfigurationEntry.CONTENT, bundle.toString());
        values.put(ConfroidContract.ConfigurationEntry.VERSION, "1");
        values.put(ConfroidContract.ConfigurationEntry.DATE, String.valueOf(new Date()));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ConfroidContract.ConfigurationEntry.TABLE_NAME,null, values);
        Log.i("id", String.valueOf(newRowId));
    }

    public static Intent loadConfiguration(Context context, String name, String requestID, String version) {
        ConfroidDbHelper dbHelper = new ConfroidDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

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


    /*
    * OLD VERSION
     public static Configuration getLatestConfiguration(String name) {
         List<Configuration> configurationsList = configurations.get(name);
         return configurationsList.get(configurationsList.size() - 1);
         }
    */

}
