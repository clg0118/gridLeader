package xxy.com.gridleader.activity;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.model.PostionModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

/**
 * Created by XPS13 on 2018/8/13.
 */

public class GpsLocationThread implements Runnable{

    //查询到的城市信息
    private HashMap<String,Double> map = new HashMap<>();
    //设置为静态成员方便在 Activity 中引用
//    public static Handler handler;
    private long userId;
    private long actualCheckId;

    public GpsLocationThread(HashMap<String,Double> map,long userId,long actualCheckId) {
        this.map = map;
        this.userId = userId;
        this.actualCheckId = actualCheckId;
//        this.handler = handler;
    }
    @Override
    public void run() {
        while(MapService.flag) {
            try {
                Thread.sleep(5000);
                List<PostionModel> tempList = new ArrayList<>();
//                List<PostionModel> pList = MapService.postionList;
                if(MapService.postionList == null) {
                    continue;
                }
                tempList.addAll(MapService.postionList);
                MapService.postionList.clear();
                //如果点少于3个，舍弃不要
                if(tempList.size() <= 3) {
                    return;
                }
                List<PostionModel> addrList = new ArrayList<>();
                List<PostionModel> resultList = new ArrayList<>();
                int n = 0;
                //todo
                for(PostionModel pos : tempList) {
                    //取精度够的点
                    if (pos.getRadius() < 500) {
                        addrList.add(pos);
                        n++;
                    }
                }
                //不足3个本次不要
                if(n < 3) {
                    return;
                }
                for(int i = 0;i < addrList.size();i++) {
                    int num = 0;
                    for(int j = 0;j < addrList.size();j++) {
                        if(i == j) {
                            continue;
                        }else {
                            double dis = GetDistance(addrList.get(i).getLatitude(),addrList.get(i).getLongitude(),addrList.get(j).getLatitude(),addrList.get(j).getLongitude());
                            if(dis < 200) {
                                num ++;
                            }
                        }
                    }
                    //权值小于3舍弃
                    if(num >= 3) {
                        resultList.add(addrList.get(i));
                    }
                }
                double lat = 0;
                double lng = 0;
                for(PostionModel pos : resultList) {
                    lat += pos.getLatitude();
                    lng += pos.getLongitude();
                }
                double resLat = lat / resultList.size();
                double resLng = lng / resultList.size();

                map.put("latitude",resLat);
                map.put("longitude",resLng);
                UPDATE_POSITION_CALL(userId,resLng,resLat,actualCheckId);
                    //向实例化 Handler 的线程发送消息
//                    handler.sendEmptyMessage(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static double EARTH_RADIUS = 6371.393;

    //计算两点间距离
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    private void UPDATE_POSITION_CALL(long userId,double longitude,double latitude,long acturalRecordId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.UPDATE_POSITION_CALL(userId,longitude,latitude,acturalRecordId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {


                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
