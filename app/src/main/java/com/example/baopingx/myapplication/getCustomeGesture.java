package com.example.baopingx.myapplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.os.Build;
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
    private TelephonyManager tm = null;
    private String gesPath = "" ;
    private Button mbutton = null;
    private short REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static List<String> permissions = new ArrayList<String>();

    /*
    * 程序预制了几个手势
    *   1 当画C字母的时候 会进行打电话处理
    *   2 当画叉的时候 程序会退出
    *   3 当画V的时候，会打开摄像头
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_custome_gesture);

        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            Toast.makeText(this, "SDcard not exits！", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        init();
        gesPath= new File(Environment.getExternalStorageDirectory(),
                "gestures").getAbsolutePath();
        File f = new File(gesPath);
        if (!f.exists())
        {
            Log.d(TAG, "use prepare GestureLibraries" );
            library = GestureLibraries.fromRawResource(this, R.raw.gestures);//用预制的代替
        }

        library = GestureLibraries.fromFile(f);
        if(!library.load()){
            Toast.makeText(getApplicationContext(), "gesture file load failed", 1).show();
            this.finish();
        }
        final Set<String> entries = library.getGestureEntries();
        Log.i(TAG, "current GestureLibraries record = " + entries.size());
    }

    public void init()
    {
        mbutton = (Button) this.findViewById(R.id.reconize);
        overlayView = (GestureOverlayView) this.findViewById(R.id.gestures);
        overlayView.addOnGestureListener(new GestureListener());//为view添加手势监听事件

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (!permissions.isEmpty()) {
                    requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_WRITE_EXTERNAL_STORAGE);
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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!hasPermission(Manifest.permission.CALL_PHONE)&&!hasPermission(Manifest.permission.READ_PHONE_STATE)) {
                            permissions.add(Manifest.permission.CALL_PHONE);
                            permissions.add(Manifest.permission.READ_PHONE_STATE);
                            if (!permissions.isEmpty()) {
                                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        }
                    }
                    Log.i(TAG, "prediction.name = "+prediction.name);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:13866668888"));
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
                    if ("video".equals(prediction.name)) {
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
