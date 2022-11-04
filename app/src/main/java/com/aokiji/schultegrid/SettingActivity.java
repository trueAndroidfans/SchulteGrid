package com.aokiji.schultegrid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tencent.mmkv.MMKV;

public class SettingActivity extends AppCompatActivity {

    private static final String KEY_BE_QUIET = "KEY_BE_QUIET";

    private Toolbar toolbar;
    private Switch switchBeQuiet;
    private LinearLayout llViewOnGitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }


    private void initView()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_setting));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        switchBeQuiet = findViewById(R.id.switchBeQuiet);
        switchBeQuiet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    MMKV.defaultMMKV().encode(KEY_BE_QUIET, "1");
                } else
                {
                    MMKV.defaultMMKV().encode(KEY_BE_QUIET, "0");
                }
            }
        });
        llViewOnGitHub = findViewById(R.id.llViewOnGitHub);
        llViewOnGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/trueAndroidfans/SchulteGrid"));
                startActivity(intent);
            }
        });
    }


    private void initData()
    {
        String beQuietStr = MMKV.defaultMMKV().decodeString(KEY_BE_QUIET, "0");
        if ("1".equals(beQuietStr))
        {
            switchBeQuiet.setChecked(true);
        } else
        {
            switchBeQuiet.setChecked(false);
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

}