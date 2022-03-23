package com.example.landd;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.example.landd.Model.Menu;
import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.landd.Common.Common;
import com.example.landd.Database.Database;
import com.example.landd.Interface.ItemClickListener;
import com.example.landd.Model.Banner;
import com.example.landd.Services.OrderListenService;
import com.example.landd.ViewHolders.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

import static android.widget.Toast.LENGTH_SHORT;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference Menus;
    TextView textViewName;
    RecyclerView recyclerView_menu;
    RecyclerView.LayoutManager layoutManager;
    CounterFab fab;
    ImageSlider mainslider;

    FirebaseRecyclerAdapter<com.example.landd.Model.Menu, MenuViewHolder> adapter;

    HashMap<String,String > image_list;
    SliderLayout mSlider;
 @Override
 protected void onCreate(Bundle savedInstanceState){
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_home);
     Toolbar toolbar=findViewById(R.id.toolbar);
     toolbar.setTitle("");
     setSupportActionBar(toolbar);

     //Set firebase
     firebaseDatabase = FirebaseDatabase.getInstance();
     Menus = firebaseDatabase.getReference("Menu");
     Paper.init(this);
     fab=findViewById(R.id.fab);
     fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
//             Snackbar.make(view,"Replace",Snackbar.LENGTH_LONG).setAction("ACTION",null).show()
//             ;
             Intent cartI=new Intent(Home.this,Cart.class);
             startActivity(cartI);
         }
     });
     fab.setCount(new Database(this).getOrderCount());
     DrawerLayout drawer=findViewById(R.id.drawer_layout);
     ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
             this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
     drawer.setDrawerListener(toggle);
     toggle.syncState();
     NavigationView navigationView=findViewById(R.id.nav_view);
     navigationView.setNavigationItemSelectedListener(this);

View headerView=navigationView.getHeaderView(0);
textViewName=headerView.findViewById(R.id.textviewName);
textViewName.setText(Common.currentUser.getName());
recyclerView_menu=findViewById(R.id.recycler_menu);
recyclerView_menu.setHasFixedSize(true);

//layoutManager=new LinearLayoutManager(this);
//recyclerView_menu.setLayoutManager(layoutManager);
     recyclerView_menu.setLayoutManager(new GridLayoutManager(this,2));
     if (Common.isInternetAvailable(getBaseContext())) {
         loadmenu();


         //fab.setCount(new Database(this).getOrderCount());
     }else {
         Toast.makeText(Home.this, "Please check internet", LENGTH_SHORT).show();
         return;
     }
     Intent intentServer = new Intent(Home.this, OrderListenService.class);
     startService(intentServer);
//     setUpslider();

     //slider
     mainslider=(ImageSlider)findViewById(R.id.image_slider);
     final List<SlideModel> remoteimages=new ArrayList<>();

     FirebaseDatabase.getInstance().getReference().child("Banner")
             .addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                 {
                     for(DataSnapshot data:dataSnapshot.getChildren()) {
                         remoteimages.add(new SlideModel(data.child("image").getValue().toString(), data.child("name").getValue().toString(), ScaleTypes.FIT));

                     }
                     mainslider.setImageList(remoteimages,ScaleTypes.FIT);

                     mainslider.setItemClickListener(new com.denzcoskun.imageslider.interfaces.ItemClickListener() {
                         @Override
                         public void onItemSelected(int i) {

                             Toast.makeText(getApplicationContext(),remoteimages.get(i).getTitle().toString(),Toast.LENGTH_SHORT).show();
                         }
                     });

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
 }

