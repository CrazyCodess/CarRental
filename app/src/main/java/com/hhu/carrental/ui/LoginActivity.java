package com.hhu.carrental.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.main.MainActivity;
import com.hhu.carrental.util.StatusBarUtils;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.listener.SaveListener;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * 登陆
 */
public class LoginActivity extends Activity  implements View.OnClickListener ,GestureDetector.OnGestureListener{

    private EditText userName;
    private EditText password;
    private Button loginbtn;
    private Button regisbtn;
    private ImageButton back;
    private BmobUserManager userManager;
    private TextView forgetPass;
    private ImageView clearUsername,clearPassword;
    private GestureDetector detector;
    public final int FLIP_DISTANCE = 50;
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e2.getX()-e1.getX()>FLIP_DISTANCE){
            finish();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = BmobUserManager.getInstance(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        detector = new GestureDetector(this,this);
        userName = (EditText)findViewById(R.id.et_username);
        password = (EditText)findViewById(R.id.et_password);
        forgetPass = (TextView)findViewById(R.id.foget_password);
        clearUsername = (ImageView)findViewById(R.id.clear_username);
        clearPassword = (ImageView)findViewById(R.id.clear_password);
        loginbtn = (Button)findViewById(R.id.btn_login);
        back = (ImageButton)findViewById(R.id.login_back);
        back.setOnClickListener(this);
        regisbtn = (Button)findViewById(R.id.btn_register_1);
        clearUsername.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        regisbtn.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    clearUsername.setVisibility(View.VISIBLE);
                }
                else {
                    clearUsername.setVisibility(View.GONE);
                }
            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    clearPassword.setVisibility(View.VISIBLE);
                }
                else {
                    clearPassword.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onClick(View v){

        final ProgressDialog mpd = new ProgressDialog(this);
        String username = userName.getText().toString();
        String passwd = password.getText().toString();

        switch (v.getId()){
            case R.id.login_back:
                finish();
                break;
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
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(FLAG_ACTIVITY_CLEAR_TOP));
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
                break;
            case R.id.foget_password:
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
                break;
            case R.id.clear_username:
                userName.setText(null);
                break;
            case R.id.clear_password:
                password.setText(null);
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
