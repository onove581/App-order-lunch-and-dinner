package com.example.landdserver.ViewHolders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Interface.ItemClickListener;
import com.example.landdserver.R;

public class ShowCommentViewHolder  extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener {

private ItemClickListener itemClickListener;

public TextView txt;
    public TextView txtUserphone,txtcomment,txtnamecomment;
    public RatingBar rating;
public ShowCommentViewHolder (View itemView) {
        super(itemView);

//        itemView.setOnClickListener(this);
    txtUserphone=itemView.findViewById(R.id.txtUserphone);
    txtcomment=itemView.findViewById(R.id.txtshowcomm);
    txtnamecomment=itemView.findViewById(R.id.txtshowname);
    rating=itemView.findViewById(R.id.showratingbar);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0, 0, getAdapterPosition(), Common.DELETE);

    }
}
