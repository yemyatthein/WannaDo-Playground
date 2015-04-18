package com.yemyatthein.wannado.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

public class TestUtil extends AndroidTestCase {

    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    public static ContentValues createTestValueThingTable() {
        ContentValues testValues = new ContentValues();
        testValues.put(DataContract.ThingEntry.COLUMN_NAME, "Algorithm Study");
        testValues.put(DataContract.ThingEntry.COLUMN_CREATED_DATE, TEST_DATE);
        testValues.put(DataContract.ThingEntry.COLUMN_DESCRIPTION, "Algorithm Study");
        return testValues;
    }

    public static ContentValues createTestValueExpressTable() {
        ContentValues testValues = new ContentValues();
        testValues.put(DataContract.ExpressEntry.COLUMN_THING_ID, 12345L);
        testValues.put(DataContract.ExpressEntry.COLUMN_DATE, TEST_DATE);
        testValues.put(DataContract.ExpressEntry.COLUMN_TYPE, 0);
        return testValues;
    }

}
