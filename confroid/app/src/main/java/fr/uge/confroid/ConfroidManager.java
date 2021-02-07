package fr.uge.confroid;

import android.content.ContentValues;
import android.content.Context;
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

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConfroidContract.ConfroidEntry.NAME, name);
        values.put(ConfroidContract.ConfroidEntry.TAG, tag);
        values.put(ConfroidContract.ConfroidEntry.CONTENT, bundle.toString());
        values.put(ConfroidContract.ConfroidEntry.VERSION, "0");
        values.put(ConfroidContract.ConfroidEntry.DATE, String.valueOf(new Date()));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ConfroidContract.ConfroidEntry.TABLE_NAME, null, values);

        Log.i("id", String.valueOf(newRowId));
    }


    public static Bundle loadConfiguration(Context context, String name, String requestID, String version) {
        ConfroidDbHelper dbHelper = new ConfroidDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ConfroidContract.ConfroidEntry.NAME,
                ConfroidContract.ConfroidEntry.CONTENT
        };

        // Filter results WHERE "name" = name
        String selection = ConfroidContract.ConfroidEntry.NAME + " = ?";
        String[] selectionArgs = { name };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ConfroidContract.ConfroidEntry.NAME + " DESC";

        Cursor cursor = db.query(
                ConfroidContract.ConfroidEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                sortOrder               // The sort order
        );

        List<String> itemContents = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemContent = cursor.getString(
                    cursor.getColumnIndexOrThrow(ConfroidContract.ConfroidEntry.CONTENT));
            itemContents.add(itemContent);
        }
        cursor.close();

        Bundle contentBundle = ConfroidUtils.toBundle(itemContents);
        for(String content : itemContents){
            Log.i("content", content);
        }

        return contentBundle;
    }

    /*
    * OLD VERSION
     public static Configuration getLatestConfiguration(String name) {
         List<Configuration> configurationsList = configurations.get(name);
         return configurationsList.get(configurationsList.size() - 1);
         }
    */

}
