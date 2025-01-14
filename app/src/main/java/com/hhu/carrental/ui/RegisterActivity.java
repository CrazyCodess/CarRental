package com.hhu.carrental.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.util.CheckNetwork;
import com.hhu.carrental.util.StatusBarUtils;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * 注册
 */
public class RegisterActivity extends Activity  implements View.OnClickListener ,GestureDetector.OnGestureListener{

    private EditText eUserName;
    private EditText eUserPassword;
    private EditText mUserPassword;
    private EditText eEmail;
    private Button btn_register;
    private Button	btn_cancel;
    private BmobUserManager userManager;
    private ImageButton back;
    private ImageView clearRegisname,clearFirstname,clearConfirmname,clearEmail;
    private GestureDetector detector;
    public  final int FLIP_DISTANCE = 50;
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
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView(){
        detector  = new GestureDetector(this,this);
        back = (ImageButton)findViewById(R.id.regis_back);
        back.setOnClickListener(this);
        clearRegisname = (ImageView)findViewById(R.id.clear_regisname);
        clearFirstname = (ImageView)findViewById(R.id.clear_firstpass);
        clearConfirmname = (ImageView)findViewById(R.id.clear_confirmpass);
        clearEmail = (ImageView)findViewById(R.id.clear_email);
        eUserName = (EditText)findViewById(R.id.et_username);
        eUserPassword= (EditText)findViewById(R.id.et_password);
        mUserPassword = (EditText)findViewById(R.id.et_SurePassword);
        eEmail = (EditText) findViewById(R.id.et_email);
        btn_register = (Button)findViewById(R.id.btn_register_2);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        clearRegisname.setOnClickListener(this);
        clearFirstname.setOnClickListener(this);
        clearConfirmname.setOnClickListener(this);
        clearEmail.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        userManager = BmobUserManager.getInstance(this);
        addTextListener();
    }

    private void addTextListener(){
        eUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    clearRegisname.setVisibility(View.VISIBLE);
                }
                else {
                    clearRegisname.setVisibility(View.GONE);
                }
            }
        });

        eUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    clearFirstname.setVisibility(View.VISIBLE);
                }
                else {
                    clearFirstname.setVisibility(View.GONE);
                }
            }
        });

        mUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    clearConfirmname.setVisibility(View.VISIBLE);
                }
                else {
                    clearConfirmname.setVisibility(View.GONE);
                }
            }
        });

        eEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    clearEmail.setVisibility(View.VISIBLE);
                }
                else {
                    clearEmail.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onClick(View v){
        final ProgressDialog mpd = new ProgressDialog(this);
        String username = eUserName.getText().toString();
        String userpassword = eUserPassword.getText().toString();
        String mpass = mUserPassword.getText().toString();
        String email = eEmail.getText().toString();
        boolean isNetConnected = CheckNetwork.isNetworkAvailable(this);
        switch (v.getId()){
            case R.id.clear_regisname:
                eUserName.setText(null);
                break;
            case R.id.clear_firstpass:
                eUserPassword.setText(null);
                break;
            case R.id.clear_confirmpass:
                mUserPassword.setText(null);
                break;
            case R.id.clear_email:
                eEmail.setText(null);
                break;

            case R.id.regis_back:
                finish();
                break;
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
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class).setFlags(FLAG_ACTIVITY_CLEAR_TOP));
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
