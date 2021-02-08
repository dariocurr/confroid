package fr.uge.confroid.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConfroidDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "confroid.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
                    ConfroidContract.ConfigurationEntry.TABLE_NAME + "(" +
                    ConfroidContract.ConfigurationEntry._ID + " INTEGER PRIMARY KEY," +
                    ConfroidContract.ConfigurationEntry.NAME + " TEXT, " +
                    ConfroidContract.ConfigurationEntry.CONTENT + " TEXT, " +
                    ConfroidContract.ConfigurationEntry.TAG + " TEXT, " +
                    ConfroidContract.ConfigurationEntry.VERSION + " TEXT, " +
                    ConfroidContract.ConfigurationEntry.DATE + " DATE " + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ConfroidContract.ConfigurationEntry.TABLE_NAME;

    public ConfroidDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.i("create1", "create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("upgrade1", "upgrade");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}