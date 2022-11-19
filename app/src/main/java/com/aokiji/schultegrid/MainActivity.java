package com.aokiji.schultegrid;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aokiji.schultegrid.db.entities.Record;
import com.aokiji.schultegrid.db.entities.Times;
import com.aokiji.schultegrid.ui.adapter.ButtonAdapter;
import com.aokiji.schultegrid.ui.widget.ViewAnimator;
import com.aokiji.schultegrid.utils.DateUtil;
import com.aokiji.schultegrid.utils.ScreenUtil;
import com.aokiji.schultegrid.utils.SystemUtil;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tencent.mmkv.MMKV;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_BE_QUIET = "KEY_BE_QUIET";
    private static final String KEY_TOUCH = "KEY_TOUCH";

    private Chronometer tvTimer;
    private FrameLayout flPanel;
    private RecyclerView rvPanel;
    private ImageView ivMist;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fabStart;

    private List<Integer> mList = new ArrayList<>();
    private ButtonAdapter mAdapter;
    private int mSmallGoal = 1;
    private int mFinalGoal = 25;
    private long mStartTimeMills, mEndTimeMills;
    private AudioAttributes mAudioAttributes;
    private SoundPool mSoundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView()
    {
        tvTimer = findViewById(R.id.tv_timer);
        flPanel = findViewById(R.id.flPanel);
        rvPanel = findViewById(R.id.rv_panel);
        ivMist = findViewById(R.id.iv_mist);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        fabStart = findViewById(R.id.fabStart);
        initRecyclerView();
        initBottomAppBar();
        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                start();
            }
        });
        Glide.with(this).load(R.drawable.ic_mist).into(ivMist);
    }


    private void initRecyclerView()
    {
        int recyclerViewHeight = ScreenUtil.getScreenWidth(this) - ScreenUtil.dp2px(this, 20);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rvPanel.getLayoutParams();
        layoutParams.height = recyclerViewHeight;
        rvPanel.requestLayout();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        rvPanel.setLayoutManager(layoutManager);
        mAdapter = new ButtonAdapter(this, mList);
        mAdapter.setOnItemClickListener(new ButtonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position)
            {
                if (mList.get(position) == mSmallGoal)
                {
                    if (mList.get(position) == mFinalGoal)
                    {
                        // 计算耗时
                        mEndTimeMills = System.currentTimeMillis();
                        BigDecimal endTime = new BigDecimal(mEndTimeMills);
                        BigDecimal startTime = new BigDecimal(mStartTimeMills);
                        String timeConsuming = endTime.subtract(startTime).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP).toString();
                        // 保存记录
                        Record record = new Record();
                        record.setTimeConsuming(timeConsuming);
                        record.setCreateTime(System.currentTimeMillis());
                        record.setCreator(SystemUtil.getDeviceBrand());
                        record.save();
                        Times times = new Times();
                        times.setCount(1);
                        times.setCompletionTime(DateUtil.date2String(System.currentTimeMillis()));
                        times.setCreateTime(System.currentTimeMillis());
                        times.setCreator(SystemUtil.getDeviceBrand());
                        times.save();
                        // UI
                        if (!isQuietMode())
                        {
                            alert(R.raw.a5);
                        }
                        tvTimer.stop();
                        rvPanel.postDelayed(new Runnable() {
                            @Override
                            public void run()
                            {
                                ivMist.setVisibility(View.VISIBLE);
                                initData();
                            }
                        }, 100);
                    } else
                    {
                        mSmallGoal = mSmallGoal + 1;
                    }
                    if (isTouchMode())
                    {
                        shock(50);
                    }
                } else
                {
                    if (!isQuietMode())
                    {
                        alert(R.raw.a4);
                        shock(500);
                    }
                    ViewAnimator.wobble(flPanel);
                }
            }
        });
        rvPanel.setAdapter(mAdapter);
    }


    private void initBottomAppBar()
    {
        bottomAppBar.replaceMenu(R.menu.menu_main);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_statistics:
                        Intent statistics = new Intent(MainActivity.this, ChartActivity.class);
                        startActivity(statistics);
                        return true;
                    case R.id.action_setting:
                        Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(setting);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    private void initData()
    {
        mList.clear();
        Random random = new Random();
        while (mList.size() < 25)
        {
            int value = random.nextInt(25) + 1;
            if (!mList.contains(value))
            {
                mList.add(value);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    private void start()
    {
        // UI
        if (!isQuietMode())
        {
            alert(R.raw.a3);
        }
        ivMist.setVisibility(View.GONE);
        tvTimer.setBase(SystemClock.elapsedRealtime());
        tvTimer.start();
        // 记录开始时间
        mStartTimeMills = System.currentTimeMillis();
        // 重置
        mSmallGoal = 1;
    }


    private void shock(long milliseconds)
    {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }


    private void alert(int resId)
    {
        mAudioAttributes = new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build();
        mSoundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(mAudioAttributes).build();
        final int voiceId = mSoundPool.load(this, resId, 1);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
            {
                mSoundPool.play(voiceId, 1, 1, 1, 0, 1);
            }
        });
    }


    private boolean isQuietMode()
    {
        String beQuietStr = MMKV.defaultMMKV().decodeString(KEY_BE_QUIET, "0");
        return "1".equals(beQuietStr) ? true : false;
    }


    private boolean isTouchMode()
    {
        String touchStr = MMKV.defaultMMKV().decodeString(KEY_TOUCH, "1");
        return "1".equals(touchStr) ? true : false;
    }

}