package com.example.baopingx.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.*;

public class AudioRecordActivity extends Activity {
    private static final String TAG = "wilson_record";
    private static final byte MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private MediaPlayer myPlay = null;
    private MediaRecorder recorder = null;
    private String strAudioFile = "";
    private TextView mText = null;
    private boolean sdCardExit = false;
    private Button startRecord = null;
    private Button startPlay = null;
    private Button stopRecord = null;
    private Button stopPlay = null;
    private Button DelRecord = null;
    private static List<String> permissions = new ArrayList<String>();
    private short RECORD_AUDIO =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        Log.d(TAG, "now enter to AudioRecordActivity");
        init();

    }

    public void init(){
        //request permission at runtime
        if (!(hasPermission(Manifest.permission.RECORD_AUDIO) && !hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && !hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), RECORD_AUDIO);
            }
        }

        sdCardExit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        startRecord = (Button) findViewById(R.id.button6);
        startPlay = (Button) findViewById(R.id.button8);
        DelRecord = (Button) findViewById(R.id.button14);
        mText = (TextView)findViewById(R.id.textView3);

        //create audiofile
        if(strAudioFile.equals(""))
        {
            strAudioFile = Environment.getExternalStorageDirectory().getAbsolutePath();
            strAudioFile += File.separator+"wilson.aac";
        }
        else
        {
            File file = new File(strAudioFile);
            if(file.exists()) {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        else
        {
            Log.d(TAG, "not M build.do nothing");
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mStartRecord(View view)
    {
        mText.setText("now begin to recoring.....");
        Log.d(TAG, "now begin to recording");
        // 设置录制好的音频文件保存路径
        if(!sdCardExit)
        {
            Toast.makeText(this,"pls insert SD card",Toast.LENGTH_LONG).show();
            return;
        }
        recorder = new MediaRecorder();
        // 设置MediaRecorder的音频源为麦克风
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置MediaRecorder录制的音频格式
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //设置MediaRecorder audiofile
        recorder.setOutputFile(strAudioFile);
        // 设置MediaRecorder录制音频的编码为amr.
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        try {
            recorder.prepare();// 准备录制
            recorder.start();// 开始录制
            mText.setText(" recoring.....");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mPlayRecord(View view)
    {
        if(null != recorder) mStopRecord();
        mText.setText("now begin to playing.....");
        Log.d(TAG, "now begin to playing");
        myPlay = new MediaPlayer();
        try {
            myPlay.setDataSource(strAudioFile);
            myPlay.prepare();
            myPlay.start();
        } catch (IOException e) {
            e.printStackTrace();
            myPlay.release();
            myPlay = null;
        }
        Log.d(TAG, "now end to playing");
    }

    public void mStopRecord() {
        mText.setText("now ending recording.....");
        Log.d(TAG, "now begin to stop recording");
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder =null;
        Log.d(TAG, "now end to stop recording");
    }


    public void mdelRecord(View view)
    {
        mText.setText("now enter to delrecording");
        Log.d(TAG, "now delete to delrecording");
        File file = new File(strAudioFile);
        if(file.exists()) {
            Log.d(TAG, strAudioFile + " was exites");
            if(!file.delete()) {
                Log.d(TAG, "Delete recording failed");
            }
        }
        Log.d(TAG, strAudioFile + " was not exites");
        return;
    }
}