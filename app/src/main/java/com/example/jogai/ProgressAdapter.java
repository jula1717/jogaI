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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>{

   List<AsanaType> types;
   Context context;
   List<Point> progress;
   List<Point> quantity;
   JogaDbHelper dbHelper;

    public ProgressAdapter(List<AsanaType> types, Context context, List<Point> quantity) {
        this.types = types;
        this.context = context;
        this.quantity = quantity;
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
        int allQuantity = 0;
        for (Point single:quantity) {
            if (single.x==type.getId()){
                allQuantity = single.y;
            }
        }
        progress = dbHelper.countDoneAmountSpecificType();
        int doneQuantity = 0;
        for (Point single:progress) {
            if (single.x==type.getId()){
                doneQuantity = single.y;
            }
        }
        ProgressBarAnimation anim = new ProgressBarAnimation(holder.pbAsanaType, 0, doneQuantity*1000);
        anim.setDuration(1000);
        holder.pbAsanaType.setMax(allQuantity*1000);
        holder.pbAsanaType.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAsanaTypeImage;
        TextView txtAsanaType;
        ProgressBar pbAsanaType;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAsanaTypeImage = itemView.findViewById(R.id.asanaTypeImg);
            txtAsanaType = itemView.findViewById(R.id.asanaTypeTxt);
            pbAsanaType = itemView.findViewById(R.id.asanaTypeProgressBar);
        }
    }
}
