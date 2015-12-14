package com.example.baopingx.myapplication;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import static android.view.View.*;
import static com.example.baopingx.myapplication.R.layout.activity_get_mobile_info;

public class GetMobileInfo extends AppCompatActivity {
    private TextView mText = null;
    private File fileDir;
    private File sdcardDir;
    private  Button internalDisk;
    private  Button emulatorDisk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_get_mobile_info);

        mText = (TextView) findViewById(R.id.textView4);
        internalDisk = (Button) findViewById(R.id.button15);
        emulatorDisk = (Button) findViewById(R.id.button16);
    }
    public void getMobileInfo(View view) {
        TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //String mtype = android.os.Build.MODEL; // 手机型号
        String mtype =getFilesDir().toString();
        mText.setText(mtype);

        sdcardDir = Environment.getExternalStorageDirectory();

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED))
        {
            emulatorDisk.setEnabled(false);
            emulatorDisk.setVisibility(View.INVISIBLE);
        }

    }
}