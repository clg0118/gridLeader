package xxy.com.gridleader.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.ProblemDetailActivity;
import xxy.com.gridleader.activity.ScreenActivity;
import xxy.com.gridleader.adapter.ProblemListAdapter;
import xxy.com.gridleader.model.AlarmListModel;
import xxy.com.gridleader.model.AlarmModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;
import xxy.com.gridleader.utils.WeiboDialogUtils;
import xxy.com.gridleader.view.XListView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllProblemFragment extends Fragment {
    private XListView lv;
    private Button bt_screen;
    private SharedPreferences loginSP;
    private long userId;
    private Dialog mWeiboDialog;
    private TextView btn_quanbu, btn_daifenpai, btn_chulizhong, btn_daiqueren, btn_yiqueren;
    private ArrayList<AlarmModel> list = new ArrayList<>();
    private ProblemListAdapter adapter;
    private int index = 0;
    private int pageSize = 10;
    private int status = 0;

    public AllProblemFragment() {
        // Required empty public constructor
    }

    private static final int SCREEN = 0x000001;
    private static final int DETAIL = 0x000002;
    private String latitude = "";
    private String longitude = "";
    private String location = "";
    private String startTime = "";
    private String endTime = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_allproblem, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
    }

    private void initUi() {
        final List<TextView> tvList = new ArrayList<>();
        btn_quanbu = getView().findViewById(R.id.btn_quanbu);
        btn_daifenpai = getView().findViewById(R.id.btn_daifenpei);
        btn_chulizhong = getView().findViewById(R.id.btn_chulizhong);
        btn_daiqueren = getView().findViewById(R.id.btn_daiqueren);
        btn_yiqueren = getView().findViewById(R.id.btn_yiqueren);
        bt_screen = getView().findViewById(R.id.bt_screen);

        bt_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScreenActivity.class);
                startActivityForResult(intent, SCREEN);
            }
        });

        tvList.add(btn_quanbu);
        tvList.add(btn_daifenpai);
        tvList.add(btn_chulizhong);
        tvList.add(btn_daiqueren);
        tvList.add(btn_yiqueren);


        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(), "加载中...");

        loginSP = getActivity().getSharedPreferences("loginSP", MODE_PRIVATE);
        userId = loginSP.getLong("userId", 0);

        lv = getView().findViewById(R.id.lv);
        lv.setPullRefreshEnable(false);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                ALARMLIST_CALL(userId, status, false);
            }
        });
        adapter = new ProblemListAdapter(list, getContext(), new ProblemListAdapter.onItemClick() {
            @Override
            public void click(int i) {

                    Intent intent = new Intent(getContext(), ProblemDetailActivity.class);
                    intent.putExtra("alarmId",list.get(i).getId());
                    intent.putExtra("status",String.valueOf(list.get(i).getStatus()));
                    startActivityForResult(intent,DETAIL);
            }
        });
        lv.setAdapter(adapter);
        ALARMLIST_CALL(userId, 0, true);

        btn_quanbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (TextView tv : tvList) {
                    tv.setTextSize(12);
                    tv.setBackground(null);
                    tv.setTextColor(Color.parseColor("#19A491"));
                }
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(), "加载中...");

                btn_quanbu.setTextSize(15);
                btn_quanbu.setTextColor(Color.WHITE);
                btn_quanbu.setBackgroundResource(R.drawable.btn_round_coner_bg);
                status = 0;
                index = 0;
                list.clear();
                ALARMLIST_CALL(userId, 0, false);
            }
        });
        btn_daifenpai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (TextView tv : tvList) {
                    tv.setTextSize(12);
                    tv.setBackground(null);
                    tv.setTextColor(Color.parseColor("#19A491"));
                }
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(), "加载中...");

                btn_daifenpai.setTextSize(15);
                btn_daifenpai.setTextColor(Color.WHITE);
                btn_daifenpai.setBackgroundResource(R.drawable.btn_round_coner_bg);
                status = 2;
                index = 0;
                list.clear();
                ALARMLIST_CALL(userId, 2, false);
            }
        });
        btn_chulizhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (TextView tv : tvList) {
                    tv.setTextSize(12);
                    tv.setBackground(null);
                    tv.setTextColor(Color.parseColor("#19A491"));
                }
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(), "加载中...");

                btn_chulizhong.setTextSize(15);
                btn_chulizhong.setTextColor(Color.WHITE);
                btn_chulizhong.setBackgroundResource(R.drawable.btn_round_coner_bg);
                status = 5;
                index = 0;
                list.clear();
                ALARMLIST_CALL(userId, 5, false);
            }
        });
        btn_daiqueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (TextView tv : tvList) {
                    tv.setTextSize(12);
                    tv.setBackground(null);
                    tv.setTextColor(Color.parseColor("#19A491"));
                }
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(), "加载中...");

                btn_daiqueren.setTextSize(15);
                btn_daiqueren.setTextColor(Color.WHITE);
                btn_daiqueren.setBackgroundResource(R.drawable.btn_round_coner_bg);
                status = 6;
                index = 0;
                list.clear();
                ALARMLIST_CALL(userId, 6, false);
            }
        });
        btn_yiqueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (TextView tv : tvList) {
                    tv.setTextSize(12);
                    tv.setBackground(null);
                    tv.setTextColor(Color.parseColor("#19A491"));
                }
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(), "加载中...");

                btn_yiqueren.setTextSize(15);
                btn_yiqueren.setTextColor(Color.WHITE);
                btn_yiqueren.setBackgroundResource(R.drawable.btn_round_coner_bg);
                status = 7;
                index = 0;
                list.clear();
                ALARMLIST_CALL(userId, 7, false);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void ALARMLIST_CALL(long userId, int status, final boolean isFirst) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.ALARMLIST_CALL1(userId, status, index, pageSize,startTime,endTime,location);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    AlarmListModel alarmListModel = new Gson().fromJson(response.body(), AlarmListModel.class);
                    if (!isFirst) {
                        index += 10;
                    }
                    if (alarmListModel.getAlarmList().size() > 0) {
                        list.addAll(alarmListModel.getAlarmList());

                    } else {
                        lv.setPullLoadEnable(false);
                    }
                    adapter.notifyDataSetChanged();
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCREEN:
                if (resultCode == RESULT_OK && data != null) {
                    latitude = data.getStringExtra("latitude");
                    longitude = data.getStringExtra("longitude");
                    location = data.getStringExtra("location");
                    startTime = data.getStringExtra("startTime");
                    endTime = data.getStringExtra("endTime");
                    index = 0;
                    lv.setPullLoadEnable(true);
                    list.clear();
                    ALARMLIST_CALL(userId,status,false);
                }
                break;
            case DETAIL:
                index = 0;
                lv.setPullLoadEnable(true);
                list.clear();
                ALARMLIST_CALL(userId,status,false);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
