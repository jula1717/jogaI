package com.example.jogai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.TimingLogger;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jogai.comparators.DifficultyComparator;
import com.example.jogai.comparators.DoneComparator;
import com.example.jogai.comparators.NameComparator;
import com.example.jogai.comparators.SanskritComparator;
import com.example.jogai.comparators.TypeComparator;
import com.example.jogai.comparators.UndoneComparator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabase db;
    RecyclerView.LayoutManager layoutManager;
    AsanasAdapter adapter;
    JogaDbHelper dbHelper;
    ArrayList<Asana> asanas;

    //sort type
    int comparatorNumber=2;
    public static final String KEY_COMPARATOR = "com.example.jogai.MainActivity.KEY_COMPARATOR";

    //dark mode
    boolean nightmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String KEY_ASANAS_LIST = "com.example.jogai.MainActivity.KEY_ASANAS_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        dbHelper = JogaDbHelper.getInstance(this);
        if(asanas==null) {
            asanas = dbHelper.getAsanas();
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        Comparator comparator = new DoneComparator();
        Collections.sort(asanas, comparator);
        adapter= new AsanasAdapter(getApplicationContext(),asanas);
        if(savedInstanceState!=null){
            comparatorNumber = savedInstanceState.getInt(KEY_COMPARATOR,2);
        }
        mySort(comparatorNumber);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        //dark mode
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightmode = sharedPreferences.getBoolean("night",false);
        changeMode2();

        adapter.setListener(new AsanasAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                AsanaFragment fragment = AsanaFragment.newInstance(asanas.get(position),getApplicationContext());
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                transaction.replace(R.id.frameLayout,fragment).addToBackStack("asana_fragment").commit();
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
        saveData();
    }

    private void saveData(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("ASANAS",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(asanas);
        editor.putString(KEY_ASANAS_LIST,json);
        editor.putInt(KEY_COMPARATOR,comparatorNumber);
        editor.apply();
    }

    private void loadData(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("ASANAS",MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_ASANAS_LIST,null);
        comparatorNumber = sharedPreferences.getInt(KEY_COMPARATOR,2);
        Type type = new TypeToken<ArrayList<Asana>>(){}.getType();
        asanas = gson.fromJson(json,type);
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
                changeMode();
                return true;
            case R.id.sort_difficulty: {
                if(comparatorNumber==1) return true;
                comparatorNumber = 1;
                mySort(comparatorNumber);
                return true;
            }
            case R.id.sort_done: {
                if(comparatorNumber==2) return true;
                comparatorNumber = 2;
                mySort(comparatorNumber);
                return true;
            }
            case R.id.sort_name: {
                if(comparatorNumber==3) return true;
                comparatorNumber = 3;
                mySort(comparatorNumber);
                return true;
            }
            case R.id.sort_sanskrit: {
                if(comparatorNumber==4) return true;
                comparatorNumber = 4;
                mySort(comparatorNumber);
                return true;
            }
            case R.id.sort_type: {
                if(comparatorNumber==5) return true;
                comparatorNumber = 5;
                mySort(comparatorNumber);
                return true;
            }
            case R.id.sort_undone: {
                if(comparatorNumber==6) return true;
                comparatorNumber = 6;
                mySort(comparatorNumber);
                return true;
            }
            case R.id.achievements:{
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(isFragmentInBackstack(fragmentManager,"progress_fragment")){
                    return true;
                }
                if(isFragmentInBackstack(fragmentManager,"about_fragment")){
                    fragmentManager.popBackStack ("about_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                Fragment progressFragment = ProgressFragment.newInstance(asanas,getApplicationContext());
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                transaction.replace(R.id.frameLayout,progressFragment).addToBackStack("progress_fragment").commit();
                return true;
            }
            case R.id.about:{
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(isFragmentInBackstack(fragmentManager,"about_fragment")){
                    return true;
                }
                if(isFragmentInBackstack(fragmentManager,"progress_fragment")){
                    fragmentManager.popBackStack ("progress_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                Fragment aboutFragment = new AboutFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                transaction.replace(R.id.frameLayout,aboutFragment).addToBackStack("about_fragment").commit();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeMode() {

        if(nightmode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor = sharedPreferences.edit();
            editor.putBoolean("night",false);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor = sharedPreferences.edit();
            editor.putBoolean("night",true);
        }
        editor.apply();
    }

    private void changeMode2() {

        if(nightmode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_COMPARATOR,comparatorNumber);

    }

    private void mySort(int comparatorNumber) {
        Comparator comparator;
        switch (comparatorNumber){
            case 1:
            default:
                comparator = new DifficultyComparator();
                break;
            case 2:
                comparator = new DoneComparator();
                break;
            case 3:
                comparator = new NameComparator();
                break;
            case 4:
                comparator = new SanskritComparator();
                break;
            case 5:
                comparator = new TypeComparator();
                break;
            case 6:
                comparator = new UndoneComparator();
                break;
        }
        Collections.sort(asanas, comparator);
        adapter.notifyDataSetChanged();
        saveData();
    }

    public static boolean isFragmentInBackstack(final FragmentManager fragmentManager, final String fragmentTagName) {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getName())) {
                return true;
            }
        }
        return false;
    }

}