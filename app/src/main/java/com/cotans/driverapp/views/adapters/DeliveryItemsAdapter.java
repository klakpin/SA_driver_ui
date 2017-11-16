package com.cotans.driverapp.views.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cotans.driverapp.R;
import com.cotans.driverapp.views.items.OrderProperties;

import java.util.List;

public class DeliveryItemsAdapter extends RecyclerView.Adapter<DeliveryItemsAdapter.myViewHolder> {

    private List<OrderProperties> values;
    private Context context;

    public DeliveryItemsAdapter(Context context, List<OrderProperties> values) {
        this.values = values;
        this.context = context;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item_property, parent, false);
        return new DeliveryItemsAdapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        OrderProperties properties = values.get(position);
        holder.name.setText(properties.getPropertyName());
        holder.value.setText(properties.getPropertyValue());

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        public TextView name, value;
        public LinearLayout card;

        public myViewHolder(View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.dit_propertyName);
            name = itemView.findViewById(R.id.dit_propertyValue);
            card = itemView.findViewById(R.id.dit_layout);
        }
    }
}
