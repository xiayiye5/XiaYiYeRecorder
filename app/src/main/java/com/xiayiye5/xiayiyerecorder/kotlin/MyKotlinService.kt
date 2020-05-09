package com.xiayiye5.xiayiyerecorder.kotlin

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Environment
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MyKotlinService : Service() {
    var tm: TelephonyManager? = null
    private var listener: MyListener? = null
    private var mediaRecorder: MediaRecorder? = null

    override fun onCreate() {
        tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        listener = MyListener()
        //监听通话状态
        tm?.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    internal inner class MyListener : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            when (state) {
                TelephonyManager.CALL_STATE_IDLE ->
                    //闲置状态
                    if (mediaRecorder != null) {
                        mediaRecorder!!.stop()//停止录音
                        mediaRecorder!!.release()//重置录音
                        mediaRecorder = null
                        Log.e("打印录音", "录音完毕")
                    }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    //通话状态，以下设置的顺序不能错，否则报错
                    mediaRecorder = MediaRecorder()
                    mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)//设置资源录制涞源为MIC
                    mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)//设置资源输出格式为默认
                    //设置资源输出路径为根目录
                    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)//设置日期格式
                    mediaRecorder!!.setOutputFile(
                        File(
                            Environment.getExternalStorageDirectory(),
                            df.format(Date()) + ".3gp"
                        ).absolutePath
                    )
                    mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)//设置资源编码为默认
                    //准备录音
                    try {
                        mediaRecorder!!.prepare()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    //开始播放
                    mediaRecorder!!.start()
                }
                else -> {
                }
            }
            super.onCallStateChanged(state, incomingNumber)
        }
    }

    override fun onDestroy() {
        tm?.listen(listener, PhoneStateListener.LISTEN_NONE)
        listener = null//置为空
        super.onDestroy()
    }
}
