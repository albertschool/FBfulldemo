package com.videxedge.fbfulldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Dbact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbact);
    }


    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuLogin) {
            Intent si = new Intent(Dbact.this,Loginok.class);
            startActivity(si);
        }
        if (id==R.id.menuDB) {
            Intent si = new Intent(Dbact.this,Dbact.class);
            startActivity(si);
        }
        if (id==R.id.menuStore) {
            Intent si = new Intent(Dbact.this,Storing.class);
            startActivity(si);
        }
        return true;
    }

}
