package com.cotans.driverapp.views.Interfaces;


import android.view.View;
import android.widget.CompoundButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;

public interface AccountInfoInterface {

    BarChart getBarChart();

    LineChart getLineChart();

    void setLogoutButtonListener(View.OnClickListener listener);

    void setGpsSwitchChangeListener(CompoundButton.OnCheckedChangeListener listener);

    void setGpsSenderStatus(boolean status);

    void startLoginActivity();

    void setFuelConsumptionData(LineData data);

    void setKmData(BarData data);
}
