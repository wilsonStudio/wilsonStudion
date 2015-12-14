package com.example.baopingx.myapplication;

import java.util.ArrayList;

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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class getCustomeGesture extends AppCompatActivity {
    private static final String TAG = "wilson_gesture";
    private GestureLibrary library;
    private Gesture mgesture;
    private GestureOverlayView overlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_custome_gesture);

        library = GestureLibraries.fromRawResource(this, R.raw.gestures);//得到手势库
        Log.e(TAG, "GestureLibraries load success");
        overlayView = (GestureOverlayView) this.findViewById(R.id.gestures);
        overlayView.addOnGestureListener(new GestureListener());//为view添加手势监听事件
    }

    public void find(View v) {
        recognize(mgesture);
        overlayView.clear(true);
    }

    private final class GestureListener implements GestureOverlayView.OnGestureListener {
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            Log.i(TAG, "onGestureStarted()");
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
            if (prediction.score >= 6) {//匹配率大于60%
                if ("call".equals(prediction.name)) {
                    Log.i(TAG, "prediction.name = "+prediction.name);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1350505050"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    Log.i(TAG, "now begin to start activity call");
                    startActivity(intent);
                }else if("close".equals(prediction.name)){
                    Log.i(TAG, "prediction.name = "+prediction.name);
                    finish();//关闭Activity
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
