package com.example.landdserver.ViewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landdserver.Model.Order;
import com.example.landdserver.R;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView names,ok,quantity,price,discount;
            public MyViewHolder(View itemview)
            {

                super(itemview);
                names=itemview.findViewById(R.id.product_names);

            quantity=itemview.findViewById(R.id.product_quantity);
            price=itemview.findViewById(R.id.product_price);
            discount=itemview.findViewById(R.id.product_discount);

            }

}
public class OrderDetailAdapt extends RecyclerView.Adapter<MyViewHolder> {
List<Order> myorder;

    public OrderDetailAdapt(List<Order> myorder) {
        this.myorder = myorder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_order__detail__layout,parent,false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
Order order=myorder.get(position);

        holder.names.setText(String.format("Name: %s",order.getMonName()));
holder.quantity.setText(String.format("Quantity: %s",order.getQuantity()));
        holder.price.setText(String.format("Price: %s",order.getPrice()));
        holder.discount.setText(String.format("Discount: %s",order.getDiscount()));


    }

    @Override
    public int getItemCount() {
        return myorder.size();
    }
}
