package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hhu.carrental.R;
import com.hhu.carrental.util.StatusBarUtils;

public class AboutMeActivity extends Activity implements View.OnClickListener{

    private ImageButton back;
    private TextView phone;
//    private TextView phone,email,link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);
        setContentView(R.layout.activity_about_me);
        initView();
    }

    private void initView(){
        back = (ImageButton)findViewById(R.id.about_me_back);
        back.setOnClickListener(this);
        phone = (TextView)findViewById(R.id.my_call);
//        email = (TextView)findViewById(R.id.my_email);
//        link = (TextView)findViewById(R.id.my_link);
        phone.setOnClickListener(this);
//                email.setOnClickListener(this);
//        link.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about_me_back:
                finish();
                break;
            case R.id.my_call:
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText().toString().trim()));
                try{
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
//            case R.id.my_email:
//
//                break;
//            case R.id.my_link:
//                break;
            default:
                break;
        }

    }
}
