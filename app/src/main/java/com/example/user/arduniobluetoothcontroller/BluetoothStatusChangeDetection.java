package com.example.user.arduniobluetoothcontroller;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class BluetoothStatusChangeDetection extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state){
                case BluetoothAdapter.STATE_OFF:
                    Toast.makeText(context,"Bluetooth turn off", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new BluetoothStatus("OFF"));
                    break;
                case BluetoothAdapter.STATE_ON:
                    Toast.makeText(context,"Bluetooth turn on", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new BluetoothStatus("ON"));
                    break;
            }
        }
    }
}