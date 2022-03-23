package com.example.landd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.landd.Common.Common;
import com.example.landd.Database.Database;
import com.example.landd.Model.Mon;
import com.example.landd.Model.Order;
import com.example.landd.Model.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import static android.widget.Toast.LENGTH_SHORT;

public class MonDetail extends AppCompatActivity implements RatingDialogListener {
    TextView mon_name,mon_price,mon_decription;
    ImageView mon_imgage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btncart;
    ElegantNumberButton numberButton;
    String Monid="";
    FirebaseDatabase database;
    DatabaseReference mons;
Mon mo;
RatingBar ratingbar;
FloatingActionButton btnrating;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference foods;
    DatabaseReference ratings;
    private int ratingValue = 4;
    Button btnshowco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
       database=FirebaseDatabase.getInstance();
       mons=database.getReference("Mon");
      ratings = database.getReference("Rating");
       numberButton=findViewById(R.id.btn_price);
       mon_decription=findViewById(R.id.food_description);
       mon_name=findViewById(R.id.food_name);
       mon_price=findViewById(R.id.food_price);
       mon_imgage=findViewById(R.id.img_food);
       btncart=findViewById(R.id.btnCart);
       btnrating=findViewById(R.id.btnRating);
ratingbar=findViewById(R.id.food_rating);
btnshowco=findViewById(R.id.btnshowcomment);
btnshowco.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent id=new Intent(MonDetail.this,ShowComment.class);
                id.putExtra("MonId",Monid);
        startActivity(id);
    }
});
       btnrating.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              showRatingDialog();
           }
       });
       btncart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new Database(getBaseContext()).addToCart(new Order(
                       Monid,
                       mo.getName(),
                       numberButton.getNumber(),

mo.getPrice(),
                       mo.getDiscount(),
                       mo.getImage()

                       ));
               Toast.makeText(MonDetail.this,"Add to cart",Toast.LENGTH_LONG).show();
           }
       });
       collapsingToolbarLayout=findViewById(R.id.collapsing);
collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedAppbar);
collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.colapsedAppbar);
if(getIntent()!=null)
{
    Monid=getIntent().getStringExtra("Monid");

}
if(!Monid.isEmpty())
        {
           if (Common.isInternetAvailable(getBaseContext())) {
               getDetailMon(Monid);
               getRaing(Monid);
           }else {
               Toast.makeText(MonDetail.this, "Please check internet", LENGTH_SHORT).show();
               return;
           }
        }

    }

    private void getRaing(String monid) {
        Query foodrating=ratings.orderByChild("Monid").equalTo(monid);
        foodrating.addValueEventListener(new ValueEventListener() {
           int count=0,sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postsnap:snapshot.getChildren())
                {
                    Rating item=postsnap.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRating());
                    count++;
                }
                if (count !=0)
                {
                    float aver=sum/count;
                    ratingbar.setRating(aver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(5)
                .setTitle("Rate this food")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colordefault)
                .setDescriptionTextColor(R.color.purple_500)
                .setHint("Please wrtite your comment")
                .setHintTextColor(R.color.white)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.colordefault)
                .setWindowAnimation(R.style.RatingDialogFadeAnimation)
                .create(MonDetail.this)
                .show();
    }
//
    private void getDetailMon(String Monid){
        mons.child(Monid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mo=dataSnapshot.getValue(Mon.class);
                assert mo != null;
                Picasso.get().load(mo.getImage()).into(mon_imgage);
                collapsingToolbarLayout.setTitle(mo.getName());
                mon_price.setText(mo.getPrice());
                mon_name.setText(mo.getName());
                mon_decription.setText(mo.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public void onNegativeButtonClicked() {
        Toast.makeText(this, "You provide no rating", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onPositiveButtonClicked(int i,  String s) {
         Rating rating =  new Rating(Common.currentUser.getPhoned(),Monid,String.valueOf(i),s,mo.getName());
         ratings.push().setValue(rating)
                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         Toast.makeText(MonDetail.this, "Thank you for submit rating", Toast.LENGTH_SHORT).show();
                     }
                 });
//        ratings.child(Common.currentUser.getPhoned()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//if (snapshot.child(Common.currentUser.getPhoned()).exists())
//{
//    ratings.child(Common.currentUser.getPhoned()).removeValue();
//ratings.child(Common.currentUser.getPhoned()).setValue(rating);
//}
//else {
//    ratings.child(Common.currentUser.getPhoned()).setValue(rating);
//}
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        Toast.makeText(this, "Thank you for rating food", Toast.LENGTH_SHORT).show();
    }

}
