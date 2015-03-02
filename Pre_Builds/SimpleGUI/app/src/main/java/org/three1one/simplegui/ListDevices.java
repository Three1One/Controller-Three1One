package org.three1one.simplegui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListDevices extends Activity {
    private BluetoothAdapter mBluetoothAdapter;
    public static List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();
    private String DEVICE_NAME = "devName";
    private String DEVICE_ADDRESS = "devAddress";
    public final static String EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS";
    public final static String EXTRA_DEVICE_NAME = "EXTRA_DEVICE_NAME";

    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 3000;


    private List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
    private ListView listDevices;

    // create the grid item mapping
    private String[] from = new String[] {"devName", "devAddress"};
    private int[] to = new int[] { R.id.devName, R.id.devAddress};
    // prepare the list of all records

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_devices);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Ble not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Ble not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        String readLine ="";
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput("savedDevices");
            int i;
            char c;
            while((i = inputStream.read()) != -1)
            {
                c=(char)i;
                readLine+=c;
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        listDevices = (ListView) findViewById(R.id.listView);


        try {
            String[] devices = readLine.split(";");
            for (String n : devices) {
                String[] deviceDetails = n.split(",");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("devName", deviceDetails[0]);
                map.put("devAddress", deviceDetails[1]);
                fillMaps.add(map);


            }
            refreshList();
        }
        catch(ArrayIndexOutOfBoundsException e){
            Toast.makeText(getApplicationContext(), "no saved devices found", Toast.LENGTH_SHORT).show();
        }

    }

    public void refreshList(){
        SimpleAdapter mAdapter = new SimpleAdapter(getApplicationContext(), fillMaps, R.layout.grid_item, from, to);
        listDevices.setAdapter(mAdapter);

        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            HashMap<String, String> hashMap = (HashMap<String, String>) fillMaps.get(position);
            String addr = hashMap.get(DEVICE_ADDRESS);
            String name = hashMap.get(DEVICE_NAME);

            Intent intent = new Intent(ListDevices.this, LightController.class);
            intent.putExtra(EXTRA_DEVICE_ADDRESS, addr);
            intent.putExtra(EXTRA_DEVICE_NAME, name);
            startActivity(intent);

            finish();
            }
        });
    }

    public void findDevices(){
        fillMaps.clear();
        mDevices.clear();

        scanLeDevice();

    }

    private void scanLeDevice() {
        new Thread() {

            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mLeScanCallback);

                try {
                    Thread.sleep(SCAN_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshList();
                        saveDeviceList();
                    }
                });


            }
        }.start();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device != null) {
                        if (!mDevices.contains(device)) {
                            mDevices.add(device);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("devName", device.getName());
                            map.put("devAddress", device.getAddress());
                            fillMaps.add(map);
                        }
                    }
                }
            });
        }
    };

    public void clearDeviceList(){
        fillMaps.clear();
        mDevices.clear();

        String string = "";
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("savedDevices", Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshList();
    }

    public void saveDeviceList(){
        String string = "";


        for (BluetoothDevice item : mDevices) {
            string=string+item.getName()+","+item.getAddress()+";";
            Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_SHORT).show();
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("savedDevices", Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_devices, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnFindDevices) {
            findDevices();
        }
        if (id == R.id.btnClearDevices){
            clearDeviceList();
        }

        return super.onOptionsItemSelected(item);
    }
}
