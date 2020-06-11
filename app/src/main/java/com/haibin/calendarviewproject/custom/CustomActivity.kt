package com.haibin.calendarviewproject.custom

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION_CODES.M
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi

import androidx.recyclerview.widget.LinearLayoutManager

import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarLayout
import com.haibin.calendarview.CalendarView
import com.haibin.calendarviewproject.Article
import com.haibin.calendarviewproject.ArticleAdapter
import com.haibin.calendarviewproject.R
import com.haibin.calendarviewproject.base.activity.BaseActivity
import com.haibin.calendarviewproject.colorful.ColorfulActivity
import com.haibin.calendarviewproject.group.GroupItemDecoration
import com.haibin.calendarviewproject.group.GroupRecyclerView
import com.haibin.calendarviewproject.index.IndexActivity
import com.haibin.calendarviewproject.simple.SimpleActivity
import kotlinx.android.synthetic.main.activity_custom.*

import java.util.HashMap

class CustomActivity : BaseActivity(), CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, View.OnClickListener {

    lateinit var mTextMonthDay: TextView

    lateinit var mTextYear: TextView

    lateinit var mTextLunar: TextView

    lateinit var mTextCurrentDay: TextView

    lateinit var mCalendarView: CalendarView

    lateinit var mRelativeTool: RelativeLayout
    private var mYear: Int = 0
    lateinit var mCalendarLayout: CalendarLayout

    private var showCalendar = true

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView2(webView: WebView) {
        webView.setBackgroundResource(android.R.color.black)
        webView.settings.let {
            it.javaScriptEnabled = true
            it.javaScriptCanOpenWindowsAutomatically = true
            it.displayZoomControls = false
            //自适应屏幕开关
            it.useWideViewPort = true
            it.loadWithOverviewMode = true
            it.setSupportMultipleWindows(true)
            it.defaultTextEncodingName = "utf-8"
            it.setSupportZoom(false)
            it.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                it.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }
        }

        webView.webViewClient = object : WebViewClient() {

            @RequiresApi(LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (request != null) {
                    webView.loadUrl(request.url.toString(), request.requestHeaders)
                }
//                return super.shouldOverrideUrlLoading(view, request)
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                return super.shouldOverrideUrlLoading(view, url)
                if (!url.isNullOrBlank()) {
                    webView.loadUrl(url)
                }
                return true
            }

            @TargetApi(M)
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
            }

            @TargetApi(LOLLIPOP)
            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//                super.onReceivedSslError(view, handler, error)
                handler?.proceed()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_custom
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setStatusBarDarkMode()
        mTextMonthDay = findViewById(R.id.tv_month_day)
        mTextYear = findViewById(R.id.tv_year)
        mTextLunar = findViewById(R.id.tv_lunar)
        mRelativeTool = findViewById(R.id.rl_tool)
        mCalendarView = findViewById(R.id.calendarView)
        mTextCurrentDay = findViewById(R.id.tv_current_day)
        mTextMonthDay.setOnClickListener(View.OnClickListener {
            if (!mCalendarLayout.isExpand) {
                mCalendarLayout.expand()
                return@OnClickListener
            }
            mCalendarView.showYearSelectLayout(mYear)
            mTextLunar.visibility = View.GONE
            mTextYear.visibility = View.GONE
            mTextMonthDay.text = mYear.toString()
        })
        findViewById<View>(R.id.fl_current).setOnClickListener {
            //                mCalendarView.scrollToCurrent();

            if (showCalendar) {
                showCalendar = false
                mCalendarLayout.hideCalendarView2()
            } else {
                showCalendar = true
                mCalendarLayout.showCalendarView2()
                mCalendarLayout.shrink()
            }

//            if (showCalendar) {
//                showCalendar = false
//                mCalendarLayout.hideCalendarView()
//            } else {
//                showCalendar = true
//                mCalendarLayout.showCalendarView()
//                mCalendarLayout.shrink()
//            }

            //mCalendarView.addSchemeDate(getSchemeCalendar(2019, 6, 1, 0xFF40db25, "假"));
            //                int year = 2019;
            //                int month = 6;
            //                Map<String, Calendar> map = new HashMap<>();
            //                map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
            //                        getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
            //                map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
            //                        getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
            //                map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
            //                        getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
            //                map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
            //                        getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
            //                mCalendarView.addSchemeDate(map);
        }
        mCalendarLayout = findViewById(R.id.calendarLayout)
        mCalendarView.setOnCalendarSelectListener(this)
        mCalendarView.setOnYearChangeListener(this)
        mTextYear.text = mCalendarView.curYear.toString()
        mYear = mCalendarView.curYear
        mTextMonthDay.text = mCalendarView.curMonth.toString() + "月" + mCalendarView.curDay + "日"
        mTextLunar.text = "今日"
        mTextCurrentDay.text = mCalendarView.curDay.toString()
//        mCalendarLayout.hideCalendarView()
        mCalendarLayout.hideCalendarView2()
        showCalendar = false

