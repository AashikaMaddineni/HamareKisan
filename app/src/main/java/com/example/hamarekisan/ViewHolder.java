package com.example.hamarekisan;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import com.bumptech.glide.Glide;
import android.graphics.BitmapFactory;


public class ViewHolder extends RecyclerView.ViewHolder {
    View mview;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mview = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return false;
            }
        });

    }
    public void setDetails(Context ctx, String prediction, String image, String confidence, String date, String uploadtype, String uid) {
        TextView mTitle=mview.findViewById(R.id.titleView);
        ImageView mImage=mview.findViewById(R.id.imageView);
        TextView mPrediction=mview.findViewById(R.id.predictionView);
        TextView mDate=mview.findViewById(R.id.dateView);
        mTitle.setText("Prediction : "+prediction);
        mPrediction.setText("Confidence : "+confidence+"%");
        mDate.setText("Date : "+date);
        if(uploadtype.equals("Camera")){
            byte[] bytes=Base64.decode(image ,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            mImage.setImageBitmap(bitmap);
        }
        else if(uploadtype.equals("Storage")) {
            Glide.with(ctx)
                    .load(image).placeholder(R.mipmap.ic_launcher).fitCenter().centerCrop()
                    .into(mImage);
        }
    }
    private ClickListener mClickListener;



    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(ClickListener clickListener){
        mClickListener=clickListener;
    }

}

