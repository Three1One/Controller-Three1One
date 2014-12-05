package org.three1one.simplegui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_devices);


        String filename = "savedDevices";
        String string = "phone1,0124953;phone2,34564;phone3,349663;";


        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String readLine ="";
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput(filename);
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

        ListView listDevices = (ListView) findViewById(R.id.listView);
        // create the grid item mapping
        String[] from = new String[] {"devName", "devAddress"};
        int[] to = new int[] { R.id.devName, R.id.devAddress};
        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();

        String[] devices = readLine.split(";");
        for( String n : devices) {
            String[] deviceDetails = n.split(",");

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("devName",deviceDetails[0]);
            map.put("devAddress",deviceDetails[1]);
            fillMaps.add(map);
        }

        for(int a=0;a<5;a++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("devName","new Device: " + a);
            map.put("devAddress","dev ID: " + a);
            fillMaps.add(map);
        }

        SimpleAdapter mAdapter = new SimpleAdapter(getApplicationContext(), fillMaps, R.layout.grid_item, from, to);
        listDevices.setAdapter(mAdapter);

        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
                Intent newActivity = new Intent(ListDevices.this, LightController.class);
                startActivity(newActivity);
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
