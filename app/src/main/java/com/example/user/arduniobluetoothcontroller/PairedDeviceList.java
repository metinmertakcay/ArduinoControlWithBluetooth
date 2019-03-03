package com.example.user.arduniobluetoothcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class PairedDeviceList extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter = null;
    private Set<BluetoothDevice> pairedDevices;
    private ListView list;
    private Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicelist);

        initialize();
        checkBluetoothActive();
        findButtonClick();
    }

    public void initialize() {
        list = (ListView) findViewById(R.id.list);
        find = (Button) findViewById(R.id.find);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void checkBluetoothActive() {
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }
    }

    public void findButtonClick() {
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });
    }

    public void pairedDevicesList() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList deviceList = new ArrayList();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : pairedDevices) {
                deviceList.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter adapter = new ArrayAdapter(PairedDeviceList.this, android.R.layout.simple_list_item_1, deviceList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(myListClickListener);
    }

    public AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View view, int arg2, long arg3) {
            String infoDevice = ((TextView) view).getText().toString();
            String deviceAddress = infoDevice.substring(infoDevice.length() - 17);

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if(extras != null){
                Intent in = new Intent(PairedDeviceList.this, MainActivity.class);
                in.putExtra("deviceAddress", deviceAddress);
                in.putExtra("paired","");
                startActivity(in);
            }
        }
    };
}