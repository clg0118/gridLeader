package xxy.com.gridleader.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import xxy.com.gridleader.activity.GongdanListActivity;
import xxy.com.gridleader.activity.OutCheckActivity;
import xxy.com.gridleader.activity.PictureActivity;
import xxy.com.gridleader.activity.ProblemListActivity;
import xxy.com.gridleader.adapter.ThirdGridViewAdapter;
import xxy.com.gridleader.model.CategoryListModel;
import xxy.com.gridleader.model.CheckStatusModel;
import xxy.com.gridleader.model.ThirdGridViewModel;
import xxy.com.gridleader.model.TotalAlarmModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondMainFragment extends Fragment {
    private GridView gridView;
    private SharedPreferences loginSP;
    private long userId;
    private int item_height;
    private TextView tv_gongdan_num,tv_to_distribute_num,tv_hangding_num,tv_to_confirm_num,tv_confirmed_num,tv_confirmed_change;
    private ImageView img_confirmed;
    private LinearLayout linear_month,linear_year;
    private View view_month_bottom,view_year_bottom;
    private TextView tv_report;
    private RelativeLayout layout_outcheck,linear_chulizhong,linear_daiqueren,linear_yiqueren,linear_wodegongdan;


    public SecondMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
    }

    private void initUi(){
        MyConstants.LOADING(getContext(),200);


        View view = LayoutInflater.from(getContext()).inflate(R.layout.third_grid_item, null);
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
//        view.getMeasuredWidth(); // 获取宽度
        item_height = view.getMeasuredHeight(); // 获取高度

        loginSP = getContext().getSharedPreferences("loginSP", Context.MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);

        linear_chulizhong = getView().findViewById(R.id.linear_chulizhong_2);
        linear_daiqueren = getView().findViewById(R.id.linear_daiqueren_2);
        linear_yiqueren = getView().findViewById(R.id.linear_yiqueren_2);
        linear_wodegongdan = getView().findViewById(R.id.linear_wodegongdan);
        layout_outcheck = getView().findViewById(R.id.layout_outcheck);


        tv_hangding_num = getView().findViewById(R.id.tv_hangding_num);
        tv_to_confirm_num = getView().findViewById(R.id.tv_to_confirm_num);
        tv_confirmed_num = getView().findViewById(R.id.tv_confirmed_num);
        tv_confirmed_change = getView().findViewById(R.id.tv_confirmed_change);

        img_confirmed = getView().findViewById(R.id.img_confirmed);

        linear_month = getView().findViewById(R.id.linear_month);
        linear_year = getView().findViewById(R.id.linear_year);

        view_month_bottom = getView().findViewById(R.id.view_month_bottom);
        view_year_bottom = getView().findViewById(R.id.view_year_bottom);

//        gridView = getView().findViewById(R.id.grid_view);
        tv_report = getView().findViewById(R.id.tv_report);



        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PictureActivity.class);
                intent.putExtra("source","main");
                startActivity(intent);
            }
        });

//        CATEALARM_CALL(userId,0);
        TOTALALARM_CALL(userId,0);

        linear_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyConstants.LOADING(getContext(),200);
                view_month_bottom.setVisibility(View.VISIBLE);
                view_year_bottom.setVisibility(View.INVISIBLE);
//                CATEALARM_CALL(userId,0);
                TOTALALARM_CALL(userId,0);
            }
        });

        linear_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyConstants.LOADING(getContext(),200);
                view_month_bottom.setVisibility(View.INVISIBLE);
                view_year_bottom.setVisibility(View.VISIBLE);
