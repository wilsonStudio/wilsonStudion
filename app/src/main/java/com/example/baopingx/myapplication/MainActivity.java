package com.example.baopingx.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification.Builder;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "wilson_main";
    private static final String INTENTAL_1 = "com.example.baopingx.myapplication";
    TextView Ev1 = null;
    int count=0;//count click time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "now begin to MainActivity");
        Ev1 = (TextView)findViewById(R.id.textView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    public void BtnOnClick(View view) {
        count++;
        Ev1.setText(String.valueOf(count));
        if (4 == count) {
            Ev1.setText(this.getString(R.string.warring));
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("warring");
            builder.setMessage("Oops! get max times");
            builder.setPositiveButton("exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //exit current activity
                    MainActivity.this.finish();
                }
            });


            builder.setNegativeButton("reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Ev1.setText(R.string.context1);
                    count = 0;//reset count times
                }
            });
            builder.create().show();
        }
    }

    public void sendBroadCast(View view){
        //Context mContext;
        //mContext=getApplicationContext();
        Intent intent = new Intent(INTENTAL_1);
        //intent.setAction("com.android.my.action");
        //intent.setFlags(1);
        Log.d(TAG, "wilson begin to send broadcast");
        sendBroadcast(intent);
        String action =intent.getAction();
        if(action.equals(INTENTAL_1)){
            Log.d(TAG, "wilson begin to receive broadcast");
        }
        Ev1.setText(R.string.context1);
    }

    public void SwitchBt(View view){
        Log.d(TAG, "begin to switch BT activity");
        Ev1.setText("begin to switch BT activity");
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, BtActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void SwitchRecord(View view){
        Log.d(TAG, "begin to switch record activity");
        Ev1.setText("begin to switch record activity");
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AudioRecordActivity.class);
        startActivity(intent);
        this.finish();
    }
}
