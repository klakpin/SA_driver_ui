package com.cotans.driverapp.views.implementations;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.network.EmergencyAlarmService;
import com.cotans.driverapp.models.scopes.ActivityComponent;
import com.cotans.driverapp.models.scopes.DaggerActivityComponent;
import com.cotans.driverapp.presenters.EmergencyScreenPresenter;
import com.cotans.driverapp.presenters.MapScreenPresenter;
import com.cotans.driverapp.presenters.OrderCompletionScreenPresenter;
import com.cotans.driverapp.views.Interfaces.EmergencyScreenInterface;
import com.cotans.driverapp.views.Interfaces.MapScreenInterface;
import com.cotans.driverapp.views.Interfaces.OrderCompletionInterface;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapScreenFragment extends Fragment implements MapScreenInterface, OrderCompletionInterface, EmergencyScreenInterface {

    MapView mMapView;
    AlertDialog alertDialog;
    View dialogView;
    @BindView(R.id.v2mp_progressbar)
    ProgressBar pBar;
    @BindView(R.id.v2ml_emergencyButton)
    Button emergencyButton;
    @BindView(R.id.v2ml_finishButton)
    Button finishOrderButton;
    @BindView(R.id.v2ml_PickUpButton)
    Button pickUpOrderButton;
    @BindView(R.id.v2ml_destinationOfParcel)
    TextView destinationOfParcel;
    @BindView(R.id.v2ml_nameOfParcel)
    TextView nameOfParcel;
    @BindView(R.id.v2ml_order_info_screen)
    CardView orderInfoScreen;
    @BindView(R.id.v2ml_driveButton)
    Button driveButton;

    private CameraPosition lastRouteOverview;

    CameraPosition lastPos;
    private boolean routeJustCreated;
    LocationManager manager;

    @Inject
    EmergencyScreenPresenter emergencyScreenPresenter;
    @Inject
    public MapScreenPresenter mapScreenPresenter;
    @Inject
    public OrderCompletionScreenPresenter mOrderCompletionScreenPresenter;

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.v2_map_layout, container, false);

        ButterKnife.bind(this, rootView);

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(mMap -> mapScreenPresenter.mapReady(mMap));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActivityComponent component = DaggerActivityComponent.builder().build();
        component.inject(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("MapScreenFragment", "onResume");
        mapScreenPresenter.bindView(this);
        mapScreenPresenter.init();
        mMapView.getMapAsync(mMap -> mapScreenPresenter.mapReady(mMap));

    }



    @Override
    public void hideParcelOverlay() {
        pickUpOrderButton.setVisibility(View.GONE);
        finishOrderButton.setVisibility(View.GONE);
        orderInfoScreen.setVisibility(View.GONE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void startLocationRequests(LocationListener listener) {
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
    }

    @Override
    public void stopLocationRequests() {
        if (manager != null) {
            manager.removeUpdates(mapScreenPresenter);
        }
    }

    @Override
    public CameraPosition getLastRouteOverview() {
        return lastRouteOverview;
    }

    @Override
    public void setLastRouteOverview(CameraPosition lastRouteOverview) {
        this.lastRouteOverview = lastRouteOverview;
    }

    @Override
    public void setDriveButtonListener(View.OnClickListener listener) {
        driveButton.setOnClickListener(listener);
    }

    @Override
    public void setEmergencyButtonListener(View.OnClickListener onClickListener) {
        emergencyButton.setOnClickListener(onClickListener);
    }

    @Override
    public void showFinishButton() {
        Log.d("MapSCrFr", "Show finish button");

        finishOrderButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideFinishButton() {
        finishOrderButton.setVisibility(View.GONE);
    }

    @Override
    public void showPickUpButton() {
        pickUpOrderButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePickUpButton() {
        pickUpOrderButton.setVisibility(View.GONE);
    }

    @Override
    public CameraPosition getLastPos() {
        return lastPos;
    }

    @Override
    public void setLastPos(CameraPosition pos) {
        lastPos = pos;
    }

    @Override
    public void setFinishClickListener(View.OnClickListener onClickListener) {
        finishOrderButton.setOnClickListener(onClickListener);

    }

    @Override
    public void setPickUpClickListener(View.OnClickListener listener) {
        pickUpOrderButton.setOnClickListener(listener);

    }

    @Override
    public void setCancelButtonListener(View.OnClickListener listener) {
        alertDialog.findViewById(R.id.es_cancelButton).setOnClickListener(listener);
    }

    @Override
    public void onErrorWhilePickUp(String s) {
        Toast.makeText(getActivity(), "Oops! Error arrised while pick up: " + s, Toast.LENGTH_LONG).show();
    }


    @Override
    public void notifyUserAboutSuccessfulPickUp() {
        Toast.makeText(getActivity(), "Successfully picked up!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConfirmationPopup() {
        mOrderCompletionScreenPresenter.bindView(this);

        Button sendCodeButton;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.confirmation_screen, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        sendCodeButton = dialogView.findViewById(R.id.cs_send_button);
        sendCodeButton.setOnClickListener(v -> {
            TextView input = alertDialog.findViewById(R.id.cs_confirm_code);
            Log.d("MapFragment", "input: " + input.getText().toString());
            onSendConfirmationCodeButtonPress(input.getText().toString());
        });
        alertDialog.show();
    }

    @Override
    public void onSendConfirmationCodeButtonPress(String input) {
        mOrderCompletionScreenPresenter.onSendCompletionCodeButtonPressed(input);
    }

    @Override
    public void onInvalidCodeInput() {
        Toast.makeText(getActivity(), "Please, enter valid code", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stop() {
        //TODO:

    }

    @Override
    public void setDestinationOfParcel(String destinationOfParcel) {
        orderInfoScreen.setVisibility(View.VISIBLE);
        this.destinationOfParcel.setText(destinationOfParcel);
    }

    @Override
    public void setNameOfParcel(String nameOfParcel) {
        orderInfoScreen.setVisibility(View.VISIBLE);
        this.nameOfParcel.setText(nameOfParcel);

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
    public void notifyUserAboutSuccessfulOrderFinish() {
        Toast.makeText(getActivity(), "Order is finished successfully!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void finishActivity() {
        alertDialog.cancel();
        hideParcelOverlay();
        hideFinishButton();
        mapScreenPresenter.onFinishActivity();
    }

    @Override
    public void notifyUserAboutNotSuccessfulOrderFinish(String message) {
        Toast.makeText(getActivity(), "Order was not completed. " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public int getOrderId() {
        try {
            Bundle extras = getActivity().getIntent().getExtras();
            int id = extras.getInt("orderId");
            return id;
        } catch (NullPointerException e) {
            //TODO redo
            return -1;
        }
    }

    @Override
    public void showError(String s) {
        Log.d("MapScreenFragment", "Error! " + s);
        Toast.makeText(getActivity(), "Error, " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void openEmergencyAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.emergency_screen, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();

        alertDialog.setCancelable(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

        Button btn = alertDialog.findViewById(R.id.es_cancelButton);
        btn.setOnClickListener(v -> alertDialog.cancel());

        emergencyScreenPresenter.bindView(this);
        emergencyScreenPresenter.init();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (alertDialog != null)
            alertDialog.dismiss();
        stopLocationRequests();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null)
            alertDialog.dismiss();
        stopLocationRequests();
    }

    @Override
    public void setEsText(String text) {
        getActivity().runOnUiThread(() -> {
            TextView txt = alertDialog.findViewById(R.id.es_text);

            final Animation in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(200);

            final Animation out = new AlphaAnimation(1.0f, 0.0f);
            out.setDuration(200);

            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    txt.setText(text);
                    txt.startAnimation(in);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            txt.startAnimation(out);
        });
    }

    @Override
    public boolean isRouteJustCreated() {
        return routeJustCreated;
    }

    @Override
    public void setRouteJustCreated(boolean value) {
        routeJustCreated = value;
    }

    @Override
    public void cancelAlert() {
        alertDialog.cancel();
    }
}
