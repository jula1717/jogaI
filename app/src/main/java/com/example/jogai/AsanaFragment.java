package com.example.jogai;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public static AsanaFragment newInstance(Asana asana){
        AsanaFragment fragment = new AsanaFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ASANA,asana);
        fragment.setArguments(args);
        return fragment;
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
        iconDone = view.findViewById(R.id.iconDone);
        image = view.findViewById(R.id.asanaImg);
        if(getArguments()!=null){
            asana = getArguments().getParcelable(ARG_ASANA);
        }
        sankritName.setText(asana.getName());
        name.setText(asana.getName());
        description.setText(asana.getDescription()); //TODO: co jak napcham za dużo tekstu, czy to się scrolluje, czy trzeba to doprogramować
        type.setText(""+asana.getColumnTypeId()); //TODO: zmienić sql tak, żeby tu była konkretna kategoria, a nie jej id
        difficulty.setText(""+asana.getDifficulty());
        if(asana.isDone()) iconDone.setVisibility(View.VISIBLE);
        else iconDone.setVisibility(View.INVISIBLE);
        image.setImageBitmap(BitmapFactory.decodeByteArray(asana.getImage(), 0, asana.getImage().length));
        image.setVisibility(View.VISIBLE);
        details.setVisibility(View.INVISIBLE);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:dodać animację
                image.setVisibility(View.INVISIBLE);
                details.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }


}
