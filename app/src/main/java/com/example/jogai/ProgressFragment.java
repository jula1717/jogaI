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

import java.util.ArrayList;

public class ProgressFragment extends Fragment {
    public static final String ARG_ASANAS="asanas";
    private ArrayList<Asana> asanas;
    Context context;
    JogaDbHelper dbHelper;

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
        TextView textView;
        textView = view.findViewById(R.id.textView);
        if(getArguments()!=null){
            asanas = getArguments().getParcelableArrayList(ARG_ASANAS);
        }

        dbHelper = JogaDbHelper.getInstance(context);
        //tu wywołanie metody, która będzie obliczała progress i inicjalizacja gui tymi danymi

        return view;
    }
}
