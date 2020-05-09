package com.xiayiye5.xiayiyerecorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xiayiye5.xiayiyerecorder.java.RecorderActivity
import com.xiayiye5.xiayiyerecorder.kotlin.KotlinRecorderActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @author xiayiye5
 * 2020年5月9号 15点33分
 * 电话录音机无界面区分Java和kotlin版本
 * 切记不能同时启动两个录音服务，否则后启动的会有冲突报错
 *      2020-05-09 15:22:21.113 14587-14587/com.xiayiye5.xiayiyerecorder E/AndroidRuntime: FATAL EXCEPTION: main
 *      Process: com.xiayiye5.xiayiyerecorder, PID: 14587
 *      java.lang.IllegalStateException
 *      at android.media.MediaRecorder.start(Native Method)
 *      at com.xiayiye5.xiayiyerecorder.kotlin.MyKotlinService$MyListener.onCallStateChanged(MyKotlinService.kt:66)
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }

    /**
     * 开始录音
     */
    private fun startRecorder() {
        lvRecorder.setOnItemClickListener { p0, p1, position, p3 ->
            when (position) {
                0 -> startActivity(Intent(this@MainActivity, RecorderActivity::class.java))
                1 -> startActivity(
                    Intent(
                        this@MainActivity,
                        KotlinRecorderActivity::class.java
                    )
                )
            }
        }
    }

    /**
     * 权限申请
     */
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf<String>(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this, permissions, 200)
                } else {
                    startRecorder()
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                } else {
                    startRecorder()
                }
            }
        }
    }

}
