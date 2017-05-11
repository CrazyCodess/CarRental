package com.hhu.carrental.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.main.MainActivity;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登陆
 */
public class LoginActivity extends Activity  implements View.OnClickListener {

    private EditText userName;
    private EditText password;
    private Button loginbtn;
    private Button regisbtn;
    private BmobUserManager userManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = BmobUserManager.getInstance(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        userName = (EditText)findViewById(R.id.et_username);
        password = (EditText)findViewById(R.id.et_password);
        loginbtn = (Button)findViewById(R.id.btn_login);
        regisbtn = (Button)findViewById(R.id.btn_register_1);
        regisbtn.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
    }

    public void onClick(View v){

        final ProgressDialog mpd = new ProgressDialog(this);
        String username = userName.getText().toString();
        String passwd = password.getText().toString();

        switch (v.getId()){
            case R.id.btn_login:
                if(username.equals("")||passwd.equals("")){
                    Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_LONG).show();
                }else{
                    final User user = new User();
                    user.setUsername(username.trim());
                    user.setPassword(passwd.trim());
                    mpd.setTitle("请稍后");
                    mpd.setMessage("正在登陆...");
                    mpd.show();
                    userManager.login(user, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            if(user.getEmailVerified()){
                                mpd.setMessage("登陆成功");
                                mpd.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else{
                                mpd.setMessage("请先验证邮箱并激活账号");
                                mpd.dismiss();
                            }


                        }

                        @Override
                        public void onFailure(int i, String s) {
                            mpd.setMessage("登录失败,请输入正确的用户名和密码");
                            mpd.dismiss();
                        }
                    });
                }
                break;
            case R.id.btn_register_1:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
