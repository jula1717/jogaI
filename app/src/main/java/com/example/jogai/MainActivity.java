package com.example.jogai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabase db;
    RecyclerView.LayoutManager layoutManager;
    AsanasAdapter adapter;
    JogaDbHelper dbHelper;
    ArrayList<Asana> asanas;

    //TODO: w shared preferences sortowanie i pozostałe ustawienia, sprawdzić też czy nie listę pytań, bo tak mam w quizie

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        dbHelper = JogaDbHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();
        asanas=dbHelper.getSortedAsanas((byte)2); //TODO: teraz to wyświetli tylko te o trudności dwa , a to ma przecież po tym sortować, zmienić SQL, wywalić WHERE a zrobić ORDER BY....
        adapter= new AsanasAdapter(getApplicationContext(), getAllAsanas());
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.swapCursor(getAllAsanas());
        adapter.setListener(new AsanasAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                //TODO: dodać animację
                recyclerView.setVisibility(View.GONE);
                AsanaFragment fragment = AsanaFragment.newInstance(asanas.get(position),getApplicationContext());
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            }
        });
    }

    private Cursor getAllAsanas(){
        return db.query(JogaContract.Asana.TABLE_NAME,null,null,null,null,null,null);
    }

}