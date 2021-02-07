package fr.uge.confroid.sqlite;

import android.provider.BaseColumns;

public final class ConfroidContract {

    private ConfroidContract(){}

    public static class ConfroidEntry implements BaseColumns {
        public static final String TABLE_NAME = "configurations";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CONTENT = "content";

    }
}
