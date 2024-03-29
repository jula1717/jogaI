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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
    RecyclerView.LayoutManager layoutManager;
    AsanasAdapter adapter;
    JogaDbHelper dbHelper;
    ArrayList<AsanaModel> asanas;
    int comparatorNumber=2;
    boolean nightmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FragmentManager fragmentManager;

    //KEYS AND TAGS
    public static final String KEY_ASANAS_LIST = "com.example.jogai.MainActivity.KEY_ASANAS_LIST";
    public static final String SHARED_PREFERENCES_KEY = "com.example.jogai.MainActivity.SHARED_PREFERENCES_KEY";
    public static final String NIGHTMODE_KEY = "com.example.jogai.MainActivity.NIGHTMODE_KEY";
    public static final String KEY_COMPARATOR = "com.example.jogai.MainActivity.KEY_COMPARATOR";
    public static final String ABOUT_FRAGMENT_TAG = "com.example.jogai.MainActivity.ABOUT_FRAGMENT_TAG";
    public static final String PROGRESS_FRAGMENT_TAG = "com.example.jogai.MainActivity.PROGRESS_FRAGMENT_TAG";
    public static final String ASANA_FRAGMENT_TAG = "com.example.jogai.MainActivity.ASANA_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loadData();
        dbHelper = JogaDbHelper.getInstance(this);
        if(asanas==null) {
            asanas = dbHelper.getAsanas();
        }
        initializeAsanasRecyclerView(savedInstanceState);
        fragmentManager= getSupportFragmentManager();
        nightmode = sharedPreferences.getBoolean(NIGHTMODE_KEY,false);
        changeModeFirstTime();
    }

    private void getMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }
    }

    AsanasAdapter.OnItemClickListener onItemClickListener = new AsanasAdapter.OnItemClickListener() {
        @Override
        public void onItemClicked(int position) {
            AsanaFragment fragment = AsanaFragment.newInstance(asanas.get(position),getApplicationContext());
            replaceFragment(fragmentManager, fragment, ASANA_FRAGMENT_TAG);
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        public void onDoneClicked(int position, ImageView imgIconDone, ImageView imgAsana) {
            changeState(position, imgIconDone,imgAsana);
        }
    };

    private void initializeAsanasRecyclerView(Bundle savedInstanceState) {
        recyclerView = findViewById(R.id.asanasRecyclerView);
        recyclerView.setHasFixedSize(true);
        Comparator comparator = new DoneComparator();   // default comparator
        Collections.sort(asanas, comparator);
        adapter= new AsanasAdapter(getApplicationContext(),asanas);
        if(savedInstanceState !=null){
            comparatorNumber = savedInstanceState.getInt(KEY_COMPARATOR,2);
        }
        mySort(comparatorNumber);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setListener(onItemClickListener);
    }

    public void changeState(int position, ImageView imgIconDone,ImageView imgAsana) {
        boolean newState = true;
        if(asanas.get(position).isDone()){
            newState = false;
        }
        asanas.get(position).setDone(newState);
        dbHelper.changeAsanaState(position,newState);
        if(asanas.get(position).isDone()){
            imgIconDone.setColorFilter(getResources().getColor(R.color.s3));
            imgAsana.setColorFilter(getResources().getColor(R.color.s3));
        }else{
            if(!nightmode){
                imgAsana.setColorFilter(getResources().getColor(R.color.very_light_gray));
            }else{
                imgAsana.setColorFilter(getResources().getColor(R.color.light_gray));
                imgIconDone.setColorFilter(getResources().getColor(R.color.gray));
            }
        }
        mySort(comparatorNumber);
        adapter.notifyDataSetChanged();
        saveData();
    }

    private void saveData(){
        Gson gson = new Gson();
        String json = gson.toJson(asanas);
        editor.putString(KEY_ASANAS_LIST,json);
        editor.putInt(KEY_COMPARATOR,comparatorNumber);
        editor.apply();
    }

    private void loadData(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_ASANAS_LIST,null);
        comparatorNumber = sharedPreferences.getInt(KEY_COMPARATOR,2);
        Type type = new TypeToken<ArrayList<AsanaModel>>(){}.getType();
        asanas = gson.fromJson(json,type);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
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
                return changeComparatorNumber(1);
            }
            case R.id.sort_done: {
                return changeComparatorNumber(2);
            }
            case R.id.sort_name: {
                return changeComparatorNumber(3);
            }
            case R.id.sort_sanskrit: {
                return changeComparatorNumber(4);
            }
            case R.id.sort_type: {
                return changeComparatorNumber(5);
            }
            case R.id.sort_undone: {
                return changeComparatorNumber(6);
            }
            case R.id.achievements:{
                if (checkBackstack(PROGRESS_FRAGMENT_TAG, ABOUT_FRAGMENT_TAG)) return true;
                Fragment progressFragment = ProgressFragment.newInstance(getApplicationContext());
                replaceFragment(fragmentManager, progressFragment, PROGRESS_FRAGMENT_TAG);
                return true;
            }
            case R.id.about:{
                if (checkBackstack(ABOUT_FRAGMENT_TAG, PROGRESS_FRAGMENT_TAG)) return true;
                Fragment aboutFragment = new AboutFragment();
                recyclerView.setVisibility(View.GONE);
                replaceFragment(fragmentManager, aboutFragment, ABOUT_FRAGMENT_TAG);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkBackstack(String fragmentTagName, String fragmentToPopTagName) {
        if (isFragmentInBackstack(fragmentManager, fragmentTagName)) {
            return true;
        }
        if (isFragmentInBackstack(fragmentManager, fragmentToPopTagName)) {
            fragmentManager.popBackStack(fragmentToPopTagName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        return false;
    }

    private void replaceFragment(FragmentManager fragmentManager, Fragment fragment, String fragmentTag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.replace(R.id.fragmentContainer, fragment).addToBackStack(fragmentTag).commit();
    }

    private boolean changeComparatorNumber(int i) {
        if (comparatorNumber == i) return true;
        comparatorNumber = i;
        mySort(comparatorNumber);
        return true;
    }

    private void changeMode() {

        if(nightmode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean(NIGHTMODE_KEY,false);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean(NIGHTMODE_KEY,true);
        }
        editor.apply();
    }

    private void changeModeFirstTime() {

        getMode();
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