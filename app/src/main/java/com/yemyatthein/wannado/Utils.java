package com.yemyatthein.wannado;

import android.content.ContentValues;
import android.database.Cursor;

import com.yemyatthein.wannado.data.DataContract;

public class Utils {

    public static ContentValues convertCursorRowToContentValThing(Cursor cursor) {
        // TODO: Test required
        // Cursor to ContentValues (DatabaseUtils?)
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataContract.ThingEntry._ID, cursor.getInt(0));
        contentValues.put(DataContract.ThingEntry.COLUMN_NAME, cursor.getString(1));
        contentValues.put(DataContract.ThingEntry.COLUMN_DESCRIPTION, cursor.getString(2));
        contentValues.put(DataContract.ThingEntry.COLUMN_CREATED_DATE, cursor.getLong(3));
        contentValues.put(DataContract.ThingEntry.COLUMN_IS_CURRENT, cursor.getInt(4));
        contentValues.put(DataContract.ThingEntry.COLUMN_CTOUCH, cursor.getInt(5));
        return contentValues;
    }

}