//    private void setUpslider() {
//     mSlider=findViewById(R.id.slider);
//     image_list=new HashMap<>();
//
//        DatabaseReference banners=firebaseDatabase.getReference("Banner");
//        banners.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot postsnashot:snapshot.getChildren())
//                {
//                    Banner banner=postsnashot.getValue(Banner.class);
//                    image_list.put(banner.getName()+"@@@"+banner.getId(),banner.getImage());
//                }
//                for (String key:image_list.keySet())
//                {
//                    String[] keySplit=key.split("@@@");
//                    String nameOfFood=keySplit[0];
//                    String idOfFood=keySplit[1];
////
//                    final TextSliderView textSliderView=new TextSliderView(getBaseContext());
//                    textSliderView
//                            .description(nameOfFood)
//                            .image(image_list.get(key))
//                            .setScaleType(BaseSliderView.ScaleType.Fit)
//
//                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                @Override
//                                public void onSliderClick(BaseSliderView slider) {
//                                    Intent inte=new Intent(Home.this,MonDetail.class);
//                                    inte.putExtras(textSliderView.getBundle());
//                                    startActivity(inte);
//                                }
//                            });
//
//                    textSliderView.bundle(new Bundle());
//                    textSliderView.getBundle().putString("Monid",idOfFood);
//                    mSlider.addSlider(textSliderView);
//                    banners.removeEventListener(this);
//
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        mSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
//        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mSlider.setCustomAnimation(new DescriptionAnimation());
//        mSlider.setDuration(3000);
//
//
//    }

    private void loadmenu()
    {

        FirebaseRecyclerOptions<com.example.landd.Model.Menu> options = new FirebaseRecyclerOptions.Builder<com.example.landd.Model.Menu>()
                .setQuery(Menus, com.example.landd.Model.Menu.class).build();
        adapter=new FirebaseRecyclerAdapter<com.example.landd.Model.Menu, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int i, @NonNull com.example.landd.Model.Menu menu) {
                TextView textViewMenuName = menuViewHolder.itemView.findViewById(R.id.menu_name);
                ImageView imageViewMenuImage = menuViewHolder.itemView.findViewById(R.id.menu_image);

                textViewMenuName.setText(menu.getName());
                Picasso.get().load(menu.getImage()).into(imageViewMenuImage);
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //We need to send CategoryId to the next sub-activity
                        Intent intentFood = new Intent(Home.this, MonList.class);
                        intentFood.putExtra("MenuId", adapter.getRef(position).getKey());
                        startActivity(intentFood);
                    }
                });
                menuViewHolder.txtmenu.setText(menu.getName());
                Picasso.get().load(menu.getImage()).into(menuViewHolder.imageView);
                final com.example.landd.Model.Menu clickItem = menu;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Home.this,""+clickItem.getName(),Toast.LENGTH_LONG).show();
                        Intent intentFood = new Intent(Home.this, MonList.class);
                        intentFood.putExtra("MenuId", adapter.getRef(position).getKey());
                        startActivity(intentFood);
                    }
                });

            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(view);
            }

        };
        recyclerView_menu.setAdapter(adapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
       //mSlider.startAutoCycle();
    }
    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getOrderCount());



    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
       //mSlider.stopAutoCycle();
    }

    @Override
    public void onBackPressed(){

     DrawerLayout drawer=findViewById(R.id.drawer_layout);
     if(drawer.isDrawerOpen(GravityCompat.START)){
         drawer.closeDrawer(GravityCompat.START);
     }else{super.onBackPressed();}
        }
@Override
public boolean onCreateOptionsMenu(Menu menu){
     getMenuInflater().inflate(R.menu.home,menu);
     return true;
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
if (item.getItemId()==R.id.refresh)
    loadmenu();
        if (item.getItemId()==R.id.searchall)
        {
            Intent intent = new Intent(Home.this, Searchall.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);


    }
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_restaurant) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(Home.this, Cart.class);
            startActivity(intent);
        }
            else if (id==R.id.nav_localtion)
            {
                Intent intent = new Intent(Home.this, Tracking_Order.class);
                startActivity(intent);

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(Home.this, OrderStatus.class);
            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {
            //Paper.book(CLIENT).delete(USER_PHONE);
           // Paper.book(CLIENT).delete(USER_PASSWORD);
            //Paper.book(CLIENT).delete(USER_NAME);
Paper.book().destroy();
            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //clearing remember me data in PaperDb

        } else if (id== R.id.nav_ser)
        {
showchanpassworddialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showchanpassworddialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
        builder.setTitle("Change Password");
        builder.setMessage("Please fill all information");
        LayoutInflater inflater=LayoutInflater.from(this);
        View layout_pwd=inflater.inflate(R.layout.change_password,null);
        EditText editpass=layout_pwd.findViewById(R.id.editpassword);
        EditText editnewpass=layout_pwd.findViewById(R.id.editnewpassword);
        EditText editrepeatpass=layout_pwd.findViewById(R.id.Repeatpassword);
        builder.setView(layout_pwd);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.app.AlertDialog waitingdialog=new SpotsDialog(Home.this);
                waitingdialog.show();

                if (editpass.getText().toString().equals(Common.currentUser.getPassword()))
                {
                    if (editnewpass.getText().toString().equals(editrepeatpass.getText().toString()))
                    {
                        Map<String,Object> passwordUpdate=new HashMap<>();
                        passwordUpdate.put("password",editnewpass.getText().toString());
DatabaseReference account=FirebaseDatabase.getInstance().getReference("Account");
account.child(Common.currentUser.getPhoned()).updateChildren(passwordUpdate)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                waitingdialog.dismiss();
                Toast.makeText(Home.this,"Password was update", Toast.LENGTH_SHORT).show();
            }
        });
                    }
                    else {
                        Toast.makeText(Home.this,"Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Home.this,"Password wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
            }
        });
        builder.show();

    }
}
