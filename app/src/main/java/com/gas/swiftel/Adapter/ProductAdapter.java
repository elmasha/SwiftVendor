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
import com.gas.swiftel.Model.Product;
import com.gas.swiftel.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductViewHolder> {

    private OnItemCickListener listener;
    public List<Product> products;
    public Context context;


    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
        holder.Name.setText(model.getGas_Name());
        holder.price.setText("Buy: " +model.getGas_Price()+"");
        holder.refillprice.setText("Refill: "+model.getGas_RefillPrice()+"");
        holder.kgs.setText(model.getGas_Kgs());

        Picasso.with(context).load(model.getGas_Image()).placeholder(R.drawable.load).error(R.drawable.errorimage).into(holder.gasImage);


//        long milisecond = model.getTimestamp().getTime();
//        String date = DateFormat.format("MMMM/dd/yyyy hh:mm a",new Date(milisecond)).toString();

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gas_ror,parent,false);
        this.context = parent.getContext();
        return new ProductViewHolder(v);
    }


    ///Delete item
    public void deleteItem (int position) {
     getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
       private TextView Name,price,refillprice,kgs;
       private FloatingActionButton EditItem;
       private ImageView gasImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Card_name);

            price = itemView.findViewById(R.id.Card_price);
            refillprice  = itemView.findViewById(R.id.Card_refillprice);
            kgs = itemView.findViewById(R.id.Card_kgs);
            gasImage = itemView.findViewById(R.id.Card_Image);
            kgs= itemView.findViewById(R.id.Card_kgs);
            EditItem = itemView.findViewById(R.id.Edit_item);

            EditItem.setOnClickListener(new View.OnClickListener() {
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
