package com.videxedge.fbfulldemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static com.google.firebase.storage.StorageException.ERROR_OBJECT_NOT_FOUND;
import static com.videxedge.fbfulldemo.FBref.refFiles;
import static com.videxedge.fbfulldemo.FBref.refImages;

public class Filestore extends AppCompatActivity {

    EditText eTfile, eTcontent;
    TextView tVcontent;
    String fname,fcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filestore);

        eTfile=(EditText)findViewById(R.id.eTfile);
        eTcontent=(EditText)findViewById(R.id.eTcontent);
        tVcontent=(TextView)findViewById(R.id.tVcontent);
    }

    /**
     * Downloading selected text file from Firebase Storage
     * <p>
     * 1. Checking if filename is empty
     * 2. Reading the contect from Edittext
     * 3. Creating local file
     * 4. Uploading file to FirebaseStorage
     * 5. deleting local file
     *
     * @param view
     */
    public void upload(View view) {
        fname=eTfile.getText().toString();
        if (fname.isEmpty()) {
            Toast.makeText(this, "Please fill filename to upload", Toast.LENGTH_LONG).show();
        } else {
            fname+=".txt";
            fcontent=eTcontent.getText().toString();

            try {
                FileOutputStream fos = openFileOutput(fname, Context.MODE_APPEND);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(fcontent);
                bw.close();
                osw.close();
                fos.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }   catch (IOException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

            final ProgressDialog pd=ProgressDialog.show(this,"Upload file","Uploading...",true);

            StorageReference refFile = refFiles.child(fname);

            final File localFile = new File(this.getFilesDir(), fname);
            refFile.putFile(Uri.fromFile(localFile))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(Filestore.this, "File Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            pd.dismiss();
                            Toast.makeText(Filestore.this, "File uploading failed", Toast.LENGTH_LONG).show();
                            Log.e("Filestore",exception.toString());
                        }
                    });
            deleteFile(fname);
        }
    }

    /**
     * Downloading selected text file from Firebase Storage
     * <p>
     * 1. Checking if filename is empty
     * 2. Downloading file from FirebaseStorage to local file
     * 3. Reading the contect from file to string
     * 4. presenting the string to Textview
     * 5. deleting local file
     *
     * @param view
     */
    public void download(View view) throws IOException {
        fname=eTfile.getText().toString();
        if (fname.isEmpty()) {
            Toast.makeText(this, "Please fill filename to download", Toast.LENGTH_LONG).show();
        } else {
            fname+=".txt";

            final ProgressDialog pd=ProgressDialog.show(this,"File download","downloading...",true);

            StorageReference refFile = refFiles.child(fname);

            final File localFile = new File(this.getFilesDir(), fname);
            refFile.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(Filestore.this, "File download success", Toast.LENGTH_LONG).show();
                    try {
                        InputStream is=openFileInput(fname);
                        InputStreamReader isr=new InputStreamReader(is);
                        BufferedReader br=new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        is.close();
                        isr.close();
                        br.close();
                        tVcontent.setText(sb);
                        deleteFile(fname);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(Filestore.this, "File not downloaded yet", Toast.LENGTH_LONG).show();
                    } catch (IOException exception) {
                        Log.e("Filestore",exception.toString());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pd.dismiss();
                    if (((StorageException) exception).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        Toast.makeText(Filestore.this, "File not exist in storage", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Filestore.this, "File download failed", Toast.LENGTH_LONG).show();
                        Log.e("Filestore",exception.toString());
                    }
                }
            });
        }
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
