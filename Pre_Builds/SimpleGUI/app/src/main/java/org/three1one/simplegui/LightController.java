package org.three1one.simplegui;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class LightController extends Activity {
    private final static String TAG = LightController.class.getSimpleName();
    private Boolean recordModeState = false;
    private String mDeviceName;
    private String mDeviceAddress;
    private RBLService mBluetoothLeService;
    private Map<UUID, BluetoothGattCharacteristic> map = new HashMap<UUID, BluetoothGattCharacteristic>();
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((RBLService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (RBLService.ACTION_GATT_DISCONNECTED.equals(action)) {
            } else if (RBLService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                getGattService(mBluetoothLeService.getSupportedGattService());
            } else if (RBLService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getByteArrayExtra(RBLService.EXTRA_DATA));
            }
        }
    };

    private TextView tv = null;

    private Button btnColourPickRed;
    private Button btnColourPickGreen;
    private Button btnColourPickBlue;
    private Button btnColourPickCyan;
    private Button btnColourPickYellow;
    private Button btnColourPickMagenta;
    private Button btn_Start;
    private Button btn_Rec;
    private Button btn_Save;
    private Button btn_Stop;
    private Button btn_Delete;
    private Spinner dropdown;

    private String curCustomCmd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_controller);

        tv = (TextView) findViewById(R.id.textView);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        btnColourPickRed = (Button) findViewById(R.id.btnRed) ;
        btnColourPickGreen = (Button) findViewById(R.id.btnGreen) ;
        btnColourPickBlue = (Button) findViewById(R.id.btnBlue) ;
        btnColourPickCyan = (Button) findViewById(R.id.btnCyan) ;
        btnColourPickYellow = (Button) findViewById(R.id.btnYellow) ;
        btnColourPickMagenta = (Button) findViewById(R.id.btnMagenta) ;
        btn_Start = (Button) findViewById(R.id.btn_Start) ;
        btn_Rec = (Button) findViewById(R.id.btn_Rec) ;
        btn_Save = (Button) findViewById(R.id.btn_Save) ;
        btn_Stop = (Button) findViewById(R.id.btn_Stop) ;
        btn_Delete = (Button) findViewById(R.id.btn_Del) ;

        btnColourPickRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(recordModeState){
                     dropdown.setEnabled(true);
                 }
                 else
                 {
                     BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                     mBluetoothLeService.writeCharacteristic(characteristic,"k,0000;?");
                 }


            }
        });

        btnColourPickGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  if(recordModeState){
                      dropdown.setEnabled(true);
                  }
                  else
                  {
                       BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                       mBluetoothLeService.writeCharacteristic(characteristic,"v,0000;?");

                  }


            }
        });

        btnColourPickBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(recordModeState){
                     dropdown.setEnabled(true);
                 }
                 else
                 {
                      BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                      mBluetoothLeService.writeCharacteristic(characteristic,"m,0000;?");

                 }


            }
        });

        btnColourPickCyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(recordModeState){
                     dropdown.setEnabled(true);
                 }
                 else
                 {

                       BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                       mBluetoothLeService.writeCharacteristic(characteristic,"t,0000;?");
                 }


            }
        });

        btnColourPickYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordModeState){
                    dropdown.setEnabled(true);
                }
                else
                {
                      BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                      mBluetoothLeService.writeCharacteristic(characteristic,"r,0000;?");

                }


            }
        });

        btnColourPickMagenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordModeState){
                    dropdown.setEnabled(true);
                }
                else
                {
                   BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                   mBluetoothLeService.writeCharacteristic(characteristic,"a,0000;?");

                }


            }
        });

        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordModeState){
                    dropdown.setEnabled(true);

                }
                else
                {
                     BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                     mBluetoothLeService.writeCharacteristic(characteristic,"m,0100;v,0100;k,0100;?");

                }



            }
        });

        btn_Rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordMode(true);
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 recordMode(false);
            }
        });

        btn_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        Intent intent = getIntent();

        mDeviceAddress = intent.getStringExtra(ListDevices.EXTRA_DEVICE_ADDRESS);
        mDeviceName = intent.getStringExtra(ListDevices.EXTRA_DEVICE_NAME);

        //Toast.makeText(getApplicationContext(), mDeviceAddress + " - " + mDeviceName, Toast.LENGTH_SHORT).show();

        Intent gattServiceIntent = new Intent(this, RBLService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        dropdown = (Spinner)findViewById(R.id.lst_DropdownTime);
        String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);


    }

    private void recordMode(Boolean setOn){
        recordModeState = setOn;
        if(setOn){
            btn_Delete.setEnabled(false);
            btn_Start.setEnabled(false);
            btn_Rec.setEnabled(false);
            btn_Save.setEnabled(true);
            btn_Stop.setEnabled(false);
            btn_Delete.setEnabled(false);
            dropdown.setEnabled(false);
        }
        else
        {
            btn_Delete.setEnabled(false);
            btn_Start.setEnabled(false);
            btn_Rec.setEnabled(true);
            btn_Save.setEnabled(false);
            btn_Stop.setEnabled(false);
            btn_Delete.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_light_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.viewSettings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBluetoothLeService.disconnect();
        mBluetoothLeService.close();

        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mGattUpdateReceiver);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(RBLService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(RBLService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(RBLService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(RBLService.ACTION_DATA_AVAILABLE);

        return intentFilter;
    }


    private void displayData(byte[] byteArray) {
        if (byteArray != null) {
            String data = new String(byteArray);

            tv.append(data);
            // find the amount we need to scroll. This works by
            // asking the TextView's internal layout for the position
            // of the final line and then subtracting the TextView's height
            final int scrollAmount = tv.getLayout().getLineTop(tv.getLineCount()) - tv.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                tv.scrollTo(0, scrollAmount);
            else
                tv.scrollTo(0, 0);
        }
    }

    private void getGattService(BluetoothGattService gattService) {
        if (gattService == null)
            return;

        BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(RBLService.UUID_BLE_SHIELD_TX);
        map.put(characteristic.getUuid(), characteristic);

        BluetoothGattCharacteristic characteristicRx = gattService.getCharacteristic(RBLService.UUID_BLE_SHIELD_RX);
        mBluetoothLeService.setCharacteristicNotification(characteristicRx, true);
        mBluetoothLeService.readCharacteristic(characteristicRx);
    }
}
