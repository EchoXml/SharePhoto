package com.echo.sharephoto;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.echo.sharephoto.util.NfcUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Intent mRequestIntent;

    private final static Integer SHARE_REQUEST_CODE=1;

    NfcAdapter nfcAdapter;

    private Activity thisActivity;

    private NfcUtil nfcUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestIntent=new Intent(Intent.ACTION_PICK);
        mRequestIntent.setType("image/png");
        thisActivity=this;
        Button btnShare= (Button) findViewById(R.id.btnRequestShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Intent.createChooser(mRequestIntent,"选择"),SHARE_REQUEST_CODE);
            }
        });
        Button btnTestNfc= (Button) findViewById(R.id.btnTestNfc);

        nfcUtil=new NfcUtil(getApplicationContext());


        btnTestNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)){
                    Toast.makeText(getApplicationContext(),"很抱歉，您的手持设备不具有Nfc功能！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"恭喜，您的手持设备具有Nfc功能！",Toast.LENGTH_SHORT).show();
                    nfcAdapter=NfcAdapter.getDefaultAdapter(getApplicationContext());
                    nfcAdapter.setBeamPushUrisCallback(nfcUtil,thisActivity);
                }
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    Uri contact = data.getData();
                    String mimeType=getContentResolver().getType(contact);
                    Cursor cursor=getContentResolver().query(contact,null,null,null,null);
                    int nameIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE);
                    cursor.moveToFirst();
                    String name=cursor.getString(nameIndex);
                    String size=cursor.getString(sizeIndex);
                    Toast.makeText(getApplicationContext(),mimeType+" "+name+" "+size,Toast.LENGTH_LONG).show();
                    try {
                        Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(), contact);
                        ImageView imageView= (ImageView) findViewById(R.id.ivPhoto);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
    }
}
