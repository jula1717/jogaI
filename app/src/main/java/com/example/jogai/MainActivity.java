package com.example.jogai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabase db;
    RecyclerView.LayoutManager layoutManager;
    AsanasAdapter adapter;
    JogaDbHelper dbHelper;
    ArrayList<Asana> asanas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        dbHelper = JogaDbHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();
        String columnSortBy = JogaContract.Asana.COLUMN_SANSKRIT_NAME;
//        asanas=dbHelper.getSortedAsanas(columnSortBy);
//        adapter = new AsanasAdapter(getApplicationContext(),asanas);
//        adapter= new AsanasAdapter(getApplicationContext(), getAllAsanas(columnSortBy));
        getAllAsanas();
        adapter= new AsanasAdapter(getApplicationContext(),asanas);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


        adapter.setListener(new AsanasAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                recyclerView.setVisibility(View.GONE);
                AsanaFragment fragment = AsanaFragment.newInstance(asanas.get(position),getApplicationContext());
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).addToBackStack("my_fragment").commit();
            }

            @Override
            public void onDoneClicked(int position, ImageView imgIconDone, ImageView imgAsana) {
                changeState(position, imgIconDone,imgAsana);
            }
        });
    }

    public void changeState(int position, ImageView imgIconDone,ImageView imgAsana) {
        boolean newState = true;
        if(asanas.get(position).isDone()){
            newState = false;
        }
        asanas.get(position).setDone(newState);
        dbHelper.changeAsanaState(position,newState);
        //ColorFilter colorFilter = imgAsana.getColorFilter();
        if(asanas.get(position).isDone()){
            imgIconDone.setColorFilter(getResources().getColor(R.color.teal_200));
            imgAsana.setColorFilter(getResources().getColor(R.color.teal_200));
        }else{
            imgIconDone.setColorFilter(getResources().getColor(R.color.gray));
            imgAsana.setColorFilter(getResources().getColor(R.color.gray));
        }
        adapter.notifyDataSetChanged();
    }

    private void getAllAsanas(){
        db = dbHelper.getWritableDatabase();
        int number = dbHelper.countAsanas();
        for(int i=1;i<=number;i++){
            asanas.add(dbHelper.getAsana(i));
        }
    }
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
        recyclerView.setVisibility(View.VISIBLE);

    }

}