package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hhu.carrental.R;

public class UserInfoActivity extends Activity {

    Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        loginbtn = (Button)findViewById(R.id.login0);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserInfoActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
