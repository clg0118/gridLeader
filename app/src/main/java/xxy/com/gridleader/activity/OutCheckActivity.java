package xxy.com.gridleader.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.model.CheckEndModel;
import xxy.com.gridleader.model.StartCheckModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;
import xxy.com.gridleader.zxing.activity.CaptureActivity;

public class OutCheckActivity extends AppCompatActivity {
    private long userId;
    private String longitude,latitude;
    private TextView tv_time,tv_status,tv_endtime,tv_durtime,tv_todaytime;
    private MapService.MyBinder binder;
    private long configTime = 1;
    private long actualCheckId;
    private Button btn_finish;
    private RelativeLayout layout_mark;
    private TextView tv_mark;
    private ImageView img_back;
    private Intent intent;


    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_check);
        init();

        if (Integer.valueOf(intent.getStringExtra("checkStatus")) == 1){
            actualCheckId = Long.valueOf(intent.getStringExtra("actualCheckId"));
            tv_time.setText("开始时间：" + intent.getStringExtra("checkStartTimeStr"));
            location();

        }else if (Integer.valueOf(intent.getStringExtra("checkStatus")) == 2){
            CheckStartCall(userId);
        }
    }

    private void init(){
        intent = getIntent();
        userId = Long.valueOf(intent.getStringExtra("userId"));

        img_back = findViewById(R.id.img_back);
        tv_time = findViewById(R.id.tv_time);
        tv_endtime = findViewById(R.id.tv_endtime);
        tv_durtime = findViewById(R.id.tv_durtime);
        tv_todaytime = findViewById(R.id.tv_todaytime);
        tv_status = findViewById(R.id.tv_status);
        btn_finish = findViewById(R.id.btn_finish);
        layout_mark = findViewById(R.id.layout_mark);
        layout_mark.setVisibility(View.GONE);
        tv_mark = findViewById(R.id.tv_mark);
        tv_mark.setVisibility(View.GONE);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService();
                MapService.flag = false;
//                Toast.makeText(OutCheckActivity.this,"" + actualCheckId,Toast.LENGTH_SHORT).show();
                CHECK_END_CALL(userId,actualCheckId);
            }
        });

        layout_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(OutCheckActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(OutCheckActivity.this, Manifest.permission.VIBRATE)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(OutCheckActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(OutCheckActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                    ActivityCompat.requestPermissions(OutCheckActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA, Manifest.permission.VIBRATE}, 888);
                } else {
                    Intent intent = new Intent(OutCheckActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCameraIntent, 888);
            } else {
                Toast.makeText(this, "CAMERA PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");

            if (scanResult.contains(MyConstants.BASEURL + "?params=")){
                Intent intent = new Intent(OutCheckActivity.this,SignInActivity.class);
                intent.putExtra("scanresult",scanResult);
                intent.putExtra("longitude",String.valueOf(longitude));
                intent.putExtra("latitude",String.valueOf(latitude));
                intent.putExtra("userId",String.valueOf(userId));
                intent.putExtra("actualCheckId",String.valueOf(actualCheckId));
                startActivity(intent);
            }else {
                Toast.makeText(OutCheckActivity.this,"二维码解析失败",Toast.LENGTH_SHORT).show();
//                finish();
            }
        }
    }




    private void CheckStartCall(long userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.CHECK_START_CALL(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    StartCheckModel startCheckModel = new Gson().fromJson(response.body(),StartCheckModel.class);
                    tv_time.setText("开始时间：" + startCheckModel.getCheckStartTimeStr());
                    configTime = startCheckModel.getConfigTime();

//                    if (Integer.valueOf(intent.getStringExtra("checkStatus")) == 1){
//                        actualCheckId = Long.valueOf(intent.getStringExtra("actualCheckId"));
//                    }else if (Integer.valueOf(intent.getStringExtra("checkStatus")) == 2){
//                        actualCheckId = startCheckModel.getActualCheckId();
//                    }

                    actualCheckId = startCheckModel.getActualCheckId();
                    location();


//                    Toast.makeText(OutCheckActivity.this,actualCheckId + "",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    /**
     * 绑定定位服务
     */
    private void location() {
        Intent intent = new Intent(this, MapService.class);
        intent.putExtra("configTime",String.valueOf(configTime));
        intent.putExtra("userId",userId);
        intent.putExtra("actualCheckId",actualCheckId);
        startService(intent);
        //传入本地的 Handler
//        MapService.handler = handler;
    }

    private void unbindService() {
        Intent intent = new Intent(this, MapService.class);
        stopService(intent);
    }

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取服务的代理对象
            binder = (MapService.MyBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            MapService.flag = false;
        }
    };
    /**
     * 接受到消息后将城市名显示出来
     */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (binder != null) {
                latitude = String.valueOf(binder.getCity().get("latitude"));
                longitude = String.valueOf(binder.getCity().get("longitude"));

//                Toast.makeText(OutCheckActivity.this,binder.getCity().get("latitude") + "",Toast.LENGTH_SHORT).show();
//                UPDATE_POSITION_CALL(userId,binder.getCity().get("longitude"),binder.getCity().get("latitude"),actualCheckId);
            }
        }
    };




    private void CHECK_END_CALL(long userId,long actualCheckId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.CHECK_END_CALL(userId, actualCheckId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    CheckEndModel checkEndModel = new Gson().fromJson(response.body(),CheckEndModel.class);

                    btn_finish.setVisibility(View.GONE);
                    tv_status.setText("巡查结束");
                    tv_time.setText("开始时间：" + checkEndModel.getCheckStartTimeStr());
                    tv_endtime.setVisibility(View.VISIBLE);
                    tv_endtime.setText("结束时间：" + checkEndModel.getCheckEndTimeStr());
                    tv_durtime.setVisibility(View.VISIBLE);
                    tv_durtime.setText("本次巡检时长：" + checkEndModel.getCheckDuration());
                    tv_todaytime.setVisibility(View.VISIBLE);
                    tv_todaytime.setText("本日累积时长：" + checkEndModel.getTodayDuration());

                    tv_mark.setVisibility(View.GONE);
                    layout_mark.setVisibility(View.GONE);
                    MapService.flag = false;
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}
