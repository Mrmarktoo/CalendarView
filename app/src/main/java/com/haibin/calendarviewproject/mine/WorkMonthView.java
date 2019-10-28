package com.haibin.calendarviewproject.mine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarUtil;
import com.haibin.calendarview.MonthView;

/**
 * 下标标记的日历控件
 *
 * @author huanghaibin
 * @date 2018/10/17
 */

public class WorkMonthView extends MonthView {
    private Paint mSchemeBasicPaint = new Paint();
    private int mPadding;
    private int mH, mW;
    private Paint mTextPaint = new Paint();

    public WorkMonthView(Context context) {
        super(context);
        mTextPaint.setTextSize(CalendarUtil.dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setColor(0xff333333);
        mSchemeBasicPaint.setFakeBoldText(true);
        mPadding = CalendarUtil.dipToPx(getContext(), 4);
        mH = CalendarUtil.dipToPx(getContext(), 8);
        mW = CalendarUtil.dipToPx(getContext(), 8);
    }


    /**
     * onDrawSelected
     * 绘制选中状态的日期
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        // TODO: 2019/10/18 选中日期的背景绘制
        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSelectedPaint);
        return true;
    }

    private boolean showLunar = false;

    /**
     * onDrawScheme
     * 绘制标记
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        // TODO: 2019/10/18 绘制标记
        int top = mItemHeight - mH * 2 - mPadding;
        int bottom = mItemHeight - mH - mPadding;
        if (showLunar) {
            if (calendar.getSchemes() != null && calendar.getSchemes().size() > 0) {
                if (calendar.getSchemes().size() == 1) {
                    Calendar.Scheme scheme = calendar.getSchemes().get(0);
                    if (scheme.getType() == 2) {
                        mTextPaint.setColor(scheme.getShcemeColor());
                        int textRange = CalendarUtil.dipToPx(getContext(), 8);
                        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
                        float mSchemeBaseLine = textRange - metrics.descent + (metrics.bottom - metrics.top) / 2
                                + CalendarUtil.dipToPx(getContext(), 1);
                        canvas.drawText(calendar.getScheme(),
                                x + mItemWidth - mPadding - textRange,
                                y + mPadding + mSchemeBaseLine - textRange / 2,
                                mTextPaint);
                    } else {
                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                        canvas.drawRect(x + mItemWidth / 2 - mW / 2, top,
                                x + mItemWidth / 2 + mW / 2, bottom, mSchemeBasicPaint);
                    }
                } else {
                    for (Calendar.Scheme scheme : calendar.getSchemes()) {
                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                        if (scheme.getType() == 0) {
                            canvas.drawRect(x + mItemWidth / 4 - mW / 2, top,
                                    x + mItemWidth / 4 + mW / 2, bottom, mSchemeBasicPaint);
                        } else {
                            canvas.drawRect(x + mItemWidth / 4 * 3 - mW / 2, top,
                                    x + mItemWidth / 4 * 3 + mW / 2, bottom, mSchemeBasicPaint);
                        }
                    }
                }
            }
        } else {
            int centerX;
            mSchemeBasicPaint.setTextSize(mSelectedLunarTextPaint.getTextSize());
            if (calendar.getSchemes() != null && calendar.getSchemes().size() > 0) {
                if (calendar.getSchemes().size() == 1) {
                    Calendar.Scheme scheme = calendar.getSchemes().get(0);
                    if (scheme.getType() == 2) {
                        mTextPaint.setColor(scheme.getShcemeColor());
                        int textRange = CalendarUtil.dipToPx(getContext(), 8);
                        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
                        float mSchemeBaseLine = textRange - metrics.descent + (metrics.bottom - metrics.top) / 2
                                + CalendarUtil.dipToPx(getContext(), 1);
                        canvas.drawText(calendar.getScheme(),
                                x + mItemWidth - mPadding - textRange,
                                y + mPadding + mSchemeBaseLine - textRange / 2,
                                mTextPaint);
                    } else {
                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                        centerX = x + mItemWidth / 2;
                        canvas.drawText(scheme.getScheme(),
                                centerX, mTextBaseLine + y + mItemHeight / 10,
                                mSchemeBasicPaint);
                    }

                } else {
                    for (Calendar.Scheme scheme : calendar.getSchemes()) {
                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                        if (scheme.getType() == 0) {
                            centerX = x + mItemWidth / 4;
                            canvas.drawText(scheme.getScheme(),
                                    centerX, mTextBaseLine + y + mItemHeight / 10,
                                    mSchemeBasicPaint);
                        } else {
                            centerX = x + mItemWidth * 3 / 4;
                            canvas.drawText(scheme.getScheme(),
                                    centerX, mTextBaseLine + y + mItemHeight / 10,
                                    mSchemeBasicPaint);
                        }
                    }
                }
            }
        }

        //        if (calendar.getSchemes() != null && calendar.getSchemes().size() > 0) {
        //            int cx = x + mItemWidth / 2;
        //            if (calendar.getSchemes().size() == 1) {
        //                canvas.drawRect(x + mItemWidth / 2 - mW / 2, y + top,
        //                        x + mItemWidth / 2 + mW / 2, y + bottom, mSchemeBasicPaint);
        //            } else {
        //                for (Calendar.Scheme scheme : calendar.getSchemes()) {
        //                    mSchemeBasicPaint.setColor(scheme.getShcemeColor());
        //                    if (scheme.getType() == 0) {
        //                        canvas.drawRect(x + mItemWidth / 4 - mW / 2, y + top,
        //                                x + mItemWidth / 4 + mW / 2, y + bottom, mSchemeBasicPaint);
        //                    } else {
        //                        canvas.drawRect(x + mItemWidth / 4 * 3 - mW / 2, y + top,
        //                                x + mItemWidth / 4 * 3 + mW / 2, y + bottom, mSchemeBasicPaint);
        //                        //                    canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
        //                        //                            calendar.isCurrentDay() ? mCurDayLunarTextPaint :
        //                        //                                    mCurMonthLunarTextPaint);
        //                    }
        //                }
        //            }
        //        } else {
        //            mSchemeBasicPaint.setColor(calendar.getSchemeColor());
        //            canvas.drawRect(x + mItemWidth / 2 - mW / 2, y + top,
        //                    x + mItemWidth / 2 + mW / 2, y + bottom, mSchemeBasicPaint);
        //        }
    }

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
            if (showLunar) {
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                        calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                                mCurMonthLunarTextPaint);
            }
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            if (showLunar) {
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mCurMonthLunarTextPaint);
            }
        }
    }

}
