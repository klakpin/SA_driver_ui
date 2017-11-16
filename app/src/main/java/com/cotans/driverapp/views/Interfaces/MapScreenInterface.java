package com.cotans.driverapp.views.Interfaces;


import android.annotation.SuppressLint;
import android.location.LocationListener;
import android.view.View;

import com.google.android.gms.maps.model.CameraPosition;

public interface MapScreenInterface {

    @SuppressLint("MissingPermission")
    void startLocationRequests(LocationListener listener);

    void stopLocationRequests();

    CameraPosition getLastRouteOverview();

    void setLastRouteOverview(CameraPosition lastRouteOverview);

    void setDriveButtonListener(View.OnClickListener listener);

    /**
     * Set emergency button action listener
     *
     * @param onClickListener listener that should be attached to emergency button
     */
    void setEmergencyButtonListener(View.OnClickListener onClickListener);

    void showFinishButton();

    void showPickUpButton();

    void hidePickUpButton();

    void hideFinishButton();

    CameraPosition getLastPos();

    void setLastPos(CameraPosition pos);

    /**
     * Set finishActivity action listener
     *
     * @param onClickListener listener that should be attached to finishActivity button
     */
    void setFinishClickListener(View.OnClickListener onClickListener);

    /**
     * Set pick up action listener
     *
     * @param listener listener that should be attached to pick up button
     */
    void setPickUpClickListener(View.OnClickListener listener);

    /**
     * Set destination of parcel on user's screen
     *
     * @param string destination of parcel that should be printed
     */
    void setDestinationOfParcel(String string);

    /**
     * Set name of parcel on user's screen
     *
     * @param string name of parcel that should be printed
     */
    void setNameOfParcel(String string);

    /**
     * Show to user progress bar
     */
    void startProgressBar();

    /**
     * Hide progress bar from user
     */
    void stopProgressBar();

    /**
     * Notify user about error while pickup action
     *
     * @param s text that describes error
     */
    void onErrorWhilePickUp(String s);

    /**
     * Notify user about pickup action success
     */
    void notifyUserAboutSuccessfulPickUp();

    /**
     * Show popup screen (for example AlertDialog) for order confirmation processing
     */
    void showConfirmationPopup();

    /**
     * Hide all elements related to parcel delivery from map
     */
    void hideParcelOverlay();

    /**
     * Provides id of current order if there is any
     *
     * @return null if there is no order for navigation, order id otherwise
     */
    int getOrderId();

    /**
     * Notificate user about internal error
     *
     * @param s error description
     */
    void showError(String s);

    /**
     * Creates new emergency activity
     */
    void openEmergencyAlertDialog();

    boolean isRouteJustCreated();

    void setRouteJustCreated(boolean value);
}