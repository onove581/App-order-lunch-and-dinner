//package com.example.landd;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.widget.RelativeLayout;
//
//import com.example.landd.ViewHolders.FavoritesAdapter;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class FavoritesActiviry extends AppCompatActivity {
//RecyclerView recyclerView;
//RecyclerView.LayoutManager layoutManager;
//FirebaseDatabase database;
//DatabaseReference Monlist;
//FavoritesAdapter adapter;
//RelativeLayout rootlay;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_favorites_activiry);
//        rootlay=findViewById(R.id.root_layout);
//        recyclerView=findViewById(R.id.recycler_fa);
//        layoutManager=new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        String name=recyclerView.getAdapter().get
//    }
//}