package com.example.landdserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Model.Banner;
import com.example.landdserver.Model.Rating;
import com.example.landdserver.ViewHolders.BannerViewHolder;
import com.example.landdserver.ViewHolders.ShowCommentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Comment extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference retingtbl;
    SwipeRefreshLayout swipeRefreshLayout;
    String monId = "";
    FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        database = FirebaseDatabase.getInstance();
        retingtbl = database.getReference("Rating");
        recyclerView = findViewById(R.id.recycler_comment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = findViewById(R.id.showcomment);

        loadlistcomment();

    }

    private void loadlistcomment() {
        FirebaseRecyclerOptions<Rating> allbaner=new FirebaseRecyclerOptions.Builder<Rating>()
                .setQuery(retingtbl,Rating.class).build();
        adapter=new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(allbaner) {
            @Override
            protected void onBindViewHolder(@NonNull ShowCommentViewHolder bannerViewHolder, int i, @NonNull Rating banner) {
                bannerViewHolder.txtUserphone.setText(String.format("SDT: %s",banner.getUserPhone()));
                bannerViewHolder.txtnamecomment.setText(String.format("Mon an: %s",banner.getMonName()));
                bannerViewHolder.txtcomment.setText(String.format("Noi dung: %s",banner.getComment()));
                bannerViewHolder.rating.setRating(Float.parseFloat(banner.getRating()));
            }

            @NonNull
            @Override
            public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
                return new ShowCommentViewHolder(view);

            }
        };
//        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
//        if(searchAdapter != null)
//            searchAdapter.stopListening();
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

         if(item.getTitle().equals(Common.DELETE))
        {
            deleteBannerFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteBannerFood(String key) {
        retingtbl.child(key).removeValue();
    }

}