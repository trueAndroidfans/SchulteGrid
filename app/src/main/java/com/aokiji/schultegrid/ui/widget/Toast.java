package com.aokiji.schultegrid.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.aokiji.schultegrid.R;

public class Toast {

    private Toast()
    {
    }


    public static void e(Context context, String text)
    {
        create(context, R.layout.view_toast_e, text).show();
    }


    private static android.widget.Toast create(Context context, @LayoutRes int resId, String text)
    {
        android.widget.Toast toast = new android.widget.Toast(context);
        View view = LayoutInflater.from(context).inflate(resId, null);
        TextView tvText = view.findViewById(R.id.tv_text);
        tvText.setText(text);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 15);

        return toast;
    }

}
