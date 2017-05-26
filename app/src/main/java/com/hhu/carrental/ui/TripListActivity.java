package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hhu.carrental.R;
import com.hhu.carrental.bean.RouteInfo;
import com.hhu.carrental.util.StatusBarUtils;
import com.hhu.carrental.util.TripListAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class TripListActivity extends Activity implements View.OnClickListener{
    private PullToRefreshListView pullRefreshList;
    private ILoadingLayout loadingLayout;
    ListView listView;
    ArrayList<RouteInfo> tripList = new ArrayList<>();
    private TripListAdapter tripListAdapter;
    private ImageButton tripBack;
    private RouteInfo routeInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);
        setContentView(R.layout.activity_trip_list);
        initPullListView();
    }

    private void initPullListView(){

        tripBack = (ImageButton)findViewById(R.id.trip_back);
        tripBack.setOnClickListener(this);
        queryRouteList(0,STATE_REFRESH);
        pullRefreshList = (PullToRefreshListView)findViewById(R.id.trip_list);
        loadingLayout = pullRefreshList.getLoadingLayoutProxy();
        loadingLayout.setLastUpdatedLabel("");
        loadingLayout
                .setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
        loadingLayout
                .setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
        loadingLayout
                .setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));

        pullRefreshList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    loadingLayout.setLastUpdatedLabel("");
                    loadingLayout
                            .setPullLabel(getString(R.string.pull_to_refresh_top_pull));
                    loadingLayout
                            .setRefreshingLabel(getString(R.string.pull_to_refresh_top_refreshing));
                    loadingLayout
                            .setReleaseLabel(getString(R.string.pull_to_refresh_top_release));
                } else if (firstVisibleItem + visibleItemCount + 1 == totalItemCount) {
                    loadingLayout.setLastUpdatedLabel("");
                    loadingLayout
                            .setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
                    loadingLayout
                            .setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
                    loadingLayout
                            .setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
                }
            }
        });

        //下拉刷新监听
        pullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 下拉刷新(从第一页开始装载数据)
                queryRouteList(0,STATE_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 上拉加载更多(加载下一页数据)
                queryRouteList(curPage,STATE_MORE);
            }
        });

        listView = pullRefreshList.getRefreshableView();
        tripListAdapter = new TripListAdapter(this,tripList);
        listView.setAdapter(tripListAdapter);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                routeInfo = (RouteInfo) parent.getItemAtPosition(position);
                startActivity(new Intent(TripListActivity.this, RouteDetailActivity.class).putExtra("routeInfo",routeInfo));
            }
        });
    }

    static final int STATE_REFRESH = 0;//进行刷新
    static final int STATE_MORE = 1;//加载更多
    private int limit = 10;		// 每页的数据是10条
    private int curPage = 0;   // 当前页的编号，从0开始

    /**
     * 查询行程列表数据
     * @param page
     * @param actionType
     */
    private void queryRouteList(int page,final int actionType){
        BmobQuery<RouteInfo> query = new BmobQuery<>();
        query.setLimit(limit);
        query.setSkip(page*limit);
        query.include("user");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);

        query.findObjects(this, new FindListener<RouteInfo>() {
            @Override
            public void onSuccess(List<RouteInfo> list) {
                if(list.size()>0){
                    if(actionType==STATE_REFRESH){
                        curPage=0;
                        tripList.clear();
                    }
                    for(RouteInfo routeInfo:list){
                        tripList.add(routeInfo);
                    }
                    curPage++;
                }else if(actionType==STATE_MORE){
                    toast("没有更多行程");
                }
                else if(actionType==STATE_REFRESH){
                    toast("没有更多行程");
                }
                else if(list.size()==0){
                    toast("暂时还没有行程");
                }

                pullRefreshList.onRefreshComplete();

            }

            @Override
            public void onError(int i, String s) {
                toast("查询失败:"+s);

                pullRefreshList.onRefreshComplete();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.trip_back:
                finish();
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        queryRouteList(0,STATE_REFRESH);
        tripListAdapter = new TripListAdapter(this,tripList);
        listView.setAdapter(tripListAdapter);
    }

    private void toast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
