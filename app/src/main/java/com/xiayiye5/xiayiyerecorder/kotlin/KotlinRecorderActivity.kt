package com.xiayiye5.xiayiyerecorder.kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class KotlinRecorderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //开启服务
        //       Intent intent = new Intent(this,MyKotlinService.class);
        val intent = Intent()
        intent.action = "cn.yhsh.recorder.kotlin"
        intent.setPackage(packageName)
        startService(intent)
        Log.e("kotlin版本服务开启检测", "已开启服务")
        finish()
    }
}
