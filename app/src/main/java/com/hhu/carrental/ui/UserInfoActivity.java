package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.main.MainActivity;

import cn.bmob.im.BmobUserManager;

public class UserInfoActivity extends Activity implements View.OnClickListener{

    private BmobUserManager userManager;
    private TextView nametv;
    private TextView logout;
    private TextView rentOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = BmobUserManager.getInstance(this);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    private void initView(){
        nametv = (TextView) findViewById(R.id.username);
        logout = (TextView) findViewById(R.id.logout);
        rentOut = (TextView) findViewById(R.id.rent_out);
        User user = userManager.getCurrentUser(User.class);
        nametv.setText(user.getUsername());
        logout.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.logout:
                userManager.logout();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.rent_out:
                startActivity(new Intent(this,RentOutActivity.class));
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
