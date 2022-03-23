//package com.example.landd.ViewHolders;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.landd.Common.Common;
//import com.example.landd.Database.Database;
//import com.example.landd.Interface.ItemClickListener;
////import com.example.landd.Model.Favorites;
//import com.example.landd.Model.Mon;
//import com.example.landd.Model.Order;
//import com.example.landd.MonDetail;
//import com.example.landd.MonList;
//import com.example.landd.R;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {
//private Context context;
//private List<Favorites> favorites;
//public FavoritesAdapter(Context context,List<Favorites> favoritesList)
//{
//    this.context=context;
//    this.favorites=favoritesList;
//}
//    @NonNull
//    @Override
//    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//    View view= LayoutInflater.from(context).inflate(R.layout.favorites_item,parent,false);
//
//    return new FavoritesViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
//        holder.monname.setText(favorites.get(position).getMonName());
//        holder.mon_price.setText(String.format("$ %s",favorites.get(position).getMonPrice().toString()));
//        Picasso.get().load(favorites.get(position).getMonImage()).into(holder.mon_image);
//        holder.card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Database(context).addToCart(new Order(
//                        Common.currentUser.getPhoned(),
//                        favorites.get(position).getMonid(),
//                        favorites.get(position).getMonName(),
//                        favorites.get(position).getMonPrice(),
//                        favorites.get(position).getMonDiscount(),
//                        favorites.get(position).getMonImage()
//                ));
//                Toast.makeText(context,"Add to cart",Toast.LENGTH_LONG).show();
//            }
//
//        });
//        final Favorites local=favorites.get(position);
//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onclick(View view, int position, boolean isLongClick) {
//                Intent mondetail=new Intent(context, MonDetail.class);
//                mondetail.putExtra("Monid", favorites.get(position).getMonid());
//                context.startActivity(mondetail);
//
//            }
//        });
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return favorites.size();
//    }
//    public void removeItem(int postion)
//    {
//        favorites.remove(postion);
//        notifyItemRemoved(postion);
//    }
//    public void restoreItem(Favorites item,int pot)
//    {
//        favorites.add(pot,item);
//        notifyItemInserted(pot);
//    }
//}
