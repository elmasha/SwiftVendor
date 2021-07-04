package com.gas.swiftel.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Adapter.ProductAdapter;
import com.gas.swiftel.Model.Product;
import com.gas.swiftel.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
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
public class GasFragment extends Fragment {



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorRef = db.collection("VendorsShop");
    CollectionReference CartListRef = db.collection("Cart_list");
    private ProductAdapter adapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText go,gasName,gasPrice,gasRefillprice,gasDesc;
    private TextView kgview;

    private Spinner gas_Kg;
    private ProgressBar progressBar;
    private String category = "";
    private int amount ;
    private long amountTotal ;
    private int Cart_Total;
    private RadioGroup radioGroup;
    private String Id,gas_name,date,gasKgs,gasImage,gas_Desc;
    private int gas_price,gas_refillprice;
    private AlertDialog dialogEdit;
    private Button sendRequest;
    private String DOC_id;
    private String Qty,KG;
    private ImageView itemIMAge,Gas_image;

    private int Deposit;
    private FirebaseAuth mAuth;
    double totalAmount;
    private long cart_Amount;

    public GasFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        FetchProduct();
        adapter.startListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_gas, container, false);

        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = root.findViewById(R.id.RecyclerViewshop);
        swipeRefreshLayout = root.findViewById(R.id.SwipeGAs);
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


    private int deletePosition;
    private void FetchProduct() {

        String MUID = mAuth.getCurrentUser().getUid();

        Query query = vendorRef.whereEqualTo("User_ID",MUID)
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(30);
        FirestoreRecyclerOptions<Product> transaction = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new ProductAdapter(transaction);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {





            }
        }).attachToRecyclerView(mRecyclerView);





        adapter.setOnItemClickListener(new ProductAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                deletePosition = position;

                Product product =documentSnapshot.toObject(Product.class);
                DOC_id = documentSnapshot.getId();
                gas_name = product.getGas_Name();
                gas_price =  product.getGas_Price();
                gas_refillprice =  product.getGas_RefillPrice();
                gasKgs = product.getGas_Kgs();
                gasImage = product.getGas_Image();
                gas_Desc = product.getGas_Desc();
                Edit_Gas();

            }
        });





    }




    private FloatingActionButton deleteGas;
    private void Edit_Gas(){

        final  AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_gas, null);
        mbuilder.setView(mView);
        dialogEdit = mbuilder.create();
        dialogEdit.show();
        Gas_image = mView.findViewById(R.id.Gas_image);
        gasName = mView.findViewById(R.id.gas_name);
        gasPrice = mView.findViewById(R.id.gas_price);
        gasRefillprice = mView.findViewById(R.id.gas_refill);
        gas_Kg = mView.findViewById(R.id.gas_kgs);
        gasDesc = mView.findViewById(R.id.gas_desc);
        sendRequest = mView.findViewById(R.id.Add_Gas);
        kgview = mView.findViewById(R.id.GasView);
        deleteGas = mView.findViewById(R.id.delete_gas);

        deleteGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                String date = DateFormat.format("dd MMM ,yyyy | hh:mm a",new Date(String.valueOf(currentTime))).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Gas");
                builder.setIcon(R.drawable.trash);
                builder.setMessage("Are you sure.? \n"+"Date "+date);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteItem(deletePosition);
                                Toast.makeText(getContext(), "Gas deleted", Toast.LENGTH_SHORT).show();
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

//        closeDia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogEdit.dismiss();
//            }
//        });


        gasName.setText(gas_name);
        gasPrice.setText(gas_price+"");
        gasRefillprice.setText(gas_refillprice+"");
        gasDesc.setText(gas_Desc);
        kgview.setText(gasKgs);

        Picasso.with(getContext()).load(gasImage).placeholder(R.drawable.load).error(R.drawable.errorimage).into(Gas_image);




        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!ValidaTion()) {

                } else {

                    Update_Details();
                }




            }
        });




    }


    private boolean ValidaTion() {

        gas_name = gasName.getText().toString();
        gas_Desc = gasDesc.getText().toString();
        gas_price = Integer.parseInt(gasPrice.getText().toString());
        gas_refillprice = Integer.parseInt(gasRefillprice.getText().toString());
        KG = gas_Kg.getSelectedItem().toString();


        if (gas_name.isEmpty()) {
            gasName.setError("Gas Name is empty");
            return false;
        }

        else if (gas_Desc.isEmpty()) {
            gasDesc.setError("Gas Description is Empty");
            return false;
        }
        else if (KG.isEmpty()) {
            Toast.makeText(getContext(), "Update gas Kg", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (gas_price == 0) {
            gasPrice.setError("Purchase price is Empty");
            return false;
        }
        else if (gas_refillprice == 0) {
            gasRefillprice.setError("Refill price is Empty");
            return false;
        }
        else {
            gasName.setError(null);
            gasDesc.setError(null);
            gasPrice.setError(null);
            gasRefillprice.setError(null);
            return true;
        }

    }


    private void Update_Details() {


        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading changes...");
        progressDialog.show();
        progressDialog.setCancelable(false);


        int price = Integer.parseInt(gasPrice.getText().toString());
        int Rprice = Integer.parseInt(gasRefillprice.getText().toString());



        HashMap<String, Object> store = new HashMap<>();
        store.put("Gas_Name", gasName.getText().toString());
        store.put("Gas_Desc", gasDesc.getText().toString());
        store.put("Gas_Price", price);
        store.put("Gas_RefillPrice",Rprice);
        store.put("Gas_Kgs", KG);
        store.put("User_ID", mAuth.getCurrentUser().getUid());
        store.put("Gas_Image", gasImage);
        store.put("timestamp", FieldValue.serverTimestamp());

        vendorRef.document(DOC_id).update(store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getContext(), "Updated Successfully..", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    dialogEdit.dismiss();


                } else {

                    Toast.makeText(getContext(), "Updating Failed!! try again later..", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }
        });


    }


}
