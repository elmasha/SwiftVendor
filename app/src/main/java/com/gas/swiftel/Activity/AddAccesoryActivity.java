package com.gas.swiftel.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

public class AddAccesoryActivity extends AppCompatActivity {

    private Uri imageUri ;
    private Uri itemUri ;
    private String Gas_Image;

    //DialogGas
    private int Gasprice,GasrefillPrice ;
    private int ItemPriceInput;
    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Bitmap compressedImageBitmap;
    private String GasName,GasDesc,GasKgs,GasimageUrl;
    private ImageView itemImage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorShopAccesRef = db.collection("Accessories");
    private UploadTask uploadTask;


    private Button submitItem;
    private EditText itemName;
    private EditText itemDesc;
    private EditText itemAmount;

    private int WinsdowState = 0;
    //Strings

    private String ItemNameInput, ItemDescInput;
    private TextView backGas,backAccess;
    private TextView AddGasCamera;
    private FloatingActionButton AddAccessCamera,backToshop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accesory);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        AddAccessCamera = findViewById(R.id.item_txt1);

        itemName = findViewById(R.id.item_name);
        itemDesc = findViewById(R.id.item_desc);
        itemAmount = findViewById(R.id.item_price);
        itemImage = findViewById(R.id.item_image);
        submitItem = findViewById(R.id.BtnAdd_Item);


        AddAccessCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(AddAccesoryActivity.this);
            }
        });



        submitItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ValidaTion2()) {


                }else if (itemUri==null){

                    Toast.makeText(AddAccesoryActivity.this, "No image selected", Toast.LENGTH_SHORT).show();

                }else {
                    UploadAccesories();
                }


            }
        });

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
                        Toast.makeText(AddAccesoryActivity.this, e, Toast.LENGTH_SHORT).show();
                    }
                }
            });


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




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    //data.getData returns the content URI for the selected Image


                    itemUri = result.getUri();
                    itemImage.setImageURI(itemUri);


                    break;
            }


    }
}