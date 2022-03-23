package com.example.landd.ViewHolders;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.landd.Cart;
import com.example.landd.Common.Common;
import com.example.landd.Database.Database;
import com.example.landd.Interface.ItemClickListener;
import com.example.landd.Model.Order;
import com.example.landd.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHodel extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView txtcartname, txtprice;
//    public ImageView im_count;
    public ElegantNumberButton btn_quantity;
    public ImageView cartImage;
    private ItemClickListener itemClickListener;

    public void setTxtcartname(TextView txtcartname) {
        this.txtcartname = txtcartname;
    }

    public CartViewHodel(@NonNull View itemView) {
        super(itemView);
        txtcartname = itemView.findViewById(R.id.cart_item_name);
        txtprice = itemView.findViewById(R.id.cart_item_price);
//        im_count = itemView.findViewById(R.id.cart_item_image);
        btn_quantity=itemView.findViewById(R.id.btn_price_giohang);
        cartImage=itemView.findViewById(R.id.cardImage);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);

    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHodel>{
    private List<Order> listData=new ArrayList<>();
   private Cart card;
   public CartAdapter(List<Order> listData,Cart card)
   {
       this.listData=listData;
       this.card=card;
   }
    @NonNull
    @Override
    public CartViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(card);
        View itemview=inflater.inflate(R.layout.cart_item,parent,false);
       return new CartViewHodel(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHodel holder, int position) {
//        TextDrawable drawable=TextDrawable.builder().buildRound(""+listData.get(position).getQuantity(), Color.RED);
//
//        holder.im_count.setImageDrawable(drawable);
        Picasso.get().load(listData.get(position).getImage()).into(holder.cartImage);
holder.btn_quantity.setNumber(listData.get(position).getQuantity());
holder.btn_quantity.setOnValueChangeListener((view, oldValue, newValue) -> {
    Order order=listData.get(position);
    order.setQuantity(String.valueOf(newValue));
    new Database(card).updateCart(order);
    int total=0;
    List<Order> orders=new Database(card).getCarts();
    for (Order item: orders)
        total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
    Locale locale=new Locale("en","US");
    NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
   card.textViewPrice.setText(fmt.format(total));
});
        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));

        holder.txtprice.setText(fmt.format(price));
        holder.txtcartname.setText(listData.get(position).getMonName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}

