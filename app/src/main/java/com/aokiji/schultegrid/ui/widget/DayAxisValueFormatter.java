package com.aokiji.schultegrid.ui.widget;

import com.aokiji.schultegrid.db.entities.Times;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter {

    private final BarLineChartBase<?> chart;
    private List<Times> times;

    public DayAxisValueFormatter(BarLineChartBase<?> chart, List<Times> times)
    {
        this.chart = chart;
        this.times = times;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis)
    {
        String result = "";
        if (!times.isEmpty())
        {
            String completionTime = times.get((int) value).getCompletionTime();
            if (completionTime.indexOf("-") != -1)
            {
                completionTime = completionTime.substring(completionTime.indexOf("-") + 1);
            }
            result = completionTime;
        }
        return result;
    }
}
