package bestgroupever.vehicleinfotainmentsystem.Controller;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import bestgroupever.vehicleinfotainmentsystem.Controller.MapsActivity;
import bestgroupever.vehicleinfotainmentsystem.Model.Weather_Activity;
import bestgroupever.vehicleinfotainmentsystem.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView main_map_button;
    BluetoothAdapter bluetoothAdapter;
    Intent intent;


    //Creating a calendar object to pull system times and dates for the main display

    Date currentTime = Calendar.getInstance().getTime();
    TextView time;
    public Boolean isSafe = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Gear listeners created by Bryan Cazadero

        Button parkbtn = findViewById(R.id.park);
        Button reversebtn = findViewById(R.id.reverse);
        Button neutral1btn = findViewById(R.id.neutral1);
        Button drivebtn = findViewById(R.id.drive);


        parkbtn.setOnClickListener(this);
        reversebtn.setOnClickListener(this);
        neutral1btn.setOnClickListener(this);
        drivebtn.setOnClickListener(this);

        //boolean blocking user interface

        Boolean isSafe = true;



        ImageView bluetoothOnOff = findViewById(R.id.main_menu_settings);

        //Button bluetoothOnOff = findViewById(R.id.onOffBluetooth);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //set time in the main menu
        time = findViewById(R.id.time_view);
        time.setText(currentTime.toString());


        //button enables and disables bluetooth
        /* bluetoothOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Tag", "onClick: enabling/disabling bluetooth.");
                enableDisableBT();
            }
        }); */

    }

    //this code adds the little settings menu in the top right

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag", "onDestroy: called");
        unregisterReceiver(broadcastReceiver);
    }

    //Method to enable bluetooth if not enabled, and conversely
    protected void enableDisableBT() {
        if (bluetoothAdapter == null) {
            Log.d("tag", "enableDisableBT: Does not have bluetooth capabilities.");
        }

        if (!bluetoothAdapter.isEnabled()) {
            Log.d("tag", "enableDisableBT: enabling bluetooth");
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetoothIntent);

            IntentFilter bluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(broadcastReceiver, bluetoothIntent);
        }

        if (bluetoothAdapter.isEnabled()) {
            Log.d("tag", "enableDisableBT: disabling bluetooth");
            bluetoothAdapter.disable();

            IntentFilter bluetoothIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(broadcastReceiver, bluetoothIntent);
        }
    }

    //Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //When discovery finds a device
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                //tracks the state of the Bluetooth status
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d("Tag", "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d("Tag", "BroadcastReceiver: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d("Tag", "BroadcastReceiver: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d("Tag", "BroadcastReceiver: STATE TURNING ON");
                        break;
                }
            }
        }
    };



//this method is used to make the imageViews work. Call this method in the android:onClick in the xml file

    public void changeLayout(View v){

        switch (v.getId()){

            case R.id.main_gps_button:
               if (isSafe == true){
                setContentView(R.layout.activity_maps);
           /*     intent = new Intent(this, MapsActivity.class);
                startActivity(intent);*/}
                else{
                   Toast.makeText(this,"You are now driving! It is not safe to use this infotainment system.", Toast.LENGTH_LONG).show();
               }
                break;

            case R.id.main_music_button:
                if (isSafe == true) {
                setContentView(R.layout.music_menu);
                intent = new Intent(this, Spotify.class);
                startActivity(intent);}
                else{
                    Toast.makeText(this,"You are now driving! It is not safe to use this infotainment system.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.main_hvac_button:
                intent = new Intent(this, HVACController.class);
                startActivity(intent);
                break;

            case R.id.main_weather_button:
                if (isSafe ==true) {
                intent = new Intent(this, Weather_Activity.class);
                startActivity(intent);}
                else{
                    Toast.makeText(this,"You are now driving! It is not safe to use this infotainment system.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.back_button:
                setContentView(R.layout.activity_main);




        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.park:
                Toast.makeText(this,"user interface is now enabled", Toast.LENGTH_SHORT).show();
                isSafe =true;
                break;
            case R.id.reverse:
                Toast.makeText(this,"user interface is now disabled", Toast.LENGTH_SHORT).show();
                isSafe = false;
                break;
            case R.id.neutral1:
                Toast.makeText(this,"user interface is now enabled", Toast.LENGTH_SHORT).show();
                isSafe = true;
                break;
            case R.id.drive:
                Toast.makeText(this,"user interface is now disabled", Toast.LENGTH_SHORT).show();
                isSafe = false;
                break;
        }
    }
}


