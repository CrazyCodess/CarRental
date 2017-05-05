package com.hhu.carrental.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hhu.carrental.R;
import com.hhu.carrental.bean.BikeInfo;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.main.MainActivity;
import com.hhu.carrental.service.LocationService;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.SaveListener;

/**
 * 出租单车
 */
public class RentOutActivity extends Activity implements View.OnClickListener{

    private BikeInfo bikeInfo = null;
    private String biketype;
    private BmobGeoPoint location;
    private User user;
    private String phoneNumber;
    private String unlockPass;
    private String bikedetail;
    private String renttime;
    private EditText bikeType;
    private EditText rentTime;
    private EditText bikephone;
    private EditText bikePass;
    private EditText bikeDetail;
    private Button rentbtn;
    private MapView mapView = null;
    private boolean isFirstLoc = true;
    private BaiduMap locMap;
    private LocationService locationService;
    private BDLocationListener myListenter = new MyLocationListenner();
    private BmobUserManager userManager;
    private double latitude;
    private double longtitude;
    private BitmapDescriptor mCurrentMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = BmobUserManager.getInstance(this);
        setContentView(R.layout.activity_rent_out);
        initView();
    }

    private void initView(){

        bikeType = (EditText)findViewById(R.id.bike_type);
        rentTime = (EditText)findViewById(R.id.rent_time);
        bikephone = (EditText)findViewById(R.id.bike_phone);
        bikePass = (EditText)findViewById(R.id.bike_pass);
        bikeDetail = (EditText)findViewById(R.id.bike_detail);
        rentbtn = (Button)findViewById(R.id.rent_btn);
        mapView=(MapView)findViewById(R.id.loc_bmapView);
        locMap = mapView.getMap();
        locMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL,true,mCurrentMarker
        ));
        locMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        locMap.setTrafficEnabled(false);
        locMap.setIndoorEnable(true);
        locMap.setMyLocationEnabled(true);
        locMap.setBuildingsEnabled(true);
        locMap.setMaxAndMinZoomLevel(3,21);
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(myListenter);
        user = userManager.getCurrentUser(User.class);
        rentbtn.setOnClickListener(this);
        locationService.start();
    }

    public void onClick(View v){

        biketype = bikeType.getText().toString();
        renttime = rentTime.getText().toString();
        phoneNumber = bikephone.getText().toString();
        unlockPass = bikePass.getText().toString();
        bikedetail = bikeDetail.getText().toString();

        switch (v.getId()){
            case R.id.rent_btn:
                saveBikeInfo();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;

        }
    }

    public class MyLocationListenner implements BDLocationListener {

        private String lastFloor = null;

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null || mapView == null) {
                return;
            }
            latitude = location.getLatitude();
            longtitude = location.getLongitude();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            locMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                locMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public void saveBikeInfo(){
        location = new BmobGeoPoint(longtitude,latitude);
        bikeInfo = new BikeInfo(unlockPass,location,user,phoneNumber,biketype,bikedetail,renttime);
        final ProgressDialog mpd = new ProgressDialog(this);
        mpd.setTitle("请稍后");
        mpd.setMessage("正在上传...");
        mpd.show();
        bikeInfo.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                mpd.setMessage("出租成功");
                mpd.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {
                mpd.setMessage("出租失败");
                mpd.dismiss();
            }
        });
    }



    @Override
    protected void onDestroy() {

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        locationService.stop();
        locMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理

        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

}
