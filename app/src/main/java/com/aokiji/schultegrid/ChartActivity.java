package com.aokiji.schultegrid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.aokiji.schultegrid.db.entities.Record;
import com.aokiji.schultegrid.ui.widget.MyMarkerView;
import com.aokiji.schultegrid.ui.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private Toolbar toolbar;
    private LineChart chart;

    private Typeface tfRegular;

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
    }


    private void initTypeface()
    {
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
    }


    private void setChartStyle()
    {
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
        XAxis xAxis;
        {
            xAxis = chart.getXAxis();
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }
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
        List<Record> records = LitePal.findAll(Record.class);
        setData(records);

        chart.animateX(1500);

        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
    }


    private void setData(List<Record> records)
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
        }
        else
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
            }
            else
            {
                set1.setFillColor(Color.BLACK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            chart.setData(data);
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