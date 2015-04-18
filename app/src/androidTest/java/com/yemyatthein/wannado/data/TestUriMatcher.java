package com.yemyatthein.wannado.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {

    public static final Uri TEST_THING_DIR = DataContract.ThingEntry.CONTENT_URI;
    public static final Uri TEST_THING_ITEM = DataContract.ThingEntry.buildThingUri(1234L);
    public static final Uri TEST_EXPRESS_DIR = DataContract.ExpressEntry.CONTENT_URI;
    public static final Uri TEST_EXPRESS_ITEM = DataContract.ExpressEntry.buildExpressUri(1234L);
    public static final Uri TEST_EXPRESS_THING_DIR = DataContract.ExpressEntry.buildExpressWithThingUri(2345L);

    public void testUriMatcher() {
        UriMatcher testUriMatcher = DataProvider.buildUriMatcher();

        assertEquals("", testUriMatcher.match(TEST_THING_DIR), DataProvider.THING);
        assertEquals("", testUriMatcher.match(TEST_EXPRESS_DIR), DataProvider.EXPRESS);
        assertEquals("", testUriMatcher.match(TEST_THING_ITEM), DataProvider.THING_WITH_ID);
        assertEquals("", testUriMatcher.match(TEST_EXPRESS_ITEM), DataProvider.EXPRESS_WITH_ID);
        assertEquals("", testUriMatcher.match(TEST_EXPRESS_THING_DIR), DataProvider.EXPRESS_WITH_THING);
    }
}
