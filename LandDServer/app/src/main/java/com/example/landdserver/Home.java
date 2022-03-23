package com.example.landdserver;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.example.landd.Model.Menu;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Interface.ItemClickListener;
import com.example.landdserver.Services.OrderListenService;
import com.example.landdserver.ViewHolders.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference Menus;
    TextView textViewName;
    RecyclerView recyclerView_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<com.example.landdserver.Model.Menu, MenuViewHolder> adapter;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
     MaterialEditText etName;
     Button btnUpload;
     Button btnSelect;

    com.example.landdserver.Model.Menu mons;
    Uri saveUri;
     DrawerLayout drawer;
     boolean isSinglePressed;
     static final int IMAGE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Menu management");
        setSupportActionBar(toolbar);

//firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        Menus = firebaseDatabase.getReference("Menu");
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
     //Snackbar.make(view,"Replace",Snackbar.LENGTH_LONG).setAction("ACTION",null).show();
                addMenuDialog();

            }
        });
drawer=findViewById(R.id.drawer_layout);
ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
drawer.setDrawerListener(toggle);
toggle.syncState();
;
NavigationView navigationView=findViewById(R.id.nav_view);
navigationView.setNavigationItemSelectedListener(this);
View headerv=navigationView.getHeaderView(0);
textViewName=headerv.findViewById(R.id.textviewName);
textViewName.setText(Common.currentUser.getName());
        //fab.setCount(new Database(this).getOrderCount());
        recyclerView_menu = findViewById(R.id.recycler_menu);
        recyclerView_menu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView_menu.setHasFixedSize(true);
        loadMenu();
        Intent intentServer = new Intent(Home.this, OrderListenService.class);
        startService(intentServer);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE)
        {
            if(data != null && data.getData() != null)
            {
                saveUri = data.getData();
                btnSelect.setText("Image selected");
                //Toast.makeText(this, "Image selected", LENGTH_SHORT).show();
            }
        }

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
//        int id=item.getItemId()
//;
//        if (id==R.id.action_settings){
//            return true
//;        }// Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_restaurant) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(Home.this, Comment.class);
            startActivity(intent);


        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(Home.this, OrderStatus.class);
            startActivity(intent);
        }else if(id==R.id.nav_banner)
        {
            Intent intents = new Intent(Home.this, BannerActivity.class);
            startActivity(intents);
        } else if (id == R.id.nav_sign_out) {
            //Paper.book(CLIENT).delete(USER_PHONE);
            // Paper.book(CLIENT).delete(USER_PASSWORD);
            //Paper.book(CLIENT).delete(USER_NAME);

            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //clearing remember me data in PaperDb

        }else if(id==R.id.nav_camera)
        {
            Intent intents = new Intent(Home.this, activityCamera.class);
            startActivity(intents);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void addMenuDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Add new Menu");
        alertDialog.setMessage("Please provide information about category");

        LayoutInflater inflater=this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_menu_new_layout, null);


        etName = view.findViewById(R.id.food_name);
        btnUpload = view.findViewById(R.id.btnUpload);

//
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        btnSelect = view.findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
//////dialog.dismiss();
//////if(mons!=null)
//////{
//////Menus.push().setValue(mons);
//////Snackbar.make(drawer,"New menu"+mons.getName()+ "was added",Snackbar.LENGTH_SHORT).show();
//////}
////
////            }
////        });
        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                saveUri = null;
            }
        });
//        btnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // selectImage();
//            }
//        });
//
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // uploadImage();
//            }
//        });

        alertDialog.show();
    }
    private void loadMenu() {
        ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        FirebaseRecyclerOptions< com.example.landdserver.Model.Menu> options = new FirebaseRecyclerOptions.Builder< com.example.landdserver.Model.Menu>().setQuery(Menus, com.example.landdserver.Model.Menu.class).build();
        adapter = new FirebaseRecyclerAdapter< com.example.landdserver.Model.Menu, MenuViewHolder>(options) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull final com.example.landdserver.Model.Menu model) {
                TextView textViewMenuName = holder.itemView.findViewById(R.id.menu_name);
                ImageView imageViewMenuImage = holder.itemView.findViewById(R.id.menu_image);

                textViewMenuName.setText(model.getName());

                Picasso.get().load(model.getImage()).into(imageViewMenuImage);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //We need to send CategoryId to the next sub-activity
                        Intent intentFood = new Intent(Home.this, MonList.class);
                        intentFood.putExtra("MenuId", adapter.getRef(position).getKey());
                        startActivity(intentFood);
                    }
                });
            }
        };
//adapter.notifyDataSetChanged();
        recyclerView_menu.setAdapter(adapter);
 }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),IMAGE_REQUEST_CODE);
    }

    private void uploadImage() {
        if(saveUri != null && !etName.getText().toString().equals(""))
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final String imageName = UUID.randomUUID().toString();
            final StorageReference imageStorage = storageReference.child("images/" + imageName);
            imageStorage.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            mons=new com.example.landdserver.Model.Menu(etName.getText().toString(), uri.toString());
                            //Menus.push().setValue(mons);
                            Menus.push().setValue(mons);
Snackbar.make(drawer,"New menu"+mons.getName()+ "was added",Snackbar.LENGTH_SHORT).show();
                            saveUri = null;

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Home.this,""+e.getMessage(), LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int) (100.0 * (taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading : " + progress);
                }
            });
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
          updateCategory(adapter.getRef(item.getOrder()).getKey(), (com.example.landdserver.Model.Menu) adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
           deleteCategory(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }
    private void updateCategory(final String key, final com.example.landdserver.Model.Menu item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please update information about category");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        View view = getLayoutInflater().inflate(R.layout.custom_add_mon_dialog, null);
        alertDialog.setView(view);

        etName = view.findViewById(R.id.food_name);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnSelect = view.findViewById(R.id.btnSelect);

        etName.setText(item.getName());

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImage(key, item);
            }
        });


        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveUri = null;
            }
        });

        alertDialog.show();
    }
    private void updateImage(final String key, final com.example.landdserver.Model.Menu item) {
        if(saveUri != null && !etName.getText().toString().equals(""))
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final String imageName = UUID.randomUUID().toString();
            final StorageReference imageStorage = storageReference.child("images/" + imageName);
            imageStorage.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            item.setName(etName.getText().toString());
                            item.setImage(uri.toString());
                            Menus.child(key).setValue(item);
                            Snackbar.make(drawer, "Category updated successfully", Snackbar.LENGTH_SHORT).show();
                            saveUri = null;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(drawer, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int) (100.0 * (taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading : " + progress);
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please provide name and image both", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteCategory(final String key) {
        Menus.child(key).removeValue();
        DatabaseReference foods =  firebaseDatabase.getReference("Menu");
        foods.orderByChild("menuId").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot food : dataSnapshot.getChildren())
                {
                    food.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//
//
//
}