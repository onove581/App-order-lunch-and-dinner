package com.example.landd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.landd.Common.Common;
import com.example.landd.Model.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_SHORT;

public class SignUp extends AppCompatActivity {
    ImageView back;
    EditText phone,name,pass;
    Button res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        back=findViewById(R.id.icon_back);
        phone=findViewById(R.id.editphone);
        name=findViewById(R.id.editname);
        pass=findViewById(R.id.input_Password);
        res=findViewById(R.id.btn_register);
        //Init dataabse
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_account=database.getReference("Account");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(SignUp.this,SignIn.class);
                startActivity(back);
            }
        });
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isInternetAvailable(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please waitting...");
                    mDialog.show();
                    table_account.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(phone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone are ready", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                Account account = new Account(name.getText().toString(), pass.getText().toString());
                                table_account.child(phone.getText().toString()).setValue(account);
                                Toast.makeText(SignUp.this, "Sign Up is successfull", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Please check internet", LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
