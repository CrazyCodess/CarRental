package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.util.StatusBarUtils;

import cn.bmob.v3.listener.ResetPasswordByEmailListener;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class ResetPasswordActivity extends Activity implements View.OnClickListener,GestureDetector.OnGestureListener{

    private ImageButton back;
    private EditText email;
    private TextView suretv;
    private ImageView textClear;
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
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);
        setContentView(R.layout.activity_reset_password);
        initView();
    }

    private void initView(){
        detector = new GestureDetector(this,this);
        back = (ImageButton)findViewById(R.id.forget_pass_back);
        email = (EditText)findViewById(R.id.et_resetpassword_email);
        suretv = (TextView)findViewById(R.id.btn_sure);
        textClear = (ImageView)findViewById(R.id.clear_text);
        textClear.setOnClickListener(this);
        suretv.setOnClickListener(this);
        back.setOnClickListener(this);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    textClear.setVisibility(View.VISIBLE);
                }else{
                    textClear.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.forget_pass_back:
                finish();
                break;
            case R.id.btn_sure:
                final String emails = email.getText().toString();
                User.resetPasswordByEmail(this, emails, new ResetPasswordByEmailListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ResetPasswordActivity.this, "重置密码成功，请到"+emails+"查看邮件并进行密码重置", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class).setFlags(FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(ResetPasswordActivity.this, "重置密码失败，请检查邮箱是否填写正确", Toast.LENGTH_LONG).show();
                    }
                });

                break;
            case R.id.clear_text:
                email.setText(null);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
