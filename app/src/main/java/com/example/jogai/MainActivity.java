package com.example.jogai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.jogai.comparators.DifficultyComparator;
import com.example.jogai.comparators.DoneComparator;
import com.example.jogai.comparators.NameComparator;
import com.example.jogai.comparators.SanskritComparator;
import com.example.jogai.comparators.TypeComparator;
import com.example.jogai.comparators.UndoneComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        Comparator comparator = new DoneComparator();
        Collections.sort(asanas, comparator);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dark_theme:
                return true;
            case R.id.sort_difficulty: {
                Comparator comparator = new DifficultyComparator();
                mySort(comparator);
                return true;
            }
            case R.id.sort_done: {
                Comparator comparator = new DoneComparator();
                mySort(comparator);
                return true;
            }
            case R.id.sort_name: {
                Comparator comparator = new NameComparator();
                mySort(comparator);
                return true;
            }
            case R.id.sort_sanskrit: {
                Comparator comparator = new SanskritComparator();
                mySort(comparator);
                return true;
            }
            case R.id.sort_type: {
                Comparator comparator = new TypeComparator();
                mySort(comparator);
                return true;
            }
            case R.id.sort_undone: {
                Comparator comparator = new UndoneComparator();
                mySort(comparator);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mySort(Comparator comparator) {
        Collections.sort(asanas, comparator);
        adapter.notifyDataSetChanged();
    }
}