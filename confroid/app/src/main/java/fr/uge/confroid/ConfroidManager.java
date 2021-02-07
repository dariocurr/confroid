package fr.uge.confroid;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import fr.uge.confroid.sqlite.ConfroidContract;
import fr.uge.confroid.sqlite.ConfroidDbHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void addConfiguration(String name, Bundle bundle, String tag) {
        ConfroidDbHelper dbHelper = new ConfroidDbHelper(null);

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConfroidContract.ConfroidEntry.NAME, name);
        values.put(ConfroidContract.ConfroidEntry.TAG, tag);
        values.put(ConfroidContract.ConfroidEntry.CONTENT, bundle.toString());
        values.put(ConfroidContract.ConfroidEntry.VERSION, null);
        values.put(ConfroidContract.ConfroidEntry.DATE, new Date());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ConfroidContract.ConfroidEntry.TABLE_NAME, null, values);
    }

    /*
    * OLD VERSION
     public static Configuration getLatestConfiguration(String name) {
         List<Configuration> configurationsList = configurations.get(name);
         return configurationsList.get(configurationsList.size() - 1);
         }
    */

}
