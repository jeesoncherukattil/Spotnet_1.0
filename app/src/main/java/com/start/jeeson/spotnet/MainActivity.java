package com.start.jeeson.spotnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       ImageButton share=(ImageButton)findViewById(R.id.imageButton);
        share.setOnClickListener(this);

       ImageButton join=(ImageButton) findViewById(R.id.imageButton2);
        join.setOnClickListener(this);
    }

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
    public void onClick(View v) {
        if(v.getId()==R.id.imageButton){
            Intent CreateHotspot= new Intent(this,com.start.jeeson.spotnet.CreateHotspot.class);
            startActivity(CreateHotspot);
        }
        if(v.getId()==R.id.imageButton2) {
            Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
            Intent qrscanner = new Intent(MainActivity.this,Scanqrcode
                    .class);
            startActivity(qrscanner);
        }


    }


}

