package com.aokiji.schultegrid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aokiji.schultegrid.db.entities.Record;
import com.aokiji.schultegrid.ui.adapter.ButtonAdapter;
import com.aokiji.schultegrid.ui.widget.Toast;
import com.aokiji.schultegrid.utils.DateUtil;
import com.aokiji.schultegrid.utils.ScreenUtil;
import com.aokiji.schultegrid.utils.SystemUtil;
import com.bumptech.glide.Glide;

import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private static final String TAG = "MainActivity";

    private Chronometer tvTimer;
    private RecyclerView rvPanel;
    private ImageView ivMist, ivChart, ivStart;

    private List<Integer> mList = new ArrayList<>();
    private ButtonAdapter mAdapter;

    private int mSmallGoal = 1;
    private int mFinalGoal = 25;

    private long mStartTimeMills, mEndTimeMills;

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
        rvPanel = findViewById(R.id.rv_panel);
        ivMist = findViewById(R.id.iv_mist);
        ivChart = findViewById(R.id.iv_chart);
        ivStart = findViewById(R.id.iv_start);

        initRecyclerView();

        ivChart.setOnClickListener(this);
        ivStart.setOnClickListener(this);
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
        mAdapter.setOnItemClickListener(new ButtonAdapter.OnItemClickListener()
        {
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
                        record.setCreateTime(DateUtil.convertDateToString(System.currentTimeMillis()));
                        record.setCreator(SystemUtil.getDeviceBrand());
                        record.save();
                        // UI
                        tvTimer.stop();
                        rvPanel.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ivMist.setVisibility(View.VISIBLE);
                                initData();
                            }
                        }, 100);
                    }
                    else
                    {
                        mSmallGoal = mSmallGoal + 1;
                    }
                }
                else
                {
                    shock();
                    Toast.e(MainActivity.this, "喝多了?");
                }
            }
        });
        rvPanel.setAdapter(mAdapter);
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


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_chart:
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_start:
                // UI
                ivMist.setVisibility(View.GONE);
                tvTimer.setBase(SystemClock.elapsedRealtime());
                tvTimer.start();
                // 记录开始时间
                mStartTimeMills = System.currentTimeMillis();
                // 重置
                mSmallGoal = 1;
                break;
            default:
                break;
        }
    }


    private void shock()
    {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(250);
    }

}