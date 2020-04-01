package com.videxedge.fbfulldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Filestore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filestore);
    }


    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuLogin) {
            Intent si = new Intent(Filestore.this,Loginok.class);
            startActivity(si);
        }
        else if (id==R.id.menuDB) {
            Intent si = new Intent(Filestore.this,Dbact.class);
            startActivity(si);
        }
        else if (id==R.id.menuStoreimage) {
            Intent si = new Intent(Filestore.this,Storing.class);
            startActivity(si);
        }
        else if (id==R.id.menuStorefile) {
            Intent si = new Intent(Filestore.this,Filestore.class);
            startActivity(si);
        }
        return true;
    }
}
