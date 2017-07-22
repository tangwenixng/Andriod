package com.twx.broadcast2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AnotherBroadcastReceiver extends BroadcastReceiver {
    public AnotherBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"I have receive Broadcast1 project message",Toast.LENGTH_SHORT).show();
        abortBroadcast();
    }
}
