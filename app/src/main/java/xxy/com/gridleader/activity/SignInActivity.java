package xxy.com.gridleader.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.model.LoginModel;
import xxy.com.gridleader.model.ScanQRModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

public class SignInActivity extends AppCompatActivity {
    private String scanresult;
    private double longitude,latitude;
    private TextView tv_location_info,tv_content;
    private Button btn_signin;
    private long userId,addressId,actualCheckId;
    private ImageView img_back;
    private static double EARTH_RADIUS = 6371.393;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
    }

    private void init(){
        Intent intent = getIntent();
        scanresult = intent.getStringExtra("scanresult");
        userId = Long.valueOf(intent.getStringExtra("userId"));
        actualCheckId = Long.valueOf(intent.getStringExtra("actualCheckId"));
        tv_location_info = findViewById(R.id.tv_location_info);
        tv_content = findViewById(R.id.tv_content);
        btn_signin = findViewById(R.id.btn_signin);
        //当前自身位置
        if(intent.getStringExtra("longitude").equals("null")  || intent.getStringExtra("latitude").equals("null")) {
            tv_content.setTextColor(Color.RED);
            tv_content.setText("位置获取失败，请查看GPS是否已经打开");
            btn_signin.setText("返回");
            btn_signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            return;
        }
        longitude = Double.valueOf(intent.getStringExtra("longitude"));
        latitude = Double.valueOf(intent.getStringExtra("latitude"));

        int index = scanresult.indexOf("{");
        ScanQRModel scanQRModel = new Gson().fromJson(scanresult.substring(index),ScanQRModel.class);
        double QRlng = Double.valueOf(scanQRModel.getLongitude());
        double QRla = Double.valueOf(scanQRModel.getLatitude());
        addressId = Long.valueOf(scanQRModel.getAddressId());

        Log.d("SignActivity",scanresult);
        Log.d("SignActivity",addressId + "");
        Log.d("SignActivity",latitude + "");
        Log.d("SignActivity",longitude + "");
        Log.d("SignActivity",QRla + "");
        Log.d("SignActivity",QRlng + "");

        tv_location_info.setText(scanQRModel.getAddress());
//        if (GetDistance(latitude,longitude,QRla,QRlng) > 200){
////            if (GetDistance(latitude,longitude,latitude,longitude) > 200){
//            tv_content.setTextColor(Color.RED);
//            tv_content.setText("不在签到范围内，请查看GPS是否已经打开");
//            btn_signin.setText("返回");
//            btn_signin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }else {
            tv_content.setTextColor(Color.GREEN);
            tv_content.setText("已在签到范围内，可以签到");
            btn_signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SIGN_CALL(userId,addressId,actualCheckId,longitude,latitude);
                }
            });
//        }

        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


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



    private void SIGN_CALL(final long userId, final long addressId, final long actualCheckId,final double longitude,final double latitude){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.SIGN_CALL(userId,addressId,actualCheckId,longitude,latitude);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("SignActivity",userId + "");
                Log.d("SignActivity",addressId + "");
                Log.d("SignActivity",actualCheckId + "");

                if (response.isSuccessful()) {
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if (loginModel.getResultValue()){
                        Toast.makeText(SignInActivity.this,"签到成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}
