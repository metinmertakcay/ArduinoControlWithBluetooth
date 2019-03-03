package com.example.user.arduniobluetoothcontroller;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BroadcastReceiver bluetoothStatusChangeDetection;
    private BluetoothAdapter bluetoothAdapter = null;
    private OutputStream outputStream = null;
    private BluetoothSocket socket = null;
    public String DEVICE_ADDRESS = null;

    private ImageButton back, forward, upward, downward;
    private TextView title, servo;
    private SeekBar gripper;
    private String command;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("deviceAddress")){
                DEVICE_ADDRESS = intent.getStringExtra("deviceAddress");
                if(extras.containsKey("paired")){
                    Toast.makeText(MainActivity.this, "Device selected", Toast.LENGTH_SHORT).show();
                }
            }
        }

        initializeComponent();
        backCommandListener();
        forwardCommandListener();
        upwardCommandListener();
        downwardCommandListener();
        gripperCommandListener();
    }

    public void initializeComponent() {
        back = (ImageButton) findViewById(R.id.back);
        forward = (ImageButton) findViewById(R.id.forward);
        upward = (ImageButton) findViewById(R.id.upward);
        downward = (ImageButton) findViewById(R.id.downward);
        title = (TextView)findViewById(R.id.title);
        servo = (TextView)findViewById(R.id.servo);
        gripper = (SeekBar)findViewById(R.id.gripper);
        bluetoothStatusChangeDetection = new BluetoothStatusChangeDetection();
    }

    public void backCommandListener() {
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "243 :";
                    try {
                        if (outputStream != null) {
                            //Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
                            outputStream.write(command.toString().getBytes());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    command = "200 :";
                    try {
                        if (outputStream != null) {
                            //Toast.makeText(MainActivity.this, "Touch finish", Toast.LENGTH_SHORT).show();
                            outputStream.write(command.toString().getBytes());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    public void forwardCommandListener() {
        forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "244 :";
                    try {
                        if (outputStream != null) {
                            //Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
                            outputStream.write(command.toString().getBytes());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    command = "200 :";
                    try {
                        if (outputStream != null) {
                            //Toast.makeText(MainActivity.this, "Touch finish", Toast.LENGTH_SHORT).show();
                            outputStream.write(command.toString().getBytes());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    public void upwardCommandListener() {
        upward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "241 :";
                    try {
                        if (outputStream != null) {
                            //Toast.makeText(MainActivity.this, "Forward", Toast.LENGTH_SHORT).show();
                            outputStream.write(command.toString().getBytes());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    command = "200 :";
                    try {
                        if (outputStream != null) {
                            //Toast.makeText(MainActivity.this, "Touch finish", Toast.LENGTH_SHORT).show();
                            outputStream.write(command.toString().getBytes());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    public void downwardCommandListener() {
        downward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "242 :";
                    try {
                        if (outputStream != null) {
                            //Toast.makeText(MainActivity.this, "Backward", Toast.LENGTH_SHORT).show();
                            outputStream.write(command.toString().getBytes());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    command = "200 :";
                    try {
                        if (outputStream != null) {
                            //Toast.makeText(MainActivity.this, "Touch finish", Toast.LENGTH_SHORT).show();
                            outputStream.write(command.toString().getBytes());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    private class bluetoothConnection extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if(DEVICE_ADDRESS != null) {
                    if (socket == null) {
                        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
                        socket = dispositivo.createInsecureRfcommSocketToServiceRecord(PORT_UUID);
                        socket.connect();
                        outputStream = socket.getOutputStream();
                    }
                } else{
                    Log.d("KK","Please Select a Paired Device");
                }
            } catch (IOException e) {
                disconnectResource();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (outputStream == null) {
                title.setTextColor(getResources().getColor(R.color.red));
                servo.setTextColor(getResources().getColor(R.color.red));
            } else {
                title.setTextColor(getResources().getColor(R.color.green));
                servo.setTextColor(getResources().getColor(R.color.green));
            }
            dialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;
        return firstLaunchSetVisibilityBluetoothIcon();
    }

    public boolean firstLaunchSetVisibilityBluetoothIcon() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device Doesn't Support Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                MenuItem item = menu.findItem(R.id.disabled);
                item.setVisible(true);
                item = menu.findItem(R.id.connect);
                item.setVisible(false);
            } else {
                MenuItem item = menu.findItem(R.id.disabled);
                item.setVisible(false);
                item = menu.findItem(R.id.connect);
                item.setVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.connect) {
            closeBluetooth();
            disconnectResource();
        } else if (item.getItemId() == R.id.disabled) {
            openBluetooth();
        } else if (item.getItemId() == R.id.device_connection) {
            connectionControl();
        } else if(item.getItemId() == R.id.pair){
            Intent intent = new Intent(MainActivity.this, PairedDeviceList.class);
            intent.putExtra("activity", "main");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void openBluetooth() {
        BluetoothAdapter.getDefaultAdapter().enable();
        MenuItem item = menu.findItem(R.id.disabled);
        item.setVisible(false);
        item = menu.findItem(R.id.connect);
        item.setVisible(true);
    }

    public void closeBluetooth() {
        BluetoothAdapter.getDefaultAdapter().disable();
        MenuItem item = menu.findItem(R.id.disabled);
        item.setVisible(true);
        item = menu.findItem(R.id.connect);
        item.setVisible(false);
        title.setTextColor(getResources().getColor(R.color.red));
        servo.setTextColor(getResources().getColor(R.color.red));
    }

    public void disconnectResource() {
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void connectionControl() {
        if (bluetoothAdapter.isEnabled()) {
            new bluetoothConnection().execute();
        } else {
            Toast.makeText(MainActivity.this, "Bluetooth can't be operated without open", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        disconnectResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(bluetoothStatusChangeDetection, filter);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(bluetoothStatusChangeDetection);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onBluetoothStatusReceived(BluetoothStatus event){
        String bluetoothStatus = event.getBluetoothStatus();
        if(bluetoothStatus.equals("OFF")){
            disconnectResource();
            closeBluetooth();
        } else{
            openBluetooth();
        }
    }

    public void gripperCommandListener(){
        gripper.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                if (outputStream != null) {
                    progress += 10;
                    command = String.valueOf(progress) + " :";
                    try {
                        outputStream.write(command.toString().getBytes());
                        Log.d("KK", "Covered: " + progress + "/" + seekBar.getMax());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}