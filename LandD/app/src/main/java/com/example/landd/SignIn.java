package com.example.landd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.landd.Common.Common;
import com.example.landd.Model.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static android.widget.Toast.LENGTH_SHORT;

public class SignIn extends AppCompatActivity {
    TextView register;
    EditText phone,password;
    Button login;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        register=findViewById(R.id.gotoRegister);
        phone=findViewById(R.id.inputphone);
        password=findViewById(R.id.inputPassword);
        login=findViewById(R.id.btnLogin);
        checkBox=findViewById(R.id.checsign);
        Paper.init(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent starts=new Intent(SignIn.this, SignUp.class);
                startActivity(starts);
            }
        });
        //Init dataabse
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_account=database.getReference("Account");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isInternetAvailable(getBaseContext())){
if (checkBox.isChecked())
{
    Paper.book().write(Common.USER_NAME,phone.getText().toString());
    Paper.book().write(Common.PWD_Key,password.getText().toString());
}
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waitting...");
                mDialog.show();
                table_account.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(phone.getText().toString()).exists()) {

                            Account account = dataSnapshot.child(phone.getText().toString()).getValue(Account.class);
                            account.setPhone(phone.getText().toString());
                            if (account.getPassword().equals(password.getText().toString())) {
                                Toast.makeText(SignIn.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common.currentUser = account;
                                startActivity(homeIntent);
                                finish();
                                table_account.removeEventListener(this);
                            } else {
                                Toast.makeText(SignIn.this, "Dang nhap that bai", Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "Khong co tai khoan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else{ Toast.makeText(SignIn.this, "Please check internet", LENGTH_SHORT).show();return;}
        }
        });
    }
}
