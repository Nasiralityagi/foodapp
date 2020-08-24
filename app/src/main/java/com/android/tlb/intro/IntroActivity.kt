package com.android.tlb.intro

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.android.tlb.R
import com.android.tlb.auth.ui.LoginActivity
import com.android.tlb.home.HomeActivity
import com.android.tlb.store.SharedPrefManager


class IntroActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var dotsLayout: LinearLayout? = null
    private var dots: Array<TextView?>?=null
    private var layouts: IntArray?=null
    private var btnSkip: Button? = null
    private var btnNext: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Checking for first time launch - before calling setContentView()
       /*if (SharedPrefManager[this,"firstLaunch",false] as Boolean) {
            launchHomeScreen()
            finish()
        }*/
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setContentView(R.layout.activity_intro)
        viewPager = findViewById<View>(R.id.view_pager) as ViewPager
        dotsLayout = findViewById<View>(R.id.layoutBars) as LinearLayout
        btnSkip = findViewById<View>(R.id.skip) as Button
        btnNext = findViewById<View>(R.id.next) as Button
        // layouts of all welcome sliders
// add few more layouts if you want
        layouts = intArrayOf(
            R.layout.intro_screen1,
            R.layout.intro_screen2,
            R.layout.intro_screen3)
        // adding bottom dots
        addBottomDots(0)
        // making notification bar transparent
        changeStatusBarColor()
        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager!!.adapter = myViewPagerAdapter
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)
        btnSkip!!.setOnClickListener { launchHomeScreen() }
        btnNext!!.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(+1)
            if (current < layouts!!.size) { // move to next screen
                viewPager!!.currentItem = current
            } else {
                launchHomeScreen()
            }
        }
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts!!.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        dotsLayout!!.removeAllViews()
        for (i in dots!!.indices) {
            dots!![i] = TextView(this)
            dots!![i]!!.text = Html.fromHtml("&#8226;")
            dots!![i]!!.textSize = 35f
            dots!![i]!!.setTextColor(colorsInactive[currentPage])
            dotsLayout!!.addView(dots!![i])
        }
        if (dots!!.size > 0) dots!![currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        SharedPrefManager[this, "firstLaunch"] = true
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    //  viewpager change listener
    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts!!.size - 1) { // last page. make button text to GOT IT
                btnNext!!.text = "GET STARTED"
                btnSkip!!.visibility = View.GONE
            } else { // still pages are left
                btnNext!!.text = "NEXT"
                btnSkip!!.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.TRANSPARENT)
        }
    }

    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view =
                layoutInflater!!.inflate(layouts!![position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(
            view: View,
            obj: Any
        ): Boolean {
            return view === obj
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            `object`: Any
        ) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}

//class IntroActivity : AppCompatActivity() {
//    var Layout_bars: LinearLayout? = null
//    var bottomBars: ArrayList<TextView>?=null
//    var screens: ArrayList<Int>?=null
//    var Skip: Button? = null
//    var Next: Button? = null
//    var vp: ViewPager? = null
//    var myvpAdapter: MyViewPagerAdapter? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intro)
//        vp = findViewById<View>(R.id.view_pager) as ViewPager
//        Layout_bars = findViewById<View>(R.id.layoutBars) as LinearLayout
//        Skip = findViewById<View>(R.id.skip) as Button
//        Next = findViewById<View>(R.id.next) as Button
//        screens!!.add(R.layout.intro_screen1)
//        screens!!.add(R.layout.intro_screen2)
//        screens!!.add( R.layout.intro_screen3)
//        myvpAdapter = MyViewPagerAdapter()
//        vp!!.adapter = myvpAdapter
//        vp!!.addOnPageChangeListener(viewPagerPageChangeListener)
//
//        if (SharedPrefManager[this,"firstLaunch",false] as Boolean) {
//            launchMain()
//            finish()
//        }
//
//        ColoredBars(0)
//    }
//
//    fun next(v: View?) {
//        val i = getItem(+1)
//        if (i < screens!!.size) {
//            vp!!.currentItem = i
//        } else {
//            launchMain()
//        }
//    }
//
//    fun skip(view: View?) {
//        launchMain()
//    }
//
//    private fun ColoredBars(thisScreen: Int) {
//        val colorsInactive =
//            resources.getIntArray(R.array.array_dot_inactive)
//        val colorsActive = resources.getIntArray(R.array.array_dot_active)
//        bottomBars = ArrayList<TextView>(screens!!.size)
//        Layout_bars!!.removeAllViews()
//        for (i in bottomBars!!.indices) {
//            bottomBars!![i] = TextView(this)
//            bottomBars!![i]!!.textSize = 100f
//            bottomBars!![i]!!.text = Html.fromHtml("¯")
//            Layout_bars!!.addView(bottomBars!![i])
//            bottomBars!![i]!!.setTextColor(colorsInactive[thisScreen])
//        }
//        if (bottomBars!!.isNotEmpty()) bottomBars!![thisScreen]!!.setTextColor(colorsActive[thisScreen])
//    }
//
//    private fun getItem(i: Int): Int {
//        return vp!!.currentItem + i
//    }
//
//    private fun launchMain() {
//        SharedPrefManager[this, "firstLaunch"] = true
//
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }
//
//
//    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
//        override fun onPageSelected(position: Int) {
//            ColoredBars(position)
//            if (position == screens!!.size - 1) {
//                Next!!.text = "start"
//                Skip!!.visibility = View.GONE
//            } else {
//                Next!!.text = "Next"
//                Skip!!.visibility = View.VISIBLE
//            }
//        }
//
//        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
//        override fun onPageScrollStateChanged(arg0: Int) {}
//    }
//
//    inner class MyViewPagerAdapter : PagerAdapter() {
//        private var inflater: LayoutInflater? = null
//        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val view = inflater!!.inflate(screens!![position], container, false)
//            container.addView(view)
//            return view
//        }
//
//        override fun getCount(): Int {
//            return screens!!.size
//        }
//
//        override fun destroyItem(
//            container: ViewGroup,
//            position: Int,
//            `object`: Any
//        ) {
//            val v = `object` as View
//            container.removeView(v)
//        }
//
//        override fun isViewFromObject(v: View, `object`: Any): Boolean {
//            return v === `object`
//        }
//    }
//}
