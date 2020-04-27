package xxy.com.gridleader.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.List;

import xxy.com.gridleader.R;

public class MapViewActivity extends AppCompatActivity {
    private Button btn_confirm;
    private TextView tv_location_content;
    private ImageView img_back;
    private MapView mMapView;
    private BitmapDescriptor bitmap;

    private boolean isFirstLocation = true;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;

    private double lat;
    private double lon;
    private String address= "";
    double latitude = 0;
    double longitude = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map_view);

        tv_location_content = findViewById(R.id.tv_location_content);
        img_back = findViewById(R.id.img_back);
        btn_confirm = findViewById(R.id.btn_confirm);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("location","");
                intent.putExtra("latitude",String.valueOf(latitude));
                intent.putExtra("longitude",String.valueOf(longitude));
                setResult(RESULT_OK,intent);
                mMapView.onPause();
                finish();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_location_content.getText().toString().trim().equals(""))
                    Toast.makeText(MapViewActivity.this,"请选择事发地点",Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent();
                    intent.putExtra("location",tv_location_content.getText().toString().trim());
                    intent.putExtra("latitude",String.valueOf(latitude));
                    intent.putExtra("longitude",String.valueOf(longitude));
                    setResult(RESULT_OK,intent);
                    mMapView.onPause();
                    finish();
                }
            }
        });

        mMapView = (MapView) findViewById(R.id.bmapView);
        //获取BaiduMap对象
        mBaiduMap = mMapView.getMap();
        //声明LocationClient类

//        mBaiduMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        //配置定位参数
        initLocation();
        //开始定位
        mLocationClient.start();


        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            //此方法就是点击地图监听
            @Override
            public void onMapClick(LatLng latLng) {
                //获取经纬度
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                System.out.println("latitude=" + latitude + ",longitude=" + longitude);
                //先清除图层
                mBaiduMap.clear();
                // 定义Maker坐标点
                LatLng point = new LatLng(latitude, longitude);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions().position(point)
                        .icon(bitmap);
                // 在地图上添加Marker，并显示
                mBaiduMap.addOverlay(options);
                //实例化一个地理编码查询对象
                GeoCoder geoCoder = GeoCoder.newInstance();
                //设置反地理编码位置坐标
                ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                op.location(latLng);
                //发起反地理编码请求(经纬度->地址信息)
//                geoCoder.reverseGeoCode(op);
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                        //获取点击的坐标地址

//                        Toast.makeText(MapViewActivity.this,arg0.getPoiList().get(0).name.toString(),Toast.LENGTH_SHORT).show();
                        address = arg0.getAddress();
                        System.out.println("address="+address);
                        tv_location_content.setText(arg0.getPoiList().get(0).name.toString());
                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult arg0) {

                    }
                });
                geoCoder.reverseGeoCode(op);
            }
        });
    }

    private void setMarker() {
        Log.v("pcw","setMarker : lat : "+ lat+" lon : " + lon);
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    /**
     * 设置中心点
     */
    private void setUserMapCenter() {
        Log.v("pcw","setUserMapCenter : lat : "+ lat+" lon : " + lon);
        LatLng cenpt = new LatLng(lat,lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(30)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

    }

    /**
     * 配置定位参数
     */
    private void initLocation(){
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
        mLocationClient.setLocOption(option);
    }



    /**
     * 实现定位监听 位置一旦有所改变就会调用这个方法
     * 可以在这个方法里面获取到定位之后获取到的一系列数据
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            lat = location.getLatitude();
            lon = location.getLongitude();

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if(isFirstLocation){
                isFirstLocation = false;
                setMarker();
                setUserMapCenter();
            }
            Log.v("pcw","lat : " + lat+" lon : " + lon);
            Log.i("BaiduLocationApiDem", sb.toString());
        }
    }

    /**
     * 必须要实现
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.unRegisterLocationListener(myListener);
    }

    /**
     * 必须要实现
     */
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    /**
     * 必须要实现
     */
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        mLocationClient.unRegisterLocationListener(myListener);
    }
}
