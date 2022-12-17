package com.example.jogai;

import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

public class AsanaFragment extends Fragment {
    public static final String ARG_ASANA="asana";
    private AsanaModel asana;
    Context context;
    JogaDbHelper dbHelper;
    TextView sankritName,name,type,description;
    RoundCornerProgressBar pbDifficulty;
    ImageView iconDone,image,change;
    boolean imageVisible = true;

    public static AsanaFragment newInstance(AsanaModel asana, Context context){
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
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        sankritName = view.findViewById(R.id.txtSanskritName);
        name = view.findViewById(R.id.txtName);
        type = view.findViewById(R.id.txtAsanaType);
        description = view.findViewById(R.id.txtDescription);
        iconDone = view.findViewById(R.id.imgDone);
        image = view.findViewById(R.id.imgAsana);
        change = view.findViewById(R.id.imgDescriptionImageChange);
        pbDifficulty = view.findViewById(R.id.pbDifficultyLevel);


        if(getArguments()!=null){
            asana = getArguments().getParcelable(ARG_ASANA);
        }

        sankritName.setText(asana.getSanskritName());
        name.setText(asana.getName());
        ProgressBarAnimation anim = new ProgressBarAnimation(pbDifficulty, 0, asana.getDifficulty()*1000);
        anim.setDuration(1000);
        pbDifficulty.setMax(5*1000);
        pbDifficulty.startAnimation(anim);
        pbDifficulty.setProgress(asana.getDifficulty()*1000);
        type.setText(getTypeName());
        description.setText(asana.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());
        change.setOnClickListener(imageAndDescriptionListener);
        image.setImageResource(asana.getImgRes());
        iconDoneDisplay();
        description.setVisibility(View.INVISIBLE);
    }

    private void iconDoneDisplay() {
        if(asana.isDone()) {
            iconDone.setVisibility(View.VISIBLE);
        }
        else {
            iconDone.setVisibility(View.INVISIBLE);
        }
    }

    private String getTypeName() {
        dbHelper = JogaDbHelper.getInstance(context);
        String typeName = dbHelper.getTypeNameById(asana.getColumnTypeId());
        return typeName;
    }


    View.OnClickListener imageAndDescriptionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(imageVisible){
                image.startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_out));
                description.startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_in));
                imageVisible=false;
            }else{
                image.startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_in));
                description.startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_out));
                imageVisible=true;
            }
        }
    };

}
