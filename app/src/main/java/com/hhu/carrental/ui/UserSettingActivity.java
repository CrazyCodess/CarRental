package com.hhu.carrental.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.util.StatusBarUtils;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 个人信息
 */
public class UserSettingActivity extends Activity implements GestureDetector.OnGestureListener,View.OnClickListener{

    private GestureDetector detector;
    private final int FLIP_DISTANCE = 50;
    private ImageButton back;
    private RelativeLayout layoutSex,layoutNick,layoutPhone;
    private TextView showSex,showNick,showEmail,showPhone;
    private BmobUserManager userManager;
    private User user;
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
        setContentView(R.layout.activity_user_setting);
        userManager = BmobUserManager.getInstance(this);
        initView();
    }

    private void initView(){
        user = userManager.getCurrentUser(User.class);
        detector = new GestureDetector(this,this);
        showNick = (TextView)findViewById(R.id.nick_name);
        layoutNick = (RelativeLayout)findViewById(R.id.layout_nick);
        layoutPhone = (RelativeLayout)findViewById(R.id.layout_phone);
        back = (ImageButton)findViewById(R.id.user_setting_back);
        layoutSex = (RelativeLayout)findViewById(R.id.layout_sex);
        showSex = (TextView)findViewById(R.id.show_sex);
        showEmail = (TextView)findViewById(R.id.show_email);
        showPhone = (TextView)findViewById(R.id.show_phone);
        showNick.setText(user.getUsername());
        showSex.setText(user.getSex()?"男":"女");
        showEmail.setText(user.getEmailVerified()?"已验证":"未验证");
        layoutSex.setOnClickListener(this);
        showPhone.setText((user.getMobilePhoneNumber().equals("")||user.getMobilePhoneNumber()==null)?"未填写":user.getMobilePhoneNumber());
        back.setOnClickListener(this);
        layoutPhone.setOnClickListener(this);
        layoutNick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,UserInfoSettingActivity.class);
        switch (v.getId()){
            case R.id.user_setting_back:
                finish();
                break;
            case R.id.layout_sex:
                updateSex();
                break;
            case R.id.layout_nick:
                intent.putExtra("msg","nick");
                intent.putExtra("content",user.getUsername());
                startActivity(intent);
                break;
            case R.id.layout_phone:
                intent.putExtra("msg","phone");
                intent.putExtra("content",user.getMobilePhoneNumber());
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void updateSex(){
        new  AlertDialog.Builder(this)

                .setSingleChoiceItems(new  String[] {"男", "女" },  0 ,
                        new  DialogInterface.OnClickListener() {

                            public   void  onClick(DialogInterface dialog,  int  which) {
                                //dialog.

                                user.setSex(which==0);
                                showSex.setText((which==0)?"男":"女");
                                user.update(UserSettingActivity.this,new UpdateListener(){
                                    @Override
                                    public void onSuccess(){
                                        Toast.makeText(UserSettingActivity.this,"设置成功",Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(UserSettingActivity.this,"设置失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        }
                )
               // .setNegativeButton("取消" ,  null )
                .show();


    }
}
