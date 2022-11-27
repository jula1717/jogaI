package com.example.jogai;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;

public class AsanaFragment extends Fragment {
    public static final String ARG_ASANA="asana";
    private Asana asana;
    Context context;
    JogaDbHelper dbHelper;

    public static AsanaFragment newInstance(Asana asana, Context context){
        AsanaFragment fragment = new AsanaFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ASANA,asana);
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
        View view = inflater.inflate(R.layout.asana,container,false);
        TextView sankritName,name,difficulty,type,description;
        ImageView iconDone,image;
        LinearLayout details;
        details = view.findViewById(R.id.detailsLayout);
        sankritName = view.findViewById(R.id.sanskritNameTxt);
        name = view.findViewById(R.id.sanskritNameTxt);
        difficulty = view.findViewById(R.id.difficultyLevelTxt);
        type = view.findViewById(R.id.asanaTypeTxt);
        description = view.findViewById(R.id.descriptionTxt);
        description.setMovementMethod(new ScrollingMovementMethod());
        iconDone = view.findViewById(R.id.iconDone);
        image = view.findViewById(R.id.asanaImg);
        image.setColorFilter(getResources().getColor(R.color.dark_gray));
        if(getArguments()!=null){
            asana = getArguments().getParcelable(ARG_ASANA);
        }
        sankritName.setText(asana.getName());
        name.setText(asana.getName());
        description.setText(asana.getDescription());
        dbHelper = JogaDbHelper.getInstance(context);
        String category = dbHelper.getCategoryNameById(asana.getColumnTypeId());
        type.setText(category.toString());
        difficulty.setText(""+asana.getDifficulty());
        if(asana.isDone()) {
            iconDone.setVisibility(View.VISIBLE);
            iconDone.setColorFilter(context.getResources().getColor(R.color.teal_200));
        }
        else {
            iconDone.setVisibility(View.INVISIBLE);
            iconDone.setColorFilter(context.getResources().getColor(R.color.gray));
        }
        image.setImageResource(asana.getImgRes());
        image.setVisibility(View.VISIBLE);
        description.setVisibility(View.INVISIBLE);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_out));
                description.startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_in));
//                image.setEnabled(false);
//                image.setVisibility(View.INVISIBLE);
//                description.setVisibility(View.VISIBLE);
            }
        });
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_in));
                description.startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_out));
//                image.setVisibility(View.VISIBLE);
//                description.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }


}
