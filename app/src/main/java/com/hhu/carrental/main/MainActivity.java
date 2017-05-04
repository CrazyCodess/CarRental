package com.hhu.carrental.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hhu.carrental.R;
import com.hhu.carrental.ui.LoginActivity;
import com.hhu.carrental.ui.UserInfoActivity;

import cn.bmob.im.BmobUserManager;

public class MainActivity extends Activity {

    MapView mapView = null;
    private LocationMode mLocMode;
    //public LocationClient mLocationClient = null;
    public BDLocationListener myListenter = new MyLocationListenner();
    MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    BaiduMap baiduMap=null;
    Marker marker = null;
    ImageButton slidebtn = null;
    ImageView locbtn = null;
    public LocationClient mLocationClient = null;
    boolean isFirstLoc = true;
    private BitmapDescriptor mCurrentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_main);
       // BmobChat.DEBUG_MODE = true;
       // BmobChat.getInstance(this).init("67636fb1d0e031952bd2fb8956cfd1b6");
        //BmobChat.getInstance(this).init("bc48a49d18b462fd2114fe71f4f95722");
        initmap();//初始化百度地图
        location();//进行定位

    }

    private void initmap(){
        locbtn = (ImageView)findViewById(R.id.loc_btn);
        locbtn.setScaleType(ImageView.ScaleType.FIT_START);
        mapView=(MapView)findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setTrafficEnabled(false);
        baiduMap.setIndoorEnable(true);
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setBuildingsEnabled(true);
        baiduMap.setMaxAndMinZoomLevel(3,21);
        mLocMode = LocationMode.NORMAL;
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListenter);
        mLocMode = LocationMode.NORMAL;
        //initSlidingMenu();
        initSlide();
        initLocation();

    }

    private void location(){
        locbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mLocMode){
                    case NORMAL://处于正常模式
                        mLocMode = LocationMode.FOLLOWING;
                        locbtn.setImageResource(R.drawable.location0);
                        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                                mLocMode,true,mCurrentMarker
                        ));
                        break;
                    case COMPASS://处于罗盘模式
                        mLocMode = LocationMode.NORMAL;
                        locbtn.setImageResource(R.drawable.location0);
                        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                                mLocMode,true,mCurrentMarker
                        ));
                        break;
                    case FOLLOWING://处于跟随模式
                        mLocMode = LocationMode.COMPASS;
                        locbtn.setImageResource(R.drawable.location1);
                        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                                mLocMode,true,mCurrentMarker
                        ));
                        break;
                    default:break;
                }

            }
        });
        mLocationClient.start();

    }



    private void initLocation(){
        //配置定位SDK各配置参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);

    }

    public class MyLocationListenner implements BDLocationListener {

        private String lastFloor = null;
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location == null || mapView==null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            if(isFirstLoc){
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }


        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

        public void onConnectHotSpotMessage(String s, int i){

        }
    }

    private void initSlide(){
        slidebtn = (ImageButton)findViewById(R.id.slide_btn);
        slidebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUserManager userManager = BmobUserManager.getInstance(MainActivity.this);
                if(userManager.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this,UserInfoActivity.class));
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
/*                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);*/
                //slidMenu.toggle();
            }
        });
    }
/*    private void initSlidingMenu(){
        slidMenu = new SlidingMenu(this);

        slidMenu.setMode(SlidingMenu.LEFT);
        slidMenu.setSecondaryMenu(R.layout.slide_layout);
        slidMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidMenu.setShadowWidth(10);
        slidMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);// 设置偏离距离
        slidMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏模式，全屏滑动都可打开
        slide();
    }*/



    @Override
    protected void onDestroy() {

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
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
