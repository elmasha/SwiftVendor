package com.gas.swiftel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Model.Payment_Hist;
import com.gas.swiftel.Model.Product;
import com.gas.swiftel.R;
import com.gas.swiftel.TimeAgo;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.List;

public class PaymentAdapter extends FirestoreRecyclerAdapter<Payment_Hist, PaymentAdapter.PayViewHolder> {

    private OnItemCickListener listener;
    public List<Product> products;
    public Context context;


    public PaymentAdapter(@NonNull FirestoreRecyclerOptions<Payment_Hist> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PayViewHolder holder, int position, @NonNull Payment_Hist model) {
        holder.Name.setText(model.getName());
        holder.desc.setText(model.getType());
        holder.Amount.setText("Ksh "+model.getAmount());
        holder.phone.setText(model.getPhoneNo());
        long milisecond = model.getTimestamp().getTime();

        holder.time.setText(TimeAgo.getTimeAgo(milisecond));



        Date today = new Date();
        long diff =  today.getTime() - milisecond;
        int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
        int hours = (int) (diff / (1000 * 60 * 60));
        int minutes = (int) (diff / (1000 * 60));
        int seconds = (int) (diff / (1000));

//        if (numOfDays == 1){
//            holder.view.setVisibility(View.INVISIBLE);
//        }


    }

    @NonNull
    @Override
    public PayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_histrow,parent,false);
        this.context = parent.getContext();
        return new PayViewHolder(v);
    }


    ///Delete item
    public void deleteItem (int position) {
     getSnapshots().getSnapshot(position).getReference().delete();
    }

    class PayViewHolder extends RecyclerView.ViewHolder{
       private TextView Name, desc, time,Amount,phone;

        public PayViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Payhist_name);

            desc = itemView.findViewById(R.id.Payhist_desc);
            time = itemView.findViewById(R.id.Payhist_time);
            Amount = itemView.findViewById(R.id.Payhist_amount);
            phone = itemView.findViewById(R.id.Payhist_phone);

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
