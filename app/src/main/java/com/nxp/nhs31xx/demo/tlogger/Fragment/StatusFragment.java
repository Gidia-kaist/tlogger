/*
 * Copyright 2019 NXP.
 * This software is owned or controlled by NXP and may only be used
 * strictly in accordance with the applicable license terms.  By expressly
 * accepting such terms or by downloading, installing, activating and/or
 * otherwise using the software, you are agreeing that you have read, and
 * that you agree to comply with and are bound by, such license terms.  If
 * you do not agree to be bound by the applicable license terms, then you
 * may not retain, install, activate or otherwise use the software.
 */


package com.nxp.nhs31xx.demo.tlogger.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nxp.nhs31xx.demo.tlogger.MainActivity;
import com.nxp.nhs31xx.demo.tlogger.Message.Response.GetConfigResponse;
import com.nxp.nhs31xx.demo.tlogger.Message.Response.MeasureTemperatureResponse;
import com.nxp.nhs31xx.demo.tlogger.R;
import com.nxp.nhs31xx.demo.tlogger.SampleApplication;
import com.nxp.nhs31xx.demo.tlogger.StartActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kr.co.mediex.myhealth.v1.view.MyHealthActivity;
import kr.co.mediex.myhealth.v1.view.MyHealthWebActivity;

import static android.content.ContentValues.TAG;

public class StatusFragment extends Fragment {

