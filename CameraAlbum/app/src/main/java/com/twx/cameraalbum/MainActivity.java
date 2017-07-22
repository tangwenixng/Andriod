package com.twx.cameraalbum;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private ImageView pic;
    private Button choosePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pic =  (ImageView) findViewById(R.id.pic);

        Button takePhoto = (Button) findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outFile = new File(getExternalCacheDir(), "output_image.jpg");
                if (outFile.exists()) {
                    outFile.delete();
                }
               imageUri =   FileProvider.getUriForFile(MainActivity.this, "com.twx.cameraalbum.fileprovider", outFile);

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        choosePhoto = (Button) findViewById(R.id.choosePhoto);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "will request permission");
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });


    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Log.d("Main", "onRequestPermissionsResult: false"+grantResults[0]);
                    Toast.makeText(this, "You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        pic.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    Uri uri = data.getData();
                    System.out.println(uri);
                    if ("content".equalsIgnoreCase(uri.getScheme())) {
                        Log.d("Main", "onActivityResult: content");
                        imagePath = getImagePath(uri,null);
                    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                        Log.d("Main", "onActivityResult: file");
                        imagePath = uri.getPath();
                    }
                    displayImage(imagePath);
                }
                break;
            default:
                break;
        }
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            pic.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this,"fail to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri,String selection) {
        String imagePath = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                System.out.println(MediaStore.Images.Media.DATA);
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                System.out.println("ImagePath-->>"+imagePath);
            }
            cursor.close();
        }
        return  imagePath;
    }
}
