package com.example.bluetoothcarremotecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity {
    public byte message[] = new byte[1];
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket = null;
    OutputStream outputStream = null ;

    private int ENABLE_BLUETOOTH = 2;

    private static final UUID MY_UUID_SECURE=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String blueAddress="00:18:E5:04:A1:88"; //蓝牙模块的MAC地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (bluetoothAdapter == null){
            return; //设备不适用于蓝牙
        }
        if (!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,ENABLE_BLUETOOTH);
        }

        ButtonListener buttonListener = new ButtonListener();

        ImageButton GoForward = (ImageButton)findViewById(R.id.btn01);
        ImageButton TurnLeft = (ImageButton)findViewById(R.id.btn02);
        ImageButton TurnRight = (ImageButton)findViewById(R.id.btn03);
        ImageButton GoBack = (ImageButton)findViewById(R.id.btn04);

        GoForward.setOnTouchListener(buttonListener);
        TurnLeft.setOnTouchListener(buttonListener);
        TurnRight.setOnTouchListener(buttonListener);
        GoBack.setOnTouchListener(buttonListener);

    }

    class ButtonListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (v.getId()){
                case R.id.btn01:
                    if (event.getAction() == MotionEvent.ACTION_DOWN){//按下按钮
                        message[0] = (byte) 0x31;//传入前进参数
                        SendtoBlueTooth(message);
                        Log.d("KroFin", ""+message[0]);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP){//松开按钮
                        message[0] = (byte) 0x30;//传入停止参数
                        SendtoBlueTooth(message);
                        Log.d("KroFin", ""+message[0]);
                    }break;
                case R.id.btn02:
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        message[0] = (byte) 0x32;//传入左转向参数
                        SendtoBlueTooth(message);
                        Log.d("KroFin", ""+message[0]);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP){//松开按钮
                        message[0] = (byte) 0x30;//传入停止参数
                        SendtoBlueTooth(message);
                        Log.d("KroFin", ""+message[0]);
                    }break;
                case R.id.btn03:
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        message[0] = (byte) 0x33;//传入后退参数
                        SendtoBlueTooth(message);
                        Log.d("KroFin", ""+message[0]);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP){//松开按钮
                        message[0] = (byte) 0x30;//传入停止参数
                        SendtoBlueTooth(message);
                        Log.d("KroFin", ""+message[0]);
                    }break;
                case R.id.btn04:
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        message[0] = (byte) 0x34;//传入右转向参数
                        SendtoBlueTooth(message);
                        Log.d("KroFin", ""+message[0]);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP){//松开按钮
                        message[0] = (byte) 0x30;//传入停止参数
                        SendtoBlueTooth(message);
                        Log.d("KroFin", ""+message[0]);
                    }break;
            }
            return false;
        }
    }
    //蓝牙发送数据
    public void SendtoBlueTooth(byte[] message){
        try{
            outputStream = bluetoothSocket.getOutputStream();
            Log.d("send", Arrays.toString(message));
            outputStream.write(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            bluetoothSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(blueAddress);
        try{
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
            Log.d("true","开始连接");
            bluetoothSocket.connect();
            Log.d("true","完成连接");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}