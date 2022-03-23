package com.example.landd;

import android.content.ContentProvider;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.landd.Model.Rating;
import com.example.landd.ViewHolders.ShowCommentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowComment  extends AppCompatActivity {
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
        setContentView(R.layout.content_show_comment);
        database = FirebaseDatabase.getInstance();
        retingtbl = database.getReference("Rating");
        recyclerView = findViewById(R.id.recycle_coomment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = findViewById(R.id.swiperlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent()!=null)
                    monId=getIntent().getStringExtra("MonId");

                if (!monId.isEmpty()&& monId!=null) {
                    Query query = retingtbl.orderByChild("monId").equalTo(monId);
                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query, Rating.class).build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_comment_layout, parent, false);

                            return new ShowCommentViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder showCommentViewHolder, int i, @NonNull Rating rating) {
                            showCommentViewHolder.rating.setRating(Float.parseFloat(rating.getRating()));
                            showCommentViewHolder.txtcomment.setText(rating.getComment());
                            showCommentViewHolder.txtUserphone.setText(rating.getUserPhone());
                        }
                    };
loadComment(monId);
                    }
                }




        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                if (getIntent()!=null)
                    monId=getIntent().getStringExtra("MonId");

                if (!monId.isEmpty()&& monId!=null) {
                    Query query = retingtbl.orderByChild("monId").equalTo(monId);
                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query, Rating.class).build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_comment_layout, parent, false);

                            return new ShowCommentViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder showCommentViewHolder, int i, @NonNull Rating rating) {
                            showCommentViewHolder.rating.setRating(Float.parseFloat(rating.getRating()));
                            showCommentViewHolder.txtcomment.setText(rating.getComment());
                            showCommentViewHolder.txtUserphone.setText(rating.getUserPhone());
                        }
                    };
                    loadComment(monId);
                }


            }
        });

    }
        @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
        @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }
    private void loadComment(String monid) {
   adapter.startListening();
   recyclerView.setAdapter(adapter);
   swipeRefreshLayout.setRefreshing(false);
    }
}








//

//



