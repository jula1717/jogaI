package com.example.jogai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.jogai.JogaContract.*;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class JogaDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="joga.db";
    public static final int DATABASE_VERSION=2;
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
                + Asanas.TABLE_NAME + " ("
                + Asanas._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Asanas.COLUMN_SANSKRIT_NAME+" TEXT,"
                + Asanas.COLUMN_NAME+" TEXT,"
                + Asanas.COLUMN_IMAGE +" BLOB,"
                + Asanas.COLUMN_DESCRIPTION+" TEXT,"
                + Asanas.COLUMN_TYPE_ID+" INTEGER,"
                + Asanas.COLUMN_DIFFICULTY+" TINYINT,"
                + Asanas.COLUMN_DONE+" BOOLEAN);";

        sqLiteDatabase.execSQL(SQL_CREATE_ASANAS_TABLE);

        fillTypesTable();
        fillAsanasTable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Asanas.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ JogaContract.Types.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public String getTypeNameById(int categoryId) {
        String typeName="";
        db = getReadableDatabase();

        String[] selectionArgs = {String.valueOf(categoryId)};
        Cursor c = db.rawQuery("SELECT " + Types.COLUMN_TYPE + " FROM " + JogaContract.Types.TABLE_NAME + " WHERE " + Types._ID + "=?", selectionArgs);

        if (c.moveToFirst()) {
            typeName = c.getString(c.getColumnIndexOrThrow(Types.COLUMN_TYPE));
        }
        c.close();
        return typeName;
    }


    public ArrayList<Point> countNumberOfAllAsanasSpecificType(){
        db = getReadableDatabase();
        final String COLUMN_QUANTITY = "quantity";
        Cursor c = db.rawQuery("SELECT "+ Asanas.COLUMN_TYPE_ID +", COUNT("+ Asanas._ID+") AS "+COLUMN_QUANTITY+" FROM "+ Asanas.TABLE_NAME+
                " GROUP BY "+ Asanas.COLUMN_TYPE_ID,null);
                //SELECT count(_id) AS ilosc, type FROM asanas GROUP BY type;
        ArrayList<Point> specificAsanas = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                Point point = new Point();
                int typeId = c.getInt(c.getColumnIndexOrThrow(Asanas.COLUMN_TYPE_ID));
                int quantity = c.getInt(c.getColumnIndexOrThrow(COLUMN_QUANTITY));
                point.set(typeId,quantity);
                specificAsanas.add(point);
            }while(c.moveToNext());
        }
        c.close();
        return specificAsanas;
    }

    public ArrayList<Point> countNumberOfDoneAsanasSpecificType(){
        db = getReadableDatabase();
        final String COLUMN_QUANTITY = "quantity";
        String[] selectionArgs = {String.valueOf(1)};
        Cursor c = db.rawQuery("SELECT "+ Asanas.COLUMN_TYPE_ID +", COUNT("+ Asanas._ID+") AS "+COLUMN_QUANTITY+" FROM "+ Asanas.TABLE_NAME+
                " WHERE "+ Asanas.COLUMN_DONE +"=? GROUP BY "+ Asanas.COLUMN_TYPE_ID,selectionArgs);
        //SELECT type, count(_id) AS ilosc FROM asanas WHERE done=1 GROUP BY type;
        ArrayList<Point> specificDoneAsanas = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                Point point = new Point();
                int typeId = c.getInt(c.getColumnIndexOrThrow(Asanas.COLUMN_TYPE_ID));
                int quantity = c.getInt(c.getColumnIndexOrThrow(COLUMN_QUANTITY));
                point.set(typeId,quantity);
                specificDoneAsanas.add(point);
            }while(c.moveToNext());
        }
        c.close();
        return specificDoneAsanas;
    }

    public ArrayList<TypeModel> getTypes() {
        db = getReadableDatabase();
        Cursor c = db.query(JogaContract.Types.TABLE_NAME,null,null,null,null,null,null);
        ArrayList<TypeModel> types = new ArrayList<>();
        if (c.moveToFirst()) {
            do{
                TypeModel type = new TypeModel();
                type.setImgRes(c.getInt(c.getColumnIndexOrThrow(Types.COLUMN_IMAGE_RESOURCE)));
                type.setType(c.getString(c.getColumnIndexOrThrow(Types.COLUMN_TYPE)));
                type.setId(c.getInt(c.getColumnIndexOrThrow(Types._ID)));
                types.add(type);
            }while(c.moveToNext());
        }
        c.close();
        return types;
    }

    public ArrayList<AsanaModel> getAsanas() {
        db = getReadableDatabase();
        Cursor c = db.query(Asanas.TABLE_NAME,null,null,null,null,null,null);
        ArrayList<AsanaModel> asanas = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                AsanaModel asana = new AsanaModel();
                asana.setName(c.getString(c.getColumnIndexOrThrow(Asanas.COLUMN_NAME)));
                asana.setSanskritName(c.getString(c.getColumnIndexOrThrow(Asanas.COLUMN_SANSKRIT_NAME)));
                asana.setDescription(c.getString(c.getColumnIndexOrThrow(Asanas.COLUMN_DESCRIPTION)));
                asana.setDifficulty((byte) c.getInt(c.getColumnIndexOrThrow(Asanas.COLUMN_DIFFICULTY)));
                asana.setImgRes(c.getInt(c.getColumnIndexOrThrow(Asanas.COLUMN_IMAGE)));
                boolean value = c.getInt(c.getColumnIndexOrThrow(Asanas.COLUMN_DONE)) > 0;
                asana.setDone(value);
                asana.setColumnTypeId(c.getInt(c.getColumnIndexOrThrow(Asanas.COLUMN_TYPE_ID)));
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
            c = db.rawQuery("UPDATE " + Asanas.TABLE_NAME + " SET " + Asanas.COLUMN_DONE + "=1" + " WHERE " + Asanas._ID + "=?", selectionArgs);
        }
        else{
            c = db.rawQuery("UPDATE " + Asanas.TABLE_NAME + " SET " + Asanas.COLUMN_DONE + "=0" + " WHERE " + Asanas._ID + "=?", selectionArgs);
        }
        c.moveToFirst();
        c.close();
    }

    //fill tables, could comment later

    public void fillTypesTable(){
        TypeModel t1 = new TypeModel("pozycja stojąca",R.drawable.s1);
        insertType(t1);
        TypeModel t2 = new TypeModel("pozycja siedząca",R.drawable.s2);
        insertType(t2);
        TypeModel t3 = new TypeModel("skłon do przodu",R.drawable.s3);
        insertType(t3);
        TypeModel t4 = new TypeModel("wygięcie do tyłu",R.drawable.s4);
        insertType(t4);
    }

    private void insertType(TypeModel type) {
        ContentValues cv = new ContentValues();
        cv.put(Types.COLUMN_TYPE,type.getType());
        cv.put(Types.COLUMN_IMAGE_RESOURCE,type.getImgRes());
        db.insert(Types.TABLE_NAME,null,cv);
    }

    public void fillAsanasTable() {
        for(int i = 0 ; i<5;i++) {
            String descriptionChair = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse nisi purus, aliquet non turpis sit amet, euismod dapibus lacus. Aenean luctus at augue a tristique. Quisque in dui erat. Nunc et congue lacus. Nulla cursus nibh lacus, non facilisis mi.";
            AsanaModel a1 = new AsanaModel("Utkatasana", "Krzesło", descriptionChair, TypeModel.POZYCJA_STOJACA, (byte) 1, false, R.drawable.camel);
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

    public void insertAsana(AsanaModel asana) throws SQLiteException{
        ContentValues cv = new  ContentValues();
        cv.put(Asanas.COLUMN_SANSKRIT_NAME,asana.getSanskritName());
        cv.put(Asanas.COLUMN_NAME,asana.getName());
        cv.put(Asanas.COLUMN_IMAGE,asana.getImgRes());
        cv.put(Asanas.COLUMN_DESCRIPTION,asana.getDescription());
        cv.put(Asanas.COLUMN_TYPE_ID,asana.getColumnTypeId());
        cv.put(Asanas.COLUMN_DIFFICULTY,asana.getDifficulty());
        cv.put(Asanas.COLUMN_DONE,asana.isDone());
        db.insert( Asanas.TABLE_NAME, null, cv );
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
