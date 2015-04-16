package com.yemyatthein.wannado.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yemyatthein.wannado.data.DataContract.ThingEntry;
import com.yemyatthein.wannado.data.DataContract.ExpressEntry;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "wannado.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_THING_TABLE = "CREATE TABLE " + ThingEntry.TABLE_NAME + " (" +
                ThingEntry._ID + " INTEGER PRIMARY KEY," +
                ThingEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                ThingEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ThingEntry.COLUMN_CREATED_DATE + " INTEGER NOT NULL, " +
                " );";

        final String SQL_CREATE_EXPRESS_TABLE = "CREATE TABLE " + ExpressEntry.TABLE_NAME + " (" +
                ExpressEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                ExpressEntry.COLUMN_THING_ID + " INTEGER NOT NULL, " +
                ExpressEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                ExpressEntry.COLUMN_TYPE + " INTEGER NOT NULL," +

                " FOREIGN KEY (" + ExpressEntry.COLUMN_THING_ID + ") REFERENCES " +
                ThingEntry.TABLE_NAME + " (" + ThingEntry._ID + "), " +

                " UNIQUE (" + ExpressEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_THING_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EXPRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ThingEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpressEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
