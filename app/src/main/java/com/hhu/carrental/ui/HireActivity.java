package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.BikeInfo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class HireActivity extends Activity implements View.OnClickListener{

    private String bikeLat;
    private String bikeLon;
    private TextView biketype,hiretime,bikephone,bikePwd,bikedetail;
    private BikeInfo bikeInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);
        init();
    }

    private void init(){

        Intent intent = getIntent();
        bikeLat = intent.getStringExtra("markerLat");
        bikeLon = intent.getStringExtra("markerLong");
        biketype = (TextView)findViewById(R.id.bike_t);
        hiretime = (TextView)findViewById(R.id.hire_time);
        bikephone = (TextView)findViewById(R.id.bike_con);
        bikePwd = (TextView)findViewById(R.id.bike_paw);
        bikedetail = (TextView)findViewById(R.id.bike_desc);
        queryBikeInfo();
        if(bikeInfo == null)Log.e("sucess","*"+bikeLat+"---------------------"+bikeLon+"*");
        else Log.e("sucess",bikeInfo.toString());
        biketype.setText(bikeInfo.getBikeType());
        hiretime.setText(bikeInfo.getRentTime());
        bikephone.setText(bikeInfo.getPhoneNumber());
        bikePwd.setText(bikeInfo.getUnlockPass());
        bikePwd.setText(bikeInfo.getBikeDetail());
    }


    private void queryBikeInfo(){
        Log.e("queryBikeInfo","成功成功成功");
        BmobQuery<BikeInfo> query = new BmobQuery<>();
//        query.addWhereEqualTo("latitude","31.91709");
//        query.addWhereEqualTo("longitude","118.796024");
        query.setCachePolicy(BmobQuery.CachePolicy.IGNORE_CACHE);
        Log.e("queryBikeInfo","start");
        query.findObjects(this, new FindListener<BikeInfo>() {

            @Override
            public void onSuccess(List<BikeInfo> list) {
                Log.e("sucess","成功成功成功");

                bikeInfo = list.get(0);
                Log.e("sucess",bikeInfo.toString());
            }


            @Override
            public void onError(int i, String s) {
                Log.e("错误错误",i+s);
                Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_LONG).show();

            }
        });

        Log.e("queryBikeInfo","stop");
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
                startActivity(new Intent());
                finish();
                break;
            default:
                break;
        }
    }


}
