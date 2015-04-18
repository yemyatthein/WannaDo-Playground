package com.yemyatthein.wannado.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

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

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                DataProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: DataProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + DataContract.CONTENT_AUTHORITY,
                    providerInfo.authority, DataContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: DataProvider not registered at " + mContext.getPackageName(),
                    false);
        }
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

    public void testInsertAndQueryActionThingTable() {
        ContentValues testThingValues = TestUtil.createTestValueThingTable();
        Uri thingUri = mContext.getContentResolver().insert(DataContract.ThingEntry.CONTENT_URI,
                                                            testThingValues);
        // Without condition
        Cursor thingCursor = mContext.getContentResolver()
                                        .query(DataContract.ThingEntry.CONTENT_URI,
                                                null, null, null, null);
        TestUtil.validateCursor("Error", thingCursor, testThingValues);

        // With Condition
        long thingId = ContentUris.parseId(thingUri);
        Log.d("XXXX", "Thing ID is " + thingId);
        thingCursor = mContext.getContentResolver()
                .query(DataContract.ThingEntry.CONTENT_URI,
                        null, DataContract.ThingEntry._ID + " = ?",
                        new String[] {String.valueOf(thingId)}, null);
        TestUtil.validateCursor("Error", thingCursor, testThingValues);

        // Update Test
        testThingValues.put(DataContract.ThingEntry.COLUMN_DESCRIPTION, "New Description");
        int rowsAffected = mContext.getContentResolver().update(DataContract.ThingEntry.CONTENT_URI,
                                            testThingValues,
                                            DataContract.ThingEntry.TABLE_NAME + "." +
                                                    DataContract.ThingEntry._ID + " = ?",
                                            new String[] {String.valueOf(thingId)});
        assertEquals("Update fails by ContentProvider.", 1, rowsAffected);
    }

    public void testInsertAndQueryActionExpressTable() {
        ContentValues testExpressValues = TestUtil.createTestValueExpressTable();
        Uri expressUri = mContext.getContentResolver().insert(DataContract.ExpressEntry.CONTENT_URI,
                testExpressValues);

        // Without Condition
        Cursor expressCursor = mContext.getContentResolver()
                .query(DataContract.ExpressEntry.CONTENT_URI,
                        null, null, null, null);
        TestUtil.validateCursor("Error", expressCursor, testExpressValues);

        // With Condition
        long expressId = ContentUris.parseId(expressUri);
        expressCursor = mContext.getContentResolver()
                .query(DataContract.ExpressEntry.CONTENT_URI,
                        null, DataContract.ExpressEntry._ID + " = ?",
                        new String[] {String.valueOf(expressId)}, null);
        TestUtil.validateCursor("Error", expressCursor, testExpressValues);

    }
}
