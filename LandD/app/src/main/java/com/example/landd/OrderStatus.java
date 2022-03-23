package com.example.landd;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landd.Common.Common;
import com.example.landd.Model.Request;
import com.example.landd.Services.OrderListenService;
import com.example.landd.ViewHolders.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");
recyclerView=findViewById(R.id.recycler_order_status);
recyclerView.setHasFixedSize(true);
layoutManager=new LinearLayoutManager(this);
recyclerView.setLayoutManager(layoutManager);


loadOrders(Common.currentUser.getPhoned());

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
    }

    private void loadOrders(String phoned) {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                request.orderByChild("phone").equalTo(phoned), Request.class).build();
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Request request) {
orderViewHolder.txtOrderid.setText(String.format("code order: %s",adapter.getRef(i).getKey()));

orderViewHolder.txtOrderStatus.setText(String.format("Status: %s",convertCodeToStatus(request.getStatus())));
orderViewHolder.txtOrderadress.setText(String.format("Address: %s",request.getAddress()));
orderViewHolder.txtOrderPhone.setText(String.format("Number Phone: %s",request.getPhone()));
orderViewHolder.txttotal.setText(String.format("Total: %s",request.getTotal()));
orderViewHolder.txtOrderdate.setText(String.format("Date: %s",Common.getdate(Long.parseLong(adapter.getRef(i).getKey()))));

                orderViewHolder.detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderStatus.this, Order_Detail.class);

                        Common.currentRequest = request;
                        intent.putExtra("OrderId",adapter.getRef(i).getKey());
                        startActivity(intent);
                    }
                });
                orderViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.getItem(i).getStatus().equals("0"))
                        {
                            deleteorder(adapter.getRef(i).getKey());
                        }
                        else
                            Toast.makeText(OrderStatus.this, "Don hang cua ban da duoc duyet", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void deleteorder(final  String key) {
        request.child(key)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(OrderStatus.this, new StringBuffer("Order").append(key)
                        .append("has been deleted").toString(),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String convertCodeToStatus(String status){
if (status.equals("0"))
    return "Place";
else if (status.equals("1"))
    return "On my way";
else return "Shipped";
}


}//class ends
