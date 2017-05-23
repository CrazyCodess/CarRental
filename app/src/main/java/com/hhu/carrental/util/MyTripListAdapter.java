package com.hhu.carrental.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhu.carrental.R;
import com.hhu.carrental.bean.RouteInfo;

import java.util.ArrayList;

/**
 * Created by demeiyan on 2017/5/24 01:48.
 */

public class MyTripListAdapter extends BaseAdapter implements View.OnClickListener{

    private RouteInfo routeInfo;
    private Context context;
    private ArrayList<RouteInfo> routeInfoList;
    public MyTripListAdapter(Context context,ArrayList<RouteInfo> list) {
        this.context = context;
        this.routeInfoList = list;
    }

    class ViewHolder{
        LinearLayout layoutTrip;
        TextView tripfinishTime,tripTime,tripCost,bikeNo;
    }
    @Override
    public int getCount() {
        return routeInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return routeInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.route_listadapter, parent, false);
            holder=new ViewHolder();
            holder.layoutTrip = (LinearLayout)convertView.findViewById(R.id.layout_trip);
            holder.tripfinishTime = (TextView)convertView.findViewById(R.id.trip_finish_time);
            holder.tripTime = (TextView)convertView.findViewById(R.id.trip_time);
            holder.tripCost = (TextView)convertView.findViewById(R.id.trip_cost);
            holder.bikeNo = (TextView)convertView.findViewById(R.id.bike_no);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        routeInfo = (RouteInfo)getItem(position);
        //TODO　
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_trip:
                break;
            default:
                break;
        }
    }
}
