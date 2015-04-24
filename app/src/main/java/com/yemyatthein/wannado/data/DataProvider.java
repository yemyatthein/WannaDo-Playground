package com.yemyatthein.wannado.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataProvider extends ContentProvider {

    private DbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int THING = 100;
    static final int THING_WITH_ID = 101;
    static final int EXPRESS_WITH_THING = 103;
    static final int EXPRESS_WITH_ID = 104;
    static final int EXPRESS = 105;

    private static final SQLiteQueryBuilder thingJoinedExpress;

    static{
        thingJoinedExpress = new SQLiteQueryBuilder();
        thingJoinedExpress.setTables(
                DataContract.ThingEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.ExpressEntry.TABLE_NAME +
                        " ON " + DataContract.ThingEntry.TABLE_NAME +
                        "." + DataContract.ThingEntry._ID +
                        " = " + DataContract.ExpressEntry.TABLE_NAME +
                        "." + DataContract.ExpressEntry.COLUMN_THING_ID);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DataContract.PATH_THING, THING);
        matcher.addURI(authority, DataContract.PATH_EXPRESS, EXPRESS);
        matcher.addURI(authority, DataContract.PATH_THING + "/#", THING_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_EXPRESS + "/#", EXPRESS_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_EXPRESS + "/thing/#", EXPRESS_WITH_THING);

        return matcher;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case THING: {
                retCursor = db.query(
                        DataContract.ThingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case EXPRESS: {
                retCursor = db.query(
                        DataContract.ExpressEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case THING_WITH_ID: {
                retCursor = db.query(
                        DataContract.ThingEntry.TABLE_NAME,
                        projection,
                        DataContract.ThingEntry._ID + " = ?",
                        new String[] {DataContract.ThingEntry.getIdFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case EXPRESS_WITH_ID: {
                retCursor = db.query(
                        DataContract.ExpressEntry.TABLE_NAME,
                        projection,
                        DataContract.ExpressEntry._ID + " = ?",
                        new String[] {DataContract.ExpressEntry.getIdFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case EXPRESS_WITH_THING: {
                retCursor = db.query(
                        DataContract.ExpressEntry.TABLE_NAME,
                        projection,
                        DataContract.ExpressEntry.COLUMN_THING_ID + " = ?",
                        new String[] {DataContract.ExpressEntry.getThingIdFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case THING:
                return DataContract.ThingEntry.CONTENT_TYPE;
            case EXPRESS:
                return DataContract.ExpressEntry.CONTENT_TYPE;
            case THING_WITH_ID:
                return DataContract.ThingEntry.CONTENT_ITEM_TYPE;
            case EXPRESS_WITH_ID:
                return DataContract.ExpressEntry.CONTENT_ITEM_TYPE;
            case EXPRESS_WITH_THING:
                return DataContract.ExpressEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (sUriMatcher.match(uri)) {
            case THING:
                long thingId = db.insert(DataContract.ThingEntry.TABLE_NAME, null, values);
                if ( thingId > 0 )
                    returnUri = DataContract.ThingEntry.buildThingUri(thingId);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case EXPRESS:
                long expressId = db.insert(DataContract.ExpressEntry.TABLE_NAME, null, values);
                if ( expressId > 0 )
                    returnUri = DataContract.ExpressEntry.buildExpressUri(expressId);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        switch (sUriMatcher.match(uri)) {
            case THING:
                rowsDeleted = db.delete(DataContract.ThingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EXPRESS:
                rowsDeleted = db.delete(DataContract.ExpressEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsAffected = 0;
        switch (sUriMatcher.match(uri)) {
            case THING:
                rowsAffected = db.update(DataContract.ThingEntry.TABLE_NAME, values, selection,
                                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsAffected > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsAffected;
    }
}
