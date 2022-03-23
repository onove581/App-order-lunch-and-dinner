package com.example.landdserver.ViewHolders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Interface.ItemClickListener;
import com.example.landdserver.R;

public class BannerViewHolder extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener {

    private ItemClickListener itemClickListener;

    public TextView banner_name;
    public ImageView banner_image;
    public BannerViewHolder(View itemView) {
        super(itemView);
        banner_name=itemView.findViewById(R.id.banner_name);
        banner_image=itemView.findViewById(R.id.banner_image);
//        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }


//    public void setItemClickListener(ItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }

//    @Override
//    public void onClick(View view) {
//        itemClickListener.onclick(view, getAdapterPosition(), false);
//    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select action");
        contextMenu.add(0, 0, getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0, 1, getAdapterPosition(), Common.DELETE);
    }
}