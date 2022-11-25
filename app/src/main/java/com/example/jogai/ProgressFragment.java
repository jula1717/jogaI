package com.example.jogai;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class ProgressFragment extends Fragment {
    public static final String ARG_ASANAS="asanas";
    private ArrayList<Asana> asanas;
    private ArrayList<AsanaType> types;
    Context context;
    JogaDbHelper dbHelper;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressAdapter adapter;

    public static ProgressFragment newInstance(ArrayList<Asana> asanas, Context context){
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ASANAS,asanas);
        fragment.setArguments(args);
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
        if(getArguments()!=null){
            asanas = getArguments().getParcelableArrayList(ARG_ASANAS);
        }
        types = new ArrayList<AsanaType>();
//        types.add(new AsanaType("pozycje stojace"));
//        types.add(new AsanaType("pozycje siedzace"));
//        types.add(new AsanaType("pozycje lezace"));
//        types.add(new AsanaType("inne pozycje"));
        recyclerView = view.findViewById(R.id.progressRecyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new ProgressAdapter(types,context);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        dbHelper = JogaDbHelper.getInstance(context);
        //tu wywołanie metody, która będzie obliczała progress i inicjalizacja gui tymi danymi

        return view;
    }
}
