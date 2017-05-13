package com.hhu.carrental.util;

import android.os.Handler;
import android.os.Message;

import com.hhu.carrental.main.MainActivity;

import java.lang.ref.WeakReference;

/**
 * 进行时间更新
 * Created by demeiyan on 2017/5/14 02:27.
 */

public class TimeHandler extends Handler {
    private WeakReference mActivity;
    public TimeHandler(MainActivity activity){
        mActivity = new WeakReference(activity);
    }

    @Override
    public void handleMessage(Message msg){
        MainActivity activity = (MainActivity) mActivity.get();
        switch (msg.what){
            case 1:
                activity.updateCurrentTime();
                sendEmptyMessageDelayed(1,500);
                break;
            default:
                break;
        }
    }
}
