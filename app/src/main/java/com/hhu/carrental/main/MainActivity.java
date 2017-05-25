package com.hhu.carrental.main;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.hhu.carrental.R;
import com.hhu.carrental.bean.BikeInfo;
import com.hhu.carrental.bean.RouteInfo;
import com.hhu.carrental.bean.RoutePoint;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.service.LocationService;
import com.hhu.carrental.ui.HireActivity;
import com.hhu.carrental.ui.LoginActivity;
import com.hhu.carrental.ui.UserInfoActivity;
import com.hhu.carrental.util.FormatHandler;
import com.hhu.carrental.util.StatusBarUtils;
import com.hhu.carrental.util.WalkingRouteOverlay;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

import static com.baidu.mapapi.utils.DistanceUtil.getDistance;


/**
 * 主界面显示
 */
public class MainActivity extends Activity implements View.OnClickListener,OnGetRoutePlanResultListener
        ,SensorEventListener {

    private BmobUserManager userManager = null;

    private MapView mapView = null;
    private LocationMode mLocMode;
    //public LocationClient mLocationClient = null;
    public BDLocationListener myListenter = new MyLocationListenner();
    private MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    private BaiduMap baiduMap=null;
    private Marker marker = null;
    private ImageButton slidebtn = null;
    private ImageView locbtn = null;
    private boolean isFirstLoc = true;
    private BitmapDescriptor mCurrentMarker;
    private LocationService locationService;
    private BitmapDescriptor bitmap;
    private RelativeLayout hireLayout;
    private double locLatitude;
    private double locLongtitude;
    private double markerLat,markerLong;
    private Button hirebtn,hireFinish;
    private TextView markerLocation;
    private String city,loccity;
    private BikeInfo bikeInfo;
    private PlanNode sNode  = null,eNode = null;
    private RoutePlanSearch mSearch = null;
    private TextView distance,contract,bikingCost,bikingDistance,bikingTime;
    private LinearLayout ll1,ll2,ll3,bikingL1,bikingL2;
    private String hireMsg ;
    private WalkingRouteOverlay walkingRouteOverlay;
    private Timer timer;
    private long beginTime,totalTime;
    private double totalDistance;
    private  float totalPrice,lastPrice;
    private float mCurrentX;
    private SensorManager sensorManager;
    private ArrayList<RoutePoint> routePointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        initmap();//初始化百度地图
        updateHireUi();

    }
    /**
     *   更新骑行时间
     */


    public void updateCurrentTime(){
        //TODO 把显示系统时间改成骑行的时间
        Date now = new Date();
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                DateFormat.MEDIUM, Locale.CHINA);
        bikingTime.setText(format.format(now));
    }

    /**
     *  更新租用界面
      */


    private void updateHireUi(){

        if((hireMsg=getIntent().getStringExtra("msg") )!= null){
            baiduMap.clear();
            hirebtn.setVisibility(View.GONE);
            hireFinish.setVisibility(View.VISIBLE);
            totalDistance = 0;
            totalPrice = 0;
            lastPrice = 1;
            routePointList = new ArrayList<RoutePoint>();
            ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            bikingL1.setVisibility(View.VISIBLE);
            bikingL2.setVisibility(View.VISIBLE);


            baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    LocationMode.NORMAL,true,mCurrentMarker
            ));
            baiduMap.clear();
            hirebtn.setVisibility(View.GONE);
            hireFinish.setVisibility(View.VISIBLE);
            hireLayout.setVisibility(View.VISIBLE);
            baiduMap.setOnMapClickListener(null);

            //setAddress(new LatLng(locLatitude,locLongtitude));
            //Log.e("Adress----",loccity+locLatitude+"*"+locLongtitude);
            if(loccity != null&&loccity != ""){
                markerLocation.setText(loccity);
            }

            beginTime = System.currentTimeMillis();
            final Handler myHandler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    if (msg.what == 0x2333){
                        bikingTime.setText(FormatHandler.timeMillisTotime((totalTime=System.currentTimeMillis()-beginTime)));
                        totalPrice = (float) (Math.floor(totalTime/1000/60 / 30) * 0.5 + 0.5);
                        if(totalPrice > lastPrice){
                            bikingCost.setText("￥"+String .format("%.1f",totalPrice)+"元");
                            lastPrice = totalPrice;
                        }
                    }
                }
            };



            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    myHandler.sendEmptyMessage(0x2333);
                }
            },0,1000);

        }
        else{
            queryBikeList();
        }
    }

    /**
     * 初始化地图
     */


    private void initmap(){
        userManager= BmobUserManager.getInstance(MainActivity.this);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        hireMsg = null;
        totalDistance = 0.0;
        bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.stable_cluster_marker_one_normal);
        bikingL1 = (LinearLayout)findViewById(R.id.hire_linear1);
        bikingL2 = (LinearLayout)findViewById(R.id.hire_linear2);
        bikingCost = (TextView)findViewById(R.id.biking_cost);
        bikingTime = (TextView)findViewById(R.id.biking_time);
        bikingDistance = (TextView)findViewById(R.id.biking_distance);
        bikingDistance.setText("0.0米");
        ll2 = (LinearLayout)findViewById(R.id.linearLayout2);
        ll3 = (LinearLayout)findViewById(R.id.linearLayout3);
        ll1 = (LinearLayout)findViewById(R.id.linearLayout);
        distance = (TextView)findViewById(R.id.distance);
        contract = (TextView)findViewById(R.id.contract);
        locbtn = (ImageView)findViewById(R.id.loc_btn);
        locbtn.setScaleType(ImageView.ScaleType.FIT_START);
        mapView=(MapView)findViewById(R.id.bmapView);
        hireLayout = (RelativeLayout)findViewById(R.id.hire_layout);
        hirebtn = (Button)findViewById(R.id.hirebtn);
        hireFinish = (Button)findViewById(R.id.finish_hire);
        markerLocation = (TextView)findViewById(R.id.marker_location);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setTrafficEnabled(false);
        baiduMap.setIndoorEnable(true);
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setBuildingsEnabled(true);
        baiduMap.setMaxAndMinZoomLevel(3,21);
        location();
        walkingRouteOverlay = new WalkingRouteOverlay(baiduMap);
        mLocMode = LocationMode.NORMAL;

        slidebtn = (ImageButton)findViewById(R.id.slide_btn);
        slidebtn.setOnClickListener(this);
        hireFinish.setOnClickListener(this);
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(myListenter);
        locationService.start();
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener(){
            public boolean onMarkerClick(final Marker marker) {
                walkingRouteOverlay.removeFromMap();
                //mSearch.destroy();
                LatLng latLng =  marker.getPosition();
                setAddress(latLng);
                markerLocation.setText(city);
                marker.setToTop();
                String bql = "select * from BikeInfo where location  near ["+latLng.longitude+","+latLng.latitude+"] max 1 miles";
                BmobQuery<BikeInfo> bquery = new BmobQuery<BikeInfo>();
                bquery.setSQL(bql);
                bquery.setLimit(3);
                bquery.include("user[username]");
                //bquery.setPreparedParams(new Object[]{latLng.longitude,latLng.latitude});
                bquery.doSQLQuery(MainActivity.this, new SQLQueryListener<BikeInfo>() {
                    @Override
                    public void done(BmobQueryResult<BikeInfo> bmobQueryResult, BmobException e) {
                        if(e ==null){
                            List<BikeInfo> list = (List<BikeInfo>) bmobQueryResult.getResults();
                            if(list!=null && list.size()>0){
                                bikeInfo = list.get(0);
                                //Log.e("BikeInfo:",bikeInfo.toString());
                            }else{
                                Log.e("msg", "查询成功，无数据返回");
                            }
                        }else{
                            //Log.e("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            //Log.e("异常:",Log.getStackTraceString(e));
                            //e.printStackTrace();
                        }
                    }
                });


                hireLayout.setVisibility(View.VISIBLE);
                bikingL1.setVisibility(View.GONE);
                bikingL2.setVisibility(View.GONE);
                marker.setZIndex(5);
                markerLat = latLng.latitude;
                markerLong = latLng.longitude;
                eNode = PlanNode.withLocation(latLng);
                sNode = PlanNode.withLocation(new LatLng(locLatitude,locLongtitude));
                mSearch.walkingSearch((new WalkingRoutePlanOption())
                        .from(sNode).to(eNode));
                double time = getDistance(new LatLng(locLatitude,locLongtitude),latLng)+0.5;
                distance.setText((int)time+"米");


                contract.setText(((int)(time/60.0+0.5)+1)+"分钟");
                //Log.e("BikeInfo1:",bikeInfo.toString());
                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                walkingRouteOverlay.removeFromMap();
                hireLayout.setVisibility(View.GONE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    /**
     * 点击定位按钮进行定位
     */


    private void location(){

        locbtn.setOnClickListener(this);
        hirebtn.setOnClickListener(this);

    }



    private void setAddress(LatLng latLng){
        GeoCoder geoCoder = GeoCoder.newInstance();
        ReverseGeoCodeOption op = new ReverseGeoCodeOption();

        op.location(latLng);
        geoCoder.reverseGeoCode(op);
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                //获取点击的坐标地址
                city = arg0.getAddress();
                loccity = arg0.getAddress();
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
            }
        });
    }


    /**
     *  定位监听器
      */


    private class MyLocationListenner implements BDLocationListener {

        private String lastFloor = null;
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location == null || mapView==null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(mCurrentX).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            //Log.e("定位方向改变-----",mCurrentX+"");
            baiduMap.setMyLocationData(locData);
            locLatitude = location.getLatitude();
            locLongtitude = location.getLongitude();
            //Log.e("hireMsg",(hireMsg==null)?"null":hireMsg);
            Log.e("大小大小0000",(routePointList==null)?"null":routePointList.size()+"");
            if(hireMsg != null&&!hireMsg .equals("")){
                setAddress(new LatLng(locLatitude,locLongtitude));
                markerLocation.setText(loccity);
                ll1.setVisibility(View.VISIBLE);
                RoutePoint routePoint = new RoutePoint();
                routePoint.setRouteLat(locLatitude);
                routePoint.setRouteLng(locLongtitude);

                if(routePointList == null){
                    routePointList = new ArrayList<RoutePoint>();
                    routePointList.add(routePoint);
                }else if(routePointList.size() == 0){
                    routePointList.add(routePoint);
                }else {
                    RoutePoint lastPoint = routePointList.get(routePointList.size()-1);
                    if(lastPoint.getRouteLat() !=locLatitude||lastPoint.getRouteLng()!=locLongtitude){
                        LatLng lastLng = new LatLng(lastPoint.getRouteLat(),lastPoint.getRouteLng());
                        LatLng currentLng = new LatLng(locLatitude,locLongtitude);
                        double distantce = getDistance(lastLng, currentLng);
                        Log.e("距离--------",distantce+"米");
                        if(distantce > 0.1){
                            routePointList.add(routePoint);

                            totalDistance += distantce;
                        }
                        if(totalDistance>0){
                            bikingDistance.setText(FormatHandler.formatDistance(totalDistance));
                        }
                    }
                }



            }


            if(isFirstLoc){

                isFirstLoc = false;
                LatLng mll = new LatLng(locLatitude,locLongtitude);
                setAddress(mll);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(mll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }




        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

        public void onConnectHotSpotMessage(String s, int i){

        }
    }


    /**
     *  查询单车信息并在地图上显示单车
      */
    private void queryBikeList(){
        BmobQuery<BikeInfo> query = new BmobQuery<>();
        query.setLimit(1000);
        //query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(this, new FindListener<BikeInfo>() {
            @Override
            public void onSuccess(List<BikeInfo> list) {

                List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();

                Log.e("单车总数：",list.size()+"");
                for(BikeInfo info:list){

                    LatLng point = new LatLng(info.getLocation().getLatitude(),info.getLocation().getLongitude());
                    MarkerOptions option = new MarkerOptions().position(point).icon(bitmap).zIndex(0).period(10);
                    option.animateType(MarkerOptions.MarkerAnimateType.grow);
                    markerList.add(option);
                    //baiduMap.addOverlay(option);
                }
                baiduMap.addOverlays(markerList);

            }
            @Override
            public void onError(int i, String s) {
                Log.e("错误错误",i+s);
                //Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_LONG).show();
            }
        });
    }
     /**
      *
      *  步行路径导航
      */
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this,"加载失败",Toast.LENGTH_LONG).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            for(WalkingRouteLine line:result.getRouteLines()){
                walkingRouteOverlay.setData(line);
            }

            walkingRouteOverlay.addToMap();
            walkingRouteOverlay.zoomToSpan();
        }
    }

    public void onGetTransitRouteResult(TransitRouteResult var1){

    };

    public void onGetMassTransitRouteResult(MassTransitRouteResult var1){

    };

    public void onGetDrivingRouteResult(DrivingRouteResult var1){

    };


    public void onGetIndoorRouteResult(IndoorRouteResult var1){

    };

    public void onGetBikingRouteResult(BikingRouteResult var1){

    };



    public void onClick(View v){

        switch (v.getId()){
            case R.id.loc_btn:
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(new LatLng(locLatitude,locLongtitude)).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
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
                break;
            case R.id.hirebtn:

                if(userManager.getCurrentUser() != null){
                    Intent intent = new Intent(MainActivity.this, HireActivity.class);
                    intent.putExtra("bikeInfo",bikeInfo);
                    startActivity(intent);
                    baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                            LocationMode.NORMAL,true,null
                    ));

                    baiduMap.setOnMapClickListener(null);

                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

                break;

            case R.id.finish_hire:
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                ll3.setVisibility(View.VISIBLE);
                hireFinish.setVisibility(View.GONE);
                hirebtn.setVisibility(View.VISIBLE);
                hireLayout.setVisibility(View.GONE);
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
                queryBikeList();
                uploadRouteInfo();
                break;

            case R.id.slide_btn:

                userManager = BmobUserManager.getInstance(MainActivity.this);
                if(userManager.getCurrentUser(User.class) != null){
                    startActivity(new Intent(MainActivity.this,UserInfoActivity.class));
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                break;

        }
    }
    @Override
    protected void onDestroy() {

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(timer != null)timer.cancel();
        mSearch.destroy();
        locationService.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_UI);
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE),SensorManager.SENSOR_DELAY_UI);
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop(){
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.d("demeiyan", "MainActivity------------onStart------------------");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        baiduMap.setMyLocationEnabled(true);
        locationService.start();
    }


    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis() - exitTime >2000){
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

            mCurrentX = event.values[SensorManager.DATA_X];
        }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void uploadRouteInfo(){
        BikeInfo bikeInfo = (BikeInfo) getIntent().getSerializableExtra("bikeInfo");
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setBikeId(bikeInfo.getObjectId());
        routeInfo.setUser(userManager.getCurrentUser(User.class));
        routeInfo.setRouteList(routePointList);
        routeInfo.setBikingCost(bikingCost.getText().toString());
        routeInfo.setBikingTime(bikingTime.getText().toString());
        routeInfo.setBikingDistance(bikingDistance.getText().toString());
        routeInfo.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e("routeSuccess","----");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e("routeFailure","----"+i+s);
            }
        });


    }
}
