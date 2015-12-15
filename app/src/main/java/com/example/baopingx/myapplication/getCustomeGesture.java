package com.example.baopingx.myapplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class getCustomeGesture extends AppCompatActivity {
    private static final String TAG = "wilson_gesture";
    private GestureLibrary library;
    private Gesture mgesture;
    private GestureOverlayView overlayView;
    private TelephonyManager tm;
    private String gesPath;
    private Button mbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_custome_gesture);
/*
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            Toast.makeText(this, "SDCard不存在！", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        gesPath= new File(Environment.getExternalStorageDirectory(),
                "gestures").getAbsolutePath();
        File f = new File(gesPath);
        if (!f.exists())
        {
            library = GestureLibraries.fromRawResource(this, R.raw.gestures);//得到手势库
            library.load();
            Log.e(TAG, "GestureLibraries load from app res/raw success");
        }
       library = GestureLibraries.fromFile(f);
*/
        library = GestureLibraries.fromRawResource(this, R.raw.gestures);//得到手势库
        library.load();
        final Set<String> entries = library.getGestureEntries();
        Log.i(TAG, "GestureLibraries record = " + entries.size());

        mbutton = (Button) this.findViewById(R.id.reconize);
        overlayView = (GestureOverlayView) this.findViewById(R.id.gestures);
        overlayView.addOnGestureListener(new GestureListener());//为view添加手势监听事件
    }

    public void find(View v) {
        recognize(mgesture);
        overlayView.clear(true);//clear before gesture
    }

    private final class GestureListener implements GestureOverlayView.OnGestureListener {
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            Log.i(TAG, "onGestureStarted()");
            mbutton.setEnabled(true);

        }

        public void onGesture(GestureOverlayView overlay, MotionEvent event) {
            Log.i(TAG, "onGesture()");
        }

        public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
            Log.i(TAG, "onGestureEnded()");
            mgesture = overlay.getGesture();
        }

        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
            Log.i(TAG, "onGestureCancelled()");
        }
    }

    private final class GesturePerformedListener implements GestureOverlayView.OnGesturePerformedListener {
        public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
            recognize(gesture);
        }
    }

    private void recognize(Gesture gesture) {
        //从手势库中查询匹配的内容，匹配的结果可能包括多个相似的内容，匹配度高的结果放在最前面。
        ArrayList<Prediction> predictions = library.recognize(gesture);
        if (!predictions.isEmpty()) {
            Prediction prediction = predictions.get(0);
            if (prediction.score >= 5) {//匹配率
                if ("call".equals(prediction.name)) {
                    Log.i(TAG, "prediction.name = "+prediction.name);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:13655559737"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //check whether call service can use now
                        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        switch (tm.getSimState())
                        {
                            case TelephonyManager.SIM_STATE_ABSENT:
                                Log.e(TAG, "no sim card");
                                break;
                            case TelephonyManager.SIM_STATE_UNKNOWN:
                                Log.e(TAG, "unkonw status");
                                break;
                            case TelephonyManager.SIM_STATE_READY:
                                Log.i(TAG, "sim card ready");
                                Log.i(TAG, "now begin to start activity call");
                                startActivity(intent);
                        }
                        return;
                    }
                }else if("exit".equals(prediction.name)){
                    Log.i(TAG, "prediction.name = "+prediction.name);
                    finish();//关闭Activity
                }
                else {
                    if ("camera".equals(prediction.name)) {
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivity(intent);
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(), R.string.low, 1).show();
            }
        }else{
            Log.i(TAG, "predictions.isEmpty");
            Toast.makeText(getApplicationContext(), R.string.notfind, 1).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());//关闭应用
    }
}