//                CATEALARM_CALL(userId,1);
                TOTALALARM_CALL(userId,1);
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

        int roleId = loginSP.getInt("roleId",-1);

        if (roleId == 2){
            linear_wodegongdan.setVisibility(View.GONE);
            layout_outcheck.setVisibility(View.GONE);
        }else{
            linear_wodegongdan.setVisibility(View.VISIBLE);
            layout_outcheck.setVisibility(View.VISIBLE);
        }

        layout_outcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"clicked",Toast.LENGTH_SHORT).show();
                CHECK_STATUS_CALL(userId);
            }
        });

        linear_wodegongdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),GongdanListActivity.class);

                startActivity(intent);
            }
        });


    }

    private void CHECK_STATUS_CALL(final long userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.CHECK_STATUS_CALL(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(getActivity(),userId + "response" + response.code(),Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    CheckStatusModel checkStatusModel = new Gson().fromJson(response.body(),CheckStatusModel.class);
                    Intent intent = new Intent(getActivity(),OutCheckActivity.class);
                    if (checkStatusModel.getCheckStatus() == 1){
                        intent.putExtra("userId",String.valueOf(userId));
                        intent.putExtra("checkStatus",String.valueOf(checkStatusModel.getCheckStatus()));
                        intent.putExtra("actualCheckId",String.valueOf(checkStatusModel.getActualCheckId()));
                        intent.putExtra("checkStartTimeStr",String.valueOf(checkStatusModel.getCheckStartTimeStr()));
                    }else if (checkStatusModel.getCheckStatus() == 2){
                        intent.putExtra("checkStatus",String.valueOf(checkStatusModel.getCheckStatus()));
                        intent.putExtra("userId",String.valueOf(userId));
                    }
                    startActivity(intent);

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }



    private void CATEALARM_CALL(long userId, int type){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.CATEALARM_CALL(userId,type);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    CategoryListModel categoryListModel = new Gson().fromJson(response.body(),CategoryListModel.class);


                    List<ThirdGridViewModel> thirdGridViewModels = new ArrayList<>();
                    for (int i  = 0 ; i < categoryListModel.getCategoryList().size(); i++){
                        ThirdGridViewModel thirdGridViewModel = new ThirdGridViewModel();
                        thirdGridViewModel.setChange(String.valueOf(categoryListModel.getCategoryList().get(i).getAddValue()));
                        thirdGridViewModel.setTittle(String.valueOf(categoryListModel.getCategoryList().get(i).getName()));
                        thirdGridViewModel.setPercent(String.valueOf(categoryListModel.getCategoryList().get(i).getPercent()));
                        thirdGridViewModel.setProcess_num(String.valueOf(categoryListModel.getCategoryList().get(i).getValue()));
                        thirdGridViewModels.add(thirdGridViewModel);
                    }
                    ThirdGridViewAdapter adapter = new ThirdGridViewAdapter(thirdGridViewModels,getContext());

//                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) gridView.getLayoutParams();

//                    if (thirdGridViewModels.size() % 2 == 0){
//                        params.height = item_height * (thirdGridViewModels.size()/2);
//                    }else {
//                        params.height = item_height * (thirdGridViewModels.size()/2) + 1;
//                    }
//                    gridView.setLayoutParams(params);
//
//                    gridView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
                        tv_hangding_num.setText(String.valueOf(totalAlarmModel.getInhand()));
                        tv_to_confirm_num.setText(String.valueOf(totalAlarmModel.getConfirm()));
                        tv_confirmed_num.setText(String.valueOf(totalAlarmModel.getConfi()));
                        tv_confirmed_change.setText(String.valueOf(totalAlarmModel.getAddconfi()));

                        if (totalAlarmModel.getAddconfi() > 0){
                            img_confirmed.setVisibility(View.VISIBLE);
                            img_confirmed.setImageResource(R.drawable.img_increase);
                            tv_confirmed_change.setText("+" + String.valueOf(totalAlarmModel.getAddconfi()));
                        }else if (totalAlarmModel.getAddconfi() == 0){
                            img_confirmed.setVisibility(View.INVISIBLE);
                            tv_confirmed_change.setText("0");
                        }else if (totalAlarmModel.getAddconfi() < 0){
                            img_confirmed.setVisibility(View.VISIBLE);
                            img_confirmed.setImageResource(R.drawable.img_decline);
                            tv_confirmed_change.setText(String.valueOf(totalAlarmModel.getAddconfi()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}
