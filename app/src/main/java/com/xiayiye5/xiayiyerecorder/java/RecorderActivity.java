package com.xiayiye5.xiayiyerecorder.java;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author xiayiye
 */
public class RecorderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开启服务
//       Intent intent = new Intent(this,MyKotlinService.class);
        Intent intent = new Intent();
        intent.setAction("cn.yhsh.recorder");
        intent.setPackage(getPackageName());
        startService(intent);
        Log.e("Java版本服务开启检测", "已开启服务");
        finish();
    }
}
