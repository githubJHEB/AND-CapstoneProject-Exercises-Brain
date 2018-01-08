package com.google.android.gms.example.exercisesforthebrain;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.example.exercisesforthebrain.data.Contract;


import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.example.exercisesforthebrain.R.id.chart;
import static java.lang.Float.parseFloat;

public class PerformanceAcivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnChartValueSelectedListener {
    private static final int CURSOR_LOADER_ID = 0;
    LoaderManager loadermanager;
    List<String> displayCorrectAnswer = new ArrayList<>();
    private LineChart mChart;
    private TextView detail;
    private String exerciseRequestGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_performance);

        Intent intent = getIntent();
        exerciseRequestGraph = intent.getStringExtra(getResources().getString(R.string.graphicalData));
        detail = findViewById(R.id.datee);
        loadermanager = getLoaderManager();
        loadermanager.initLoader(CURSOR_LOADER_ID, null, this);
        mChart = findViewById(chart);
        mChart.setOnChartValueSelectedListener(this);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.BLACK);
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.invalidate();

        Typeface tf = Typeface.DEFAULT;

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setFormSize(18);
        l.setFormLineWidth(8f);
        l.setTextSize(18);
        l.setTypeface(tf);
        l.setTextColor(Color.WHITE);

        XAxis x1 = mChart.getXAxis();
        x1.setPosition(XAxis.XAxisPosition.BOTTOM);
        x1.setTypeface(tf);
        x1.setTextSize(20);
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);
        x1.setEnabled(false);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setTextSize(20);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    private void addEntry(long X, double Y) {

        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(X, (float) (Y) + 0f), 0);
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(15000000);
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        String labelGraph = getResources().getString(R.string.graphicalNumberOfDay);
        if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalMath))) {
            labelGraph = getResources().getString(R.string.graphicalLabelMath);
        } else if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalColor))) {
            labelGraph = getResources().getString(R.string.graphicalLabelColor);
        } else if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalAlpha))) {
            labelGraph = getResources().getString(R.string.graphicalLabelAlphabet);
        } else if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalImage))) {
            labelGraph = getResources().getString(R.string.graphicalLabelImage);
        }

        LineDataSet set = new LineDataSet(null, labelGraph);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);


        set.setColor(Color.rgb(224, 224, 224));
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(4f);
        set.setCircleRadius(5f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(255, 255, 255));
        set.setValueTextSize(9f);
        set.setDrawValues(true);
        set.setValueTextSize(11f);
        return set;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.Entry.URI,
                null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //data.moveToFirst();
        if (data.moveToFirst()) {
            int columToRead = Contract.Entry.POSITION_MATHEM;

            if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalMath))) {
                columToRead = Contract.Entry.POSITION_MATHEM;
            } else if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalColor))) {
                columToRead = Contract.Entry.POSITION_COLOR;
            } else if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalAlpha))) {
                columToRead = Contract.Entry.POSITION_ALPHABET;
            } else if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalImage))) {
                columToRead = Contract.Entry.POSITION_IMAGENS;
            }

            List<Float> value = new ArrayList<>();
            List<Long> time = new ArrayList<>();
            List<Long> xAxisValue = new ArrayList<>();  // Eje X
            String historyValue = "noHit";
            String dataMathToShow;

            for (int i = 0; i < 30; i++) {
                dataMathToShow = data.getString(columToRead);

                if (dataMathToShow != null) {
                    if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalMath)) || exerciseRequestGraph.equals(getResources().getString(R.string.graphicalColor))) {
                        historyValue = dataMathToShow.substring(dataMathToShow.indexOf(",") + 1);
                        displayCorrectAnswer.add(dataMathToShow.substring(0, dataMathToShow.indexOf(",")));
                    } else if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalAlpha)) || exerciseRequestGraph.equals(getResources().getString(R.string.graphicalImage))) {
                        historyValue = dataMathToShow; //.substring(0);
                    }

                    value.add(parseFloat(historyValue));
                    time.add((long) (i + 1));
                    Long vvv = time.get(i);
                    xAxisValue.add(vvv);

                    if (!data.moveToNext()) { // if exit a next row the move next.
                        i = 30;
                    }

                } else {
                    if (i == 0) {
                        Toast.makeText(this, getResources().getString(R.string.graphicalNoHistata),
                                Toast.LENGTH_SHORT).show();
                    }
                    i = 30;
                }
            }
            mChart.clearValues();


            for (int i = 0; i < value.size(); i++) {
                Long gggg = xAxisValue.get(i);
                Float ggga = value.get(i);
                addEntry(gggg, ggga);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.graphicalNoHistata),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Float yData = e.getY();
        Float xData = h.getX();

        if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalMath)) || exerciseRequestGraph.equals(getResources().getString(R.string.graphicalColor))) {
            String dia = String.valueOf(xData).substring(0, String.valueOf(xData).indexOf("."));
            String time = String.valueOf(yData).substring(0, String.valueOf(yData).indexOf("."));
            String concatenateString = getString(R.string.dayEx) + dia + " " + getString(R.string.time) + time + " " +
                    getString(R.string.correctA) + displayCorrectAnswer.get(Integer.parseInt(dia) - 1) + getString(R.string.day30performance);
            detail.setText(concatenateString);
        } else if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalAlpha))) {
            String dia = String.valueOf(xData).substring(0, String.valueOf(xData).indexOf("."));
            String time = String.valueOf(yData).substring(0, String.valueOf(yData).indexOf("."));
            String concatenateString = getString(R.string.dayEx) + dia + " " +
                    getString(R.string.time) + time;
            detail.setText(concatenateString);
        } else if (exerciseRequestGraph.equals(getResources().getString(R.string.graphicalImage))) {
            String dia = String.valueOf(xData).substring(0, String.valueOf(xData).indexOf("."));
            String correct = String.valueOf(yData).substring(0, String.valueOf(yData).indexOf("."));
            String concatenateString = getString(R.string.dayEx) + dia + " " +
                    getString(R.string.correct) + correct + getString(R.string.quinceperformance);
            detail.setText(concatenateString);
        }

        detail.setTextColor(Color.WHITE);
        detail.setContentDescription(String.valueOf(e.getY()));
    }

    @Override
    public void onNothingSelected() {
        //Log.i("Nothing selected", "Nothing selected.");
    }
}

