package com.example.jogai;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>{

   List<AsanaType> types;
   Context context;
   List<Point> progress;
   List<Point> quantity;
   JogaDbHelper dbHelper;
   int ALL=0,DONE=0;

    public ProgressAdapter(List<AsanaType> types, Context context) {
        this.types = types;
        this.context = context;
        dbHelper = JogaDbHelper.getInstance(context);
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_single_progress,parent,false);
        ProgressViewHolder holder = new ProgressViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        AsanaType type = types.get(position);
        String typeName = type.getType();
        holder.txtAsanaType.setText(typeName);
        int imgRes = type.getImgRes();
        holder.imgAsanaTypeImage.setImageResource(imgRes);
        quantity = dbHelper.countAllAmountSpecificType();
        int allQuantity = 0;
        for (Point single:quantity) {
            if (single.x==type.getId()){
                allQuantity = single.y;
                ALL+=allQuantity;
            }
        }
        progress = dbHelper.countDoneAmountSpecificType();
        int doneQuantity = 0;
        for (Point single:progress) {
            if (single.x==type.getId()){
                doneQuantity = single.y;
                DONE+=doneQuantity;
            }
        }
        ProgressBarAnimation anim = new ProgressBarAnimation(holder.pbAsanaType, 0, doneQuantity*1000);
        anim.setDuration(1000);
        holder.pbAsanaType.setMax(allQuantity*1000);
        holder.pbAsanaType.startAnimation(anim);
        holder.txtDone.setText(String.valueOf(doneQuantity));
        holder.txtAll.setText(String.valueOf(allQuantity));
        if(ALL==DONE){
            StyleableToast.makeText(context,"Gratulacje! Znasz już wszystkie asany",Toast.LENGTH_SHORT,R.style.congratsToast).show();
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAsanaTypeImage;
        TextView txtAsanaType,txtDone,txtAll;
        ProgressBar pbAsanaType;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAsanaTypeImage = itemView.findViewById(R.id.asanaTypeImg);
            txtAsanaType = itemView.findViewById(R.id.asanaTypeTxt);
            pbAsanaType = itemView.findViewById(R.id.asanaTypeProgressBar);
            txtDone = itemView.findViewById(R.id.doneTxt);
            txtAll = itemView.findViewById(R.id.allTxt);
        }
    }
}
