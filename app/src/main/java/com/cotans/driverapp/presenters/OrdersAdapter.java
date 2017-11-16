package com.cotans.driverapp.presenters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.db.Order;
import com.cotans.driverapp.views.implementations.NewOrderScreenActivity;
import com.cotans.driverapp.views.implementations.OrderInfoActivity;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.myViewHolder> {

    private Context context;
    private List<Order> ordersList;
    /*
    TODO maybe add presenter here and call his method from onClick from onBindViewHolderMethod + think about constant updates
     */
    public OrdersAdapter(Context context, List<Order> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.deliveries_list_item, parent, false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {
        Order order = ordersList.get(position);
        holder.name.setText(order.getName());
        holder.source.setText(order.getSource());
        holder.destination.setText(order.getDestination());
        holder.id = order.getId();

        if (ordersList.get(holder.getAdapterPosition()).isNew()) {
            holder.isNew.setVisibility(View.VISIBLE);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (!ordersList.get(holder.getAdapterPosition()).isNew()) {
                    intent = new Intent(context, OrderInfoActivity.class);
                } else {
                    intent = new Intent(context, NewOrderScreenActivity.class);
                    intent.putExtra("FROM_ADAPTER", true);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", ordersList.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public void emptyList() {
        ordersList.clear();
    }

    public class myViewHolder extends RecyclerView.ViewHolder   {

        public TextView name, source, destination, isNew;
        public int id;
        public CardView card;

        public myViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_view);
            name = itemView.findViewById(R.id.dli_name);
            source = itemView.findViewById(R.id.dli_src);
            destination = itemView.findViewById(R.id.dli_dest);
            isNew = itemView.findViewById(R.id.dli_isNew);
        }
    }
}

