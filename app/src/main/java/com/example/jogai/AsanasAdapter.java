package com.example.jogai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AsanasAdapter extends RecyclerView.Adapter<AsanasAdapter.AsanasViewHolder>{
    List<Asana> asanas;
    Context context;

    public AsanasAdapter(Context context, List <Asana> asanas) {
        this.context = context;
        this.asanas = asanas;
    }

    @Override
    public void onBindViewHolder(@NonNull AsanasViewHolder holder, int position) {
        Asana asana = asanas.get(position);
            String sanskritName = asana.getSanskritName();
            String name = asana.getName();
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(asana.getImage(),0,asana.getImage().length);
            holder.txtName.setText(name);
            holder.txtSanskritName.setText(sanskritName);
            int drawableResourceId = context.getResources().getIdentifier("ic_done", "drawable", context.getPackageName());
            holder.imgIconDone.setImageResource(drawableResourceId);
            holder.imgAsanaImage.setImageBitmap(imageBitmap);

        if(asana.isDone()){
            holder.imgIconDone.setColorFilter(context.getResources().getColor(R.color.teal_200));
            holder.imgAsanaImage.setColorFilter(context.getResources().getColor(R.color.teal_200));
        }else{
            holder.imgIconDone.setColorFilter(context.getResources().getColor(R.color.dark_gray));
            holder.imgAsanaImage.setColorFilter(context.getResources().getColor(R.color.dark_gray));
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    @Override
    public int getItemCount() {
         return asanas.size();
    }
    @NonNull
    @Override
    public AsanasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_single_asana,parent,false);
        AsanasViewHolder holder = new AsanasViewHolder(view,this.listener);
        return holder;
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public static class AsanasViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAsanaImage;
        ImageView imgIconDone;
        TextView txtSanskritName;
        TextView txtName;
        public AsanasViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imgAsanaImage = itemView.findViewById(R.id.asanaImg);
            txtSanskritName = itemView.findViewById(R.id.asanaSanskritNameTxt);
            txtName = itemView.findViewById(R.id.asanaNameTxt);
            imgIconDone = itemView.findViewById(R.id.doneIcon);
            imgIconDone.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener!=null)
                    {
                        int position = getAbsoluteAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDoneClicked(position,imgIconDone,imgAsanaImage);
                        }
                    }
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int position = getAbsoluteAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClicked(position);
                        }
                    }
                }
            });
        }
    }
    interface  OnItemClickListener{
        public void onItemClicked(int position);
        public void onDoneClicked(int position, ImageView imgIconDone, ImageView imgAsana);
    }

}
