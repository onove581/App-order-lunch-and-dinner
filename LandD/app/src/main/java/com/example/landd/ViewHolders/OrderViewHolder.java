package com.example.landd.ViewHolders;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.landd.Interface.ItemClickListener;
import com.example.landd.R;


public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
public TextView txtOrderid,txtOrderStatus,txtOrderPhone,txtOrderadress,txttotal,txtOrderdate;
public Button detail;
public ImageView cancel;
private ItemClickListener itemClickListener;




    public OrderViewHolder(View itemView) {
        super(itemView);
txtOrderadress=itemView.findViewById(R.id.order_address);
txtOrderid=itemView.findViewById(R.id.order_id);
        txtOrderPhone=itemView.findViewById(R.id.order_phone);
        txtOrderStatus=itemView.findViewById(R.id.order_status);
        txttotal=itemView.findViewById(R.id.order_total);
        txtOrderdate=itemView.findViewById(R.id.order_date);
        detail=itemView.findViewById(R.id.order_detailsss);
        cancel=itemView.findViewById(R.id.cancelorder);
//itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onclick(view,getAdapterPosition(),false);

    }
}
