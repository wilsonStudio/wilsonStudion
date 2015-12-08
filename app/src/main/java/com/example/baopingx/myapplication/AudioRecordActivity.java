package com.example.baopingx.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        mText = (TextView)findViewById(R.id.textView3);
        sdCardExit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        Log.d(TAG, "now enter to AudioRecordActivity");
        startRecord = (Button) findViewById(R.id.button6);
        startPlay = (Button) findViewById(R.id.button8);
        stopRecord = (Button) findViewById(R.id.button7);
        stopPlay = (Button) findViewById(R.id.button10);
        DelRecord = (Button) findViewById(R.id.button14);
        //create audiofile
        if(strAudioFile.equals(""))
        {
            strAudioFile = Environment.getExternalStorageDirectory().getAbsolutePath();
            strAudioFile += "/wilson.amr";
        }
        else
        {
            Log.d(TAG, strAudioFile + "was exites");
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
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();// 准备录制
            recorder.start();// 开始录制
            mText.setText(" recoring.....");
            stopRecord.setVisibility(VISIBLE);
            stopRecord.setEnabled(true);
            DelRecord.setVisibility(VISIBLE);
            DelRecord.setEnabled(true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "now end to recording");
    }

    public void mPlayRecord(View view)
    {
        mText.setText("now begin to playing.....");
        Log.d(TAG, "now begin to playing");
        stopPlay.setVisibility(VISIBLE);
        stopPlay.setEnabled(true);

        myPlay = new MediaPlayer();
        try {
            myPlay.setDataSource(strAudioFile);
            myPlay.prepare();
            myPlay.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "now end to playing");
    }

    public void mStopRecord(View view) {
        mText.setText("now begin to stop recording.....");
        Log.d(TAG, "now begin to stop recording");
        recorder.stop();
        recorder.release();
        recorder =null;
        startRecord.setEnabled(true);
        startPlay.setEnabled(true);
        startPlay.setVisibility(VISIBLE);
        DelRecord.setVisibility(VISIBLE);
        DelRecord.setEnabled(true);
        Log.d(TAG, "now end to stop recording");
    }

    public void mStopPlay(View view)
    {
        mText.setText("now pause to playing.....");
        Log.d(TAG, "now begin to pause to playing");
        startPlay.setEnabled(true);
        stopPlay.setEnabled(false);
        myPlay.release();
        myPlay = null;
        Log.d(TAG, "now end pause to playing");
    }

    public void mdelRecord(View view)
    {
        mText.setText("now delete to recording");
        Log.d(TAG, "now delete to recording");
        File file = new File(strAudioFile);
        if(file.exists()) {
            Log.d(TAG, strAudioFile + " was exites");
            if(file.delete()) {
                mText.setText("now end to recording");
            }
            Log.d(TAG, "Delete recording failed");
        }
        Log.d(TAG, strAudioFile + " was not exites");
        return;
    }
}