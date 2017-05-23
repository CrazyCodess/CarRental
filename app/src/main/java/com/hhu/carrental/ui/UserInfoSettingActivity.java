package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.util.CheckNetwork;
import com.hhu.carrental.util.StatusBarUtils;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * 设置用户名或手机号
 */
public class UserInfoSettingActivity extends Activity implements View.OnClickListener{

    private TextView savebtn,titleName;
    private EditText resetNick,resetPhone;
    private ImageView clearText;
    private String msg;
    private ImageButton back;
    private Intent intent;
    private User user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);

        intent = getIntent();
        msg = intent.getStringExtra("msg");

        setContentView(R.layout.activity_user_info_setting);
        titleName = (TextView)findViewById(R.id.user_info_setting);

        initView();
    }

    private void initView(){
        resetNick = (EditText)findViewById(R.id.reset_nick);
        resetPhone = (EditText)findViewById(R.id.reset_phone);
        if(msg.equals("phone")){
            titleName.setText("手机号");
            resetPhone.setVisibility(View.VISIBLE);
            resetNick.setVisibility(View.GONE);
            resetPhone.setText(intent.getStringExtra("content"));
        }
        user = BmobUserManager.getInstance(this).getCurrentUser(User.class);
        clearText = (ImageView)findViewById(R.id.clear_info);
        if(!intent.getStringExtra("content").equals("")){
            clearText.setVisibility(View.VISIBLE);
        }
        savebtn = (TextView)findViewById(R.id.btn_save);
        savebtn.setOnClickListener(this);
        back = (ImageButton)findViewById(R.id.user_info_setting_back);
        back.setOnClickListener(this);
        clearText.setOnClickListener(this);
        resetNick.setText(intent.getStringExtra("content"));

        resetNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    clearText.setVisibility(View.VISIBLE);
                }else{
                    clearText.setVisibility(View.GONE);
                }
            }
        });

        resetPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    clearText.setVisibility(View.VISIBLE);
                }else{
                    clearText.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear_info:
                resetNick.setText(null);
                resetPhone.setText(null);
                break;
            case R.id.user_info_setting_back:
                finish();
                break;
            case R.id.btn_save:
                updateInfo();
                break;
        }
    }

    private void updateInfo(){


        if(msg.equals("nick")){
            user.setUsername(resetNick.getText().toString());
        }else{
            user.setMobilePhoneNumber(resetPhone.getText().toString());
            //user.setSex(false);
            Log.e("电话===",resetPhone.getText().toString());
        }
        user.update(UserInfoSettingActivity.this,user.getObjectId(),new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                //toast("更新用户信息成功:");
                startActivity(new Intent(UserInfoSettingActivity.this,UserSettingActivity.class).setFlags(FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                //toast("更新用户信息失败:" + msg);
                if(!CheckNetwork.isNetworkAvailable(UserInfoSettingActivity.this)){
                    Toast.makeText(UserInfoSettingActivity.this, "当前网络不可用，请检查您的网络", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(UserInfoSettingActivity.this, "设置失败", Toast.LENGTH_LONG).show();
                }
                Log.e("error====",code+msg+"");
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
