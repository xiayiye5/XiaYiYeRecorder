package com.xiayiye5.xiayiyerecorder.java;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {
    TelephonyManager tm;
    private MyListener listener;
    private MediaRecorder mediaRecorder;

    @Override
    public void onCreate() {
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        //监听通话状态
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //闲置状态
                    if (mediaRecorder != null) {
                        mediaRecorder.stop();//停止录音
                        mediaRecorder.release();//重置录音
                        mediaRecorder = null;
                        Log.e("打印录音", "录音完毕");
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //通话状态，以下设置的顺序不能错，否则报错
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置资源录制涞源为MIC
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置资源输出格式为默认
                    //设置资源输出路径为根目录
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//设置日期格式
                    mediaRecorder.setOutputFile(new File(Environment.getExternalStorageDirectory(), df.format(new Date()) + ".3gp").getAbsolutePath());
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//设置资源编码为默认
                    //准备录音
                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //开始播放
                    mediaRecorder.start();
                    break;
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    @Override
    public void onDestroy() {
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        //置为空
        listener = null;
        super.onDestroy();
    }
}
