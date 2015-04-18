package com.yemyatthein.wannado.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataContract {

    public static final String CONTENT_AUTHORITY = "com.yemyatthein.wannado";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_THING = "thing";
    public static final String PATH_EXPRESS = "express";

    public static final class ThingEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_THING).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THING;

        public static final String TABLE_NAME = "thing";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CREATED_DATE = "created_date";

        public static Uri buildThingUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class ExpressEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXPRESS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPRESS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPRESS;

        public static final String TABLE_NAME = "express";

        public static final String COLUMN_THING_ID = "thing_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TYPE = "type";

        public static Uri buildExpressUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildExpressWithThingUri(long thingId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_THING)
                    .appendPath(Long.toString(thingId)).build();
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getThingIdFromUri(Uri uri) {
            return uri.getPathSegments().get(3);
        }
    }

}
