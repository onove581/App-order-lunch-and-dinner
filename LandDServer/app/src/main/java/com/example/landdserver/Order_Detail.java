package com.example.landdserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.landdserver.Common.Common;
import com.example.landdserver.ViewHolders.OrderDetailAdapt;

public class Order_Detail extends AppCompatActivity {
TextView order_id,order_phone,address,total,comment,name;
String order_id_val="";
RecyclerView lsfood;
RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__detail);
        order_id=findViewById(R.id.order_ids);
        order_phone=findViewById(R.id.order_phoned);
        address=findViewById(R.id.order_addr);
        name=findViewById(R.id.order_name);
        total=findViewById(R.id.order_total);
        comment=findViewById(R.id.order_com);
        lsfood=findViewById(R.id.lstFood);
        lsfood.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        lsfood.setLayoutManager(layoutManager);
        if (getIntent()!=null)
            order_id_val=getIntent().getStringExtra("ordersId");
        order_id.setText(String.format("Order id: %s",order_id_val));
        name.setText(String.format("Name: %s",Common.currentRequest.getName()));
        order_phone.setText(String.format("Phone: %s",Common.currentRequest.getPhone()));
        address.setText(String.format("Adress: %s",Common.currentRequest.getAddress()));
        total.setText(String.format("Total: %s",Common.currentRequest.getTotal()));
        comment.setText(String.format("Status: %s",Common.currentRequest.getComment()));
        OrderDetailAdapt adapt=new OrderDetailAdapt(Common.currentRequest.getOrders());
        adapt.notifyDataSetChanged();
        lsfood.setAdapter(adapt);

    }
}