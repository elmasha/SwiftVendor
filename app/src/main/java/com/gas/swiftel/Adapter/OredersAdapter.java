package com.gas.swiftel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Model.Orders_request;
import com.gas.swiftel.Model.Product;
import com.gas.swiftel.R;
import com.gas.swiftel.TimeAgo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OredersAdapter extends FirestoreRecyclerAdapter<Orders_request, OredersAdapter.OrdersViewHolder> {

    private OnItemCickListener listener;
    public List<Product> products;
    public Context context;


    public OredersAdapter(@NonNull FirestoreRecyclerOptions<Orders_request>options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull Orders_request model) {
        holder.Name.setText(model.getName());
        holder.price.setText("Price: " +model.getPrice()+"");
        holder.refillprice.setText(model.getCategory()+"");
        holder.kgs.setText(model.getItem_desc());

        Picasso.with(context).load(model.getItem_image()).placeholder(R.drawable.load).error(R.drawable.errorimage).into(holder.gasImage);

        long milisecond = model.getTimestamp().getTime();

        holder.orderTime.setText(TimeAgo.getTimeAgo(milisecond)+"");

        holder.orderClient.setText("From: "+model.getCustomer_name()+"\n"+model.getCustomer_No());
        holder.Qty.setText("Qty: "+model.getQuantity());

        if (model.getPayment_method().equals("Cash on delivery")){

            holder.payment_Method.setText("Cash on delivery");

            if (model.getItem_desc().equals("3kg")){
                holder.payment_Method.setText("Customer will pay in cash(+remit ksh.15)");
            }else if (model.getItem_desc().equals("6kg")){
                holder.payment_Method.setText("Customer will pay in cash(+remit ksh.15)");
            }else if (model.getItem_desc().equals("13kg")){
                holder.payment_Method.setText("Customer will pay in cash(+remit ksh.15)");
            }else if (model.getItem_desc().equals("15kg")){
                holder.payment_Method.setText("Customer will pay in cash(+remit ksh.25)");
            }else if (model.getItem_desc().equals("22.5kg")){
                holder.payment_Method.setText("Customer will pay in cash(+remit ksh.25)");
            }else if (model.getItem_desc().equals("55kg")){
                holder.payment_Method.setText("Customer will pay in cash(+remit ksh.55)");
            }else if (model.getCategory().equals("Accessory")){
                holder.payment_Method.setText("Customer will pay in cash(+remit ksh.30)");
            }


        }else if (model.getPayment_method().equals("Commitment paid")){

            holder.payment_Method.setText("Customer will pay in cash");
            if (model.getCategory().equals("Accessory")){
                holder.payment_Method.setText("Customer will pay in cash");
            }

        }

        int status =  model.getOrder_status();
        if (status == 3){
            holder.price.setTextColor(R.color.ColorGrey);
            holder.orderClient.setTextColor(R.color.ColorGrey);
            holder.Qty.setTextColor(R.color.ColorGrey);
            holder.delete.setVisibility(View.VISIBLE);
            holder.refillprice.setVisibility(View.GONE);
        }
        if(status == 12){

            holder.price.setTextColor(R.color.ColorGrey);
            holder.orderClient.setTextColor(R.color.ColorGrey);
            holder.Qty.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.refillprice.setVisibility(View.GONE);
        }


    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_row,parent,false);

        return new OrdersViewHolder(v);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    ///Delete item
    public void deleteItem (int position) {
     getSnapshots().getSnapshot(position).getReference().delete();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder{
       private TextView Name,price,refillprice,kgs,orderTime,orderClient,Qty,delete,payment_Method;
       private ImageView gasImage;
       private RelativeLayout ord_layout;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.OrdCard_name);
            price = itemView.findViewById(R.id.OrdCard_price);
            refillprice  = itemView.findViewById(R.id.OrdCard_refillprice);
            kgs = itemView.findViewById(R.id.OrdCard_kgs);
            gasImage = itemView.findViewById(R.id.OrdCard_Image);
            kgs= itemView.findViewById(R.id.OrdCard_kgs);
            orderTime= itemView.findViewById(R.id.OrdCard_time);
            orderClient= itemView.findViewById(R.id.OrdCard_client);
            Qty = itemView.findViewById(R.id.OrdCard_qty);
            delete = itemView.findViewById(R.id.Delte_item);
            payment_Method = itemView.findViewById(R.id.OrdCard_payment);



            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });

            price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });

            refillprice.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public interface  OnItemCickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemCickListener listener){
        this.listener = listener;

    }





}
