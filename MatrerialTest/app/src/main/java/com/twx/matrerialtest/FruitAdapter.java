package com.twx.matrerialtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by twx on 2017/2/28.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private static final String TAG = "FruitAdapter";
    private Context mContext;
    private List<Fruit> mFruitList;
    
    public FruitAdapter() {
        Log.d(TAG, "FruitAdapter: ");
    }

     static class  ViewHolder extends  RecyclerView.ViewHolder{
          CardView cardView;
         ImageView fruitImage;
         TextView fruitName;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            fruitImage = (ImageView)view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
        }
    }

    public FruitAdapter(List<Fruit> mFruitList) {
        Log.d(TAG, "FruitAdapter List<Fruit> mFruitList: ");
        this.mFruitList = mFruitList;
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: viewType "+viewType);
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Intent intent = new Intent(mContext, FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME, fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID, fruit.getImageId());
                mContext.startActivity(intent);

//                Toast.makeText(v.getContext(),"you click view:"+fruit.getName(),Toast.LENGTH_SHORT).show();
            }
        });
       /* viewHolder.fruitImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext(),"you click image:"+fruit.getName(),Toast.LENGTH_SHORT).show();
            }
        });*/
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Fruit fruit = mFruitList.get(position);
        Log.d(TAG, "onBindViewHolder: position "+position);
        holder.fruitName.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
    }
}
