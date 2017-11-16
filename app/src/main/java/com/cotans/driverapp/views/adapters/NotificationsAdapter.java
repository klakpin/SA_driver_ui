package com.cotans.driverapp.views.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.db.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private List<Notification> notificationList;

    public NotificationsAdapter(List<Notification> notifications) {
        notificationList = notifications;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notification currNotification = notificationList.get(position);
        holder.getNotificationText().setText(currNotification.getText());
        String currDate = new SimpleDateFormat("dd MMM HH:mm").format(new Date(currNotification.getTimestamp() * 1000L));
        Log.d("NotificationsAdapter", "strCurrDate = " + currDate + " date in long = " + currNotification.getTimestamp());
        holder.getNotificationDate().setText(currDate);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView notificationName, notificationDate, notificationText;
        private CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            notificationName = itemView.findViewById(R.id.ni_notificationName);
            notificationDate = itemView.findViewById(R.id.ni_notificationDate);
            notificationText = itemView.findViewById(R.id.ni_notificationText);
            card = itemView.findViewById(R.id.ni_cardView);
        }

        public TextView getNotificationName() {
            return notificationName;
        }

        public void setNotificationName(TextView notificationName) {
            this.notificationName = notificationName;
        }

        public TextView getNotificationDate() {
            return notificationDate;
        }

        public void setNotificationDate(TextView notificationDate) {
            this.notificationDate = notificationDate;
        }

        public TextView getNotificationText() {
            return notificationText;
        }

        public void setNotificationText(TextView notificationText) {
            this.notificationText = notificationText;
        }

        public CardView getCard() {
            return card;
        }

        public void setCard(CardView card) {
            this.card = card;
        }
    }
}
