package com.google.android.gms.example.exercisesforthebrain.data;

/**
 * Created by jman on 24/08/17.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.example.exercisesforthebrain.data.Contract.Entry;


class DbHelper extends SQLiteOpenHelper {


    private static final String NAME = "BarinGameMathe.db";
    private static final int VERSION = 11;
    private static final String DATABASE_MATHE = "CREATE TABLE " + Entry.TABLE_NAME
            + "(" + Entry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Entry.COLUMN_MATHEM + " TEXT, "
            + Entry.COLUMN_COLOR + " TEXT, "
            + Entry.COLUMN_ALPHABET + " TEXT, "
            + Entry.COLUMN_IMAGENS + " TEXT" + ")";

    DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_MATHE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
        onCreate(db);
    }
}
