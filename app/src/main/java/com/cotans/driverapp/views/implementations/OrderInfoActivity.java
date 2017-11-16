package com.cotans.driverapp.views.implementations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.scopes.ActivityComponent;
import com.cotans.driverapp.models.scopes.DaggerActivityComponent;
import com.cotans.driverapp.presenters.OrderCompletionScreenPresenter;
import com.cotans.driverapp.presenters.OrderInfoScreenPresenter;
import com.cotans.driverapp.views.Interfaces.OrderCompletionInterface;
import com.cotans.driverapp.views.Interfaces.OrderInfoInterface;
import com.cotans.driverapp.views.adapters.DeliveryItemsAdapter;
import com.cotans.driverapp.views.items.OrderProperties;
import com.cotans.driverapp.views.items.SimpleDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderInfoActivity extends AppCompatActivity implements OrderInfoInterface, OrderCompletionInterface {

    @Inject
    public OrderInfoScreenPresenter orderInfoPresenter;
    @Inject
    public OrderCompletionScreenPresenter orderCompletionScreenPresenter;

    @BindView(R.id.di_progressbar)
    ProgressBar pBar;
    @BindView(R.id.di_start_driving)
    Button startDrivingButton;
    @BindView(R.id.di_ActionButton)
    Button actionButton;

    Button sendCodeButton;
    EditText confirmationCode;

    AlertDialog alertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_info);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityComponent component = DaggerActivityComponent.builder().build();
        component.inject(this);
        orderInfoPresenter.bindView(this);
        orderInfoPresenter.init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (alertDialog != null) {
            alertDialog.cancel();
        }
    }

    @Override
    public void startProgressBar() {
        pBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressBar() {
        pBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fill(List<OrderProperties> properties) {
        final Context currContext = this;
        final List<OrderProperties> props = properties;
        runOnUiThread(() -> {
            RecyclerView rv = findViewById(R.id.di_rv);
            LinearLayoutManager llm = new LinearLayoutManager(currContext);
            rv.setLayoutManager(llm);
            rv.addItemDecoration(new SimpleDividerItemDecoration(currContext));
            DeliveryItemsAdapter adapter = new DeliveryItemsAdapter(currContext, props);
            rv.setAdapter(adapter);
        });
    }

    @Override
    public void setActionButtonClickListener(View.OnClickListener listener) {
        actionButton.setOnClickListener(listener);
    }

    @Override
    public void setStartDrivingButtonClickListener(View.OnClickListener listener) {
        startDrivingButton.setOnClickListener(listener);
    }

    @Override
    public void showConfirmationPopUp() {
        orderCompletionScreenPresenter.bindView(this);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirmation_screen, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        confirmationCode = dialogView.findViewById(R.id.cs_confirm_code);
        sendCodeButton = dialogView.findViewById(R.id.cs_send_button);
        sendCodeButton.setOnClickListener(v -> onSendConfirmationCodeButtonPress(confirmationCode.getText().toString()));
        alertDialog.show();
    }


    @Override
    public int getDeliveryId() {
        return getIntent().getIntExtra("id", -1);
    }

    @Override
    public void notifyUserAboutNotSuccessfulOrderFinish(String message) {
        Toast.makeText(this, "Order was not completed. " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void notifyUserAboutSuccessfulOrderFinish() {
        Toast.makeText(this, "Order successfully finished!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void stop() {
        finish();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void onSendConfirmationCodeButtonPress(String input) {
        orderCompletionScreenPresenter.onSendCompletionCodeButtonPressed(input);

    }

    @Override
    public void onInvalidCodeInput() {
        Toast.makeText(this, "Code format is invalid.", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getOrderId() {
        return getIntent().getIntExtra("id", -1);
    }

    @Override
    public void onStartDrivingError(String s) {
        Toast.makeText(this, "Cannot find route. " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setButtonText(String text) {
        actionButton.setText(text);
    }

    @Override
    public void startMainActivity(String route, int id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("route", route);
        intent.putExtra("orderId", id);
        Log.d("OrderInfoPresenter", "Order id: " + id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUnsuccessfulOrderPickup(String status) {
        Toast.makeText(this, "Order pickup request failed. " + status, Toast.LENGTH_LONG).show();
    }
}
