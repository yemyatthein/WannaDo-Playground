package com.yemyatthein.wannado.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    void deleteTheDatabase() {
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() {
        final HashSet<String> tableNameSet = new HashSet<String>();
        tableNameSet.add(DataContract.ThingEntry.TABLE_NAME);
        tableNameSet.add(DataContract.ExpressEntry.TABLE_NAME);

        mContext.deleteDatabase(DbHelper.DATABASE_NAME);

        SQLiteDatabase db = new DbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Database not created successfully.", cursor.moveToFirst());

        do {
            tableNameSet.remove(cursor.getString(0));
        } while( cursor.moveToNext() );

        assertTrue("Error: Database was created without required tables",
                tableNameSet.isEmpty());

        cursor = db.rawQuery("PRAGMA table_info(" + DataContract.ThingEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: Unable to query the database for table information.",
                cursor.moveToFirst());

        final HashSet<String> tblThingColumnSet = new HashSet<String>();
        tblThingColumnSet.add(DataContract.ThingEntry._ID);
        tblThingColumnSet.add(DataContract.ThingEntry.COLUMN_NAME);
        tblThingColumnSet.add(DataContract.ThingEntry.COLUMN_DESCRIPTION);
        tblThingColumnSet.add(DataContract.ThingEntry.COLUMN_CREATED_DATE);
        tblThingColumnSet.add(DataContract.ThingEntry.COLUMN_IS_CURRENT);
        tblThingColumnSet.add(DataContract.ThingEntry.COLUMN_CTOUCH);

        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            tblThingColumnSet.remove(columnName);
        } while(cursor.moveToNext());

        assertTrue("Error: Table thing does not contain column as expected",
                tblThingColumnSet.isEmpty());

        cursor = db.rawQuery("PRAGMA table_info(" + DataContract.ExpressEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: Unable to query the database for table information.",
                cursor.moveToFirst());

        final HashSet<String> tblExpressColumnSet = new HashSet<String>();
        tblExpressColumnSet.add(DataContract.ExpressEntry._ID);
        tblExpressColumnSet.add(DataContract.ExpressEntry.COLUMN_THING_ID);
        tblExpressColumnSet.add(DataContract.ExpressEntry.COLUMN_DATE);
        tblExpressColumnSet.add(DataContract.ExpressEntry.COLUMN_TYPE);

        columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            tblExpressColumnSet.remove(columnName);
        } while(cursor.moveToNext());

        assertTrue("Error: Table thing does not contain column as expected",
                tblExpressColumnSet.isEmpty());

        cursor.close();
        db.close();
    }

    public void testThingTable() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtil.createTestValueThingTable();

        long thingId = db.insert(DataContract.ThingEntry.TABLE_NAME, null, testValues);

        assertTrue(thingId != -1);

        Cursor cursor = db.query(DataContract.ThingEntry.TABLE_NAME, null, null, null,
                                 null, null, null);

        assertTrue( "Error: No Records returned from thing table query", cursor.moveToFirst() );

        TestUtil.validateCurrentRecord("Error: Thing Query Validation Failed",
                cursor, testValues);

        assertFalse( "Error: More than one record returned from thing query",
                cursor.moveToNext() );

        // Update Test
        testValues.put(DataContract.ThingEntry.COLUMN_DESCRIPTION, "New Description");
        int rowsAffected = db.update(DataContract.ThingEntry.TABLE_NAME, testValues,
                                    DataContract.ThingEntry.TABLE_NAME + "." +
                                            DataContract.ThingEntry._ID + " = ?",
                                    new String[] {String.valueOf(thingId)});
        assertEquals("Update fails.", 1, rowsAffected);

        cursor.close();
        db.close();

    }

    public void testExpressTable() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtil.createTestValueExpressTable();

        long thingId = db.insert(DataContract.ExpressEntry.TABLE_NAME, null, testValues);

        assertTrue(thingId != -1);

        Cursor cursor = db.query(DataContract.ExpressEntry.TABLE_NAME, null, null, null,
                null, null, null);

        assertTrue( "Error: No Records returned from express table query", cursor.moveToFirst() );

        TestUtil.validateCurrentRecord("Error: Express Query Validation Failed",
                cursor, testValues);

        assertFalse( "Error: More than one record returned from express query",
                cursor.moveToNext() );

        cursor.close();
        db.close();

    }

}
