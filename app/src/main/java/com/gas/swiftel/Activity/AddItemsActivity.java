package com.gas.swiftel.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gas.swiftel.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class AddItemsActivity extends AppCompatActivity {


    private Uri imageUri ;
    private Uri itemUri ;
    private String Gas_Image;

    //DialogGas
    private int Gasprice,GasrefillPrice ;
    private int ItemPriceInput;
    private EditText gasName,gasDesc,gasPrice,gasRefilprice;
    private Spinner gasKgs;
    private Button submitGas;
    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Bitmap compressedImageBitmap;
    private String GasName,GasDesc,GasKgs,GasimageUrl;

    private ImageView gasImage,itemImage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorShopRef = db.collection("VendorsShop");
    CollectionReference vendorShopAccesRef = db.collection("Accessories");
    private UploadTask uploadTask;

    private CardView toAddGas,toAddPrd;
    private RelativeLayout r1,r2,r3;

    private Button submitItem;
    private EditText itemName;
    private EditText itemDesc;
    private EditText itemAmount;

    private int WinsdowState = 0;
    //Strings

    private String ItemNameInput, ItemDescInput;
    private TextView backGas,backAccess;
    private TextView AddGasCamera,AddAccessCamera;
    private FloatingActionButton backToshop;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);


//        mAuth = FirebaseAuth.getInstance();
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();
//        gasImage = findViewById(R.id.Gas_image);
//        gasName = findViewById(R.id.gas_name);
//        gasDesc = findViewById(R.id.gas_desc);
//        gasPrice = findViewById(R.id.gas_price);
//        gasRefilprice = findViewById(R.id.gas_refill);
//        gasKgs = findViewById(R.id.gas_kgs);
//        submitGas = findViewById(R.id.Add_Gas);
//
//        itemName = findViewById(R.id.item_name);
//        itemDesc = findViewById(R.id.item_desc);
//        itemAmount = findViewById(R.id.item_price);
//        itemImage = findViewById(R.id.item_image);
//        submitItem = findViewById(R.id.BtnAdd_Item);
        toAddGas = findViewById(R.id.TAdd_gas);
        toAddPrd = findViewById(R.id.TAdd_accessories);
//        r1 = findViewById(R.id.AddGasLayout);
//        r2 = findViewById(R.id.AddProductLayout);
//        r3 = findViewById(R.id.Rq13);
//        backGas = findViewById(R.id.LableBack2);
//        backAccess = findViewById(R.id.LableBack1);
        backToshop  = findViewById(R.id.LableBack123);
//        AddAccessCamera = findViewById(R.id.item_txt1);
//        AddGasCamera = findViewById(R.id.item_txt2);
//        AddAccessCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
//                        .setMinCropResultSize(512, 512)
//                        .setAspectRatio(1, 1)
//                        .start(AddItemsActivity.this);
//            }
//        });
//        AddGasCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
//                        .setMinCropResultSize(512, 512)
//                        .setAspectRatio(1, 1)
//                        .start(AddItemsActivity.this);
//            }
//        });
        backToshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toshop = new Intent(getApplicationContext(),MapsVActivity.class);
                startActivity(toshop);
                finish();
                WinsdowState = 0;
            }
        });
//        backGas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                r3.setVisibility(View.VISIBLE);
//                r1.setVisibility(View.INVISIBLE);
//                r2.setVisibility(View.INVISIBLE);
//                WinsdowState = 0;
//            }
//        });
//        backAccess.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                r3.setVisibility(View.VISIBLE);
//                r1.setVisibility(View.INVISIBLE);
//                r2.setVisibility(View.INVISIBLE);
//                WinsdowState = 0;
//            }
//        });

        toAddGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddGasActivity.class));
            }
        });

        toAddPrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(getApplicationContext(),AddAccesoryActivity.class));
            }
        });


