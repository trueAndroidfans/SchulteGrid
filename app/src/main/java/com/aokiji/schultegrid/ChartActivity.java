package com.aokiji.schultegrid;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.aokiji.schultegrid.db.entities.Record;
import com.aokiji.schultegrid.db.entities.Times;
import com.aokiji.schultegrid.ui.widget.DayAxisValueFormatter;
import com.aokiji.schultegrid.ui.widget.MyAxisValueFormatter;
import com.aokiji.schultegrid.ui.widget.MyMarkerView;
import com.aokiji.schultegrid.ui.widget.XYMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Fill;
import com.github.mikephil.charting.utils.Utils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private Toolbar toolbar;
    private LineChart chart;
    private BarChart barChart;
    private ImageView ivMist;

    private Typeface tfRegular;
    protected Typeface tfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initView();
        initTypeface();
        setChartStyle();
    }


    private void initView()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_chart));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chart = findViewById(R.id.linechart);
        barChart = findViewById(R.id.barChart);
        ivMist = findViewById(R.id.ivMist);
    }


    private void initTypeface()
    {
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    }


    private void setChartStyle()
    {
        // LineChart
        {
            chart.setBackgroundColor(Color.WHITE);
            chart.getDescription().setEnabled(false);
            chart.setTouchEnabled(true);
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);
            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
            mv.setChartView(chart);
            chart.setMarker(mv);
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            chart.setPinchZoom(true);
        }
        chart.getXAxis().setEnabled(false);
        YAxis yAxis;
        {
            yAxis = chart.getAxisLeft();
            chart.getAxisRight().setEnabled(false);
            yAxis.setDrawAxisLine(false);
            yAxis.setDrawGridLines(false);
            yAxis.enableGridDashedLine(10f, 10f, 0f);
            yAxis.setAxisMaximum(2 * 60f);
            yAxis.setAxisMinimum(0f);
        }
        List<Record> records = LitePal.order("createTime desc").limit(6).find(Record.class);
        if (!records.isEmpty())
        {
            Collections.reverse(records);
            // UI
            ivMist.setVisibility(View.GONE);
        } else
        {
            ivMist.setVisibility(View.VISIBLE);
        }
        setLineChartData(records);
        chart.animateXY(1000, 1000);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);

        // BarChart
        List<Times> times = new ArrayList<>();
        Cursor cursor = LitePal.findBySQL("SELECT SUM(count),completiontime,createtime,creator FROM Times GROUP BY completiontime ORDER BY createtime DESC LIMIT 7");
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                int count = cursor.getInt(cursor.getColumnIndex("SUM(count)"));
                String completionTime = cursor.getString(cursor.getColumnIndex("completiontime"));
                long createTime = cursor.getLong(cursor.getColumnIndex("createtime"));
                String creator = cursor.getString(cursor.getColumnIndex("creator"));
                Times item = new Times();
                item.setCount(count);
                item.setCompletionTime(completionTime);
                item.setCreateTime(createTime);
                item.setCreator(creator);
                times.add(item);
            } while (cursor.moveToNext());
        }
        if (!times.isEmpty())
        {
            Collections.reverse(times);
        }
        barChart.setOnChartValueSelectedListener(this);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart, times);
        XAxis xAxisBar = barChart.getXAxis();
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setTypeface(tfLight);
        xAxisBar.setDrawGridLines(false);
        xAxisBar.setGranularity(1);
        xAxisBar.setLabelCount(7);
        xAxisBar.setValueFormatter(xAxisFormatter);
        IAxisValueFormatter custom = new MyAxisValueFormatter();
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setGranularity(1);
        leftAxis.setLabelCount(7, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        Legend lBar = barChart.getLegend();
        lBar.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lBar.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        lBar.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        lBar.setDrawInside(false);
        lBar.setForm(Legend.LegendForm.SQUARE);
        lBar.setFormSize(9f);
        lBar.setTextSize(11f);
        lBar.setXEntrySpace(4f);
        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(barChart);
        barChart.setMarker(mv);
        setBarChartData(times);
        barChart.animateXY(1000, 1000);
    }


    private void setLineChartData(List<Record> records)
    {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < records.size(); i++)
        {
            float val = Float.valueOf(records.get(i).getTimeConsuming());
            values.add(new Entry(i, val, ContextCompat.getDrawable(this, R.drawable.star)));
        }
        LineDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0)
        {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else
        {
            set1 = new LineDataSet(values, "耗时");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setValueTextSize(9f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider)
                {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });
            if (Utils.getSDKInt() >= 18)
            {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else
            {
                set1.setFillColor(Color.BLACK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            chart.setData(data);
        }
    }


    private void setBarChartData(List<Times> times)
    {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < times.size(); i++)
        {
            values.add(new BarEntry(i, times.get(i).getCount(), getResources().getDrawable(R.drawable.star)));
        }
        BarDataSet set1;
        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0)
        {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else
        {
            set1 = new BarDataSet(values, "完成次数");
            set1.setDrawIcons(false);
            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);
            List<Fill> gradientFills = new ArrayList<>();
            gradientFills.add(new Fill(startColor1, endColor1));
            gradientFills.add(new Fill(startColor2, endColor2));
            gradientFills.add(new Fill(startColor3, endColor3));
            gradientFills.add(new Fill(startColor4, endColor4));
            gradientFills.add(new Fill(startColor5, endColor5));
            set1.setFills(gradientFills);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);
            barChart.setData(data);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (android.R.id.home == item.getItemId())
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onValueSelected(Entry e, Highlight h)
    {

    }

    @Override
    public void onNothingSelected()
    {

    }
}