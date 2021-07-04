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
import com.gas.swiftel.Model.Accessories;
import com.gas.swiftel.R;
import com.gas.swiftel.TimeAgo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AccessoriesAdapter extends FirestoreRecyclerAdapter<Accessories, AccessoriesAdapter.ProductViewHolder> {

    private OnItemCickListener listener;
    public List<Accessories> products;
    public Context context;


    public AccessoriesAdapter(@NonNull FirestoreRecyclerOptions<Accessories> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Accessories model) {
        holder.item_name.setText(model.getItem_name());
        holder.item_price.setText("Price:"+model.getItem_Price()+"");
        holder.item_desc.setText(model.getItem_Desc());

        Picasso.with(context).load(model.getItem_Image()).placeholder(R.drawable.load).error(R.drawable.errorimage).into(holder.item_Image);

        long milisecond = model.getTimestamp().getTime();
        holder.item_date.setText(TimeAgo.getTimeAgo(milisecond)+"");

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
        this.context = parent.getContext();
        return new ProductViewHolder(v);
    }




    class ProductViewHolder extends RecyclerView.ViewHolder{
       private TextView item_name,item_price,item_desc,item_date;
       private FloatingActionButton edit;
       private ImageView item_Image;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.Citem_name);
            item_price = itemView.findViewById(R.id.Citem_price);
            item_desc  = itemView.findViewById(R.id.Citem_desc);
            item_date = itemView.findViewById(R.id.Citem_date);
            item_Image = itemView.findViewById(R.id.Citem_Image);
            edit = itemView.findViewById(R.id.Edit_item2);



            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });

            item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

    ///Delete item
    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


}
