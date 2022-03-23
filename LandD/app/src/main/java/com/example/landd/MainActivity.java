package com.example.landd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.landd.Common.Common;
import com.example.landd.Model.Account;
import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start=findViewById(R.id.btn_get_start);
        Paper.init(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
printHasKey();
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start=new Intent(MainActivity.this, SignIn.class);
                startActivity(start);
            }
        });
        String user=Paper.book().read(Common.USER_NAME);
        String pass=Paper.book().read(Common.PWD_Key);
        if (user!=null && pass!=null)
        {
            if (!user.isEmpty()&& !pass.isEmpty())
                login(user,pass);
        }
    }

    private void printHasKey() {
        try {
            PackageInfo info=getPackageManager().getPackageInfo("com.example.landd", PackageManager.GET_SIGNATURES);
            for(Signature sig:info.signatures)
            {
                MessageDigest  md= MessageDigest.getInstance("SHA");
                md.update(sig.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));

            }

        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

    }

    private void login(String phone, String pass) {
        if(Common.isInternetAvailable(getBaseContext())){
            final FirebaseDatabase database=FirebaseDatabase.getInstance();
            final DatabaseReference table_account=database.getReference("Account");
            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please waitting...");
            mDialog.show();
            table_account.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(phone).exists()) {

                        Account account = dataSnapshot.child(phone).getValue(Account.class);
                        account.setPhone(phone);
                        if (account.getPassword().equals(pass)) {
                            Toast.makeText(MainActivity.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = account;
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Dang nhap that bai", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Khong co tai khoan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{ Toast.makeText(MainActivity.this, "Please check internet", LENGTH_SHORT).show();return;}

    }
}