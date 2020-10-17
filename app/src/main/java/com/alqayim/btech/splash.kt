package com.alqayim.btech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class splash_screen : AppCompatActivity() {

    val SPLASH_TIME_OUT = 2500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ///// splash start code
        Handler().postDelayed(object : Runnable {
            public override fun run() {
                val home = Intent(this@splash_screen, MainActivity::class.java)
                startActivity(home)
                finish()
            }
        }, SPLASH_TIME_OUT.toLong())
        ///////// end splatch code
    }

}