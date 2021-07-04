package com.gas.swiftel.Fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gas.swiftel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddgasFragment extends Fragment {

    private long Gasprice,GasrefillPrice,ItemPrice;
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

    private TextView toAddGas,toAddPrd;
    private RelativeLayout r1,r2;

    private Button submitItem;
    private EditText itemName;
    private EditText itemDesc;
    private EditText itemAmount;
    //Strings

    private String ItemName,ItemDesc;


    public AddgasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_addgas, container, false);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        gasImage = root.findViewById(R.id.Gas_image);
        gasName = root.findViewById(R.id.gas_name);
        gasDesc = root.findViewById(R.id.gas_desc);
        gasPrice = root.findViewById(R.id.gas_price);
        gasRefilprice = root.findViewById(R.id.gas_refill);
        gasKgs = root.findViewById(R.id.gas_kgs);
        submitGas = root.findViewById(R.id.Add_Gas);

        return root;
    }

}
