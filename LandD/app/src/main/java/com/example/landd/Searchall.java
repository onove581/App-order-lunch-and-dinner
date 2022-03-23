package com.example.landd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.landd.Common.Common;
import com.example.landd.Database.Database;
import com.example.landd.Interface.ItemClickListener;
import com.example.landd.Model.Mon;
import com.example.landd.Model.Order;
import com.example.landd.ViewHolders.FoodViewHolder;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class Searchall extends AppCompatActivity {
    RecyclerView recyclerViewFood;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference foods;
    //search
    FirebaseRecyclerAdapter<Mon, FoodViewHolder> adapter;
    FirebaseRecyclerAdapter<Mon,FoodViewHolder>searchAd;
    List<String> suggest=new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    Database localDB;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Target target=new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto=new  SharePhoto.Builder().setBitmap(bitmap).build();
            if (ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content=new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchall);

        shareDialog=new ShareDialog(this);
        callbackManager= CallbackManager.Factory.create();

        recyclerViewFood=findViewById(R.id.recycler_search);
        recyclerViewFood.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerViewFood.setLayoutManager(layoutManager);

        firebaseDatabase=FirebaseDatabase.getInstance();
        foods=firebaseDatabase.getReference("Mon");
        materialSearchBar=findViewById(R.id.searchBarall);
        materialSearchBar.setHint("Enter your rice");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggest);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> sug=new ArrayList<String>();
                for (String search:suggest)
                {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        sug.add(search);
                }
                materialSearchBar.setLastSuggestions(sug);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerViewFood.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        loadallfood();
    }

    private void loadallfood() {
        FirebaseRecyclerOptions<Mon> options = new FirebaseRecyclerOptions.Builder<Mon>().
                setQuery(foods, Mon.class).build();
        adapter =new FirebaseRecyclerAdapter<Mon, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int i, @NonNull Mon mon) {
//        TextView textViewName = viewHolder.itemView.findViewById(R.id.food_name);
//        ImageView imageView = viewHolder.itemView.findViewById(R.id.food_image);
//        final ImageView imageFbShare = viewHolder.itemView.findViewById(R.id.fb_share);
//
//        textViewName.setText(mon.getName());
//        Picasso.get().load(mon.getImage()).into(imageView);
                viewHolder.monname.setText(mon.getName());
                viewHolder.mon_price.setText(String.format("$ %s",mon.getPrice().toString()));
                Picasso.get().load(mon.getImage()).into(viewHolder.mon_image);
                final Mon local= mon;
                if (localDB.isFavorite(adapter.getRef(i).getKey(), Common.currentUser.getPhoned()))
                    viewHolder.fa_img.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                viewHolder.share_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Picasso.get().load(mon.getImage()).into(target);
                        Toast.makeText(Searchall.this,"Share is successfull",Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.fa_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    Favorites favorites=new Favorites();
//                    favorites.setMonid(adapter.getRef(i).getKey());
//                    favorites.setMonName(mon.getName());
//                    favorites.setMonDecription(mon.getDescription());
//                    favorites.setMonDiscount(mon.getDiscount());
//                    favorites.setMonImage(mon.getImage());
//                    favorites.setMonMenuid(mon.getMenuId());
//                    favorites.setUserPhone(Common.currentUser.getPhoned());
//                    favorites.setMonPrice(mon.getPrice());
                        if (!localDB.isFavorite(adapter.getRef(i).getKey(),Common.currentUser.getPhoned()))
                        {
                            localDB.addToFavorite(adapter.getRef(i).getKey(),Common.currentUser.getPhoned());
                            viewHolder.fa_img.setImageResource(R.drawable.ic_baseline_favorite_24);
                            Toast.makeText(Searchall.this,""+mon.getName()+"was add to favorites",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            localDB.removeFromFavorite(adapter.getRef(i).getKey(),Common.currentUser.getPhoned());
                            viewHolder.fa_img.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            Toast.makeText(Searchall.this,""+mon.getName()+"was remove to favorites",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                viewHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Database(getBaseContext()).addToCart(new Order(
                                searchAd.getRef(i).getKey(),
                                mon.getName(),
                                "1",
                                mon.getPrice(),
                                mon.getDiscount(),
                                mon.getImage()
                        ));
                        Toast.makeText(Searchall.this,"Add to cart",Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        Intent mondetail=new Intent(Searchall.this,MonDetail.class);
                        mondetail.putExtra("Monid", adapter.getRef(position).getKey());
                        startActivity(mondetail);
                    }
                });


            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_item, parent, false);
                return new FoodViewHolder(view);
            }
        };
        recyclerViewFood.setAdapter(adapter);
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
        if (searchAd!=null)
            searchAd.stopListening();

    }
    private void startSearch(CharSequence text) {
        FirebaseRecyclerOptions<Mon> options = new FirebaseRecyclerOptions.Builder<Mon>().
                setQuery(foods.orderByChild("name").equalTo(text.toString()), Mon.class).build();
        searchAd=new FirebaseRecyclerAdapter<Mon, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int i, @NonNull Mon mon) {
                viewHolder.monname.setText(mon.getName());
                Picasso.get().load(mon.getImage()).into(viewHolder.mon_image);
                final Mon local= mon;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        Intent mondetail=new Intent(Searchall.this,MonDetail.class);
                        mondetail.putExtra("Monid", searchAd.getRef(position).getKey());
                        startActivity(mondetail);
                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_item, parent, false);
                return new FoodViewHolder(view);
            }
        };
        recyclerViewFood.setAdapter(searchAd);
        searchAd.startListening();
    }
    private void loadSuggest()
    {
        foods.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot:snapshot.getChildren())
                {
                    Mon item=postSnapshot.getValue(Mon.class);
                    //assert item != null;
                    suggest.add(item.getName());
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}