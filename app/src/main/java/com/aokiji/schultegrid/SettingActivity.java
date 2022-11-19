package com.aokiji.schultegrid;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aokiji.schultegrid.db.entities.Record;
import com.aokiji.schultegrid.db.entities.Times;
import com.tencent.mmkv.MMKV;

import org.litepal.LitePal;

public class SettingActivity extends AppCompatActivity {

    private static final String KEY_BE_QUIET = "KEY_BE_QUIET";
    private static final String KEY_TOUCH = "KEY_TOUCH";

    private Toolbar toolbar;
    private Switch switchBeQuiet, switchTouch;
    private LinearLayout llCleanData, llViewOnGitHub;

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
        switchTouch = findViewById(R.id.switchTouch);
        switchTouch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    MMKV.defaultMMKV().encode(KEY_TOUCH, "1");
                } else
                {
                    MMKV.defaultMMKV().encode(KEY_TOUCH, "0");
                }
            }
        });
        llCleanData = findViewById(R.id.llCleanData);
        llCleanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cleanData();
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
        String touchStr = MMKV.defaultMMKV().decodeString(KEY_TOUCH, "1");
        if ("1".equals(touchStr))
        {
            switchTouch.setChecked(true);
        } else
        {
            switchTouch.setChecked(false);
        }
    }


    private void cleanData()
    {
        new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("确定删除所有记录吗?删除后不可恢复!")
                .setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        LitePal.deleteAll(Record.class);
                        LitePal.deleteAll(Times.class);
                    }
                })
                .setCancelable(false)
                .show();
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