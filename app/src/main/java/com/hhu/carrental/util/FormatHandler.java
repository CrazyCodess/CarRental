package com.hhu.carrental.util;

/**
 *各种格式处理
 * Created by demeiyan on 2017/5/14 15:46.
 */

public class FormatHandler {

    /**
     * 把时间戳转换成HH:mm:ss格式,被Java里的时区坑了，决定写一个时间格式处理
     * @param mills
     * @return
     */
    public static String timeMillisTotime(long mills){

        String hour = String.valueOf(String .format("%02d",mills/(1000*60*60)));
        String min = String.valueOf(String .format("%02d",mills/(1000*60)-(mills/(1000*60*60))*60));

        String seconds = String.valueOf(String .format("%02d",mills/1000-(mills/(1000*60))*60));
        return hour+":"+min+":"+seconds;
    }

    public static String formatDistance(long dis){
        String res;
        String km = String.valueOf(dis/1000);
        String meters = String.valueOf(dis-(dis/1000)*1000);
        if(dis/1000>0){
            return km+"km"+meters+"m";
        }else{
            return meters+"m";
        }
    }


}
