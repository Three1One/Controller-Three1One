package org.three1one.simplegui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class LightController extends Activity {

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

        btnColourPickRed = (Button) findViewById(R.id.btnRed) ;
        btnColourPickGreen = (Button) findViewById(R.id.btnGreen) ;
        btnColourPickBlue = (Button) findViewById(R.id.btnBlue) ;
        btnColourPickCyan = (Button) findViewById(R.id.btnCyan) ;
        btnColourPickYellow = (Button) findViewById(R.id.btnYellow) ;
        btnColourPickMagenta = (Button) findViewById(R.id.btnMagenta) ;

        btnColourPickRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "btnColourPickRed", Toast.LENGTH_SHORT).show();
            }
        });

        btnColourPickGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "btnColourPickGreen", Toast.LENGTH_SHORT).show();
            }
        });

        btnColourPickBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "btnColourPickBlue", Toast.LENGTH_SHORT).show();
            }
        });

        btnColourPickCyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "btnColourPickCyan", Toast.LENGTH_SHORT).show();
            }
        });

        btnColourPickYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "btnColourPickYellow", Toast.LENGTH_SHORT).show();
            }
        });

        btnColourPickMagenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "btnColourPickMagenta", Toast.LENGTH_SHORT).show();
            }
        });

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
