package com.android.tlb.intro

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.android.tlb.R
import com.android.tlb.utils.goActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            this.goActivity(IntroActivity::class.java)
            finish()
        }, 3000)

    }
}
