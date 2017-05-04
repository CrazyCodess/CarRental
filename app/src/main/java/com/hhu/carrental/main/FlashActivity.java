package com.hhu.carrental.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.hhu.carrental.R;

import java.util.Timer;
import java.util.TimerTask;

public class FlashActivity extends Activity {

    //private static final int GO_LOGIN = 0;
    //private static final int GO_MAIN = 1;
    static Timer timer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_flash);
        //BmobChat.DEBUG_MODE = true;
        //BmobChat.getInstance(this).init("67636fb1d0e031952bd2fb8956cfd1b6");


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(FlashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        System.gc();
    }
}
