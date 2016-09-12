package com.echo.sharephoto.util;

import android.content.Context;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Echo on 2016/9/9.
 */
public  class NfcUtil implements NfcAdapter.CreateBeamUrisCallback {

    private Context context;

    public NfcUtil(){

    }

    public NfcUtil(Context context){
        setContext(context);
    }



    @Override
    public  Uri[] createBeamUris(NfcEvent event) {
        return (Uri[]) getUris().toArray();
    }

    /**
     * 获取一组Uri
     * @return
     */
    private List<Uri> getUris(){
        List<Uri> uris=null;
        File[] files=new File(context.getFilesDir(),"images").listFiles();
        if (files[0]!=null) uris=new ArrayList<>();
        for (File file:files
                ) {
            file.setReadable(true,false);
            Uri uri=Uri.fromFile(file);
            uris.add(uri);
        }

        return  uris;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
