package com.gas.swiftel.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.R;
import com.gas.swiftel.Model.VendorsShop;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VendorAdapter extends FirestoreRecyclerAdapter<VendorsShop, VendorAdapter.MyShopViewHolder> {

    private OnItemCickListener listener;
    public List<VendorsShop> products;
    public Context context;


    public VendorAdapter(@NonNull FirestoreRecyclerOptions<VendorsShop> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyShopViewHolder holder, int position, @NonNull VendorsShop model) {
        holder.Name.setText("Type: "+model.getGas_Name());
        holder.price.setText("KSh" +model.getGas_Price()+"");
        holder.refillprice.setText("Refill:"+model.getGas_RefillPrice()+"");
        holder.kgs.setText(model.getGas_Kgs());
        Picasso.with(context).load(model.getGas_Image()).into(holder.gasImage);

//        long milisecond = model.getTimestamp().getTime();

    }

    @NonNull
    @Override
    public MyShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gas_ror,parent,false);
        return new MyShopViewHolder(v);
    }

    public void deleteItem (int position)
    {
     getSnapshots().getSnapshot(position).getReference().delete();
    }

    class MyShopViewHolder extends RecyclerView.ViewHolder{
       private TextView Name,price,refillprice,kgs;
       private ImageView gasImage;

        public MyShopViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Card_name);
            price = itemView.findViewById(R.id.Card_price);
            refillprice  = itemView.findViewById(R.id.Card_refillprice);
            kgs = itemView.findViewById(R.id.Card_kgs);
            gasImage = itemView.findViewById(R.id.Card_Image);
//            kgs= itemView.findViewById(R.id.TransType);
//            prvAmount = itemView.findViewById(R.id.previousAmount1);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

        }
    }


    public interface  OnItemCickListener{

        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemCickListener listener){
        this.listener = listener;
    }

}
