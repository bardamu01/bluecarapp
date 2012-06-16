package com.bluecar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.util.*;

public class BlueCarMainActivity extends Activity {

    public static final String TAG = "BlueCar";
    public static final boolean D = true;

    private static final UUID SPP_UUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private BluetoothAdapter mBluetoothAdapter = null;

    //client BT device
    private BluetoothDevice btDevice = null;
    private BluetoothSocket btSocket = null;

    //
    BlueCar blueCar = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ImageView controlsView = (ImageView) findViewById(R.id.controlsView);
        controlsView.setOnTouchListener(new ControlsTouchListener());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.d(TAG, "++ ON START ++");

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (D) Log.d(TAG, "bt already enabled");

            final BluetoothDevice[] bondedDevices = new BluetoothDevice[mBluetoothAdapter.getBondedDevices().size()];
            mBluetoothAdapter.getBondedDevices().toArray(bondedDevices);

            List<CharSequence> devices = new ArrayList<CharSequence>();

            //update the "Select device" spinner
            for (BluetoothDevice btd:bondedDevices) {
                Log.d(TAG, "Found bonded device: " + btd.getName());
                devices.add(btd.getName());
            }

            ArrayAdapter spinnerAdapter = new ArrayAdapter<CharSequence>(this,
                    android.R.layout.simple_spinner_item, devices);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner selectDeviceSpinner = (Spinner) findViewById(R.id.spinner);
            selectDeviceSpinner.setAdapter(spinnerAdapter);

            selectDeviceSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            if (D) Log.d(TAG, "Selected: " + bondedDevices[pos].getName());
                            btDevice = bondedDevices[pos];
                        }

                        public void onNothingSelected(AdapterView<?> arg0) {
                        }

                    }
            );


        }
    }

    public void connect(View view) {
        if (D) Log.d(TAG, "Establishing connection to: " + btDevice.getName());

        try{
            btSocket = btDevice.createRfcommSocketToServiceRecord(SPP_UUID);
            btSocket.connect();
            Toast.makeText(this, "Connected to: " + btDevice.getName(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to connect to: " + btDevice.getName(), Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    public void disconnect(View view){
        try {
            btSocket.close();
            Toast.makeText(this, "Disconnected from: " + btDevice.getName(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }


    //motion controls

    class ControlsTouchListener implements View.OnTouchListener{



        public boolean onTouch(View view, MotionEvent motionEvent){
            int viewHeight = view.getHeight();
            int viewWidth = view.getWidth();

            PieTouchArea pie = new PieTouchArea(150, 8, new Point(viewWidth/2, viewHeight/2), -22);

            int area = pie.getArea(new Point(motionEvent.getX(), motionEvent.getY()));

            if (D) {
                Log.d(TAG, String.format("Touched in %s (%d, %d) at %s)",view,viewWidth,viewHeight,area));
            }

            int speed = (int)(motionEvent.getPressure() * BlueCar.MAX_SPEED * 3);
            if (speed > BlueCar.MAX_SPEED)
                speed = BlueCar.MAX_SPEED;

            int command = 0;
            switch (area){
                case 0: command = BlueCar.CMD_STRAIGHT_FORWARD; break;
                case 1: command = BlueCar.CMD_RIGHT_FORWARD; break;
                case 2: command = BlueCar.CMD_RIGHT_NO_DRIVE; break;
                case 3: command = BlueCar.CMD_RIGHT_BACKWARD; break;
                case 4: command = BlueCar.CMD_STRAIGHT_BACKWARD; break;
                case 5: command = BlueCar.CMD_LEFT_BACKWARD; break;
                case 6: command = BlueCar.CMD_LEFT_NO_DRIVE; break;
                case 7: command = BlueCar.CMD_LEFT_FORWARD; break;
            }
            command = command + speed;

            return sendCommand(command);
        }
    }

    private boolean sendCommand(int command){
        byte byteCommand = (byte) command;
          try {
              if (btSocket != null){
                if (D) Log.d(TAG, "Sending command: " + String.format("%02X", byteCommand));
                btSocket.getOutputStream().write(byteCommand);
                return true;
              }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return false;
    }


    // activity related
    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.d(TAG, "+ ON RESUME +");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.d(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.d(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (D) Log.d(TAG, "--- ON DESTROY ---");
    }
}
