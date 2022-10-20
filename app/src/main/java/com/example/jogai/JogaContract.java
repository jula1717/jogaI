package com.example.jogai;

import android.provider.BaseColumns;

public class JogaContract {
    private JogaContract(){

    }
    public static final class Asana implements BaseColumns {
        public static final String TABLE_NAME="asanas";
        public static final String COLUMN_SANSKRIT_NAME="sanskrit_name";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_PICTURE="picture";
        public static final String COLUMN_DESCRIPTION="description";
        public static final String COLUMN_TYPE_ID="type";
        public static final String COLUMN_DIFFICULTY="difficulty";
        public static final String COLUMN_DONE="done";
    }
    public static final class Types implements BaseColumns{
        public static final String TABLE_NAME="types";
        public static final String COLUMN_TYPE="type";
    }
}
