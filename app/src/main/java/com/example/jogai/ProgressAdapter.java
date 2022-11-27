package com.example.jogai;

import android.content.Context;
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

import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>{

   List<TypeModel> types;
   Context context;
   List<Point> doneQuantity;
   List<Point> allQuantity;
   JogaDbHelper dbHelper;
   int allRegardlessOfType =0, doneRegardlessOfType =0;

    public ProgressAdapter(List<TypeModel> types, Context context) {
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
        TypeModel type = types.get(position);
        String typeName = type.getType();
        holder.txtAsanaType.setText(typeName);
        int imgRes = type.getImgRes();
        holder.imgAsanaType.setImageResource(imgRes);
        allQuantity = dbHelper.countNumberOfAllAsanasSpecificType();
        int allQuantity = 0;
        for (Point singleType: this.allQuantity) {
            if (singleType.x==type.getId()){
                allQuantity = singleType.y;
                allRegardlessOfType +=allQuantity;
            }
        }
        doneQuantity = dbHelper.countNumberOfDoneAsanasSpecificType();
        int doneQuantity = 0;
        for (Point singleType: this.doneQuantity) {
            if (singleType.x==type.getId()){
                doneQuantity = singleType.y;
                doneRegardlessOfType +=doneQuantity;
            }
        }
        ProgressBarAnimation anim = new ProgressBarAnimation(holder.pbAsanaType, 0, doneQuantity*1000);
        anim.setDuration(1000);
        holder.pbAsanaType.setMax(allQuantity*1000);
        holder.pbAsanaType.startAnimation(anim);
        holder.txtDone.setText(String.valueOf(doneQuantity));
        holder.txtAll.setText(String.valueOf(allQuantity));
        if(allRegardlessOfType == doneRegardlessOfType){
            StyleableToast.makeText(context, context.getString(R.string.congrats),Toast.LENGTH_SHORT,R.style.congratsToast).show();
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAsanaType;
        TextView txtAsanaType,txtDone,txtAll;
        ProgressBar pbAsanaType;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAsanaType = itemView.findViewById(R.id.imgAsanaType);
            txtAsanaType = itemView.findViewById(R.id.txtAsanaType);
            pbAsanaType = itemView.findViewById(R.id.pbAsanaType);
            txtDone = itemView.findViewById(R.id.txtDone);
            txtAll = itemView.findViewById(R.id.txtAll);
        }
    }
}
