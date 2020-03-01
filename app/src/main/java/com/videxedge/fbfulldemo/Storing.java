package com.videxedge.fbfulldemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.videxedge.fbfulldemo.FBref.refImages;
import java.io.File;
import java.io.IOException;

public class Storing extends AppCompatActivity {

    ImageView iV;

    int Gallery=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storing);

        iV=(ImageView)findViewById(R.id.iV);
    }

    /**
     * Selecting image file to upload to Firebase Storage
     * <p>
     *
     * @param view
     */
    public void upload(View view) {
        Intent si = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(si, Gallery);
    }

    /**
     * Uploading selected image file to Firebase Storage
     * <p>
     *
     * @param requestCode   The call sign of the intent that requested the result
     * @param resultCode    A code that symbols the status of the result of the activity
     * @param data          The data returned
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Gallery) {
                Uri file = data.getData();
                if (file != null) {
                    final ProgressDialog pd=ProgressDialog.show(this,"Upload image","Uploading...",true);
                    StorageReference refImg = refImages.child("aaa.jpg");
                    refImg.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    pd.dismiss();
                                    Toast.makeText(Storing.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    pd.dismiss();
                                    Toast.makeText(Storing.this, "Upload failed", Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "No Image was selected", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Downloading selected image file from Firebase Storage
     * <p>
     *
     * @param view
     */
    public void download(View view) throws IOException {
        final ProgressDialog pd=ProgressDialog.show(this,"Image download","downloading...",true);

        StorageReference refImg = refImages.child("aaa.jpg");

        final File localFile = File.createTempFile("aaa","jpg");
        refImg.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(Storing.this, "Image download success", Toast.LENGTH_LONG).show();
                String filePath = localFile.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                iV.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Toast.makeText(Storing.this, "Image download failed", Toast.LENGTH_LONG).show();
            }
        });
    }


    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menuLogin) {
            Intent si = new Intent(Storing.this,Loginok.class);
            startActivity(si);
        }
        if (id==R.id.menuDB) {
//            Intent si = new Intent(Loginok.this,Database.class);
//            startActivity(si);
        }
        if (id==R.id.menuStore) {
            Intent si = new Intent(Storing.this,Storing.class);
            startActivity(si);
        }
        return true;
    }
}
