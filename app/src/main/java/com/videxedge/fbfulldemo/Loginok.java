package com.videxedge.fbfulldemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.videxedge.fbfulldemo.FBref.refAuth;
import static com.videxedge.fbfulldemo.FBref.refUsers;

public class Loginok extends AppCompatActivity {

    String name, email, uid;
    TextView tVnameview, tVemailview, tVuidview;
    CheckBox cBconnectview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginok);

        tVnameview=(TextView)findViewById(R.id.tVnameview);
        tVemailview=(TextView)findViewById(R.id.tVemailview);
        tVuidview=(TextView)findViewById(R.id.tVuidview);
        cBconnectview=(CheckBox)findViewById(R.id.cBconnectview);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = refAuth.getCurrentUser();
        uid = user.getUid();
        Query query = refUsers
                .orderByChild("uid")
                .equalTo(uid)
                .limitToFirst(1);
        query.addListenerForSingleValueEvent(ValueEventListener);
        email = user.getEmail();
        tVemailview.setText(email);
        uid = user.getUid();
        tVuidview.setText(uid);
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        cBconnectview.setChecked(isChecked);
    }

    com.google.firebase.database.ValueEventListener ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dS) {
            if (dS.exists()) {
                for(DataSnapshot data : dS.getChildren()) {
                    User user = data.getValue(User.class);
                    tVnameview.setText(user.getName());
                }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    public void update(View view) {
        FirebaseUser user = refAuth.getCurrentUser();
        if (!cBconnectview.isChecked()){
            refAuth.signOut();
        }
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean("stayConnect",cBconnectview.isChecked());
        editor.commit();
        finish();
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuLogin) {
            Intent si = new Intent(Loginok.this,Loginok.class);
            startActivity(si);
        }
        if (id==R.id.menuDB) {
//            Intent si = new Intent(Loginok.this,Database.class);
//            startActivity(si);
        }
        if (id==R.id.menuStore) {
            Intent si = new Intent(Loginok.this,Storing.class);
            startActivity(si);
        }
        return true;
    }
}
