package com.polytrout.tarpon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class TarponTableDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.

    public TarponTableDbHelper(Context context) {
        super(context, TarponTableContract.DATABASE_NAME, null,
                TarponTableContract.DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TarponTableContract.TarponEntry.CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(TarponTableContract.TarponEntry.DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
