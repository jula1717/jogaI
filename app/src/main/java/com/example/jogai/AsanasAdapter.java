package com.example.jogai;

import android.content.Context;
import android.database.Cursor;
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
    Cursor cursor;

    public AsanasAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public void onBindViewHolder(@NonNull AsanasViewHolder holder, int position) {
        if(!cursor.moveToPosition(position)){
            return;
        }
        else{
//            long id = cursor.getLong(cursor.getColumnIndexOrThrow(GroceryEntry._ID));
//            holder.itemView.setTag(id);
            String sanskritName = cursor.getString(cursor.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_SANSKRIT_NAME));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_NAME));
            byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(JogaContract.Asana.COLUMN_IMAGE));
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.length);
            //boolean done = cursor.getInt(Integer.parseInt(JogaContract.Asana.COLUMN_DONE)) > 0; //TODO: tu chyba jest problem

            holder.txtName.setText(name);
            holder.txtSanskritName.setText(sanskritName);
            int drawableResourceId = context.getResources().getIdentifier("ic_done", "drawable", context.getPackageName());
            holder.imgIconDone.setImageResource(drawableResourceId);
            holder.imgAsanaImage.setImageBitmap(imageBitmap);
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void swapCursor(Cursor newCursor){
        if(cursor!=null){
            cursor.close();
        }
        cursor=newCursor;
        if(newCursor!=null){
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
    @NonNull
    @Override
    public AsanasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_single_item,parent,false);
        AsanasViewHolder holder = new AsanasViewHolder(view);
        return holder;
    }

    public static class AsanasViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAsanaImage;
        ImageView imgIconDone;
        TextView txtSanskritName;
        TextView txtName;
        public AsanasViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAsanaImage = itemView.findViewById(R.id.asanaImg);
            txtSanskritName = itemView.findViewById(R.id.asanaSanskritNameTxt);
            txtName = itemView.findViewById(R.id.asanaNameTxt);
            imgIconDone = itemView.findViewById(R.id.doneIcon);
        }
    }
}
