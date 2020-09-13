package xxy.com.gridleader.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
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
import xxy.com.gridleader.activity.GongdanDetailActivity;
import xxy.com.gridleader.activity.ProblemDetailActivity;
import xxy.com.gridleader.activity.ScreenActivity;
import xxy.com.gridleader.adapter.MaintainListAdapter;
import xxy.com.gridleader.model.MaintainListResultModel;
import xxy.com.gridleader.model.MaintainResultModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;
import xxy.com.gridleader.utils.WeiboDialogUtils;
import xxy.com.gridleader.view.XListView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by XPS13 on 2018/11/14.
 */

public class MaintainFragement extends Fragment {

    private XListView lv;
    private Button bt_screen;
    private SharedPreferences loginSP;
    private SearchView searchView;
    private long userId;
    private Dialog mWeiboDialog;
    private Spinner maintain_typelist;
    private List<Integer> typeList = new ArrayList<>();
    private List<String> typeStr = new ArrayList<>();
    private ArrayList<MaintainResultModel> list = new ArrayList<>();
    private  MaintainListAdapter adapter;
    private int index = 0;
    private int pageSize = 10;
    private int type = 1;
    public MaintainFragement() {
        // Required empty public constructor
    }
    private static final int SCREEN = 0x000001;
    private static final int DETAIL = 0x000002;
    private String latitude = "";
    private String longitude = "";
    private String location = "";
    private String startTime = "";
    private String endTime = "";
    private boolean isSearch = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maintain, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
    }

    private void initUi(){
        final List<TextView> tvList = new ArrayList<>();
        searchView = getView().findViewById(R.id.searchView);


//        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(),"加载中...");

        loginSP = getActivity().getSharedPreferences("loginSP",MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);

        lv = getView().findViewById(R.id.lv);
        bt_screen = getView().findViewById(R.id.bt_screen);
        bt_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScreenActivity.class);
                startActivityForResult(intent, SCREEN);
            }
        });
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(false);
        lv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                isSearch = false;
                MAINTAIN_CALL(userId,0,"",type,isSearch);
            }
        });
        adapter = new MaintainListAdapter(list, getContext(), userId, new MaintainListAdapter.onItemClick() {
            @Override
            public void click(MaintainResultModel item) {
                int status = item.getStatus();
                if (status == 1) {
                    Intent intent = new Intent(getContext(), GongdanDetailActivity.class);
                    long id = item.getId();
                    long alarmId = item.getAlarmId();
                    intent.putExtra("status", String.valueOf(status));
                    intent.putExtra("alarmId", String.valueOf(alarmId));
                    intent.putExtra("id", String.valueOf(id));
                    intent.putExtra("bianhao", item.getMlistNum());
                    intent.putExtra("miaoshu", item.getContent());
                    intent.putExtra("weizhi", item.getLocation());
                    intent.putExtra("leixin", item.getCategoryName());
                    intent.putExtra("shijian", item.getUpdateDtStr());
                    startActivityForResult(intent,DETAIL);
                } else if (status == 6 || status == 8 || status == 9){
                    Intent intent = new Intent(getContext(), GongdanDetailActivity.class);
                    long id = item.getId();
                    long alarmId = item.getAlarmId();

                    intent.putExtra("status", String.valueOf(status));
                    intent.putExtra("alarmId", String.valueOf(alarmId));
                    intent.putExtra("id", String.valueOf(id));
                    intent.putExtra("bianhao", item.getMlistNum());
                    intent.putExtra("miaoshu", item.getContent());
                    intent.putExtra("weizhi", item.getLocation());
                    intent.putExtra("leixin", item.getCategoryName());
                    intent.putExtra("shijian", item.getUpdateDtStr());
                    startActivityForResult(intent,DETAIL);
                } else if (status == 2 || status == 3 || status == 4 || status == 5 || status == 7){
                    Intent intent = new Intent(getContext(), ProblemDetailActivity.class);
                    long id = item.getId();
                    long alarmId = item.getAlarmId();
                    intent.putExtra("alarmId", String.valueOf(alarmId));
                    intent.putExtra("status", String.valueOf(status));
                    startActivityForResult(intent,DETAIL);
                }
            }
        });
        lv.setAdapter(adapter);
        maintain_typelist = getView().findViewById(R.id.maintain_typelist);

        // 设置二级下拉列表的选项内容适配器
        typeList.clear();
        typeStr.clear();
        typeStr.add("全部");
        typeStr.add("等待接单");
        typeStr.add("处理中");
        typeStr.add("待审批");
        typeList.add(1);
        typeList.add(2);
        typeList.add(3);
        typeList.add(4);
        final ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,typeStr);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maintain_typelist.setAdapter(typeAdapter);

//        MAINTAIN_CALL(userId,0,"",type,false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                isSearch = true;
                list.clear();
               type = typeList.get(maintain_typelist.getSelectedItemPosition());
                MAINTAIN_CALL(userId,0,query,typeList.get(maintain_typelist.getSelectedItemPosition()),isSearch);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if("".equals(newText)) {
                    isSearch = true;
                    list.clear();
                    type = typeList.get(maintain_typelist.getSelectedItemPosition());
                    MAINTAIN_CALL(userId,0,"",typeList.get(maintain_typelist.getSelectedItemPosition()),isSearch);
                }
                return false;
            }
        });

        maintain_typelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isSearch = false;
                list.clear();
                index = 0;
                type = typeList.get(maintain_typelist.getSelectedItemPosition());
                MAINTAIN_CALL(userId,0,"",typeList.get(maintain_typelist.getSelectedItemPosition()),isSearch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }


    private void MAINTAIN_CALL(final long userId, int status,String content,int type,boolean isSearch){
        lv.setPullLoadEnable(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        int indexs = 0;
        int count = 1000;
        if (!isSearch){
            indexs = index;
            count = pageSize;
        }
        Call<String> call = request.Maintain_LIST1(userId,status,content,indexs,count,type,startTime,endTime,location);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                WeiboDialogUtils.closeDialog(mWeiboDialog);

                if (response.isSuccessful()) {
                    MaintainListResultModel maintainListResultModel = new Gson().fromJson(response.body(), MaintainListResultModel.class);
                    index += 10;
                    if (maintainListResultModel.getMlist().size() > 0) {
                        list.addAll(maintainListResultModel.getMlist());

                    } else {
                        lv.setPullLoadEnable(false);
                    }

                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCREEN:
                if (resultCode == RESULT_OK && data != null) {
                    latitude = data.getStringExtra("latitude");
                    longitude = data.getStringExtra("longitude");
                    location = data.getStringExtra("location");
                    startTime = data.getStringExtra("startTime");
                    endTime = data.getStringExtra("endTime");
                    index = 0;
                    list.clear();
                    adapter.notifyDataSetChanged();
                    lv.setPullLoadEnable(true);
                    MAINTAIN_CALL(userId,0,"",type,isSearch);
                }
                break;

            case DETAIL:
                index = 0;
                list.clear();
                adapter.notifyDataSetChanged();
                lv.setPullLoadEnable(true);
                MAINTAIN_CALL(userId,0,"",type,isSearch);
                break;

        }
    }
}
