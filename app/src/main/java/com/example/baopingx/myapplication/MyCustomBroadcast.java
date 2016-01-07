package com.example.baopingx.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.System.exit;

public class MyCustomBroadcast extends AppCompatActivity {
    private Button sendBtn = null;
    private Button recvBtn = null;
    private TextView tv = null;
    private MyReceiver mReceiver;
    private static final String BROADCAST_ACTION ="com.example.baopingx.BroadcastAction";
    private static final String LOG_TAG ="wilson_Broadcast" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom_broadcast);
        sendBtn = (Button) findViewById(R.id.button7);
        recvBtn = (Button) findViewById(R.id.button10);

        tv = (TextView) findViewById(R.id.textView6);
        sendBtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(BROADCAST_ACTION);
                intent.putExtra("msg", "wilson zhang");
                sendBroadcast(intent);

            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mReceiver = new MyReceiver();
        IntentFilter intentFilter= new IntentFilter(BROADCAST_ACTION);
        registerReceiver(mReceiver, intentFilter);//注册广播

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Receiver constructor");
    }

}

class MyReceiver extends BroadcastReceiver {

    private static final String BROADCAST_ACTION ="com.example.baopingx.BroadcastAction";
    private String message;
    //private static final String LOG_TAG ="wilson_BroadcastRevevier" ;
    //private int level = 0;
    //private int scale = 0;
    public MyReceiver()
    {
        super();
        //Log.d(LOG_TAG, "Receiver constructor");
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Log.d(LOG_TAG, "onReceive");
        message = intent.getStringExtra("msg");
        Toast.makeText(context, "Broadcast msg: " + message, Toast.LENGTH_SHORT).show();

//      if(Intent.ACTION_BATTERY_CHANGED.equals(action)){
//            //获取当前电量
//            level = intent.getIntExtra("level", 0);
//            //电量的总刻度
//            scale = intent.getIntExtra("scale", 100);
//            //把它转成百分比
//            Toast.makeText(context, "当前电池电量为 " + ((level*100)/scale)+"%", Toast.LENGTH_SHORT).show();
//        }
    }
//    public int getBatteryLevel()
//    {
//        return (level*100)/scale;
//    }
}

