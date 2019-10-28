package com.haibin.calendarviewproject.mine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarUtil;
import com.haibin.calendarview.WeekView;
import com.haibin.calendarviewproject.R;

/**
 * 下标周视图
 *
 * @author huanghaibin
 * @date 2019/10/17
 */

public class WorkWeekView extends WeekView {
    private Paint mSchemeBasicPaint = new Paint();
    private int mPadding;
    private int mH, mW;
    private Paint mTextPaint = new Paint();

    public WorkWeekView(Context context) {
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
        mPadding = dipToPx(getContext(), 4);
        mH = dipToPx(getContext(), 8);
        mW = dipToPx(getContext(), 8);
    }

    @Override
    protected void onPreviewHook() {

    }

    /**
     * 如果这里和 onDrawScheme 是互斥的，则 return false，
     * return true 会先绘制 onDrawSelected，再绘制onDrawSelected
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x + mPadding, mPadding, x + mItemWidth - mPadding, mItemHeight - mPadding, mSelectedPaint);
        return true;
    }

    private boolean showLunar = false;

    /**
     * 绘制下标标记
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        //        mSchemeBasicPaint.setColor(calendar.getSchemeColor());
        //        canvas.drawRect(x + mItemWidth / 2 - mW / 2,
        //                mItemHeight - mH * 2 - mPadding,
        //                x + mItemWidth / 2 + mW / 2,
        //                mItemHeight - mH - mPadding, mSchemeBasicPaint);

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
                                mPadding + mSchemeBaseLine - textRange / 2,
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
                                mPadding + mSchemeBaseLine - textRange / 2,
                                mTextPaint);
                    } else {
                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                        centerX = x + mItemWidth / 2;
                        canvas.drawText(scheme.getScheme(),
                                centerX, mTextBaseLine + mItemHeight / 10,
                                mSchemeBasicPaint);
                    }
                } else {
                    for (Calendar.Scheme scheme : calendar.getSchemes()) {
                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                        if (scheme.getType() == 0) {
                            centerX = x + mItemWidth / 4;
                            canvas.drawText(scheme.getScheme(),
                                    centerX, mTextBaseLine + mItemHeight / 10,
                                    mSchemeBasicPaint);
                        } else {
                            centerX = x + mItemWidth * 3 / 4;
                            canvas.drawText(scheme.getScheme(),
                                    centerX, mTextBaseLine + mItemHeight / 10,
                                    mSchemeBasicPaint);
                        }
                    }
                }
            }
        }

        //        if (showLunar) {
        //            if (calendar.getSchemes() != null && calendar.getSchemes().size() > 0) {
        //                if (calendar.getSchemes().size() == 1) {
        //                    canvas.drawRect(x + mItemWidth / 2 - mW / 2, top,
        //                            x + mItemWidth / 2 + mW / 2, bottom, mSchemeBasicPaint);
        //                } else {
        //                    for (Calendar.Scheme scheme : calendar.getSchemes()) {
        //                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
        //                        if (scheme.getType() == 0) {
        //                            canvas.drawRect(x + mItemWidth / 4 - mW / 2, top,
        //                                    x + mItemWidth / 4 + mW / 2, bottom, mSchemeBasicPaint);
        //                        } else {
        //                            canvas.drawRect(x + mItemWidth / 4 * 3 - mW / 2, top,
        //                                    x + mItemWidth / 4 * 3 + mW / 2, bottom, mSchemeBasicPaint);
        //                        }
        //                    }
        //                }
        //            } else {
        //                mSchemeBasicPaint.setColor(calendar.getSchemeColor());
        //                canvas.drawRect(x + mItemWidth / 2 - mW / 2, top, x + mItemWidth / 2 + mW / 2, bottom, mSchemeBasicPaint);
        //            }
        //        } else {
        //            int centerX;
        //            mSchemeBasicPaint.setTextSize(mSelectedLunarTextPaint.getTextSize());
        //            if (calendar.getSchemes() != null && calendar.getSchemes().size() > 0) {
        //                if (calendar.getSchemes().size() == 1) {
        //                    mSchemeBasicPaint.setColor(calendar.getSchemes().get(0).getShcemeColor());
        //                    centerX = x + mItemWidth / 2;
        //                    canvas.drawText(calendar.getSchemes().get(0).getScheme(),
        //                            centerX, mTextBaseLine + mItemHeight / 10,
        //                            mSchemeBasicPaint);
        //                } else {
        //                    for (Calendar.Scheme scheme : calendar.getSchemes()) {
        //                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
        //                        if (scheme.getType() == 0) {
        //                            centerX = x + mItemWidth / 4;
        //                            canvas.drawText(scheme.getScheme(),
        //                                    centerX, mTextBaseLine + mItemHeight / 10,
        //                                    mSchemeBasicPaint);
        //
        //                            //                            canvas.drawRect(x + mItemWidth / 4 - mW / 2, top,
        //                            //                                    x + mItemWidth / 4 + mW / 2, bottom, mSchemeBasicPaint);
        //                        } else {
        //                            centerX = x + mItemWidth * 3 / 4;
        //                            canvas.drawText(scheme.getScheme(),
        //                                    centerX, mTextBaseLine + mItemHeight / 10,
        //                                    mSchemeBasicPaint);
        //                            //                            canvas.drawRect(x + mItemWidth / 4 * 3 - mW / 2, top,
        //                            //                                    x + mItemWidth / 4 * 3 + mW / 2, bottom, mSchemeBasicPaint);
        //                        }
        //                    }
        //                }
        //            } else {
        //                mSchemeBasicPaint.setColor(calendar.getSchemeColor());
        //                canvas.drawRect(x + mItemWidth / 2 - mW / 2, top, x + mItemWidth / 2 + mW / 2, bottom, mSchemeBasicPaint);
        //            }
        //        }

        //        if (calendar.getSchemes() != null && calendar.getSchemes().size() > 0) {
        //            if (calendar.getSchemes().size() == 1) {
        //                canvas.drawRect(x + mItemWidth / 2 - mW / 2, top,
        //                        x + mItemWidth / 2 + mW / 2, bottom, mSchemeBasicPaint);
        //            } else {
        //                for (Calendar.Scheme scheme : calendar.getSchemes()) {
        //                    mSchemeBasicPaint.setColor(scheme.getShcemeColor());
        //                    if (scheme.getType() == 0) {
        //                        canvas.drawRect(x + mItemWidth / 4 - mW / 2, top,
        //                                x + mItemWidth / 4 + mW / 2, bottom, mSchemeBasicPaint);
        //                    } else {
        //                        canvas.drawRect(x + mItemWidth / 4 * 3 - mW / 2, top,
        //                                x + mItemWidth / 4 * 3 + mW / 2, bottom, mSchemeBasicPaint);
        //                    }
        //                }
        //            }
        //        } else {
        //            mSchemeBasicPaint.setColor(calendar.getSchemeColor());
        //            canvas.drawRect(x + mItemWidth / 2 - mW / 2, top, x + mItemWidth / 2 + mW / 2, bottom, mSchemeBasicPaint);
        //        }
    }

    private Bitmap scaleBitmap(Bitmap origin, float scale) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        Matrix matrix = new Matrix();
        // 使用后乘
        matrix.postScale(scale, scale);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = -mItemHeight / 6;
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mCurMonthTextPaint);
            if (showLunar) {
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10,
                        calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                                mCurMonthLunarTextPaint);
            }
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mCurMonthTextPaint);
            if (showLunar) {
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10,
                        calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                                mCurMonthLunarTextPaint);
            }
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
