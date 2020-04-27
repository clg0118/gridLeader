package xxy.com.gridleader.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.AboutUsActivity;
import xxy.com.gridleader.activity.ChangePwdActivity;
import xxy.com.gridleader.adapter.CheckListAdapter;
import xxy.com.gridleader.adapter.ProblemListAdapter;
import xxy.com.gridleader.model.AlarmListModel;
import xxy.com.gridleader.model.AlarmModel;
import xxy.com.gridleader.model.CheckItemResultModel;
import xxy.com.gridleader.model.CheckListResultModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;
import xxy.com.gridleader.utils.WeiboDialogUtils;

/**
 * Created by XPS13 on 2018/11/8.
 */

public class GridFragment extends Fragment{

    private SharedPreferences loginSP;
    private SharedPreferences.Editor editor;
    private ListView grid_list;
    private Dialog mWeiboDialog;
    private long userId;

    public GridFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
    }

    private void initUi(){
        loginSP = getContext().getSharedPreferences("loginSP", Context.MODE_PRIVATE);
        editor = loginSP.edit();
        String pic = loginSP.getString("pic","");
        String name = loginSP.getString("name","");
        String areaName = loginSP.getString("areaName","");
        String jobNumber = loginSP.getString("jobNumber","");
        int type = loginSP.getInt("type",0);
        userId = loginSP.getLong("userId",0);

        grid_list = getView().findViewById(R.id.grid_list);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(),"加载中...");
        CheckList_Call(userId);

    }

    private void CheckList_Call(long userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.CHECK_LIST(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    CheckListResultModel checkListResultModel = new Gson().fromJson(response.body(), CheckListResultModel.class);

                    List<CheckItemResultModel> checkItemResultModels = checkListResultModel.getResultData();
                    CheckListAdapter adapter = new CheckListAdapter(checkItemResultModels,getContext());
                    grid_list.setAdapter(adapter);

                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
