package com.cotans.driverapp.views.implementations;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.cotans.driverapp.R;
import com.cotans.driverapp.models.scopes.ActivityComponent;
import com.cotans.driverapp.models.scopes.DaggerActivityComponent;
import com.cotans.driverapp.presenters.AccountInfoScreenPresenter;
import com.cotans.driverapp.views.Interfaces.AccountInfoInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountInfoFragment extends Fragment implements AccountInfoInterface {

    @BindView(R.id.v2ai_logoutButton)
    Button logoutButton;

    @BindView(R.id.v2ai_gpsSendingSwitch)
    Switch gpsSwitch;

    @BindView(R.id.v2ai_barChart)
    BarChart barChart;

    @BindView(R.id.v2ai_lineChart)
    LineChart lineChart;

    @Inject
    public AccountInfoScreenPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.v2_account_info, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        ActivityComponent component = DaggerActivityComponent.builder().build();
        component.inject(this);

        Utils.init(getActivity());

        presenter.bindView(this);
        presenter.init();
    }

    @Override
    public BarChart getBarChart() {
        return barChart;
    }

    @Override
    public LineChart getLineChart() {
        return lineChart;
    }

    @Override
    public void setLogoutButtonListener(View.OnClickListener listener) {
        logoutButton.setOnClickListener(listener);
    }

    @Override
    public void setGpsSwitchChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        gpsSwitch.setOnCheckedChangeListener(listener);
    }

    @Override
    public void setGpsSenderStatus(boolean status) {
        gpsSwitch.setChecked(status);
    }

    @Override
    public void setFuelConsumptionData(LineData data) {
        if (data == null) {
            data = generateLineLine();
        }

        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);

        lineChart.setData(data);

        lineChart.invalidate();
        lineChart.animateX(750);
    }

    @Override
    public void setKmData(BarData data) {
        if (data == null) {
            data = generateBarData();
        }
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f);

        barChart.setData(data);
        barChart.setFitBars(true);

        barChart.invalidate();
        barChart.animateY(700);
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 13; i < 21; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "Kilometers travelled");
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    private LineData generateLineLine() {

        ArrayList<Entry> e1 = new ArrayList<Entry>();

        for (int i = 13; i < 21; i++) {
            e1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(e1, "Fuel consumption");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);


        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);

        LineData cd = new LineData(sets);
        return cd;
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
}
