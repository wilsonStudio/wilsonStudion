package com.example.baopingx.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BTservice extends Service {
    public BTservice() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
