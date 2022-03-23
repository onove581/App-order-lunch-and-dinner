package com.example.landdserver;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Interface.ItemClickListener;
import com.example.landdserver.Model.Mon;
import com.example.landdserver.ViewHolders.FoodViewHolderServer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;

public class MonList extends AppCompatActivity {
   RecyclerView recyclerView;
   RecyclerView.LayoutManager layoutManager;
   FloatingActionButton fab;

   FirebaseDatabase db;
   DatabaseReference monlist;
   FirebaseStorage storage;
   StorageReference storageReference;
   String menuId="";
   FirebaseRecyclerAdapter<Mon, FoodViewHolderServer>adapter;
    MaterialSearchBar materialSearchBar;
    private Uri saveUri;
    private MaterialEditText etName;
    private MaterialEditText etDescription;
    private MaterialEditText etPrice;
    private MaterialEditText etDiscount;
    private Button btnUpload;
    private Button btnSelect;
    private static final int IMAGE_REQUEST_CODE = 100;
    CoordinatorLayout coordinatorLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_list);

        db=FirebaseDatabase.getInstance();
        monlist=db.getReference("Mon");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        coordinatorLayout = findViewById(R.id.parent);
        swipeRefreshLayout = findViewById(R.id.refreshFood);
        recyclerView=findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab=findViewById(R.id.fab);
        fab
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showaddDialog();

                    }
                });
        if (getIntent()!=null)
            menuId=getIntent().getStringExtra("MenuId");
        if (!menuId.isEmpty())
            loadlistmom(menuId);

    }

    private void showaddDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Add new Mon");
        alertDialog.setMessage("Please provide information about food");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_mon, null);
        alertDialog.setView(view);

        etName = view.findViewById(R.id.food_name);
        etDescription = view.findViewById(R.id.food_description);
        etPrice = view.findViewById(R.id.food_price);
        etDiscount = view.findViewById(R.id.food_discount);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnSelect = view.findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
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

    private void uploadImage() {
        if(saveUri != null && !etName.getText().toString().equals("") && !etDescription.getText().toString().equals("") &&
                !etPrice.getText().toString().equals("") && !etDiscount.getText().toString().equals("") && menuId != null)
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
                            Mon newFood = new Mon(etName.getText().toString(),
                                    etDescription.getText().toString(),
                                    etPrice.getText().toString(),
                                    etDiscount.getText().toString(),
                                    menuId,
                                    uri.toString());
                            monlist.push().setValue(newFood);
                            Snackbar.make(swipeRefreshLayout, "Food added successfully", Snackbar.LENGTH_SHORT).show();
                            saveUri = null;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Please provide all details", Toast.LENGTH_SHORT).show();
        }
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
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
//        if(searchAdapter != null)
//            searchAdapter.stopListening();
    }

    private void loadlistmom(String menuId) {
        FirebaseRecyclerOptions<Mon> options = new FirebaseRecyclerOptions.Builder<Mon>().
                setQuery(monlist.orderByChild("menuId").equalTo(menuId), Mon.class).build();

        adapter = new FirebaseRecyclerAdapter<Mon, FoodViewHolderServer>(options) {
            @NonNull
            @Override
            public FoodViewHolderServer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_item, parent, false);
                return new FoodViewHolderServer(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolderServer holder, int position, @NonNull final Mon model) {
                TextView textViewName = holder.itemView.findViewById(R.id.food_name);
                ImageView imageView = holder.itemView.findViewById(R.id.food_image);

                textViewName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(imageView);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //Sending food_id to FoodDetailActivity
//                        Intent intent = new Intent(FoodActivityServer.this, FoodDetailActivity.class);
//                        intent.putExtra("foodId", adapter.getRef(position).getKey());
//                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE)
        {
            if(data != null && data.getData() != null)
            {
                saveUri = data.getData();
                Toast.makeText(this, "Image selected", LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            updateFood(adapter.getRef(item.getOrder()).getKey(), (Mon) adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    private void updateFood(final String key, final Mon item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Update Food");
        alertDialog.setMessage("Please update information about food");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_mon, null);
        alertDialog.setView(view);

        etName = view.findViewById(R.id.food_name);
        etDescription = view.findViewById(R.id.food_description);
        etPrice = view.findViewById(R.id.food_price);
        etDiscount = view.findViewById(R.id.food_discount);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnSelect = view.findViewById(R.id.btnSelect);

        etName.setText(item.getName());
        etDiscount.setText(item.getDiscount());
        etPrice.setText(item.getPrice());
        etDescription.setText(item.getDescription());

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


    private void updateImage(final String key, final Mon item) {
        if(saveUri != null && !etName.getText().toString().equals("") && !etDescription.getText().toString().equals("") &&
                !etPrice.getText().toString().equals("") && !etDiscount.getText().toString().equals("") && menuId != null)
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
                            item.setDescription(etDescription.getText().toString());
                            item.setPrice(etPrice.getText().toString());
                            item.setDiscount(etDiscount.getText().toString());
                            item.setImage(uri.toString());
                            monlist.child(key).setValue(item);
                            Toast.makeText(MonList.this, "Food updated successfully", LENGTH_SHORT).show();
                            saveUri = null;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Please provide all details", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteFood(String key) {
        monlist.child(key).removeValue();
    }
}
