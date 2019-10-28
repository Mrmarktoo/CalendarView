/*
 * Copyright (C) 2016 huanghaibin_dev <huanghaibin_dev@163.com>
 * WebSite https://github.com/MiracleTimes-Dev
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haibin.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * 月视图基础控件,可自由继承实现
 * 可通过此扩展各种视图如：MonthView、RangeMonthView、MultiMonthView
 *
 * @author haibin
 */
public abstract class BaseMonthView extends BaseView {

    /**
     * 月份切换viewpager
     */
    protected MonthViewPager mMonthViewPager;

    /**
     * 当前日历卡年份
     */
    protected int mCurrentYear;

    /**
     * 当前日历卡月份
     */
    protected int mCurrentMonth;

    /**
     * 日历的行数
     */
    protected int mCurrentLineCount;

    /**
     * 日历高度
     */
    protected int mCurrentCalendarHeight;


    /**
     * 下个月偏移的数量
     */
    protected int mNextMonthDiff;

    public BaseMonthView(Context context) {
        super(context);
    }

    /**
     * 初始化日期
     *
     * @param year  year
     * @param month month
     */
    final void initMonthWithDate(int year, int month) {
        mCurrentYear = year;
        mCurrentMonth = month;
        initCalendar();
        mCurrentCalendarHeight = CalendarUtil.getMonthViewHeight(year, month, mItemHeight, mDelegate.getWeekStart(),
                mDelegate.getMonthViewShowMode());

    }

    /**
     * 初始化日历
     */
    @SuppressLint("WrongConstant")
    private void initCalendar() {

        mNextMonthDiff = CalendarUtil.getMonthEndDiff(mCurrentYear, mCurrentMonth, mDelegate.getWeekStart());
        int preDiff = CalendarUtil.getMonthViewStartDiff(mCurrentYear, mCurrentMonth, mDelegate.getWeekStart());
        int monthDayCount = CalendarUtil.getMonthDaysCount(mCurrentYear, mCurrentMonth);

        mDayItems = CalendarUtil.initCalendarForMonthView(mCurrentYear, mCurrentMonth, mDelegate.getCurrentDay(), mDelegate.getWeekStart());

        if (mDayItems.contains(mDelegate.getCurrentDay())) {
            mCurrentItem = mDayItems.indexOf(mDelegate.getCurrentDay());
        } else {
            mCurrentItem = mDayItems.indexOf(mDelegate.mSelectedCalendar);
        }

        if (mCurrentItem > 0 && mDelegate.mCalendarInterceptListener != null
                && mDelegate.mCalendarInterceptListener.onCalendarIntercept(mDelegate.mSelectedCalendar)) {
            mCurrentItem = -1;
        }

        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {
            mCurrentLineCount = 6;
        } else {
            mCurrentLineCount = (preDiff + monthDayCount + mNextMonthDiff) / 7;
        }
        addSchemesFromMap();
        invalidate();
    }

    /**
     * 获取点击选中的日期
     *
     * @return return
     */
    protected Calendar getIndex() {
        if (mItemWidth == 0 || mItemHeight == 0) {
            return null;
        }
        int indexX = (int) (mX - mDelegate.getCalendarPadding()) / mItemWidth;
        if (indexX >= 7) {
            indexX = 6;
        }
        int indexY = (int) mY / mItemHeight;
        int position = indexY * 7 + indexX;// 选择项
        if (position >= 0 && position < mDayItems.size())
            return mDayItems.get(position);
        return null;
    }

    /**
     * 记录已经选择的日期
     *
     * @param calendar calendar
     */
    final void setSelectedCalendar(Calendar calendar) {
        mCurrentItem = mDayItems.indexOf(calendar);
    }


    /**
     * 更新显示模式
     */
    final void updateShowMode() {
        mCurrentLineCount = CalendarUtil.getMonthViewLineCount(mCurrentYear, mCurrentMonth,
                mDelegate.getWeekStart(), mDelegate.getMonthViewShowMode());
        mCurrentCalendarHeight = CalendarUtil.getMonthViewHeight(mCurrentYear, mCurrentMonth, mItemHeight, mDelegate.getWeekStart(),
                mDelegate.getMonthViewShowMode());
        invalidate();
    }

    /**
     * 更新周起始
     */
    final void updateWeekStart() {
        initCalendar();
        mCurrentCalendarHeight = CalendarUtil.getMonthViewHeight(mCurrentYear, mCurrentMonth, mItemHeight, mDelegate.getWeekStart(),
                mDelegate.getMonthViewShowMode());
    }

    @Override
    void updateItemHeight() {
        super.updateItemHeight();
        mCurrentCalendarHeight = CalendarUtil.getMonthViewHeight(mCurrentYear, mCurrentMonth, mItemHeight, mDelegate.getWeekStart(),
                mDelegate.getMonthViewShowMode());
    }


    @Override
    void updateCurrentDate() {
        if (mDayItems == null)
            return;
        if (mDayItems.contains(mDelegate.getCurrentDay())) {
            for (Calendar a : mDayItems) {//添加操作
                a.setCurrentDay(false);
            }
            int index = mDayItems.indexOf(mDelegate.getCurrentDay());
            mDayItems.get(index).setCurrentDay(true);
        }
        invalidate();
    }


    /**
     * 获取选中的下标
     *
     * @param calendar calendar
     * @return 获取选中的下标
     */
    protected final int getSelectedIndex(Calendar calendar) {
        return mDayItems.indexOf(calendar);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCurrentLineCount != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mCurrentCalendarHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 开始绘制前的钩子，这里做一些初始化的操作，每次绘制只调用一次，性能高效
     * 没有需要可忽略不实现
     * 例如：
     * 1、需要绘制圆形标记事件背景，可以在这里计算半径
     * 2、绘制矩形选中效果，也可以在这里计算矩形宽和高
     */
    @Override
    protected void onPreviewHook() {
        // TODO: 2017/11/16
    }


    /**
     * 循环绘制开始的回调，不需要可忽略
     * 绘制每个日历项的循环，用来计算baseLine、圆心坐标等都可以在这里实现
     *
     * @param x 日历Card x起点坐标
     * @param y 日历Card y起点坐标
     */
    protected void onLoopStart(int x, int y) {
        // TODO: 2017/11/16  
    }

    @Override
    protected void onDestroy() {

    }
}
