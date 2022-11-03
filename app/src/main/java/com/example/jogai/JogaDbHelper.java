package com.example.jogai;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.jogai.JogaContract.*;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

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

        fillTypesTable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ JogaContract.Asana.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ JogaContract.Types.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void fillTypesTable(){
        AsanaType t1 = new AsanaType("pozycja stojaca");
        insertType(t1);
        AsanaType t2 = new AsanaType("pozycja siedzaca");
        insertType(t2);
        AsanaType t3 = new AsanaType("skłon do przodu");
        insertType(t3);
        AsanaType t4 = new AsanaType("wygięcie do tyłu");
        insertType(t4);
        AsanaType t5 = new AsanaType("skret tulowia");
        insertType(t5);
        AsanaType t6 = new AsanaType("pozycja odwrócona");
        insertType(t6);
        AsanaType t7 = new AsanaType("pozycja relaksacyjna");
        insertType(t7);
    }

    private void insertType(AsanaType type) {
        ContentValues cv = new ContentValues();
        cv.put(Types.COLUMN_TYPE,type.getType());
        db.insert(Types.TABLE_NAME,null,cv);
    }

}
