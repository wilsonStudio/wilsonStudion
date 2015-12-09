package com.example.baopingx.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import static android.os.SystemClock.sleep;

public class myProgressDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         ProgressDialog progressDlg = new ProgressDialog(this);
         progressDlg.setTitle("进度对话框测试");
         progressDlg.setMessage("测试进度");
         progressDlg.setIcon(R.mipmap.ic_launcher);
         progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
         progressDlg.setCancelable(true);
        for(int i=1;i<101;i++)
        {

            progressDlg.setProgress(i);
            sleep(1000);
            progressDlg.show();
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            ;
            Notification myNotication;
            //API level 11
            Intent intent = new Intent("com.rj.notitfications.SECACTIVITY");
            PendingIntent pendingIntent = PendingIntent.getActivity(myProgressDialog.this, 1, intent, 0);

            Notification.Builder builder = new Notification.Builder(myProgressDialog.this);

            builder.setAutoCancel(true);
            builder.setTicker("this is ticker text");
            builder.setContentTitle("WhatsApp Notification");
            builder.setContentText("You have a new message");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(true);
            builder.setSubText("This is subtext...");   //API level 16
            builder.setNumber(100);
            builder.build();

            myNotication = builder.getNotification();
            manager.notify(11, myNotication);
        }
    }


}
