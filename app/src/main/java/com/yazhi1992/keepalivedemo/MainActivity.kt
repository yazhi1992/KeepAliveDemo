package com.yazhi1992.keepalivedemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yazhi1992.keepalive.KeepAliveManager
import com.yazhi1992.keepalive.KeepAliveService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val intent = Intent()
//        intent.setClass(this@MainActivity, KeepAliveService::class.java!!)
//        startService(intent)
        KeepAliveService.StartJob(this)

//        KeepAliveManager.getInstance().keepAlive(this)
    }
}
