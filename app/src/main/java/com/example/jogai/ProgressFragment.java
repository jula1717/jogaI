package com.example.jogai;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class ProgressFragment extends Fragment {
    private ArrayList<TypeModel> types;
    Context context;
    JogaDbHelper dbHelper;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressAdapter adapter;

    public static ProgressFragment newInstance(Context context){
        ProgressFragment fragment = new ProgressFragment();
        fragment.setContext(context);
        return fragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress,container,false);
        initializeRecyclerView(view);

        return view;
    }

    private void initializeRecyclerView(View view) {
        dbHelper = JogaDbHelper.getInstance(context);
        types = dbHelper.getTypes();
        recyclerView = view.findViewById(R.id.progressRecyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new ProgressAdapter(types,context);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
