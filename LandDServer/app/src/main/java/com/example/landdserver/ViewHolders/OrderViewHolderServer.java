package com.example.landdserver.ViewHolders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Interface.ItemClickListener;
import com.example.landdserver.R;


public class OrderViewHolderServer extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener,
        View.OnCreateContextMenuListener {

    private ItemClickListener itemClickListener;
public Button btnedit,btndetail,btnremove,btnmaps;

    public OrderViewHolderServer(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        btndetail=itemView.findViewById(R.id.buttondetail);
        btnedit=itemView.findViewById(R.id.buttonedit);
        btnmaps=itemView.findViewById(R.id.buttondirection);
        btnremove=itemView.findViewById(R.id.buttonremove);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onclick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select Action");
        contextMenu.add(0, 0, getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0, 1, getAdapterPosition(), Common.DELETE);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onclick(v,getAdapterPosition(),true);

        return true;
    }
}
