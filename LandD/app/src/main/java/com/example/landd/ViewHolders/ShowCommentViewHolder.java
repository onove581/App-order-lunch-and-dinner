package com.example.landd.ViewHolders;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landd.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
    public TextView txtUserphone,txtcomment;
    public RatingBar rating;

    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        txtUserphone=itemView.findViewById(R.id.txtUserphone);
        txtcomment=itemView.findViewById(R.id.txtshowcomm);
        rating=itemView.findViewById(R.id.showratingbar);
    }
}
