package com.yemyatthein.wannado.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class TestProvider extends AndroidTestCase {

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                DataContract.ThingEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                DataContract.ExpressEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                DataContract.ThingEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from thing table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                DataContract.ExpressEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from express table during delete", 0, cursor.getCount());
        cursor.close();
    }

   public void deleteAllRecordsFromDB() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DataContract.ThingEntry.TABLE_NAME, null, null);
        db.delete(DataContract.ExpressEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testGetType() {
        // content://CONTENT_PROVIDER/thing/
        String type = mContext.getContentResolver().getType(DataContract.ThingEntry.CONTENT_URI);
        assertEquals("", DataContract.ThingEntry.CONTENT_TYPE, type);

        // content://CONTENT_PROVIDER/express/
        type = mContext.getContentResolver().getType(DataContract.ExpressEntry.CONTENT_URI);
        assertEquals("", DataContract.ExpressEntry.CONTENT_TYPE, type);

        // content://CONTENT_PROVIDER/thing/1234
        type = mContext.getContentResolver().getType(DataContract.ThingEntry.buildThingUri(1234L));
        assertEquals("", DataContract.ThingEntry.CONTENT_ITEM_TYPE, type);

        // content://CONTENT_PROVIDER/express/1234
        type = mContext.getContentResolver().getType(DataContract.ExpressEntry.buildExpressUri(1234L));
        assertEquals("", DataContract.ExpressEntry.CONTENT_ITEM_TYPE, type);

        // content://CONTENT_PROVIDER/express/thing/1234
        type = mContext.getContentResolver().getType(DataContract.ExpressEntry.buildExpressWithThingUri(1234L));
        assertEquals("", DataContract.ExpressEntry.CONTENT_TYPE, type);
    }
}
