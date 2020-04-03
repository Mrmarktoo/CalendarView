package com.haibin.calendarviewproject.index;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarviewproject.Article;
import com.haibin.calendarviewproject.ArticleAdapter;
import com.haibin.calendarviewproject.R;
import com.haibin.calendarviewproject.base.activity.BaseActivity;
import com.haibin.calendarviewproject.colorful.ColorfulActivity;
import com.haibin.calendarviewproject.group.GroupItemDecoration;
import com.haibin.calendarviewproject.group.GroupRecyclerView;
import com.haibin.calendarviewproject.meizu.MeiZuActivity;
import com.haibin.calendarviewproject.simple.SimpleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnCalendarLongClickListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener, CalendarView.OnMonthChangeListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;

    public static void show(Context context) {
        context.startActivity(new Intent(context, IndexActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_index;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextCurrentDay = findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarLongClickListener(this, false);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    protected void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        initCalendarData(year, month);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(this));
        mRecyclerView.notifyDataSetChanged();
    }

    private void initCalendarData(int year, int month) {

        //        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "白").toString(),
        //                getSchemeCalendar(year, month, 3, 0xFF40db25, "白"));
        //        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "夜").toString(),
        //                getSchemeCalendar(year, month, 6, 0xFFe69138, "夜"));
        //        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
        //                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        //        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
        //                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        //        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
        //                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        //        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
        //                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        //        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
        //                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        //        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
        //                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        //        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
        //                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        Map<String, Calendar> map = new HashMap<>();
        for (int i = 1; i < 30; i++) {
            Calendar calendar = getSchemeCalendar(year, month, i);
            map.put(calendar.toString(), calendar);
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_flyme:
                MeiZuActivity.show(this);
                break;
            case R.id.ll_simple:
                SimpleActivity.show(this);
                break;
            case R.id.ll_colorful:
                ColorfulActivity.show(this);
                break;
            case R.id.ll_index:
                IndexActivity.show(this);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        List<Calendar.Scheme> schemes = new ArrayList<>();
        if (day % 3 == 0) {
            schemes.add(new Calendar.Scheme(0, 0xFF3399ff, "白"));
            schemes.add(new Calendar.Scheme(1, 0xFF2F4F4F, "夜"));
        } else {
            if (day % 2 == 0) {
                schemes.add(new Calendar.Scheme(0, 0xFF3399ff, "白"));
            } else {
                schemes.add(new Calendar.Scheme(1, 0xFF2F4F4F, "夜"));
            }
            //如果单独标记颜色、则会使用这个颜色
            //calendar.setSchemeColor(color);
        }
        calendar.setSchemes(schemes);
        //        calendar.setScheme(text);
        return calendar;
    }

    private Calendar getSchemeCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        List<Calendar.Scheme> schemes = new ArrayList<>();
        if (day % 4 == 0) {
            schemes.add(new Calendar.Scheme(2, 0xFF40db25, "假"));
        } else if (day % 3 == 0) {
            schemes.add(new Calendar.Scheme(0, 0xFF3399ff, "白"));
            schemes.add(new Calendar.Scheme(1, 0xFF2F4F4F, "夜"));
        } else if (day % 2 == 0) {
            schemes.add(new Calendar.Scheme(0, 0xFF3399ff, "白"));
        } else {
            schemes.add(new Calendar.Scheme(1, 0xFF2F4F4F, "夜"));
        }
        calendar.setSchemes(schemes);
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarLongClick(Calendar calendar) {
        Log.e("onDateLongClick", "  -- " + calendar.getDay() + "  --  " + calendar.getMonth());
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


    @Override
    public void onMonthChange(int year, int month) {
        initCalendarData(year, month);
    }
}
