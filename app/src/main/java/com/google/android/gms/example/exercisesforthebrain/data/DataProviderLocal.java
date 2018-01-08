package com.google.android.gms.example.exercisesforthebrain.data;

/**
 * Created by jman on 24/08/17.
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.google.android.gms.example.exercisesforthebrain.data.Contract.Entry.TABLE_NAME;


public class DataProviderLocal extends ContentProvider {

    private static final int QUOTE = 100;
    private static final int QUOTE_ID = 101;


    private static final UriMatcher uriMatcher = buildUriMatcher();

    private DbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH_QUOTE, QUOTE);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH_QUOTE + "/*", QUOTE_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case QUOTE: {
                returnCursor = db.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case QUOTE_ID: {
                returnCursor = db.query(
                        TABLE_NAME,
                        projection,
                        Contract.Entry.COLUMN_ID + " = ?",
                        new String[]{Contract.Entry.getIdFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        Context context = getContext();
        if (context != null) {
            returnCursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case QUOTE:
                db.insert(
                        TABLE_NAME,
                        null,
                        values
                );
                returnUri = Contract.Entry.URI;
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        if (selection.equals("4@44")) {
            selection = null;


            switch (uriMatcher.match(uri)) {
                case QUOTE:
                    rowsDeleted = db.delete(
                            Contract.Entry.TABLE_NAME,
                            selection,
                            selectionArgs
                    );
                    db.delete(
                            Contract.Entry.SQLITE_SEQUENCE,
                            null,
                            null
                    );
                    break;

                case QUOTE_ID:
                    String symbol = Contract.Entry.getIdFromUri(uri);
                    rowsDeleted = db.delete(
                            TABLE_NAME,
                            '"' + symbol + '"' + " =" + Contract.Entry.COLUMN_MATHEM,
                            selectionArgs
                    );
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown URI:" + uri);
            }

            if (rowsDeleted != 0) {
                Context context = getContext();
                if (context != null) {
                    context.getContentResolver().notifyChange(uri, null);
                }
            }
        }
        return rowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated = -1;

        String io = Contract.Entry.getIdFromUri(uri);
        rowsUpdated = db.update(
                TABLE_NAME,
                values,
                Contract.Entry.COLUMN_ID + " = ?",
                new String[]{io}
        );

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int returnCount;
        switch (uriMatcher.match(uri)) {

            case QUOTE:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        db.insert(
                                TABLE_NAME,
                                null,
                                value
                        );
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                Context context = getContext();
                if (context != null) {
                    context.getContentResolver().notifyChange(uri, null);
                }

                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
