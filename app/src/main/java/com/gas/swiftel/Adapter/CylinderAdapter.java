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
import com.gas.swiftel.Model.Gas_Cylinder;
import com.gas.swiftel.Model.Product;
import com.gas.swiftel.R;
import com.gas.swiftel.TimeAgo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class CylinderAdapter extends FirestoreRecyclerAdapter<Gas_Cylinder, CylinderAdapter.CylinderViewHolder> {

    private OnItemCickListener listener;
    public List<Product> products;
    public Context context;


    public CylinderAdapter(@NonNull FirestoreRecyclerOptions<Gas_Cylinder> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CylinderViewHolder holder, int position, @NonNull Gas_Cylinder model) {
        holder.Name.setText(model.getName());
        holder.kg.setText(model.getKg());

        Picasso.with(context).load(model.getCylinder_image())
                .placeholder(R.drawable.load)
                .error(R.drawable.errorimage)
                .into(holder.imageView);



    }

    @NonNull
    @Override
    public CylinderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cylinder_row,parent,false);
        this.context = parent.getContext();
        return new CylinderViewHolder(v);
    }


    ///Delete item
    public void deleteItem (int position) {
     getSnapshots().getSnapshot(position).getReference().delete();
    }

    class CylinderViewHolder extends RecyclerView.ViewHolder{
       private TextView Name, kg;
       private ImageView imageView;

        public CylinderViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Cy_name);

            kg = itemView.findViewById(R.id.Cy_kgs);
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