        initWebView2(content_view)
        content_view.loadUrl("http://www.baidu.com")
    }

    override fun initData() {
        val year = mCalendarView.curYear
        val month = mCalendarView.curMonth

        val map = HashMap<String, Calendar>()
        map[getSchemeCalendar(year, month, 3, -0xbf24db, "假").toString()] = getSchemeCalendar(year, month, 3, -0xbf24db, "假")
        map[getSchemeCalendar(year, month, 6, -0x196ec8, "事").toString()] = getSchemeCalendar(year, month, 6, -0x196ec8, "事")
        map[getSchemeCalendar(year, month, 9, -0x20ecaa, "议").toString()] = getSchemeCalendar(year, month, 9, -0x20ecaa, "议")
        map[getSchemeCalendar(year, month, 13, -0x123a93, "记").toString()] = getSchemeCalendar(year, month, 13, -0x123a93, "记")
        map[getSchemeCalendar(year, month, 14, -0x123a93, "记").toString()] = getSchemeCalendar(year, month, 14, -0x123a93, "记")
        map[getSchemeCalendar(year, month, 15, -0x5533bc, "假").toString()] = getSchemeCalendar(year, month, 15, -0x5533bc, "假")
        map[getSchemeCalendar(year, month, 18, -0x43ec10, "记").toString()] = getSchemeCalendar(year, month, 18, -0x43ec10, "记")
        map[getSchemeCalendar(year, month, 25, -0xec5310, "假").toString()] = getSchemeCalendar(year, month, 25, -0xec5310, "假")
        map[getSchemeCalendar(year, month, 27, -0xec5310, "多").toString()] = getSchemeCalendar(year, month, 27, -0xec5310, "多")
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map)


        //        mRecyclerView = findViewById(R.id.recyclerView);
        //        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        //        mRecyclerView.setAdapter(new ArticleAdapter(this));
        //        mRecyclerView.notifyDataSetChanged();
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_flyme -> CustomActivity.show(this)
            R.id.ll_simple -> SimpleActivity.show(this)
            R.id.ll_colorful -> ColorfulActivity.show(this)
            R.id.ll_index -> IndexActivity.show(this)
        }
    }

    private fun getSchemeCalendar(year: Int, month: Int, day: Int, color: Int, text: String): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color//如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        calendar.addScheme(Calendar.Scheme())
        calendar.addScheme(-0xff7800, "假")
        calendar.addScheme(-0xff7800, "节")
        return calendar
    }

    override fun onCalendarOutOfRange(calendar: Calendar) {

    }

    @SuppressLint("SetTextI18n")
    override fun onCalendarSelect(calendar: Calendar, isClick: Boolean) {
        mTextLunar.visibility = View.VISIBLE
        mTextYear.visibility = View.VISIBLE
        mTextMonthDay.text = calendar.month.toString() + "月" + calendar.day + "日"
        mTextYear.text = calendar.year.toString()
        mTextLunar.text = calendar.lunar
        mYear = calendar.year

        Log.e("onDateSelected", "  -- " + calendar.year +
                "  --  " + calendar.month +
                "  -- " + calendar.day +
                "  --  " + isClick + "  --   " + calendar.scheme)
    }

    override fun onYearChange(year: Int) {
        mTextMonthDay.text = year.toString()
    }

    companion object {
        //    GroupRecyclerView mRecyclerView;

        fun show(context: Context) {
            context.startActivity(Intent(context, CustomActivity::class.java))
        }
    }


}
