package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.hhu.carrental.R;
import com.hhu.carrental.bean.RouteInfo;
import com.hhu.carrental.bean.RoutePoint;
import com.hhu.carrental.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class RouteDetailActivity extends Activity implements View.OnClickListener{

    private ImageButton back;
    private BitmapDescriptor startBmp,endBmp;
    private MapView mapView;
    private BaiduMap routeBaiduMap;
    private Intent intent;
    private RouteInfo routeInfo;
    private List<RoutePoint> routePoints;
    private List<LatLng> points = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);
        setContentView(R.layout.activity_route_detail);
        initView();
    }

    private void initView(){

        intent = getIntent();
        routeInfo = (RouteInfo)intent.getSerializableExtra("routeInfo");
        routePoints = routeInfo.getRouteList();
        mapView = (MapView)findViewById(R.id.route_detail_mapView);
        routeBaiduMap = mapView.getMap();
        routeBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        routeBaiduMap.setTrafficEnabled(false);
        routeBaiduMap.setIndoorEnable(true);
        routeBaiduMap.setBuildingsEnabled(true);
        routeBaiduMap.setMaxAndMinZoomLevel(3,21);
        mapView.showZoomControls(false);
        startBmp = BitmapDescriptorFactory.fromResource(R.mipmap.start);
        endBmp = BitmapDescriptorFactory.fromResource(R.mipmap.end);
        for(int i = 0;i < routePoints.size();i++ ){
            RoutePoint point = routePoints.get(i);
            points.add(new LatLng(point.getRouteLat(),point.getRouteLng()));
        }
        if(points.size()>2){
            OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xFF36D19D).points(points);

            routeBaiduMap.addOverlay(ooPolyline);
            RoutePoint startPoint = routePoints.get(0);
            LatLng startPosition = new LatLng(startPoint.getRouteLat(), startPoint.getRouteLng());

            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(startPosition).zoom(18.0f);
            routeBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            RoutePoint endPoint = routePoints.get(routePoints.size() - 1);
            LatLng endPosition = new LatLng(endPoint.getRouteLat(), endPoint.getRouteLng());
            addOverLayout(startPosition, endPosition);
        }
        back = (ImageButton)findViewById(R.id.route_detail_back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.route_detail_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void addOverLayout(LatLng startpos,LatLng endpos){
        MarkerOptions options = new MarkerOptions().position(startpos).icon(startBmp);
        MarkerOptions options1 = new MarkerOptions().position(endpos).icon(endBmp);
        routeBaiduMap.addOverlay(options);
        routeBaiduMap.addOverlay(options1);
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }



}
