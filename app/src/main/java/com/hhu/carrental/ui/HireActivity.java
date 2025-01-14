package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.BikeInfo;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.main.MainActivity;
import com.hhu.carrental.util.StatusBarUtils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

public class HireActivity extends Activity implements View.OnClickListener ,GestureDetector.OnGestureListener {

    private String bikeLat;
    private String bikeLon;
    private TextView biketype,hiretime,bikephone,bikePwd,bikedetail,rentSTime,bikeOwener;
    private BikeInfo bikeInfo;
    private Button hireSure;
    private ImageButton back;
    private GestureDetector detector;
    private final int FLIP_DISTANCE = 50;
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
        setContentView(R.layout.activity_hire);
        Intent intent = getIntent();
        bikeInfo = (BikeInfo)intent.getSerializableExtra("bikeInfo");

        bikeOwener = (TextView)findViewById(R.id.bike_owner);
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(this,bikeInfo.getUser().getObjectId(),new  GetListener<User>() {
            @Override
            public void onSuccess(User object) {
                // TODO Auto-generated method stub
                bikeOwener.setText(object.getUsername());
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(HireActivity.this,"查询失败",Toast.LENGTH_LONG).show();
            }
        });

        init();
    }

    private void init(){
        back = (ImageButton)findViewById(R.id.hire_back);
        back.setOnClickListener(this);
        detector = new GestureDetector(this,this);
        biketype = (TextView)findViewById(R.id.bike_t);

        hireSure = (Button)findViewById(R.id.hire_sure);
        hiretime = (TextView)findViewById(R.id.hire_time);
        rentSTime = (TextView)findViewById(R.id.rent_start_time);
        bikephone = (TextView)findViewById(R.id.bike_con);
        bikePwd = (TextView)findViewById(R.id.bike_paw);
        bikedetail = (TextView)findViewById(R.id.bike_desc);
        biketype.setText(bikeInfo.getBikeType());
        hiretime.setText(bikeInfo.getRentTime());
        bikephone.setText(bikeInfo.getPhoneNumber());
        bikePwd.setText(bikeInfo.getUnlockPass());
        bikedetail.setText(bikeInfo.getBikeDetail());
        rentSTime.setText(bikeInfo.getCreatedAt());

        //bikeOwener.setText(bikeInfo.getUser().getUsername());
        ///Log.e("bikeInfo:====",(bikeInfo.getUser()==null)+"*"+bikeInfo.getUser().getUsername()+"*"+bikeInfo.getUser().getObjectId());
        hireSure.setOnClickListener(this);
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.hire_sure:
                BikeInfo bikeupdate = new BikeInfo();
                bikeupdate.setUsed(true);
                bikeupdate.update(this, bikeInfo.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("sucess","成功成功成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.e("failure","失败失败失败");
                    }
                });
                Intent intent = new Intent(HireActivity.this, MainActivity.class);
                intent.putExtra("msg","hire");
                intent.putExtra("bikeInfo",bikeInfo);
                startActivity(intent);
                finish();
                //MainActivity.finish();
                break;
            case R.id.hire_back:
                finish();
                break;
            default:
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
