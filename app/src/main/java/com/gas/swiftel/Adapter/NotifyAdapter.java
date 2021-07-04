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
import com.gas.swiftel.Model.Notifications;
import com.gas.swiftel.Model.Product;
import com.gas.swiftel.R;
import com.gas.swiftel.TimeAgo;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.List;

public class NotifyAdapter extends FirestoreRecyclerAdapter<Notifications, NotifyAdapter.NotifyViewHolder> {

    private OnItemCickListener listener;
    public List<Product> products;
    public Context context;


    public NotifyAdapter(@NonNull FirestoreRecyclerOptions<Notifications> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotifyViewHolder holder, int position, @NonNull Notifications model) {
        holder.Name.setText(model.getName());
        holder.desc.setText(model.getType());

        long milisecond = model.getTimestamp().getTime();


        holder.time.setText(TimeAgo.getTimeAgo(milisecond));


        Date today = new Date();
        long diff =  today.getTime() - milisecond;
        int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
        int hours = (int) (diff / (1000 * 60 * 60));
        int minutes = (int) (diff / (1000 * 60));
        int seconds = (int) (diff / (1000));

        if (numOfDays == 1){
            holder.view.setVisibility(View.INVISIBLE);
        }


    }

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_row,parent,false);
        this.context = parent.getContext();
        return new NotifyViewHolder(v);
    }


    ///Delete item
    public void deleteItem (int position) {
     getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NotifyViewHolder extends RecyclerView.ViewHolder{
       private TextView Name, desc, time;
       private View view;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Notify_name);

            desc = itemView.findViewById(R.id.Notify_desc);
            time = itemView.findViewById(R.id.Notify_time);
            view = itemView.findViewById(R.id.Notify_view);

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
