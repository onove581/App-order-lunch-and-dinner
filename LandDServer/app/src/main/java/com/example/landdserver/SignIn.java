package com.example.landdserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Model.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class SignIn extends AppCompatActivity {
    EditText editPhone, editPassord;
    Button btnSignIn;
    CheckBox rememberMe;
    FirebaseDatabase db;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editPhone = findViewById(R.id.editPhone);
        editPassord = findViewById(R.id.editPassord);
        btnSignIn = findViewById(R.id.btnSignIn);
        rememberMe = findViewById(R.id.remember_me);
db=FirebaseDatabase.getInstance();
users=db.getReference("Account");
btnSignIn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
SignInUser(editPhone.getText().toString(),editPassord.getText().toString());
    }
});
    }

    private void SignInUser(String Phone, String Passs) {
        ProgressDialog dialog=new ProgressDialog(SignIn.this);
        dialog.setMessage("Please waits..");
        dialog.show();
        final String Localpho=Phone;
        final String localp=Passs;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
if(snapshot.child(Localpho).exists())
{
    dialog.dismiss();
    Account user=  snapshot.child(Localpho).getValue(Account.class);
    user.setPhone(Localpho);
if (Boolean.parseBoolean(user.getIsStaff()))
{
if (user.getPassword().equals(localp))
{
    Intent sig=new Intent(SignIn.this, Home.class);
    Common.currentUser=user;
    startActivity(sig);
    finish();
    Toast.makeText(SignIn.this,"Thanh cong",Toast.LENGTH_SHORT).show();
}
else Toast.makeText(SignIn.this,"That bai",Toast.LENGTH_SHORT).show();
}
else Toast.makeText(SignIn.this,"Stafff",Toast.LENGTH_SHORT).show();


}
else {
    dialog.dismiss();
    Toast.makeText(SignIn.this,"Khong phai la Admin",Toast.LENGTH_SHORT).show();


}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