//        itemImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
//                        .setMinCropResultSize(512, 512)
//                        .setAspectRatio(1, 1)
//                        .start(AddItemsActivity.this);
//
//            }
//        });
//        gasImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
//                        .setMinCropResultSize(512, 512)
//                        .setAspectRatio(1, 1)
//                        .start(AddItemsActivity.this);
//
//            }
//        });
//        submitGas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                ProgressDialog progressDialog = new ProgressDialog(getContext());
////                progressDialog.setMessage("Please wait store ing in process..");
////                progressDialog.setCancelable(false);
//
//                if (!ValidaTion()) {
//
//                }else if (imageUri==null){
//
//                    Toast.makeText(AddItemsActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
//
//                }else {
//                    UploadGas();
//                }
//
//
//            }
//        });
//        submitItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (!ValidaTion2()) {
//
//
//                }else if (itemUri==null){
//
//                    Toast.makeText(AddItemsActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
//
//                }else {
//                    UploadAccesories();
//                }
//
//
//            }
//        });
//
//





    }





    private void UploadAccesories() {



        if (itemUri==null){

            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();

        }else {

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait uploading Accessories..");
            progressDialog.show();


            ItemNameInput = itemName.getText().toString();

            ItemDescInput = itemDesc.getText().toString();
            ItemPriceInput = Integer.parseInt((itemAmount.getText().toString()));
            String User_ID = mAuth.getCurrentUser().getUid();


            File newimage = new File(itemUri.getPath());

            try {
                Compressor compressor = new Compressor(this);
                compressor.setMaxHeight(200);
                compressor.setMaxWidth(200);
                compressor.setQuality(10);
                compressedImageBitmap = compressor.compressToBitmap(newimage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();


            final StorageReference ref = storageReference.child("images/thumbs" + UUID.randomUUID().toString());
            uploadTask = ref.putBytes(data);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        String url34 = downloadUri.toString();

                        HashMap<String, Object> store = new HashMap<>();
                        store.put("Item_name", ItemNameInput);
                        store.put("Item_Desc", ItemDescInput);
                        store.put("Item_Price", ItemPriceInput);
                        store.put("User_ID", User_ID);
                        store.put("Item_Image", url34);
                        store.put("timestamp", FieldValue.serverTimestamp());


                        vendorShopAccesRef.document().set(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(), "Accessories Stored Successfully..", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                    itemName.setText("");
                                    itemAmount.setText("");
                                    itemDesc.setText("");
                                    itemImage.setImageURI(null);
                                    itemImage.setBackgroundResource(R.drawable.additem);
                                    gasImage.setImageURI(null);
                                    gasImage.setBackgroundResource(R.drawable.additem);


                                    gasDesc.setText("");
                                    gasName.setText("");
                                    gasRefilprice.setText("");


                                } else {

                                    Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(), "failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });


                    } else {

                        String e = task.getException().getMessage();
                        Toast.makeText(AddItemsActivity.this, e, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }


    }


    private void UploadGas() {


        if (imageUri == null){

            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();


        }else {

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait uploading Gas..");
            progressDialog.show();


            GasName = gasName.getText().toString();
            GasDesc = gasDesc.getText().toString();
            Gasprice = Integer.parseInt((gasPrice.getText().toString()));
            GasrefillPrice = Integer.parseInt((gasRefilprice.getText().toString()));
            GasKgs = gasKgs.getSelectedItem().toString();


            String User_ID = mAuth.getCurrentUser().getUid();

//        Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            File newimage = new File(imageUri.getPath());

            try {
                Compressor compressor = new Compressor(this);
                compressor.setMaxHeight(200);
                compressor.setMaxWidth(200);
                compressor.setQuality(10);
                compressedImageBitmap = compressor.compressToBitmap(newimage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] data = baos.toByteArray();


           final StorageReference ref = storageReference.child("images/thumbs" + UUID.randomUUID().toString());
            uploadTask = ref.putBytes(data);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        String url34 = downloadUri.toString();

                       HashMap<String, Object> store = new HashMap<>();
                        store.put("Gas_Name", GasName);
                        store.put("Gas_Desc", GasDesc);
                        store.put("Gas_Price", Gasprice);
                        store.put("Gas_RefillPrice", GasrefillPrice);
                        store.put("Gas_Kgs", GasKgs);
                        store.put("User_ID", User_ID);
                        store.put("Gas_Image", url34);
                        store.put("timestamp", FieldValue.serverTimestamp());

                        vendorShopRef.document().set(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(), "Stored Successfully..", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                    gasDesc.setText("");
                                    gasName.setText("");
                                    gasPrice.setText("");
                                    gasRefilprice.setText("");
                                    itemImage.setImageURI(null);
                                    itemImage.setBackgroundResource(R.drawable.additem);
                                    gasImage.setImageURI(null);
                                    gasImage.setBackgroundResource(R.drawable.additem);





                                } else {

                                    Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }
                        });


                    } else {
                        // Handle failures
                        // ...
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
//            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
//                            .getTotalByteCount());
//                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
//                }
//            });


        }


    }




    private void StoreData(Task<UploadTask.TaskSnapshot> task) {

        Uri downlloadUri;

        if (task != null){
            downlloadUri= task.getResult().getUploadSessionUri();

        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    //data.getData returns the content URI for the selected Image

                    imageUri = result.getUri();
                    gasImage.setImageURI(imageUri);


                    itemUri = result.getUri();
                    itemImage.setImageURI(itemUri);


                    break;
            }


    }


    private boolean ValidaTion() {

        GasName = gasName.getText().toString();
        GasDesc = gasDesc.getText().toString();
        Gasprice = Integer.parseInt(gasPrice.getText().toString());
        GasrefillPrice = Integer.parseInt(gasRefilprice.getText().toString());

        if (GasName.isEmpty()) {
            gasName.setError("Gas Name is empty");
            return false;
        }

        else if (GasDesc.isEmpty()) {
            gasDesc.setError("Gas Description is Empty");
            return false;
        }

        else if (Gasprice == 0.00) {
            gasPrice.setError("Purchase price is Empty");
            return false;
        }
        else if (GasrefillPrice == 0) {
            gasRefilprice.setError("Refill price is Empty");
            return false;
        }
        else {
            gasName.setError(null);
            gasDesc.setError(null);
            gasPrice.setError(null);
            gasRefilprice.setError(null);
            return true;
        }

    }


    private boolean ValidaTion2() {

        ItemNameInput = itemName.getText().toString();
        ItemDescInput = itemDesc.getText().toString();
        ItemPriceInput = Integer.parseInt((itemAmount.getText().toString()));


        if (ItemNameInput.isEmpty()) {
            itemName.setError("Item Name is empty");
            return false;
        }

        else if (ItemDescInput.isEmpty()) {
            itemDesc.setError("Item Description is Empty");
            return false;
        }

        else if (ItemPriceInput == 0) {
            itemAmount.setError("Purchase price is Empty");
            return false;
        }

        else {
            itemAmount.setError(null);
            itemName.setError(null);
            itemDesc.setError(null);
            return true;
        }

    }



    @Override
    public void onBackPressed() {
        if (WinsdowState == 0) {
            super.onBackPressed();

            Intent logout = new Intent(getApplicationContext(),Home_Activity.class);
            logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logout);
            finish();


            return;
        }else if (WinsdowState == 1){
            WinsdowState = 0;
            r3.setVisibility(View.VISIBLE);
            r1.setVisibility(View.INVISIBLE);
            r2.setVisibility(View.INVISIBLE);
        }



    }
}
