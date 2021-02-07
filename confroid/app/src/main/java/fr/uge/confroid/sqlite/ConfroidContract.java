package fr.uge.confroid.sqlite;

import android.provider.BaseColumns;

public final class ConfroidContract {

    private ConfroidContract() {}

    public static class ConfroidEntry implements BaseColumns {

        public static final String TABLE_NAME = "configurations";
        public static final String NAME = "name";
        public static final String VERSION = "version";
        public static final String TAG = "tag";
        public static final String CONTENT = "content";
        public static final String DATE = "date";

    }
}
