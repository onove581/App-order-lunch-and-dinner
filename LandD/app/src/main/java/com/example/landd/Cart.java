package com.example.landd;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landd.Common.Common;
import com.example.landd.Database.Database;
import com.example.landd.Interface.ItemClickListener;
import com.example.landd.Model.Order;
import com.example.landd.Model.Request;
import com.example.landd.ViewHolders.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public TextView textViewPrice;
    Button buttonOrder;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    CartAdapter cartAdapter;
    List<Order> orders;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");
        recyclerView=findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        textViewPrice=findViewById(R.id.order_price);
        buttonOrder=findViewById(R.id.btnPlaceOrder);
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orders.size()>0)
                showDialog();
                else Toast.makeText(Cart.this, "Cart empty", Toast.LENGTH_SHORT).show();
//                Request request=new Request(
//                        Common.currentUser.getPhoned(),
//                        Common.currentUser.getName(),
//                        Adress,
//                        textViewPrice.getText().toString(),


            }
        });



        loadCart();
    }


    //Helper Methods
    private void loadCart() {
        orders = new Database(this).getCarts();

       cartAdapter=new CartAdapter(orders,this);
       recyclerView.setAdapter(cartAdapter);


        int total = 0;
        //Calculating total price
        for(Order order: orders)
        {
            total += (Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity())
                    - Integer.parseInt(order.getDiscount()) * Integer.parseInt(order.getQuantity()));
        }
        textViewPrice.setText(String.format("Total: $ %s", total));

    }

//    private void showDialog() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("One more step!");
//        builder.setMessage("Enter your Address: ");
//        final EditText editText = new EditText(this);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        editText.setLayoutParams(layoutParams);
//        builder.setView(editText);
//        builder.setIcon(R.drawable.ic_baseline_shopping_cart_24);
//
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Request req = new Request(Common.currentUser.getName(),
//                        Common.currentUser.getPhone(),
//                        editText.getText().toString(),
//                        textViewPrice.getText().toString(),
//                        orders);
//
//                //sending to firebase
//                request.child(String.valueOf(System.currentTimeMillis())).setValue(req);
//
//                new Database(CartActivity.this).cleanCart();
//                Toast.makeText(CartActivity.this, "Order is placed. Thank You!", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//
//        builder.show();
//    }
private void showDialog() {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("One more step!");
    builder.setMessage("Enter your Address: ");
//    final EditText editText = new EditText(this);
//    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//    );
//    editText.setLayoutParams(layoutParams);
//    builder.setView(editText);
    LayoutInflater inflater=this.getLayoutInflater();
    View order_address_comment=inflater.inflate(R.layout.order_address_comment,null);
    final MaterialEditText editAdress=order_address_comment.findViewById(R.id.or_address);
    final MaterialEditText editcomm=order_address_comment.findViewById(R.id.or_comment);
builder.setView(order_address_comment);
    builder.setIcon(R.drawable.ic_baseline_shopping_cart_24);

    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Request req = new Request(Common.currentUser.getName(),
                    Common.currentUser.getPhoned(),
                    editAdress.getText().toString(),
                    textViewPrice.getText().toString(),
                    "0",//status
                    editcomm.getText().toString(),
                    orders);

            //sending to firebase
            request.child(String.valueOf(System.currentTimeMillis())).setValue(req);

            new Database(Cart.this).cleanCart();
            Toast.makeText(Cart.this, "Order is placed. Thank You!", Toast.LENGTH_SHORT).show();
            finish();
        }
    });

    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    });

    builder.show();
}
@Override
    public boolean onContextItemSelected(MenuItem item)
{
    if (item.getTitle().equals(Common.DELETE))
        deletecart(item.getOrder());

    return true;
}
private void deletecart(int p)
{
    orders.remove(p);
    new Database(this).cleanCart();
    for (Order item:orders)
        new Database(this).addToCart(item);
    loadCart();
}
}
