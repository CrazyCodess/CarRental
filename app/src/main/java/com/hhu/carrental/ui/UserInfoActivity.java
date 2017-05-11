package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
    private double longitude;
    private User user;
    private LocationService locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        BDLocationListener myListenter = new MyLocationListenner();
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(myListenter);
        locationService.start();
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
                locationService.stop();
                finish();
                break;
            default:
                break;
        }
    }

    private void rentAmoutBike(){

        BikeInfo bikeInfo = null;
        BmobGeoPoint location = null;
        double random0=0.0,random1 = 0.0;
        double varlon,varlat;
        for(int i =0;i<30;i++){
            random0 = (Math.random()/100000)*1000;
            random1 = (Math.random()/100000)*1000;
            random0 = Double.parseDouble(String .format("%.6f",random0));
            random1 = Double.parseDouble(String .format("%.6f",random1));
            varlon = longitude;
            varlat = latitude;
            Log.e("随机数：","random0:"+random0+"random1:"+random1+"longitude:"+longitude+"latitude:"+latitude);
            switch(i%10){
                case 1:
                case 2:
                case 3:
                    varlon = varlon-random0;
                    varlat = varlat-random1;
                    //location = new BmobGeoPoint(longitude-random0,latitude-random1);
                    break;
                case 4:
                case 5:
                    varlon = varlon+random0;
                    varlat = varlat-random1;
                    //location = new BmobGeoPoint(longitude+random0,latitude-random1);
                    break;
                case 7:
                case 8:
                    varlon = varlon-random0;
                    varlat = varlat+random1;
                    //location = new BmobGeoPoint(longitude-random0,latitude+random1);
                    break;
                default:
                    varlon = varlon+random0;
                    varlat = varlat+random1;
                    //location = new BmobGeoPoint(longitude+random0,latitude+random1);
                    break;
            }
            Log.e("坐标","varlon:"+varlon+"varlat:"+varlat);
            if(varlon >100&&varlat>10){
                location = new BmobGeoPoint(varlon,varlat);
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

        }



    }


    public class MyLocationListenner implements BDLocationListener {

        private String lastFloor = null;

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null ) {
                return;
            }
            //Log.e("location","notnull");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        locationService.stop();

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
