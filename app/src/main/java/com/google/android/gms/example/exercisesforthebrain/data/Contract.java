package com.google.android.gms.example.exercisesforthebrain.data;

/**
 * Created by jman on 24/08/17.
 */


import android.net.Uri;
import android.provider.BaseColumns;


public final class Contract {

    static final String AUTHORITY = "com.google.android.gms.example.exercisesforthebrain";
    static final String PATH_QUOTE = "mathematical";


    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private Contract() {
    }

    @SuppressWarnings("unused")
    public static final class Entry implements BaseColumns {
        public static final String SQLITE_SEQUENCE = "SQLITE_SEQUENCE";
        public static final String TABLE_NAME = "mathematical";
        public static final String COLUMN_ID = "id";
        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_QUOTE).build();
        public static final String COLUMN_MATHEM = "mathem";
        public static final String COLUMN_COLOR = "colortest";
        public static final String COLUMN_ALPHABET = "alphabettest";
        public static final String COLUMN_IMAGENS = "imagenstest";
        public static final String COLUMN_MATHEMATICAL = "mathematicall";

        public static final int POSITION_ID = 0;
        public static final int POSITION_MATHEM = 1;
        public static final int POSITION_COLOR = 2;
        public static final int POSITION_ALPHABET = 3;
        public static final int POSITION_IMAGENS = 4;
        public static final int POSITION_MATHEMATICAL = 5;


        public static Uri makeUriForMathe(String day) {
            return URI.buildUpon().appendPath(day).build();
        }

        static String getIdFromUri(Uri queryUri) {
            return queryUri.getLastPathSegment();
        }

        public static Uri buildWidgetPosition(int positionwidget) {
            return BASE_URI.buildUpon().appendPath("0").build();
        }

    }
}

