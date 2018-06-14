package com.a3jfernando.serialport.Services.AutoRunApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.a3jfernando.serialport.pages.LoginActivity;

/**
 * Created by Enuar Munoz on 30/06/2017.
 */

public class RunApp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
