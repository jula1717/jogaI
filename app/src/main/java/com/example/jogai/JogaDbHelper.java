package com.example.jogai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.example.jogai.JogaContract.*;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class JogaDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="joga.db";
    public static final int DATABASE_VERSION=1;
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
                + Types.COLUMN_IMAGE_RESOURCE+" INTEGER, "
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


    public ArrayList<Point> countAllAmountSpecificType(){
        db = getReadableDatabase();
        final String COLUMN_QUANTITY = "quantity";
        Cursor c = db.rawQuery("SELECT COUNT("+ JogaContract.Asana._ID+") AS "+COLUMN_QUANTITY+", "+
                        JogaContract.Asana.COLUMN_TYPE_ID+" FROM "+JogaContract.Asana.TABLE_NAME+
                " GROUP BY "+JogaContract.Asana.COLUMN_TYPE_ID,null);
                //SELECT count(_id) AS ilosc, type FROM asanas GROUP BY type;
        ArrayList<Point> progress = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                Point point = new Point();
                int typeId = c.getInt(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_TYPE_ID));
                int quantity = c.getInt(c.getColumnIndexOrThrow(COLUMN_QUANTITY));
                point.set(typeId,quantity);
                progress.add(point);
            }while(c.moveToNext());
        }
        c.close();
        return progress;
    }

    public ArrayList<Point> countDoneAmountSpecificType(){
        db = getReadableDatabase();
        final String COLUMN_QUANTITY = "quantity";
        String[] selectionArgs = {String.valueOf(1)};
        Cursor c = db.rawQuery("SELECT "+ JogaContract.Asana.COLUMN_TYPE_ID +", COUNT("+ JogaContract.Asana._ID+") AS "+COLUMN_QUANTITY+" FROM "+JogaContract.Asana.TABLE_NAME+
                " WHERE "+ JogaContract.Asana.COLUMN_DONE +"=? GROUP BY "+JogaContract.Asana.COLUMN_TYPE_ID,selectionArgs);
        //SELECT type, count(_id) AS ilosc FROM asanas WHERE done=1 GROUP BY type;
        ArrayList<Point> progress = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                Point point = new Point();
                int typeId = c.getInt(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_TYPE_ID));
                int quantity = c.getInt(c.getColumnIndexOrThrow(COLUMN_QUANTITY));
                point.set(typeId,quantity);
                progress.add(point);
            }while(c.moveToNext());
        }
        c.close();
        return progress;
    }

    public ArrayList<AsanaType> getTypes() {
        db = getReadableDatabase();
        Cursor c = db.query(JogaContract.Types.TABLE_NAME,null,null,null,null,null,null);
        ArrayList<AsanaType> types = new ArrayList<>();
        if (c.moveToFirst()) {
            do{
                AsanaType type = new AsanaType();
                type.setImgRes(c.getInt(c.getColumnIndexOrThrow(Types.COLUMN_IMAGE_RESOURCE)));
                type.setType(c.getString(c.getColumnIndexOrThrow(Types.COLUMN_TYPE)));
                type.setId(c.getInt(c.getColumnIndexOrThrow(Types._ID)));
                types.add(type);
            }while(c.moveToNext());
        }
        c.close();
        return types;
    }

    public ArrayList<Asana> getAsanas() {
        db = getReadableDatabase();
        Cursor c = db.query(JogaContract.Asana.TABLE_NAME,null,null,null,null,null,null);
        ArrayList<Asana> asanas = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                Asana asana = new Asana();
                asana.setName(c.getString(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_NAME)));
                asana.setSanskritName(c.getString(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_SANSKRIT_NAME)));
                asana.setDescription(c.getString(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_DESCRIPTION)));
                asana.setDifficulty((byte) c.getInt(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_DIFFICULTY)));
                asana.setImgRes(c.getInt(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_IMAGE)));
                boolean value = c.getInt(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_DONE)) > 0;
                asana.setDone(value);
                asana.setColumnTypeId(c.getInt(c.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_TYPE_ID)));
                asanas.add(asana);
            } while (c.moveToNext());
        }
        c.close();
        return asanas;
    }


    public void changeAsanaState(int position, boolean newState) {
        db = getWritableDatabase();
        String[] selectionArgs = {String.valueOf(position+1)};
        Cursor c;
        if(newState){
            c = db.rawQuery("UPDATE " + JogaContract.Asana.TABLE_NAME + " SET " + JogaContract.Asana.COLUMN_DONE + "=1" + " WHERE " + JogaContract.Asana._ID + "=?", selectionArgs);
        }
        else{
            c = db.rawQuery("UPDATE " + JogaContract.Asana.TABLE_NAME + " SET " + JogaContract.Asana.COLUMN_DONE + "=0" + " WHERE " + JogaContract.Asana._ID + "=?", selectionArgs);
        }
        c.moveToFirst();
        c.close();
    }

    //fill tables, could comment later

    public void fillTypesTable(){
        AsanaType t1 = new AsanaType("pozycja stojąca",R.drawable.s1);
        insertType(t1);
        AsanaType t2 = new AsanaType("pozycja siedząca",R.drawable.s2);
        insertType(t2);
        AsanaType t3 = new AsanaType("skłon do przodu",R.drawable.s3);
        insertType(t3);
        AsanaType t4 = new AsanaType("wygięcie do tyłu",R.drawable.s4);
        insertType(t4);
    }

    private void insertType(AsanaType type) {
        ContentValues cv = new ContentValues();
        cv.put(Types.COLUMN_TYPE,type.getType());
        cv.put(Types.COLUMN_IMAGE_RESOURCE,type.getImgRes());
        db.insert(Types.TABLE_NAME,null,cv);
    }

    public void fillAsanasTable() {
        for(int i = 0 ; i<50;i++) {
            String descriptionChair = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse nisi purus, aliquet non turpis sit amet, euismod dapibus lacus. Aenean luctus at augue a tristique. Quisque in dui erat. Nunc et congue lacus. Nulla cursus nibh lacus, non facilisis mi.";
            Asana a1 = new Asana("Utkatasana", "Krzesło", descriptionChair, AsanaType.POZYCJA_STOJACA, (byte) 1, false, R.drawable.camel);
            insertAsana(a1);
        }

//        String descriptionCamel = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse nisi purus, aliquet non turpis sit amet, euismod dapibus lacus. Aenean luctus at augue a tristique. Quisque in dui erat. Nunc et congue lacus. Nulla cursus nibh lacus, non facilisis mi.";
//        Bitmap bitmapCamel = BitmapFactory.decodeResource(context.getResources(), R.drawable.camel);
//        byte[] imageCamel = getBitmapAsByteArray(bitmapCamel); // this is a function
//        Asana a2 = new Asana("Ustrasana", "Wielbłąd", imageCamel, descriptionCamel, AsanaType.WYGIECIE_DO_TYLU, (byte) 2, true);
//        insertAsana(a2);
//
//        String descriptionChair2 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse nisi purus, aliquet non turpis sit amet, euismod dapibus lacus. Aenean luctus at augue a tristique. Quisque in dui erat. Nunc et congue lacus. Nulla cursus nibh lacus, non facilisis mi.";
//        Bitmap bitmapChair2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.chair2);
//        byte[] imageChair2 = getBitmapAsByteArray(bitmapChair2); // this is a function
//        Asana a3 = new Asana("Utkatasana", "Krzesło", imageChair2, descriptionChair, AsanaType.POZYCJA_STOJACA, (byte) 1, true);
//        insertAsana(a3);
//
//        String descriptionCamel2 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse nisi purus, aliquet non turpis sit amet, euismod dapibus lacus. Aenean luctus at augue a tristique. Quisque in dui erat. Nunc et congue lacus. Nulla cursus nibh lacus, non facilisis mi.";
//        Bitmap bitmapCamel2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.camel2);
//        byte[] imageCamel2 = getBitmapAsByteArray(bitmapCamel2); // this is a function
//        Asana a4 = new Asana("Ustrasana", "Wielbłąd", imageCamel2, descriptionCamel, AsanaType.WYGIECIE_DO_TYLU, (byte) 2, false);
//        insertAsana(a4);

    }

    //To store a image in to db

    public void insertAsana(Asana asana) throws SQLiteException{
        ContentValues cv = new  ContentValues();
        cv.put(JogaContract.Asana.COLUMN_SANSKRIT_NAME,asana.getSanskritName());
        cv.put(JogaContract.Asana.COLUMN_NAME,asana.getName());
        cv.put(JogaContract.Asana.COLUMN_IMAGE,asana.getImgRes());
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
