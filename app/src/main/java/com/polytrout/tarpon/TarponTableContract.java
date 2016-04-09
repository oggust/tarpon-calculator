package com.polytrout.tarpon;

import android.provider.BaseColumns;


final class TarponTableContract {

    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "TarponTable.db";

    // To prevent someone from accidentally instantiating the contract class,
    // give it a private constructor.
    private TarponTableContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TarponEntry implements BaseColumns {
        public static final String TABLE_NAME = "TarponTable";
        public static final String COLUMN_NAME_LENGTH = "length";
        public static final String COLUMN_NAME_GIRTH = "girth";
        public static final String COLUMN_NAME_CTIME = "ctime";  // catch time

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                                                  + _ID + " INTEGER PRIMARY KEY,"
                                                  + COLUMN_NAME_LENGTH + " TEXT,"
                                                  + COLUMN_NAME_GIRTH + " TEXT,"
                                                  + COLUMN_NAME_CTIME + " INTEGER"
                                                  + ")";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
