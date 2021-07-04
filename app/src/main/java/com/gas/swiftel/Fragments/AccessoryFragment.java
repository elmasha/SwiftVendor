package com.gas.swiftel.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Adapter.AccessoriesAdapter;
import com.gas.swiftel.Model.Accessories;
import com.gas.swiftel.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccessoryFragment extends Fragment {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference accessoriesRef = db.collection("Accessories");
    CollectionReference CartListRef = db.collection("Cart_list");
    private AccessoriesAdapter adapter;
    private RecyclerView mRecyclerView;
    private AlertDialog dialog2;

    private EditText ItemName, ItemPrice,ItemDesc,AddTOCart;
    private Button submitItem;
    private String DOC_id;
    private String Qty;
    private Button sendRequest;
    private String itemName,itemDesc,Item_Image;
    private int itemPrice;
    private int Cart_Total;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private ImageView itemImage;
    private AlertDialog dialogEdit;
    private SwipeRefreshLayout swipeRefreshLayout;



    public AccessoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        FetchProduct();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_accessory, container, false);

        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = root.findViewById(R.id.My_accessories);
        swipeRefreshLayout = root.findViewById(R.id.SwipeAccess);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchProduct();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        return root;
    }
    private int deletePostion;
    private void FetchProduct() {

        String MUID = mAuth.getCurrentUser().getUid();

        Query query = accessoriesRef.whereEqualTo("User_ID",MUID)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Accessories> options = new FirestoreRecyclerOptions.Builder<Accessories>()
                .setQuery(query, Accessories.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new AccessoriesAdapter(options);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                 ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(mRecyclerView);


        adapter.setOnItemClickListener(new AccessoriesAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                deletePostion = position;



            }
        });



        adapter.setOnItemClickListener(new AccessoriesAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {


                Accessories product =documentSnapshot.toObject(Accessories.class);
                DOC_id = documentSnapshot.getId();
                itemName = product.getItem_name();
                itemPrice = product.getItem_Price();
                itemDesc = product.getItem_Desc();
                Item_Image = product.getItem_Image();
                DialogAccessories();


            }
        });




    }

    private FloatingActionButton delete_accessory;
    private void DialogAccessories() {

        final  AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        mbuilder.setView(mView);
        dialogEdit = mbuilder.create();
        dialogEdit.show();

        ItemName = mView.findViewById(R.id.item_name);
        ItemDesc = mView.findViewById(R.id.item_desc);
        ItemPrice = mView.findViewById(R.id.item_price);
        itemImage = mView.findViewById(R.id.item_image);
        submitItem = mView.findViewById(R.id.BtnAdd_Item);
        delete_accessory = mView.findViewById(R.id.delete_accessory);


        delete_accessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                String date = DateFormat.format("dd MMM ,yyyy | hh:mm a",new Date(String.valueOf(currentTime))).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete accessory");
                builder.setIcon(R.drawable.trash);
                builder.setMessage("Are you sure.? \n"+"Date "+date);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteItem(deletePostion);
                                Toast.makeText(getContext(), "Accessory deleted", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                dialogEdit.dismiss();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.setCancelable(false);
                builder.show();

            }
        });


        ItemName.setText(itemName);
        ItemDesc.setText(itemDesc);
        ItemPrice.setText(itemPrice+"");

        Picasso.with(getContext()).load(Item_Image).placeholder(R.drawable.load).error(R.drawable.errorimage).into(itemImage);



        submitItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ValidaTion()){

                }else {

                    Update_accessores();
                }
            }
        });

    }

    private void Update_accessores() {



        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait updating Accessory..");
        progressDialog.show();


       int itPrice = Integer.parseInt(ItemPrice.getText().toString());


        String User_ID = mAuth.getCurrentUser().getUid();


        HashMap<String, Object> store = new HashMap<>();
        store.put("Item_name",  ItemName.getText().toString());
        store.put("Item_Desc",  ItemDesc.getText().toString());
        store.put("Item_Price", itPrice);
        store.put("User_ID", User_ID);
        store.put("Item_Image",Item_Image);
        store.put("timestamp",FieldValue.serverTimestamp());

        accessoriesRef.document(DOC_id).update(store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getContext(), "Accessories Updated Successfully..", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    dialogEdit.dismiss();


                } else {

                    Toast.makeText(getContext(), "Storing Failed!! try again later..", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    dialogEdit.dismiss();

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


    private boolean ValidaTion() {

        itemName = ItemName.getText().toString();
        itemDesc = ItemDesc.getText().toString();
        itemPrice = Integer.parseInt(ItemPrice.getText().toString());

        if (itemName.isEmpty()) {
            ItemName.setError("Gas Name is empty");
            return false;
        }

        else if (itemDesc.isEmpty()) {
            ItemDesc.setError("Gas Description is Empty");
            return false;
        }

        else if (itemPrice == 0) {
            ItemPrice.setError("Purchase price is Empty");
            return false;
        }

        else {
            ItemName.setError(null);
            ItemDesc.setError(null);
            ItemPrice.setError(null);
            return true;
        }

    }


}
