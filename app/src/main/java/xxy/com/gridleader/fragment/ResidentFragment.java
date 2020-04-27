package xxy.com.gridleader.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.RegisterActivity;
import xxy.com.gridleader.activity.ResidentResultsActivity;
import xxy.com.gridleader.model.LoginModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;
import xxy.com.gridleader.utils.WeiboDialogUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResidentFragment extends Fragment {
    private Dialog mWeiboDialog;
    private SharedPreferences loginSP;
    private long userId;
    private TextView tv_shenfenzheng,tv_huzhao,tv_jiashizheng,tv_shiminka;
    private int type = 1;
    private RelativeLayout layout_register;
    private Button img_search,btn_check;
    private EditText et_number,et_check_num,et_name,et_room;
    private LinearLayout layout_resident,layout_check;

    private SharedPreferences checkSP;
    private SharedPreferences.Editor editor;



    public ResidentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resident, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();

        layout_check.setVisibility(View.GONE);
        layout_resident.setVisibility(View.VISIBLE);

//        checkSP = getContext().getSharedPreferences("checkSP",Context.MODE_PRIVATE);
//        editor = checkSP.edit();

//        Date date=new Date();
//        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
//        String curTime = format.format(date);
//        String saveTime = checkSP.getString("time","2018-01-01");
//
//        if (Integer.valueOf(differDays(strToDate(saveTime),date)) >= 1){
//            layout_check.setVisibility(View.VISIBLE);
//            layout_resident.setVisibility(View.GONE);
//        }else {
//            layout_check.setVisibility(View.GONE);
//            layout_resident.setVisibility(View.VISIBLE);
//        }

//        Toast.makeText(getActivity(),differDays(strToDate(saveTime),date) + "",Toast.LENGTH_SHORT).show();
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static int differDays(Date date1, Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }



    private void init(){
        loginSP = getContext().getSharedPreferences("loginSP", Context.MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);



        et_number = (EditText) getView().findViewById(R.id.et_number);
        et_name = (EditText) getView().findViewById(R.id.et_name);
        et_room = (EditText) getView().findViewById(R.id.et_room);
        img_search = (Button) getView().findViewById(R.id.img_search);
        tv_shenfenzheng = (TextView) getView().findViewById(R.id.tv_shenfenzheng);
        tv_huzhao = (TextView) getView().findViewById(R.id.tv_huzhao);
        tv_jiashizheng = (TextView) getView().findViewById(R.id.tv_jiashizheng);
        tv_shiminka = (TextView) getView().findViewById(R.id.tv_shiminka);
        et_check_num = (EditText) getView().findViewById(R.id.et_check_num);
        btn_check = (Button) getView().findViewById(R.id.btn_check);
        layout_resident = (LinearLayout) getView().findViewById(R.id.layout_resident);
        layout_check = (LinearLayout) getView().findViewById(R.id.layout_check);


        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getContext(),"加载中...");
                ConfirmCodeCall(userId,et_check_num.getText().toString().trim());
            }
        });



        layout_register = (RelativeLayout) getView().findViewById(R.id.layout_register);
        layout_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra("userId",String.valueOf(userId));
                getActivity().startActivity(intent);
            }
        });




        tv_shenfenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                tv_shenfenzheng.setBackgroundColor(Color.parseColor("#13AB92"));
                tv_huzhao.setBackgroundColor(Color.WHITE);
                tv_jiashizheng.setBackgroundColor(Color.WHITE);
                tv_shiminka.setBackgroundColor(Color.WHITE);
            }
        });

        tv_huzhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                tv_shenfenzheng.setBackgroundColor(Color.WHITE);
                tv_huzhao.setBackgroundColor(Color.parseColor("#13AB92"));
                tv_jiashizheng.setBackgroundColor(Color.WHITE);
                tv_shiminka.setBackgroundColor(Color.WHITE);
            }
        });

        tv_jiashizheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 3;
                tv_shenfenzheng.setBackgroundColor(Color.WHITE);
                tv_huzhao.setBackgroundColor(Color.WHITE);
                tv_jiashizheng.setBackgroundColor(Color.parseColor("#13AB92"));
                tv_shiminka.setBackgroundColor(Color.WHITE);
            }
        });

        tv_shiminka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 4;
                tv_shenfenzheng.setBackgroundColor(Color.WHITE);
                tv_huzhao.setBackgroundColor(Color.WHITE);
                tv_jiashizheng.setBackgroundColor(Color.WHITE);
                tv_shiminka.setBackgroundColor(Color.parseColor("#13AB92"));
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((et_number.getText().toString() == null || et_number.getText().toString().equals(""))
                        &&(et_name.getText().toString() == null || et_name.getText().toString().equals(""))
                        &&(et_room.getText().toString() == null || et_room.getText().toString().equals(""))){
                    Toast.makeText(getActivity(),"请输入查询条件",Toast.LENGTH_SHORT).show();
                }else {
//                    Toast.makeText(getActivity(),"" + type,Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(),"" + userId,Toast.LENGTH_SHORT).show();
                    QueryResidentCall(type,et_number.getText().toString(),et_name.getText().toString(),et_room.getText().toString(),userId);
                }
            }
        });
    }



    private void QueryResidentCall(int type, String number,String name,String room, final long userId){
        Log.d("Resident",type+"");
        Log.d("Resident",number+"");
        Log.d("Resident",name+"");
        Log.d("Resident",room+"");
        Log.d("Resident",userId+"");
        Intent intent = new Intent(getActivity(), ResidentResultsActivity.class);
        intent.putExtra("userId",String.valueOf(userId));
        intent.putExtra("type",type);
        intent.putExtra("room",room);
        intent.putExtra("number",number);
        intent.putExtra("name",name);
        startActivity(intent);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(MyConstants.BASEURL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
//
//        Call<String> call = request.QUERY_RESIDENT(type, number,name,room, userId);
//
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
////                Toast.makeText(getActivity(),response.code() + "",Toast.LENGTH_SHORT).show();
//                if (response.isSuccessful()){
////                    ResultModel resultModel = new Gson().fromJson(response.body(),ResultModel.class);
//                    Intent intent = new Intent(getActivity(), ResidentResultsActivity.class);
//                    intent.putExtra("userId",String.valueOf(userId));
//                    intent.putExtra("response",response.body());
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });

    }


    private void ConfirmCodeCall(long userId,String checkCode){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);

        Call<String> call = request.CONFIRM_CHECK(userId,checkCode);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(getActivity(),response.code() + "",Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if (loginModel.getResultValue()){
                        Toast.makeText(getActivity(),"验证成功",Toast.LENGTH_SHORT).show();
                        layout_resident.setVisibility(View.VISIBLE);
                        layout_check.setVisibility(View.GONE);
                        Date date=new Date();
                        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        String curTime = format.format(date);
                        editor.putString("time",curTime);
                        editor.commit();
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                    }else {
                        Toast.makeText(getActivity(),"验证失败",Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}
