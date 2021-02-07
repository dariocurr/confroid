package fr.uge.confroid.utlis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import fr.uge.confroid.sqlite.ConfroidContract;
import fr.uge.confroid.sqlite.ConfroidDbHelper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface ConfroidUtils {

    default Bundle toBundle(Object object) {
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
                    bundle.putBundle(list.indexOf(elem) + "", this.toBundle(elem));
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
                    bundle.putBundle(key + "", this.toBundle(value));
                }
            }
        }
        return bundle;
    }

    default void saveConfiguration (Context context, String name, Object value, String versionName) {
        ConfroidDbHelper dbHelper = new ConfroidDbHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConfroidContract.ConfroidEntry.COLUMN_NAME_NAME, name);

        HashMap<String, Object> content = new HashMap<String,Object>((HashMap)value);
        values.put(ConfroidContract.ConfroidEntry.COLUMN_NAME_CONTENT, new JSONObject(content).toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ConfroidContract.ConfroidEntry.TABLE_NAME, null, values);
    }

    default <T> void loadConfiguration (Context context, String name, String version, Consumer<T> callback) {
        ConfroidDbHelper dbHelper = new ConfroidDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                ConfroidContract.ConfroidEntry.COLUMN_NAME_NAME,
                ConfroidContract.ConfroidEntry.COLUMN_NAME_CONTENT
        };

        // Filter results WHERE "name" = name
        String selection = ConfroidContract.ConfroidEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = { name };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ConfroidContract.ConfroidEntry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                ConfroidContract.ConfroidEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<String> itemContents = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemContent = cursor.getString(
                    cursor.getColumnIndexOrThrow(ConfroidContract.ConfroidEntry.COLUMN_NAME_CONTENT));
            itemContents.add(itemContent);
        }
        cursor.close();

        for(String content : itemContents){
            Log.i("content", content);
        }
    }

    default <T> void subscribeConfiguration (Context context, String name, Consumer <T> callback) {

    }

    default <T> void cancelConfigurationSubscription (Context context, Consumer <T> callback) {

    }

    default void getConfigurationVersions (Context context, String name, Consumer <List <Version>> callback) {
        
    }

}