    LinearLayout selector_1, selector_2, gr_1, cell_1, cell_2, cell_3;
    Button btn_cat_1, btn_cat_2,loginButton;
    Boolean selector = Boolean.FALSE;
    Button btn_reset;
    private LineChart chart;
    LineChart chart_mp;
    int X_RANGE = 1;
    int DATA_RANGE = 30;
    ScrollView scroll;
    TextView IOP_text;
    ArrayList<Entry> xVal;
    LineDataSet setXcomp;
    ArrayList<String> xVals;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<ILineDataSet> lineDataSets;
    LineData lineData;
    TextView val_real;
    TextView val_init;
    TextView val_high;
    TextView tx_cell_3;
    Button btn_export;
    float marker = -1;
    float high = 0;
    float temp = 0;
    float i_0 = 0;
    float ald_3=0;
    Double aa;
    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setRetainInstance(true);

    }



    private void chartInit() {

        // enable description text
        chart.getDescription().setEnabled(true);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis xl = chart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setSpaceTop(10f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        //feedMultiple();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_status, container, false);
        tx_cell_3 = v.findViewById(R.id.tx_cell_3);
        chart = v.findViewById(R.id.chart_mp);
        val_real = v.findViewById(R.id.val_real);
        val_high = v.findViewById(R.id.val_high);
        val_init = v.findViewById(R.id.val_init);
        selector_1 = v.findViewById(R.id.selector_1);
        selector_2 = v.findViewById(R.id.selector_2);
        cell_1 = v.findViewById(R.id.cell_1);
        cell_2 = v.findViewById(R.id.cell_2);
        cell_3 = v.findViewById(R.id.cell_3);
        scroll = v.findViewById(R.id.scroll);
        IOP_text = v.findViewById(R.id.IOP_text);
        gr_1 = v.findViewById(R.id.gr_1);
        btn_cat_1 = v.findViewById(R.id.btn_cat_1);
        btn_cat_2 = v.findViewById(R.id.btn_cat_2);
        loginButton = v.findViewById(R.id.loginButton);
        btn_reset = v.findViewById(R.id.btn_reset);
        btn_export = v.findViewById(R.id.loginButton);


        btn_cat_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selector = Boolean.FALSE;
                btn_reset.setVisibility(View.VISIBLE);
                selector_2.setVisibility(View.GONE);
                selector_1.setVisibility(View.VISIBLE);
                cell_1.setVisibility(View.GONE);
                cell_2.setVisibility(View.VISIBLE);
                cell_3.setVisibility(View.VISIBLE);
                scroll.setVisibility(View.VISIBLE);
                gr_1.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                tx_cell_3.setText("mmHg");
            }
        });

        btn_cat_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selector = Boolean.TRUE;
                btn_reset.setVisibility(View.VISIBLE);
                selector_2.setVisibility(View.GONE);
                selector_1.setVisibility(View.VISIBLE);
                cell_1.setVisibility(View.GONE);
                cell_2.setVisibility(View.GONE);
                cell_3.setVisibility(View.VISIBLE);
                scroll.setVisibility(View.GONE);
                gr_1.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                tx_cell_3.setText("mM");
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selector_1.setVisibility(View.GONE);
                selector_2.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
                btn_reset.setVisibility(View.GONE);
            }
        });
        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent loginActivityIntent = new Intent(getContext(), MyHealthWebActivity.class);
                // 라이브러리 Activity에 정보를 전달
                if(selector == Boolean.FALSE){
                    loginActivityIntent.putExtra(MyHealthActivity.SERVICE_NAME, SampleApplication.MY_SERVICE_NAME_IOP);
                    loginActivityIntent.putExtra(MyHealthActivity.SERVICE_SECRET, SampleApplication.MY_SERVICE_SECRET_IOP);
                    startActivityForResult(loginActivityIntent, 1);
                }else if(selector == Boolean.TRUE){
                    loginActivityIntent.putExtra(MyHealthActivity.SERVICE_NAME, SampleApplication.MY_SERVICE_NAME_GLUCOSE);
                    loginActivityIntent.putExtra(MyHealthActivity.SERVICE_SECRET, SampleApplication.MY_SERVICE_SECRET_GLUCOSE);
                    startActivityForResult(loginActivityIntent, 1);
                }

            }
        });

        chartInit();
        return v;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MyHealthActivity.SUCCESS_CODE) {
            Intent saveDataActivityIntent = new Intent(getContext(), StartActivity.class);
            saveDataActivityIntent.putExtra("list", arrayList);
            if(selector == Boolean.FALSE){
                saveDataActivityIntent.putExtra("selector", 0);
                Log.d("TAG_!", "iop");
            }else if(selector == Boolean.TRUE){
                saveDataActivityIntent.putExtra("selector", 1);
                Log.d("TAG_!", "glucose");
            }
            saveDataActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(saveDataActivityIntent);
        } else if (resultCode == MyHealthActivity.ERROR_CODE) {
            String errorMessage = data.getStringExtra(MyHealthActivity.ERROR_MESSAGE_KEY);
            if (errorMessage != null) {
                Log.d(TAG, errorMessage);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initFields();
    }

    public boolean liveMeasurementsIsChecked() {
        boolean checked = false;
        View view = this.getView();
        if (view != null) {
            checked = ((Switch) view.findViewById(R.id.liveMeasurementsStatusSwitch)).isChecked();
        }
        return checked;
    }

    public void update(boolean exporting) {
        View view = this.getView();
        if (view != null) {
            try {
                MainActivity activity = (MainActivity) getActivity();
                Button button = view.findViewById(R.id.exportStatusButton);
                if (activity.lastUsedTagId == null) {
                    view.findViewById(R.id.exportStatusButton).setEnabled(false);
                } else if (exporting) {
                    button.setText(R.string.exportOngoingStatus);
                    view.findViewById(R.id.exportStatusButton).setEnabled(false);
                } else {
                    button.setText(R.string.exportStatus);
                    view.findViewById(R.id.exportStatusButton).setEnabled(true);
                }
            } catch (NullPointerException e) {
                // absorb
            }
        }
    }

    public void update(int retrievedMeasurementsCount, int totalMeasurementsCount) {
        View view = this.getView();
        if (view != null) {
            TextView textView;

            textView = view.findViewById(R.id.countStatusTextView);
            if (retrievedMeasurementsCount >= totalMeasurementsCount) {
                textView.setText(String.format(getResources().getString(R.string.countEndStatus), totalMeasurementsCount));
                view.findViewById(R.id.exportStatusButton).setEnabled(true);
            } else {
                textView.setText(String.format(getResources().getString(R.string.countOngoingStatus), retrievedMeasurementsCount, totalMeasurementsCount));
            }
        }
    }

    public void update(GetConfigResponse getConfigResponse) {
        View view = this.getView();
        if (view != null) {
            TextView textView;
            try {
                MainActivity activity = (MainActivity) getActivity();
                textView = view.findViewById(R.id.tagStatusTextView);
                if (activity.lastUsedTagId == null) {
                    textView.setText("");
                    view.findViewById(R.id.exportStatusButton).setEnabled(false);
                } else {
                    textView.setText(String.format(getResources().getString(R.string.tagStatus), activity.lastUsedTagId));
                    view.findViewById(R.id.exportStatusButton).setEnabled(true);
                }

                Date start = new Date(1 * 1000);
                TextView startTextView = view.findViewById(R.id.startStatusTextView);
                TextView countTextView = view.findViewById(R.id.countStatusTextView);
                TextView limitTextView = view.findViewById(R.id.limitStatusTextView);
                TextView rangeTextView = view.findViewById(R.id.rangeStatusTextView);
                TextView alarmTextView = view.findViewById(R.id.alarmStatusTextView);
                if (getConfigResponse.isPristine()) {
                    startTextView.setText(R.string.notStartedStatus);
                    countTextView.setText(R.string.noCountStatus);
                    limitTextView.setText("");
                    rangeTextView.setText("");
                } else {
                    SimpleDateFormat format = new SimpleDateFormat(getString(R.string.simpleDateFormat), Locale.US);
                    startTextView.setText(String.format(getResources().getString(R.string.startedStatus), format.format(start), getConfigResponse.getInterval()));
                    countTextView.setText(String.format(getResources().getString(R.string.countStartStatus), getConfigResponse.getCount()));
                    if (getConfigResponse.memoryIsFull()) {
                        limitTextView.setText(R.string.memoryFullStatus);
                    }
                    else if (getConfigResponse.countIsLimited()) {
                        if (getConfigResponse.countLimitIsReached()) {
                            limitTextView.setText(R.string.limitReachedStatus);
                        } else {
                            limitTextView.setText(R.string.limitNotReachedStatus);
                        }
                    } else {
                        limitTextView.setText(R.string.noLimitStatus);
                    }
					int validMinimum = 5;
                    int validMaximum = 350;
                    String unit = getResources().getString(R.string.celsius);
                    if (validMinimum < validMaximum) {
                        rangeTextView.setText(String.format(getResources().getString(R.string.rangeStatus), 0.1 * validMinimum, unit, 0.1 * validMaximum, unit));
                        if (getConfigResponse.isValid()) {
                            alarmTextView.setText(R.string.alarmNotTrippedStatus);
                        } else {
                            alarmTextView.setText(R.string.alarmTrippedStatus);
                        }
                    } else {
                        rangeTextView.setText(R.string.noRangeStatus);
                        alarmTextView.setText("");
                    }
                }

                Button button = view.findViewById(R.id.exportStatusButton);
                button.setVisibility(getConfigResponse.getCount() > 0 ? View.VISIBLE : View.INVISIBLE);
            } catch (NullPointerException e) {
                // absorb
            }
        }
    }

    public void update(MeasureTemperatureResponse measureTemperatureResponse) {
        View view = this.getView();
        LineData data = chart.getData();
        Double ald_2 = 0.0;


        Log.d("answer", Float.toString(i_0));
        if (view != null) {
            TextView textView;
            try {
                Log.e("part2", "done");
                textView = view.findViewById(R.id.liveMeasurementsStatusSwitch);
                //textView.setText(String.format(getResources().getString(R.string.liveMeasurementStatus), 0.1f * measureTemperatureResponse.getTemperature()));

                String ald = textView.getText().toString();
                Log.d("text_t", Float.toString(0.1f* measureTemperatureResponse.getTemperature()));
                if (selector == Boolean.FALSE){
                    aa = measureTemperatureResponse.getTemperature()*0.1;
                    ald_2 = ((aa)*0.1 +10);

                }else if(selector == Boolean.TRUE){
                    ald_2 = ((aa)*0.001 + 0.37);
                }
                val_high.setText(Double.toString(ald_2));
                ald_3 = Float.parseFloat(Double.toString(ald_2));
            } catch (NullPointerException e) {
                // absorb
            }
        }
        if (data != null){
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), ald_3), 0);
            data.notifyDataChanged();
            Log.d("ald2", Float.toString(ald_3));
            arrayList.add(Float.toString(ald_3));
            //Log.d("msggg", arrayList.get(0));
            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(50);
            // move to the latest entry
            chart.moveViewToX(data.getEntryCount());

            if(selector==Boolean.FALSE){
                for(int i =0; i<arrayList.size(); i++){
                    IOP_text.append(arrayList.get(i));
                    IOP_text.append("\n");
                }
            }

        }

    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);

        set.setDrawValues(false);
        return set;
    }

    public void reset() {
        // void for now
    }

    private void initFields() {
        MainActivity activity = (MainActivity) getActivity();
        update(activity.lastGetConfigResponse);
        update(activity.lastMeasureTemperatureResponse);
    }
}
