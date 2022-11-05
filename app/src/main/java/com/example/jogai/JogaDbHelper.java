package com.example.jogai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.jogai.JogaContract.*;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class JogaDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="joga.db";
    public static final int DATABASE_VERSION=3;
    SQLiteDatabase db;
    Context context;

    private static JogaDbHelper instance;

    private JogaDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    public static synchronized JogaDbHelper getInstance(Context context){
        if(instance==null){
            instance = new JogaDbHelper(context.getApplicationContext());
        }
        return instance;
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
                +JogaContract.Asana.COLUMN_IMAGE +" BLOB,"
                +JogaContract.Asana.COLUMN_DESCRIPTION+" TEXT,"
                +JogaContract.Asana.COLUMN_TYPE_ID+" INTEGER,"
                +JogaContract.Asana.COLUMN_DIFFICULTY+" TINYINT,"
                +JogaContract.Asana.COLUMN_DONE+" BOOLEAN);";

        sqLiteDatabase.execSQL(SQL_CREATE_ASANAS_TABLE);

        fillTypesTable();
        fillAsanasTable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ JogaContract.Asana.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ JogaContract.Types.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    //sorted by difficulty
    public ArrayList<Asana> getSortedAsanas(String columnSortBy){
        ArrayList<Asana> asanas = new ArrayList<>();
        db = getReadableDatabase();

        String [] selectionsArgs = {columnSortBy};
        //Cursor c = db.rawQuery("SELECT * FROM "+ JogaContract.Asana.TABLE_NAME+" ORDER BY ?",selectionsArgs);
        Cursor c = db.query(JogaContract.Asana.TABLE_NAME,null,null,null,null,null,JogaContract.Asana.COLUMN_SANSKRIT_NAME);

        if(c.moveToFirst()){
            do{
                Asana asana = new Asana();
                asana.setName(c.getString(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_NAME)));
                asana.setSanskritName(c.getString(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_SANSKRIT_NAME)));
                asana.setDescription(c.getString(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_DESCRIPTION)));
                asana.setDifficulty((byte) c.getInt(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_DIFFICULTY)));
                asana.setImage(c.getBlob(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_IMAGE)));
                asana.setColumnTypeId(c.getInt(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_TYPE_ID)));
                asanas.add(asana);
            }while(c.moveToNext());
        }
        c.close();
        return asanas;
    }

    public String getCategoryNameById(int categoryId) {
        String categoryName="";
        db = getReadableDatabase();

        String[] selectionArgs = {String.valueOf(categoryId)};
        Cursor c = db.rawQuery("SELECT " + Types.COLUMN_TYPE + " FROM " + JogaContract.Types.TABLE_NAME + " WHERE " + Types._ID + "=?", selectionArgs);

        if (c.moveToFirst()) {
            categoryName = c.getString(c.getColumnIndexOrThrow(Types.COLUMN_TYPE));
        }
        c.close();
        return categoryName;
    }

    public void changeAsanaState(int position, boolean newState) {
        db = getWritableDatabase();
        String[] selectionArgs = {String.valueOf(position+1),String.valueOf(newState)};
        Cursor c = db.rawQuery("UPDATE " + JogaContract.Asana.TABLE_NAME + " SET " + JogaContract.Asana.COLUMN_DONE + "=?" + " WHERE " + JogaContract.Asana._ID + "=?", selectionArgs);
    }



    //fill tables, could comment later


    //TODO: polskie znaki
    public void fillTypesTable(){
        AsanaType t1 = new AsanaType("pozycja stojaca");
        insertType(t1);
        AsanaType t2 = new AsanaType("pozycja siedzaca");
        insertType(t2);
        AsanaType t3 = new AsanaType("sklon do przodu");
        insertType(t3);
        AsanaType t4 = new AsanaType("wygiecie do tylu");
        insertType(t4);
        AsanaType t5 = new AsanaType("skret tulowia");
        insertType(t5);
        AsanaType t6 = new AsanaType("pozycja odwrocona");
        insertType(t6);
        AsanaType t7 = new AsanaType("pozycja relaksacyjna");
        insertType(t7);
    }

    private void insertType(AsanaType type) {
        ContentValues cv = new ContentValues();
        cv.put(Types.COLUMN_TYPE,type.getType());
        db.insert(Types.TABLE_NAME,null,cv);
    }

    public void fillAsanasTable(){
        String descriptionMalasana="Lorem ipsum dolor sit amet, consectetur adipiscing elit. In finibus lectus sed ligula mollis ultricies. Suspendisse et libero ut risus tincidunt hendrerit a ut nisl. In hac habitasse platea dictumst. Aenean at commodo est. Nunc egestas aliquam elementum. Nulla mollis.";
        Bitmap bitmapMalasana=BitmapFactory.decodeResource(context.getResources(), R.drawable.malasana);
        byte[] imageMalasana = getBitmapAsByteArray(bitmapMalasana); // this is a function
        Asana a1= new Asana("Malasana","Girlanda",imageMalasana,descriptionMalasana,AsanaType.POZYCJA_STOJACA, (byte) 2,true);
        insertAsana(a1);

        String descriptionBalasana="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse nisi purus, aliquet non turpis sit amet, euismod dapibus lacus. Aenean luctus at augue a tristique. Quisque in dui erat. Nunc et congue lacus. Nulla cursus nibh lacus, non facilisis mi.";
        Bitmap bitmapBalasana=BitmapFactory.decodeResource(context.getResources(),R.drawable.balasana);
        byte[] imageBalasana = getBitmapAsByteArray(bitmapBalasana); // this is a function
        Asana a2= new Asana("Balasana","Dziecko",imageBalasana,descriptionBalasana,AsanaType.POZYCJA_RELAKSACYJNA, (byte) 1,true);
        insertAsana(a2);
        //TODO:zmienić na vector assety, żeby sprawdzić przezroczystość i zmianę koloru
    }

    //To store a image in to db

    public void insertAsana(Asana asana) throws SQLiteException{
        ContentValues cv = new  ContentValues();
        cv.put(JogaContract.Asana.COLUMN_SANSKRIT_NAME,asana.getSanskritName());
        cv.put(JogaContract.Asana.COLUMN_NAME,asana.getName());
        cv.put(JogaContract.Asana.COLUMN_IMAGE,asana.getImage());
        cv.put(JogaContract.Asana.COLUMN_DESCRIPTION,asana.getDescription());
        cv.put(JogaContract.Asana.COLUMN_TYPE_ID,asana.getColumnTypeId());
        cv.put(JogaContract.Asana.COLUMN_DIFFICULTY,asana.getDifficulty());
        cv.put(JogaContract.Asana.COLUMN_DONE,asana.isDone());
        db.insert( JogaContract.Asana.TABLE_NAME, null, cv );
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
