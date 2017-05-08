package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hhu.carrental.R;
import com.hhu.carrental.bean.BikeInfo;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.main.MainActivity;
import com.hhu.carrental.service.LocationService;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.SaveListener;

/**
 * 个人信息
 */
public class UserInfoActivity extends Activity implements View.OnClickListener{

    private BmobUserManager userManager;
    private TextView nametv;
    private Button logout;
    private RelativeLayout rentOut,rentAmount;
    private double latitude;
    private double longtitude;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = BmobUserManager.getInstance(this);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    private void initView(){
        nametv = (TextView) findViewById(R.id.username);
        logout = (Button) findViewById(R.id.btn_logout);
        rentOut = (RelativeLayout) findViewById(R.id.layout_rent_bike);
        user = userManager.getCurrentUser(User.class);
        nametv.setText(user.getUsername());
        logout.setOnClickListener(this);
        rentOut.setOnClickListener(this);

        rentAmount = (RelativeLayout)findViewById(R.id.layout_rent_amoutbike);
        rentAmount.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_logout:
                userManager.logout();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.layout_rent_bike:
                startActivity(new Intent(this,RentOutActivity.class));
                break;
            case R.id.layout_rent_amoutbike:
                rentAmoutBike();
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    private void rentAmoutBike(){
        LocationService locationService;
        BDLocationListener myListenter = new MyLocationListenner();
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(myListenter);
        locationService.start();
        BikeInfo bikeInfo = null;
        double random0=0.0,random1 = 0.0;
        BmobGeoPoint location = null;
        for(int i =0;i<30;i++){
            random0 = (Math.random()/100000)*400;
            random1 = (Math.random()/100000)*400;
            switch(i%10){
                case 1:
                case 2:
                case 3:
                    location = new BmobGeoPoint(longtitude-random0,latitude-random1);
                    break;
                case 4:
                case 5:
                    location = new BmobGeoPoint(longtitude+random0,latitude-random1);
                    break;
                case 7:
                case 8:
                    location = new BmobGeoPoint(longtitude-random0,latitude+random1);
                    break;
                default:
                    location = new BmobGeoPoint(longtitude+random0,latitude+random1);
                    break;
            }
            bikeInfo = new BikeInfo("123456",location,user,"110","山地车","我是一辆单车","20170506");
            bikeInfo.save(this,new SaveListener(){
                @Override
                public void onSuccess() {
                    Log.e("出租成功出租成功出租成功","----");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.e("出租失败出租失败出租失败",i+s);
                }
            });
        }

        locationService.stop();

    }


    public class MyLocationListenner implements BDLocationListener {

        private String lastFloor = null;

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null ) {
                return;
            }
            latitude = location.getLatitude();
            longtitude = location.getLongitude();
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
        public void onConnectHotSpotMessage(String s, int i) {

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
