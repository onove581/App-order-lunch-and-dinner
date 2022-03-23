package com.example.landdserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Model.Banner;
import com.example.landdserver.Model.Mon;
import com.example.landdserver.ViewHolders.BannerViewHolder;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.UUID;

import info.hoang8f.widget.FButton;

import static android.widget.Toast.LENGTH_SHORT;

public class BannerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fabss;
RelativeLayout rootlayout;
    FirebaseDatabase db;
    DatabaseReference banlist;
    FirebaseStorage storage;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST_CODE = 100;
    FirebaseRecyclerAdapter<Banner, BannerViewHolder> adapter;
    MaterialEditText editName,editFoodId;
    Button btnUpload,btnSelect;
    Banner newBanner;
    Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        db=FirebaseDatabase.getInstance();
        banlist=db.getReference("Banner");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        recyclerView=findViewById(R.id.recycler_banner);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
rootlayout=findViewById(R.id.root);
        fabss=findViewById(R.id.fabss);
        fabss
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       showaddBanner();

                    }
                });
        loadlistbanner();
    }
//
    private void loadlistbanner() {
        FirebaseRecyclerOptions<Banner> allbaner=new FirebaseRecyclerOptions.Builder<Banner>()
                .setQuery(banlist,Banner.class).build();
        adapter=new FirebaseRecyclerAdapter<Banner, BannerViewHolder>(allbaner) {
            @Override
            protected void onBindViewHolder(@NonNull BannerViewHolder bannerViewHolder, int i, @NonNull Banner banner) {
                bannerViewHolder.banner_name.setText(banner.getName());

                Picasso.get().load(banner.getImage()).into( bannerViewHolder.banner_image);
            }

            @NonNull
            @Override
            public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bannerlayout, parent, false);
                return new BannerViewHolder(view);

            }
        };
//        adapter.startListening();
        recyclerView.setAdapter(adapter);
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
    private void showaddBanner() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Add new Banner");
        alertDialog.setMessage("Please full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_new_layout=inflater.inflate(R.layout.add_new_banner,null);
        editFoodId=add_new_layout.findViewById(R.id.editFoodId);
        editName=add_new_layout.findViewById(R.id.editFoodName);
        btnSelect=add_new_layout.findViewById(R.id.btnSelectbanner);
        btnUpload=add_new_layout.findViewById(R.id.btnUploadbanner);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicture();
            }
        });
        alertDialog.setView(add_new_layout);
        alertDialog.setIcon(R.drawable.ic_banner);
        alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
if (newBanner!=null)
{
    banlist.push().setValue(newBanner);
}
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
newBanner=null;
            }
        });
        alertDialog.show();

    }

    private void uploadPicture() {
        if(filePath != null )
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final String imageName = UUID.randomUUID().toString();
            final StorageReference imageStorage = storageReference.child("images/" + imageName);
            imageStorage.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            newBanner = new Banner();
newBanner.setName(editName.getText().toString());
                            newBanner.setId(editFoodId.getText().toString());
                            newBanner.setImage(uri.toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(BannerActivity.this, "Food added successfully", Toast.LENGTH_SHORT).show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"), IMAGE_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE)
        {
            if(data != null && data.getData() != null)
            {
                filePath = data.getData();
                btnSelect.setText("Image Selected");
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            updateBannerFood(adapter.getRef(item.getOrder()).getKey(),  adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteBannerFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void updateBannerFood(String key, Banner item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Update Banner");
        alertDialog.setMessage("Please update information about food");
        alertDialog.setIcon(R.drawable.ic_banner);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_new_banner, null);
        alertDialog.setView(view);

        editName = view.findViewById(R.id.editFoodName);
        editFoodId = view.findViewById(R.id.editFoodId);
        btnUpload = view.findViewById(R.id.btnUploadbanner);
        btnSelect = view.findViewById(R.id.btnSelectbanner);

        editName.setText(item.getName());
        editFoodId.setText(item.getId());


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
                filePath = null;
            }
        });

        alertDialog.show();
    }

    private void updateImage(String key, Banner item) {
        if(filePath != null )
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final String imageName = UUID.randomUUID().toString();
            final StorageReference imageStorage = storageReference.child("images/" + imageName);
            imageStorage.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            item.setName(editName.getText().toString());
                            item.setImage(uri.toString());
                            banlist.child(key).setValue(item);
                            Toast.makeText(BannerActivity.this, "Banner updated successfully", LENGTH_SHORT).show();
                            filePath = null;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    //Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
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

    private void deleteBannerFood(String key) {
        banlist.child(key).removeValue();
    }

}