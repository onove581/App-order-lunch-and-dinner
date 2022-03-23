package com.example.landdserver;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Model.Request;
import com.example.landdserver.Services.OrderListenService;
import com.example.landdserver.ViewHolders.OrderViewHolderServer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class OrderStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView textViewName;
    TextView textViewStatus;
    TextView textViewPhone;
    TextView textViewAddress;
    TextView  textViewId,txtOrderdate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    FirebaseRecyclerAdapter<Request, OrderViewHolderServer> adapter;
    private MaterialSpinner orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");
        recyclerView=findViewById(R.id.recycler_order_status);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
                if(getIntent().getStringExtra("phone") != null)
        {
            showOrders(getIntent().getStringExtra("Phone"));
        }
        else
        {
            showOrders(Common.currentUser.getPhoned());
        }
        Intent intentServer = new Intent(OrderStatus.this, OrderListenService.class);
        startService(intentServer);
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
    private void loadOrder() {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                request,Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolderServer>(options) {
            @NonNull
            @Override
            public OrderViewHolderServer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderViewHolderServer(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolderServer holder, int position, @NonNull final Request model) {
                textViewId = holder.itemView.findViewById(R.id.order_id);
                textViewId.setText(adapter.getRef(position).getKey());

                textViewPhone = holder.itemView.findViewById(R.id.order_phone);
                textViewPhone.setText(model.getPhone());

                textViewAddress = holder.itemView.findViewById(R.id.order_address);
                textViewAddress.setText(model.getAddress());

                textViewStatus = holder.itemView.findViewById(R.id.order_status);
                textViewStatus.setText(Common.getStatus(model.getStatus()));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(OrderStatus.this, TrackingOrderActivity.class);
//                         Common.currentRequest = model;
//                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), (Request) adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    //Helper Method
    private void showOrders(String phone)
    {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                request,Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolderServer>(options) {
            @NonNull
            @Override
            public OrderViewHolderServer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderViewHolderServer(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolderServer holder, int position, @NonNull final Request model) {
                TextView textViewId = holder.itemView.findViewById(R.id.order_id);
                textViewId.setText(String.format("Order id: %s",adapter.getRef(position).getKey()));

                TextView textViewPhone = holder.itemView.findViewById(R.id.order_phone);
                textViewPhone.setText(String.format("Phone: %s",model.getPhone()));

                TextView textViewAddress = holder.itemView.findViewById(R.id.order_address);
                textViewAddress.setText(String.format("Adress: %s",model.getAddress()));

                TextView textViewStatus = holder.itemView.findViewById(R.id.order_status);
                textViewStatus.setText(String.format("Status: %s",Common.getStatus(model.getStatus())));
                txtOrderdate = holder.itemView.findViewById(R.id.order_date);
                txtOrderdate.setText(String.format("Date: %s",Common.getdate(Long.parseLong(adapter.getRef(position).getKey()))));
                holder.btnedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(), (Request) adapter.getItem(position));

                    }
                });
                holder.btnremove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });
                holder.btndetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderStatus.this, Order_Detail.class);

                       Common.currentRequest = model;
                       intent.putExtra("ordersId",adapter.getRef(position).getKey());
                        startActivity(intent);

                    }
                });
                holder.btnmaps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderStatus.this, TrackingOrder.class);
                       Common.currentRequest = model;
                        startActivity(intent);

                    }
                });
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(OrderStatus.this, TrackingOrder.class);
//                       Common.currentRequest = model;
//                        startActivity(intent);
////                        Intent intent = new Intent(OrderStatus.this, Order_Detail.class);
////
////                       Common.currentRequest = model;
////                       intent.putExtra("OrdersId",adapter.getRef(position).getKey());
////                        startActivity(intent);
//                    }
//                });
            }
        };
        recyclerView.setAdapter(adapter);
    }



    private void showUpdateDialog( String key, final Request item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");
        alertDialog.setIcon(R.drawable.ic_access_time_black_24dp);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.update_order_dialog, null);
        alertDialog.setView(view);

        orderStatus = view.findViewById(R.id.order_status);

        orderStatus.setItems("Placed", "Shipping", "Shipped");
        orderStatus.setSelectedIndex(Integer.parseInt(item.getStatus()));
final String localkey=key;
        alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setStatus(String.valueOf(orderStatus.getSelectedIndex()));
//                adapter.notifyDataSetChanged();
                request.child(localkey).setValue(item);
            }
        });

        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }


    private void deleteOrder(String key) {
        request.child(key).removeValue();
    }


}
