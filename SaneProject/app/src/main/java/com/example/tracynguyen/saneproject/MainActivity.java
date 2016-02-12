package com.example.tracynguyen.saneproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tracynguyen.network.LL1Daemon;
import com.example.tracynguyen.network.LL2P;
import com.example.tracynguyen.support.Factory;
import com.example.tracynguyen.support.NetworkConstants;
import com.example.tracynguyen.support.UIManager;

public class MainActivity extends AppCompatActivity {
    Factory myFactory;
    UIManager uiManager;
    LL1Daemon LL1_Daemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myFactory = new Factory(this);
        uiManager = myFactory.getUiManager();
        LL1_Daemon = myFactory.getLL1_Daemon();
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

        if (id == R.id.showIPAddressMenuItem){

            uiManager.raiseToast(NetworkConstants.IP_ADDRESS);
        }

        if (id == R.id.transmitLL2PFrame){
            LL1_Daemon.sendLL2PFrame();
        }

        return super.onOptionsItemSelected(item);
    }
}
