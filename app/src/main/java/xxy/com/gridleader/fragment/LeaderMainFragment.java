package xxy.com.gridleader.fragment;


import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.ProblemListActivity;
import xxy.com.gridleader.adapter.LeaderMainListAdapter;
import xxy.com.gridleader.model.AlarmListModel;
import xxy.com.gridleader.model.AlarmModel;
import xxy.com.gridleader.model.BaseInfoModel;
import xxy.com.gridleader.model.PercentModel;
import xxy.com.gridleader.model.TotalAlarmModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderMainFragment extends Fragment {
    private SharedPreferences loginSP;
    private long userId;
    private TextView tv_register_num,tv_girdman_num,tv_new_info_num;
    private TextView tv_total_num,tv_total_change,tv_month_num,tv_month_change,tv_year_num,tv_year_change,tv_to_distribute,tv_to_distribute_change,tv_distributing,tv_distributing_change,tv_to_ensure,tv_to_ensure_change,tv_ensured,tv_ensured_change;
    private ImageView img_total_change,img_month_change,img_year_change,img_to_distribute,img_distributing,img_to_ensure,img_ensured;
    private String new_info_num,total_num,month_num,month_change,month_to_distribute,month_to_distribute_change,month_distributing,month_distributing_change,month_to_ensure,month_to_ensure_change,month_ensured,month_ensured_change;
    private String   year_num,year_change,year_to_distribute,year_to_distribute_change,year_distributing,year_distributing_change,year_to_ensure,year_to_ensure_change,year_ensured,year_ensured_change;
    private LinearLayout linear_month_sta,linear_year_sta;
    private View view_month_bottom,view_year_bottom;
    private ListView list_help_handle;
    private LinearLayout linear_daifenpei,linear_chulizhong,linear_daiqueren,linear_yiqueren;
    private int item_height;
    private CircleProgressBar pro_inner,pro_outer;
    private TextView tv_progress_num;
    private Dialog mWeiboDialog;



    public LeaderMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        initUi();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initUi(){


        View view = LayoutInflater.from(getContext()).inflate(R.layout.leader_main_list_item, null);
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
//        view.getMeasuredWidth(); // 获取宽度
        item_height = view.getMeasuredHeight(); // 获取高度


        loginSP = getContext().getSharedPreferences("loginSP", Context.MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);

        pro_inner = getView().findViewById(R.id.pro_inner);
        pro_outer = getView().findViewById(R.id.pro_outer);
        tv_progress_num = getView().findViewById(R.id.tv_progress_num);


        linear_daifenpei = getView().findViewById(R.id.linear_daifenpei);
        linear_chulizhong = getView().findViewById(R.id.linear_chulizhong);
        linear_daiqueren = getView().findViewById(R.id.linear_daiqueren);
        linear_yiqueren = getView().findViewById(R.id.linear_yiqueren);



        list_help_handle = getView().findViewById(R.id.list_help_handle);
        list_help_handle.setFocusable(false);

        tv_new_info_num =getView().findViewById(R.id.tv_new_info_num);
        tv_register_num = getView().findViewById(R.id.tv_register_num);
        tv_girdman_num = getView().findViewById(R.id.tv_girdman_num);

        tv_total_num = getView().findViewById(R.id.tv_total_num);
        tv_total_change = getView().findViewById(R.id.tv_total_change);
        tv_month_num = getView().findViewById(R.id.tv_month_num);
        tv_month_change = getView().findViewById(R.id.tv_month_change);
        tv_year_num = getView().findViewById(R.id.tv_year_num);
        tv_year_change = getView().findViewById(R.id.tv_year_change);
        tv_to_distribute = getView().findViewById(R.id.tv_to_distribute);
        tv_to_distribute_change = getView().findViewById(R.id.tv_to_distribute_change);
        tv_distributing = getView().findViewById(R.id.tv_distributing);
        tv_distributing_change = getView().findViewById(R.id.tv_distributing_change);
        tv_to_ensure = getView().findViewById(R.id.tv_to_ensure);
        tv_to_ensure_change = getView().findViewById(R.id.tv_to_ensure_change);
        tv_ensured = getView().findViewById(R.id.tv_ensured);
        tv_ensured_change = getView().findViewById(R.id.tv_ensured_change);

        img_total_change = getView().findViewById(R.id.img_total_change);
        img_month_change = getView().findViewById(R.id.img_month_change);
        img_year_change = getView().findViewById(R.id.img_year_change);
        img_to_distribute = getView().findViewById(R.id.img_to_distribute);
        img_distributing = getView().findViewById(R.id.img_distributing);
        img_to_ensure = getView().findViewById(R.id.img_to_ensure);
        img_ensured = getView().findViewById(R.id.img_ensured);

        linear_month_sta = getView().findViewById(R.id.linear_month_sta);
        linear_year_sta = getView().findViewById(R.id.linear_year_sta);
        view_month_bottom = getView().findViewById(R.id.view_month_bottom);
        view_year_bottom = getView().findViewById(R.id.view_year_bottom);

        PERCENTQUERY_CALL(userId);
        TOTALALARM_CALL(userId,1);
        TOTALALARM_CALL(userId,0);
        BASEINFO_CALL(userId);
        ALARMLIST_CALL();

        linear_month_sta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_year_bottom.setVisibility(View.INVISIBLE);
                view_month_bottom.setVisibility(View.VISIBLE);
                TOTALALARM_CALL(userId,0);
            }
        });

        linear_year_sta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_year_bottom.setVisibility(View.VISIBLE);
                view_month_bottom.setVisibility(View.INVISIBLE);
                TOTALALARM_CALL(userId,1);
            }
        });


        linear_daifenpei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProblemListActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
        linear_chulizhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProblemListActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });
        linear_daiqueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProblemListActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
            }
        });
        linear_yiqueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProblemListActivity.class);
                intent.putExtra("type",4);
                startActivity(intent);
            }
        });

    }



    private void TOTALALARM_CALL(long userId, final int type){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.TOTALALARM_CALL(userId, type);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    TotalAlarmModel totalAlarmModel = new Gson().fromJson(response.body(),TotalAlarmModel.class);
                    if (totalAlarmModel != null){
                        if (type == 0){
                            setMonthData(totalAlarmModel);
                        }else if (type == 1){
                            setYearData(totalAlarmModel);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private void BASEINFO_CALL(long userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.BASEINFO_CALL(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
               if (response.isSuccessful()){
                   BaseInfoModel baseInfoModel = new Gson().fromJson(response.body(),BaseInfoModel.class);
                   tv_register_num.setText(String.valueOf(baseInfoModel.getPointInfoList().get(0).getPeople()));
                   tv_girdman_num.setText(String.valueOf(baseInfoModel.getPointInfoList().get(0).getStaffNum()));
               }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void ALARMLIST_CALL(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.WARNALARM_LIST(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(getContext(),response.body(),Toast.LENGTH_SHORT).show();
                    AlarmListModel alarmListModel = new Gson().fromJson(response.body(),AlarmListModel.class);
                    List<AlarmModel> list = alarmListModel.getAlarmList();

                    tv_new_info_num.setText(list.size()+"");

                    LeaderMainListAdapter adapter = new LeaderMainListAdapter(list,getContext());

                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) list_help_handle.getLayoutParams();
                    params.height = item_height * (list.size()+1);
                    list_help_handle.setLayoutParams(params);
                    list_help_handle.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void PERCENTQUERY_CALL(long userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.PERCENTQUERY_CALL(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    PercentModel percentModel = new Gson().fromJson(response.body(),PercentModel.class);
                    tv_progress_num.setText(String.valueOf(percentModel.getMonthCount()));

                    int p1 = (int) Math.ceil(100 * Double.valueOf(percentModel.getPercentOfYear()));
                    ValueAnimator animator1 = ValueAnimator.ofInt(0, p1);
                    animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
//                int progress = (int) animation.getAnimatedValue();
                            pro_inner.setProgress((int) animation.getAnimatedValue());
                        }
                    });
//        animator.setRepeatCount(ValueAnimator.INFINITE);
                    animator1.setDuration(1500);
                    animator1.start();

                    int p2 = (int) Math.ceil(100 * Double.valueOf(percentModel.getPercentOfTotal()));
                    ValueAnimator animator2 = ValueAnimator.ofInt(0, p2);
                    animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
//                int progress = (int) animation.getAnimatedValue();
                            pro_outer.setProgress((int) animation.getAnimatedValue());
                        }
                    });
//        animator.setRepeatCount(ValueAnimator.INFINITE);
                    animator2.setDuration(1500);
                    animator2.start();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void setMonthData(TotalAlarmModel totalAlarmModel){
        total_num = String.valueOf(totalAlarmModel.getAllTotal());
        month_num = String.valueOf(totalAlarmModel.getTotal());
        month_change = String.valueOf(totalAlarmModel.getAddtotal());
        month_to_distribute = String.valueOf(totalAlarmModel.getUnabs());
        month_distributing = String.valueOf(totalAlarmModel.getInhand());
        month_to_ensure = String.valueOf(totalAlarmModel.getConfirm());
        month_ensured = String.valueOf(totalAlarmModel.getConfi());
        month_ensured_change = String.valueOf(totalAlarmModel.getAddconfi());
        new_info_num = String.valueOf(totalAlarmModel.getAssit());

        tv_new_info_num.setText(new_info_num);
        tv_total_num.setText(total_num);
        tv_month_num.setText(month_num);

        if (totalAlarmModel.getAddtotal() > 0){
            tv_month_change.setText("("+"+"+month_change+")");
            img_month_change.setImageResource(R.drawable.img_increase);
            img_month_change.setVisibility(View.VISIBLE);
        }
        else if (totalAlarmModel.getAddtotal() == 0){
            tv_month_change.setText("(0)");
            img_month_change.setVisibility(View.INVISIBLE);
        }
        else {
            tv_month_change.setText("("+"-"+month_change+")");
            img_month_change.setImageResource(R.drawable.img_decline);
            img_month_change.setVisibility(View.VISIBLE);
        }

        tv_to_distribute.setText(month_to_distribute);
        tv_distributing.setText(month_distributing);
        tv_to_ensure.setText(month_to_ensure);
        tv_ensured.setText(month_ensured);
        tv_ensured_change.setText(month_ensured_change);
        if (totalAlarmModel.getAddconfi() > 0){
            img_ensured.setVisibility(View.VISIBLE);
            img_ensured.setImageResource(R.drawable.img_increase);
        }else if (totalAlarmModel.getAddconfi() == 0)
            img_ensured.setVisibility(View.GONE);
        else {
            img_ensured.setVisibility(View.VISIBLE);
            img_ensured.setImageResource(R.drawable.img_decline);
        }
    }

    private void setYearData(TotalAlarmModel totalAlarmModel){
        total_num = String.valueOf(totalAlarmModel.getAllTotal());
        year_num = String.valueOf(totalAlarmModel.getTotal());
        year_change = String.valueOf(totalAlarmModel.getAddtotal());
        year_to_distribute = String.valueOf(totalAlarmModel.getUnabs());
        year_distributing = String.valueOf(totalAlarmModel.getInhand());
        year_to_ensure = String.valueOf(totalAlarmModel.getConfirm());
        year_ensured = String.valueOf(totalAlarmModel.getConfi());
        year_ensured_change = String.valueOf(totalAlarmModel.getAddconfi());
        new_info_num = String.valueOf(totalAlarmModel.getAssit());

        tv_new_info_num.setText(new_info_num);
        tv_year_num.setText(year_num);
        tv_total_num.setText(total_num);
        if (totalAlarmModel.getAddtotal() > 0){
            tv_year_change.setText("("+"+"+year_change+")");
            img_year_change.setImageResource(R.drawable.img_increase);
            img_year_change.setVisibility(View.VISIBLE);
        }
        else if (totalAlarmModel.getAddtotal() == 0){
            tv_year_change.setText("(0)");
            img_year_change.setVisibility(View.INVISIBLE);
        }
        else {
            tv_year_change.setText("("+"-"+year_change+")");
            img_year_change.setImageResource(R.drawable.img_decline);
            img_year_change.setVisibility(View.VISIBLE);
        }

        tv_to_distribute.setText(year_to_distribute);
        tv_distributing.setText(year_distributing);
        tv_to_ensure.setText(year_to_ensure);
        tv_ensured.setText(year_ensured);
        tv_ensured_change.setText(year_ensured_change);
        if (totalAlarmModel.getAddconfi() > 0){
            img_ensured.setVisibility(View.VISIBLE);
            img_ensured.setImageResource(R.drawable.img_increase);
        }else if (totalAlarmModel.getAddconfi() == 0)
            img_ensured.setVisibility(View.GONE);
        else {
            img_ensured.setVisibility(View.VISIBLE);
            img_ensured.setImageResource(R.drawable.img_decline);
        }
    }
}
