package com.hhu.carrental.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.util.CheckNetwork;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity  implements View.OnClickListener {

    private EditText eUserName;
    private EditText eUserPassword;
    private EditText mUserPassword;
    private EditText eEmail;
    private Button btn_register;
    private Button	btn_cancel;
    BmobUserManager userManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView(){
        eUserName = (EditText)findViewById(R.id.et_username);
        eUserPassword= (EditText)findViewById(R.id.et_password);
        mUserPassword = (EditText)findViewById(R.id.et_SurePassword);
        eEmail = (EditText) findViewById(R.id.et_email);
        btn_register = (Button)findViewById(R.id.btn_register_2);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        userManager = BmobUserManager.getInstance(this);
    }

    public void onClick(View v){
        final ProgressDialog mpd = new ProgressDialog(this);
        String username = eUserName.getText().toString();
        String userpassword = eUserPassword.getText().toString();
        String mpass = mUserPassword.getText().toString();
        String email = eEmail.getText().toString();
        boolean isNetConnected = CheckNetwork.isNetworkAvailable(this);
        switch (v.getId()){
            case R.id.btn_register_2:
                if(!isNetConnected){
                    Toast.makeText(RegisterActivity.this, "当前网络不可用，请检查您的网络", Toast.LENGTH_LONG).show();
                }else if(username.equals("")||userpassword.equals("")){
                    Toast.makeText(RegisterActivity.this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                }else if(email.equals("")){
                    Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_LONG).show();
                }else if(!userpassword.equals(mpass)){
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致，请重新输入", Toast.LENGTH_LONG).show();
                }else{
                    final User regisUser = new User();
                    regisUser.setUsername(username);
                    regisUser.setPassword(userpassword);
                    regisUser.setEmail(email);
                    regisUser.setInstallId(BmobInstallation.getInstallationId(this));
                    regisUser.setSex(true);
                    regisUser.setDate("20170504");
                    regisUser.setSchool("河海大学");
                    regisUser.setIncredits(new Integer(10));
                    regisUser.setDeviceType("android");
                    mpd.setTitle("请稍后");
                    mpd.setMessage("正在注册...");
                    mpd.show();
                    regisUser.signUp(this, new SaveListener() {
                        @Override
                        public void onSuccess() {

                            mpd.setMessage("注册成功");
                            mpd.dismiss();
                            Toast.makeText(RegisterActivity.this,"注册成功,请前往邮箱验证",Toast.LENGTH_SHORT).show();
                            userManager.bindInstallationForRegister(regisUser.getUsername());
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(RegisterActivity.this,"注册失败"+i+s,Toast.LENGTH_SHORT).show();
                            Log.e("错误错误错误错误错误错误",i+s);
                            mpd.setMessage("注册失败:"+s);
                            mpd.dismiss();
                        }
                    });
                }
                break;
            case R.id.btn_cancel:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
