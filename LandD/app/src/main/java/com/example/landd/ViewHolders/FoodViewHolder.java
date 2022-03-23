package com.example.landd.ViewHolders;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.landd.Interface.ItemClickListener;
import com.example.landd.R;


public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
public TextView monname,mon_price;
public ImageView mon_image,fa_img,share_img,card;
    private ItemClickListener itemClickListener;


    public FoodViewHolder(View itemView) {
        super(itemView);
        monname=itemView.findViewById(R.id.food_name);
        mon_image= itemView.findViewById(R.id.food_image);
        fa_img= itemView.findViewById(R.id.favorite);
        share_img=itemView.findViewById(R.id.fb_share);
        mon_price=itemView.findViewById(R.id.fo_price);
        card=itemView.findViewById(R.id.cart);
        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onclick(view, getAdapterPosition(), false);
    }
}
