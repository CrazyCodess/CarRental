package com.hhu.carrental.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hhu.carrental.R;
import com.hhu.carrental.bean.BikeInfo;
import com.hhu.carrental.service.LocationService;
import com.hhu.carrental.ui.LoginActivity;
import com.hhu.carrental.ui.UserInfoActivity;

import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 主界面显示
 */
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
    boolean isFirstLoc = true;
    private BitmapDescriptor mCurrentMarker;
    private LocationService locationService;
    private BitmapDescriptor bitmap;
    private RelativeLayout hireLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_main);
        initmap();//初始化百度地图
        location();//进行定位

    }

    private void initmap(){
        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.booking_bike_marker);
        queryBikeList();
        locbtn = (ImageView)findViewById(R.id.loc_btn);
        locbtn.setScaleType(ImageView.ScaleType.FIT_START);
        mapView=(MapView)findViewById(R.id.bmapView);
        hireLayout = (RelativeLayout)findViewById(R.id.hire_layout);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setTrafficEnabled(false);
        baiduMap.setIndoorEnable(true);
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setBuildingsEnabled(true);
        baiduMap.setMaxAndMinZoomLevel(3,21);
        mLocMode = LocationMode.NORMAL;


        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(myListenter);


        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener(){
            public boolean onMarkerClick(final Marker marker) {
                marker.setToTop();
                hireLayout.setVisibility(View.VISIBLE);

                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hireLayout.setVisibility(View.GONE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        initSlide();
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
        locationService.start();

    }



/*    private void initLocation(){
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
        //mLocationClient.setLocOption(option);

    }*/

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
            }
        });
    }


    /**
     * 查询单车信息
     */
    private void queryBikeList(){
        BmobQuery<BikeInfo> query = new BmobQuery<>();
        //query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(this, new FindListener<BikeInfo>() {
            @Override
            public void onSuccess(List<BikeInfo> list) {

                for(BikeInfo info:list){


                    LatLng point = new LatLng(info.getLocation().getLatitude(),info.getLocation().getLongitude());
                    MarkerOptions  option = new MarkerOptions().position(point).icon(bitmap).zIndex(0).period(10);
                    option.animateType(MarkerOptions.MarkerAnimateType.grow);
                    baiduMap.addOverlay(option);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e("错误错误",i+s);
                Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        locationService.stop();
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
