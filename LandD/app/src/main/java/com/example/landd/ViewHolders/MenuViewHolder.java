package com.example.landd.ViewHolders;

//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.landd.Interface.ItemClickListener;
import com.example.landd.R;
//import com.example.salmangeforce.food_order.Interface.ItemClickListener;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;
public TextView txtmenu;
public ImageView imageView;
    public MenuViewHolder(View itemView) {
        super(itemView);

      imageView=itemView.findViewById(R.id.menu_image);
        txtmenu= itemView.findViewById(R.id.menu_name);
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
