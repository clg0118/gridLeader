package xxy.com.gridleader.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
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
import xxy.com.gridleader.view.XListView;

public class AlarmListActivity extends AppCompatActivity implements XListView.IXListViewListener {
    private SharedPreferences loginSP;
    private XListView lv;
    private ProblemListAdapter listItemAdapter = null;
    private HashMap<String,Object> map = null;
    private String index = 0 + "";
    private long userId;
    private ArrayList<HashMap<String,Object>> mapList;
    private Handler mHandler;
    private int Fflag = 0;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        initUi();

    }

    private void initUi(){
        loginSP = getSharedPreferences("loginSP",MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);
        img_back = findViewById(R.id.img_back);
        lv = (XListView) findViewById(R.id.lv);
        lv.setXListViewListener(this);
        mHandler = new Handler();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapList = new ArrayList<HashMap<String,Object>>();
        ALARMLIST_CALL(userId,Long.valueOf(index));
    }


    private void ALARMLIST_CALL(long userId,long index){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.ALARMLIST_CALL(userId,4, index,Integer.MAX_VALUE);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    AlarmListModel alarmListModel = new Gson().fromJson(response.body(),AlarmListModel.class);
                    List<AlarmModel> list = alarmListModel.getAlarmList();
                    if (alarmListModel.getAlarmList().size() > 0){
//                        if (alarmListModel.getAlarmList().size() < 6)
//                            lv.setPullLoadEnable(false);
//                        else
                        lv.setPullLoadEnable(false);

                        listItemAdapter = new ProblemListAdapter(alarmListModel.getAlarmList(),AlarmListActivity.this);

                        if (lv.getFirstVisiblePosition() == 0) {
                            Fflag = lv.getFirstVisiblePosition();
                        }

                        if (Fflag == 0) {
                            Fflag = 1;
                            lv.setAdapter(listItemAdapter);
                        } else {
                            lv.requestLayout();
                            listItemAdapter.notifyDataSetChanged();
                        }
                    }else
                        lv.setPullLoadEnable(false);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    public void messageDelay(String message) {
//        TuSdk.messageHub().showSuccess(this, message);
        new Handler().postDelayed(new Runnable() {
            public void run() {
//                TuSdk.messageHub().dismiss();
            }
        }, 1000);

    }
    public void MainActivityMessage(String message) {
//        TuSdk.messageHub().showError(this, message);
        new Handler().postDelayed(new Runnable() {
            public void run() {
//                TuSdk.messageHub().dismiss();
            }
        }, 1000);
    }

    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime(" ");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                index = 0 + "";
                mapList = new ArrayList<HashMap<String, Object>>();
                ALARMLIST_CALL(userId,Long.valueOf(index));
                onLoad();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                index = Long.valueOf(index).intValue() + 1 + "";
                ALARMLIST_CALL(userId,Long.valueOf(index));
                //listItemAdapter.notifyDataSetChanged();
                //mListView.setAdapter(listItemAdapter);
                onLoad();
            }
        }, 500);
    }

    public void back(View v) {
        finish();
    }


}
