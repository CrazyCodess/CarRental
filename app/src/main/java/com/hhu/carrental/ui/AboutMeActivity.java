package com.hhu.carrental.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.hhu.carrental.R;
import com.hhu.carrental.util.StatusBarUtils;

public class AboutMeActivity extends Activity implements View.OnClickListener{

    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);
        setContentView(R.layout.activity_about_me);
    }

    private void initView(){
        back = (ImageButton)findViewById(R.id.about_me_back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about_me_back:
                finish();
                break;
            default:
                break;
        }

    }
}
