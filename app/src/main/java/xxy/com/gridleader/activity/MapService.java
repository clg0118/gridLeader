package xxy.com.gridleader.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

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

public class MapService extends Service {
    private long configTime;
    private long userId;
    private long actualCheckId;
    private static final String TAG = "MyService";
    public static List<PostionModel> postionList = new ArrayList<>();
    public static boolean flag = false;
            ;
    //查询到的城市信息
    private HashMap<String,Double> map = new HashMap<>();
    //设置为静态成员方便在 Activity 中引用
    public static Handler handler;
    //Activity 与 Service 交互的通道
    private MyBinder binder = new MyBinder();
    //定位客户端
    private LocationClient client;

    public MapService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }


    /**
     * 初始化定位选项
     */
    private void getLocation() {
        client = new LocationClient(getApplicationContext());
        //注册监听接口
        client.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        client.setLocOption(option);
        //开始定位
        client.start();
    }

    /**
     * 在代理中返回真正地城市名信息
     */
    public class MyBinder extends Binder {
        public HashMap<String,Double> getCity() {
            return map;
        }
    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                Toast.makeText(MapService.this, "定位失败", Toast.LENGTH_SHORT).show();
                return;
            }
            if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                    || bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                //也可以使用 bdLocation.getCity()
                if (bdLocation.getDistrict() != null && client.isStarted()) {
                    Log.d(TAG, "onReceiveLocation: " + bdLocation.getDistrict());
                    PostionModel pos = new PostionModel();
                    pos.setLatitude(bdLocation.getLatitude());
                    pos.setLongitude(bdLocation.getLongitude());
                    pos.setRadius(bdLocation.getRadius());
                    postionList.add(pos);
                    Log.d(TAG, "postionList: " + postionList.size());
//                    if (handler != null) {
//
//                        map.put("latitude",bdLocation.getLatitude());
//                        map.put("longitude",bdLocation.getLongitude());
////                        city = bdLocation.getDistrict();
//                        //向实例化 Handler 的线程发送消息
//                        handler.sendEmptyMessage(0);
//
//                    }
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        configTime = Long.valueOf(intent.getStringExtra("configTime"));
        getLocation();
        flag = true;
        userId = intent.getLongExtra("userId",0);
        actualCheckId = intent.getLongExtra("actualCheckId",0);
        GpsLocationThread gpsLocationThread = new GpsLocationThread(map,userId,actualCheckId);
        Thread thread = new Thread(gpsLocationThread);
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //服务销毁停止定位
        client.stop();
//        flag = false;
    }
}
