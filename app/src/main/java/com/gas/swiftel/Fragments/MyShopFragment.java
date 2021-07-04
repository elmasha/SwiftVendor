package com.gas.swiftel.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.gas.swiftel.Activity.AddItemsActivity;
import com.gas.swiftel.Adapter.PagerAdapter;
import com.gas.swiftel.Adapter.ProductAdapter;
import com.gas.swiftel.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Model.Gas_Vendor;
import com.gas.swiftel.Model.Product;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class MyShopFragment extends Fragment {



    public static final int GALLERY_REQUEST_CODE = 1;
    FloatingActionButton fab2,fab1,fab3;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;


    TabLayout tabLayout;
    TabItem GasTab;
    TabItem AccesTab;
    ViewPager viewPager;

    PagerAdapter pagerAdapter;

    private Uri imageUri;
    private String Gas_Image;

    //DialogGas
    private EditText gasName,gasDesc,gasPrice,gasRefilprice;
    private Spinner gasKgs;
    private Button submitGas;
    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Bitmap compressedImageBitmap;

    //DialogItem

    private Button submitItem;
    private EditText itemName;
    private EditText itemDesc;
    private EditText itemAmount;
    //Strings

    private long Gasprice,GasrefillPrice,ItemPrice;
    private String ItemName,ItemDesc;

    private ImageView gasImage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorShopRef = db.collection("VendorsShop");
    CollectionReference vendorShopAccesRef = db.collection("Accessories");
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    private ProductAdapter adapter;
    private TextView ShopName,Shopno;
    private String activationFee;
    private AlertDialog dialog_success;

    Context context;

    public MyShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_shop, container, false);


        mRecyclerView = root.findViewById(R.id.RecyclerViewshop);
        ShopName = root.findViewById(R.id.ShopName);
        Shopno = root.findViewById(R.id.ShopNo);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        tabLayout = root.findViewById(R.id.Tablayout);
        GasTab = root.findViewById(R.id.Gas_tab);
        AccesTab = root.findViewById(R.id.Accesories_tab);
        viewPager = root.findViewById(R.id.ViewPager);




        pagerAdapter = new PagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 1){

                }else if (tab.getPosition() == 2){

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));







        fab1 = root.findViewById(R.id.Add_Item);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activationFee.equals("0")){


                    successAlert("To Upload your products please Visit Profile to activate your account...");

                }else if (activationFee.equals("00")){

                    successAlert("You have not remitted charges for 3 cash transactions. Remit Ksh/."+ UremittedCash +" to be able to proceed");

                }else if (activationFee.equals("200")){

                    startActivity(new Intent(getContext(), AddItemsActivity.class));
                }

            }
        });
        LoadData();
        return root;
    }

    private TextView Dia_Header,Dia_time;
    private long UremittedCash;
    public void successAlert(String responseDescription) {

        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a",new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_activate, null);
        mbuilder.setView(mView);
        dialog_success = mbuilder.create();
        dialog_success.show();

        TextView showData = mView.findViewById(R.id.LablePay2);
        Dia_Header = mView.findViewById(R.id.LablePay);
        Dia_time = mView.findViewById(R.id.LablePayTime);
        final TextView showtime = mView.findViewById(R.id.LablePayTime);
        final Button confirm = mView.findViewById(R.id.Confirm_pay);
        final Button cancel_pay = mView.findViewById(R.id.Cancel_pay);





        showData.setText(responseDescription );
        Dia_time.setText("Date: "+date);



            confirm.setText("Okay");
            confirm.setVisibility(View.INVISIBLE);


            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    confirm.setVisibility(View.GONE);
                    cancel_pay.setVisibility(View.GONE);
                    dialog_success.dismiss();




                }
            });

            cancel_pay.setText("Okay");
            cancel_pay.setVisibility(View.VISIBLE);
            cancel_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_success.dismiss();
                }
            });








    }


    private void LoadData(){

        String userid = mAuth.getCurrentUser().getUid();

        if (userid != null) {
            vendorRef.document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }

                    if (documentSnapshot.exists()){
                        Gas_Vendor vendorUser = documentSnapshot.toObject(Gas_Vendor.class);
                        String shop_no = vendorUser.getShop_No();
                        String Shopname = vendorUser.getShopName();
                        ShopName.setText(Shopname);
                        Shopno.setText("Shop_No_#" + shop_no);
                        activationFee = vendorUser.getActivation_fee();
                        UremittedCash=  vendorUser.getEarnings();
                    }
                }
            });


        }
    }






    public File saveBitmapToFile(File file){

        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=200;        // x............

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            File outPutFile = File.createTempFile("abc","image");
            FileOutputStream outputStream = new FileOutputStream(outPutFile);
            // y.......
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 95 , outputStream);

            return outPutFile;
        } catch (Exception e) {
            return null;
        }
    }




    private void AddProduct(){

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        mbuilder.setView(mView);
        AlertDialog dialog2 = mbuilder.create();
        dialog2.show();

        itemName = mView.findViewById(R.id.item_name);
        itemDesc = mView.findViewById(R.id.item_desc);
        itemAmount = mView.findViewById(R.id.item_price);

        submitItem = mView.findViewById(R.id.BtnAdd_Item);

        submitItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UploadAccesories();

            }
        });

    }

    private void UploadAccesories() {



        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait uploading details..");
        progressDialog.show();


        ItemName = itemName.getText().toString();
        ItemDesc = itemDesc.getText().toString();
        ItemPrice = Long.parseLong((itemAmount.getText().toString()));


        String User_ID = mAuth.getCurrentUser().getUid();


//        Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();

        HashMap<String, Object> store = new HashMap<>();
        store.put("Item_name", ItemName);
        store.put("Item_Desc", ItemDesc);
        store.put("Item_Price", ItemPrice);
        store.put("User_ID", User_ID);
        store.put("Item_Image","");
        store.put("timestamp", FieldValue.serverTimestamp());


        vendorShopAccesRef.document().set(store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getContext(), "Accessories Stored Successfully..", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                } else {

                    Toast.makeText(getContext(), "Storing Failed!! try again later..", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }


    private void FetchProduct() {

        String User_ID = mAuth.getCurrentUser().getUid();

        Query userQuery1 = vendorShopRef.whereEqualTo("User_ID",User_ID)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(userQuery1,Product.class)
                .build();
        adapter = new ProductAdapter(options);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setAdapter(adapter);

    }




    @Override
    public void onStart() {
        super.onStart();




    }

    @Override
    public void onStop() {
        super.onStop();

    }


}
