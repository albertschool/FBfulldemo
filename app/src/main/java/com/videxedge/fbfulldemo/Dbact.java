package com.videxedge.fbfulldemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.videxedge.fbfulldemo.FBref.refUsers;
import static com.videxedge.fbfulldemo.R.layout.support_simple_spinner_dropdown_item;

public class Dbact extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spfield, spnum;
    ListView lv;
    String [] fields, nums;
    int field=0;
    int num=0;
    ArrayList<String> datalist;
    ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbact);

        spfield=(Spinner)findViewById(R.id.spfield);
        spnum=(Spinner)findViewById(R.id.spnum);
        lv=(ListView)findViewById(R.id.lv);

        fields=new String[]{"Choose field:", "name", "email", "phone", "uid"};
        ArrayAdapter<String> adpfield=new ArrayAdapter<String>(this, support_simple_spinner_dropdown_item,fields);
        spfield.setAdapter(adpfield);
        spfield.setOnItemSelectedListener(this);

        nums=new String[]{"Choose amount:", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        ArrayAdapter<String> adpnum=new ArrayAdapter<String>(this, support_simple_spinner_dropdown_item,nums);
        spnum.setAdapter(adpnum);
        spnum.setOnItemSelectedListener(this);

        datalist = new ArrayList<>();
    }

    ValueEventListener VEL = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dS) {
            datalist.clear();
            if (dS.exists()) {
                for (DataSnapshot data : dS.getChildren()) {
                    User user = data.getValue(User.class);
                    switch (field) {
                        case 1:
                            datalist.add(user.getName());
                            break;
                        case 2:
                            datalist.add(user.getEmail());
                            break;
                        case 3:
                            datalist.add(user.getPhone());
                            break;
                        case 4:
                            datalist.add(user.getUid());
                            break;
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (spfield.equals(parent)) {
            field=pos;
        } else if (spnum.equals(parent)) {
                num=pos;
        }
        if (field!=0 && num!=0) {
            Query query = refUsers
                    .orderByChild(fields[field - 1])
                    .equalTo(fields[field - 1])
                    .limitToFirst(num);
            query.addListenerForSingleValueEvent(VEL);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void showbtn(View view) {
        if (field==0 || num==0) {
            Toast.makeText(this, "Please choose data to show", Toast.LENGTH_LONG).show();
        } else {
            ArrayAdapter<String> adp=new ArrayAdapter<String>(this, support_simple_spinner_dropdown_item,datalist);
            lv.setAdapter(adp);
//            adp.notifyDataSetChanged();
        }
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
        else if (id==R.id.menuDB) {
            Intent si = new Intent(Dbact.this,Dbact.class);
            startActivity(si);
        }
        else if (id==R.id.menuStoreimage) {
            Intent si = new Intent(Dbact.this,Storing.class);
            startActivity(si);
        }
        else if (id==R.id.menuStorefile) {
            Intent si = new Intent(Dbact.this,Filestore.class);
            startActivity(si);
        }
        return true;
    }
}
