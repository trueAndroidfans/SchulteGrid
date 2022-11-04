package com.aokiji.schultegrid;

import android.app.Application;

import com.tencent.mmkv.MMKV;

import org.litepal.LitePal;

public class App extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        LitePal.initialize(this);
        MMKV.initialize(this);
    }
}
