package com.gas.swiftel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Model.AccessModel;
import com.gas.swiftel.Model.Gas_Cylinder;
import com.gas.swiftel.Model.Product;
import com.gas.swiftel.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminAccessAdapter extends FirestoreRecyclerAdapter<AccessModel, AdminAccessAdapter.AccessViewHolder> {

    private OnItemCickListener listener;
    public List<Product> products;
    public Context context;


    public AdminAccessAdapter(@NonNull FirestoreRecyclerOptions<AccessModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AccessViewHolder holder, int position, @NonNull AccessModel model) {
//        holder.Name.setText(model.getName());

        Picasso.with(context).load(model.getAccessory_image()).fit().placeholder(R.drawable.load).error(R.drawable.errorimage).into(holder.imageView);
        holder.Name.setVisibility(View.GONE);


    }

    @NonNull
    @Override
    public AccessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cylinder_row,parent,false);
        this.context = parent.getContext();
        return new AccessViewHolder(v);
    }


    ///Delete item
    public void deleteItem (int position) {
     getSnapshots().getSnapshot(position).getReference().delete();
    }

    class AccessViewHolder extends RecyclerView.ViewHolder{
       private TextView Name;
       private ImageView imageView;

        public AccessViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Cy_name);

            imageView = itemView.findViewById(R.id.Cy_Image);
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


