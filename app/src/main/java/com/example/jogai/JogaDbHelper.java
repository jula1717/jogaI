package com.example.jogai;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class JogaDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="joga.db";
    public static final int DATABASE_VERSION=1;
    SQLiteDatabase db;
    Context context;

    public JogaDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db=sqLiteDatabase;
        final String SQL_CREATE_TYPES_TABLE = "CREATE TABLE "
                +JogaContract.Types.TABLE_NAME + " ("
                +JogaContract.Types._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +JogaContract.Types.COLUMN_TYPE+" TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_TYPES_TABLE);

        final String SQL_CREATE_ASANAS_TABLE = "CREATE TABLE "
                +JogaContract.Asana.TABLE_NAME + " ("
                +JogaContract.Asana._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +JogaContract.Asana.COLUMN_SANSKRIT_NAME+" TEXT,"
                +JogaContract.Asana.COLUMN_NAME+" TEXT,"
                +JogaContract.Asana.COLUMN_PICTURE+" BLOB,"
                +JogaContract.Asana.COLUMN_DESCRIPTION+" TEXT,"
                +JogaContract.Asana.COLUMN_TYPE_ID+" INTEGER,"
                +JogaContract.Asana.COLUMN_DIFFICULTY+" TINYINT,"
                +JogaContract.Asana.COLUMN_DONE+" BOOLEAN);";

        sqLiteDatabase.execSQL(SQL_CREATE_ASANAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ JogaContract.Asana.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ JogaContract.Types.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
