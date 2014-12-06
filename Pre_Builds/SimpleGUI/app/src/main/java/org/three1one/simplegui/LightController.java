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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class LightController extends Activity {
    private final static String TAG = LightController.class.getSimpleName();
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

        btnColourPickRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                characteristic.setValue("b");
                mBluetoothLeService.writeCharacteristic(characteristic);
            }
        });

        btnColourPickGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                characteristic.setValue("B");
                mBluetoothLeService.writeCharacteristic(characteristic);
            }
        });

        btnColourPickBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                characteristic.setValue("a");
                mBluetoothLeService.writeCharacteristic(characteristic);
            }
        });

        btnColourPickCyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                characteristic.setValue("A");
                mBluetoothLeService.writeCharacteristic(characteristic);
            }
        });

        btnColourPickYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                characteristic.setValue("A");
                mBluetoothLeService.writeCharacteristic(characteristic);
            }
        });

        btnColourPickMagenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);
                characteristic.setValue("A");
                mBluetoothLeService.writeCharacteristic(characteristic);
            }
        });

        Intent intent = getIntent();

        mDeviceAddress = intent.getStringExtra(ListDevices.EXTRA_DEVICE_ADDRESS);
        mDeviceName = intent.getStringExtra(ListDevices.EXTRA_DEVICE_NAME);

        //Toast.makeText(getApplicationContext(), mDeviceAddress + " - " + mDeviceName, Toast.LENGTH_SHORT).show();

        Intent gattServiceIntent = new Intent(this, RBLService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

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
