package xxy.com.gridleader.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.adapter.ProblemListAdapter;
import xxy.com.gridleader.model.AlarmListModel;
import xxy.com.gridleader.model.AlarmModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;
import xxy.com.gridleader.utils.WeiboDialogUtils;

public class ProblemListActivity extends AppCompatActivity {
    private ListView lv;
    private TextView tv_head;
    private ImageView img_back;
    private SharedPreferences loginSP;
    private long userId;
    private int type;
    private Integer userType;
    private Dialog mWeiboDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_list);
        initUi();
    }

    @Override
    protected void onResume() {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ProblemListActivity.this,"加载中...");
        super.onResume();
        switch (type){
            case 1:
                tv_head.setText("待分配");
                ALARMLIST_CALL(userId,2);
                break;
            case 2:
                tv_head.setText("处理中");
                ALARMLIST_CALL(userId,5);
                break;
            case 3:
                tv_head.setText("待确认");
                ALARMLIST_CALL(userId,6);
                break;
            case 4:
                tv_head.setText("已确认");
                ALARMLIST_CALL(userId,7);
                break;
            default:
                break;
        }
    }

    private void initUi(){
        Intent intent = getIntent();
        type = intent.getIntExtra("type",-1);

        loginSP = getSharedPreferences("loginSP",MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);
        userType = loginSP.getInt("type",-1);

        lv = findViewById(R.id.lv);
        tv_head = findViewById(R.id.tv_head);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        switch (type){
//            case 1:
//                tv_head.setText("待分配");
//                ALARMLIST_CALL(userId,1);
//                break;
//            case 2:
//                tv_head.setText("处理中");
//                ALARMLIST_CALL(userId,2);
//                break;
//            case 3:
//                tv_head.setText("待确认");
//                ALARMLIST_CALL(userId,3);
//                break;
//            case 4:
//                tv_head.setText("已确认");
//                ALARMLIST_CALL(userId,4);
//                break;
//            case 5:
//                tv_head.setText("协助处理");
//                ALARMLIST_CALL(userId,5);
//                break;
//            case 6:
//                tv_head.setText("我的工单");
//
//
//                ALARMLIST_CALL(userId,5);
//
//
//                break;
//            default:
//                break;
//        }

    }



    private void ALARMLIST_CALL(long userId,int status){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.ALARMLIST_CALL(userId,status,0,1000);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    AlarmListModel alarmListModel = new Gson().fromJson(response.body(), AlarmListModel.class);
                    List<AlarmModel> list = alarmListModel.getAlarmList();
                    ProblemListAdapter adapter = new ProblemListAdapter(list,ProblemListActivity.this);
                    adapter.setType(userType);
                    lv.setAdapter(adapter);

                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void DEALLIST_CALL(long userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.DEALLIST_CALL(userId,0,1000);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {


//                    AlarmListModel alarmListModel = new Gson().fromJson(response.body(), AlarmListModel.class);
//                    List<AlarmModel> list = alarmListModel.getAlarmList();
//                    ProblemListAdapter adapter = new ProblemListAdapter(list,ProblemListActivity.this);
//                    lv.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
