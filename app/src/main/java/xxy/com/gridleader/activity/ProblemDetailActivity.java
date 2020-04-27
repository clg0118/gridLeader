package xxy.com.gridleader.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.model.DepInfoModel;
import xxy.com.gridleader.model.ImgListModel;
import xxy.com.gridleader.model.LoginModel;
import xxy.com.gridleader.model.QueryProDetailModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

public class ProblemDetailActivity extends AppCompatActivity {
    private SharedPreferences loginSP;
    private int type;
    private String alarmId;
    private ImageView img_back;
    private TextView tv_uncomplete,tv_completed,tv_shijian,tv_liucheng,tv_id,tv_content,tv_location,tv_type,tv_time,tv_content_tittle,tv_handle_method;
    private EditText et_content;
    private Button btn_confirm;
    private ImageView img_1,img_2,img_3,img_4,img_5,img_11,img_22,img_33,img_44,img_55;
    private LinearLayout linear_finish_pics,linear_finish_time,linear_shuoming,linear_handle_method,linear_isComplete;
    private TextView tv_finish_time;
    private int status;
    private long userId;
    private long propertyId = 0;
    private int completed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_detail);
        initUi();
    }

    private void initUi(){
        loginSP = getSharedPreferences("loginSP",MODE_PRIVATE);
        type = loginSP.getInt("type",-1);
        userId = loginSP.getLong("userId",0);

        linear_finish_pics = findViewById(R.id.linear_finish_pics);
        linear_finish_time = findViewById(R.id.linear_finish_time);
        linear_shuoming = findViewById(R.id.linear_shuoming);
        linear_handle_method = findViewById(R.id.linear_handle_method);
        linear_isComplete = findViewById(R.id.linear_isComplete);

        tv_finish_time = findViewById(R.id.tv_finish_time);
        tv_uncomplete = findViewById(R.id.tv_uncomplete);
        tv_uncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed = 0;
                tv_uncomplete.setTextColor(Color.WHITE);
                tv_uncomplete.setBackgroundResource(R.drawable.bg_chosen);

                tv_completed.setTextColor(Color.BLACK);
                tv_completed.setBackgroundResource(R.drawable.bg_unchosen);
            }
        });

        tv_completed = findViewById(R.id.tv_completed);
        tv_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed = 1;
                tv_completed.setTextColor(Color.WHITE);
                tv_completed.setBackgroundResource(R.drawable.bg_chosen);

                tv_uncomplete.setTextColor(Color.BLACK);
                tv_uncomplete.setBackgroundResource(R.drawable.bg_unchosen);
            }
        });

        tv_id = findViewById(R.id.tv_id);
        tv_content = findViewById(R.id.tv_content);
        tv_location = findViewById(R.id.tv_location);
        tv_type = findViewById(R.id.tv_type);
        tv_time = findViewById(R.id.tv_time);
        tv_content_tittle = findViewById(R.id.tv_content_tittle);
        tv_handle_method = findViewById(R.id.tv_handle_method);
        tv_shijian = findViewById(R.id.tv_shijian);


        et_content = findViewById(R.id.et_content);
        btn_confirm = findViewById(R.id.btn_confirm);
        img_1 = findViewById(R.id.img_1);
        img_2 = findViewById(R.id.img_2);
        img_3 = findViewById(R.id.img_3);
        img_4 = findViewById(R.id.img_4);
        img_5 = findViewById(R.id.img_5);
        img_11 = findViewById(R.id.img_11);
        img_22 = findViewById(R.id.img_22);
        img_33 = findViewById(R.id.img_33);
        img_44 = findViewById(R.id.img_44);
        img_55 = findViewById(R.id.img_55);

        tv_liucheng = findViewById(R.id.tv_liucheng);
        img_back = findViewById(R.id.img_back);

        Intent intent = getIntent();
        alarmId = intent.getStringExtra("alarmId");
        status = Integer.valueOf(intent.getStringExtra("status"));

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_liucheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProblemDetailActivity.this,ProgressActivity.class);
                intent1.putExtra("alarmId",alarmId);
                startActivity(intent1);
            }
        });

        tv_handle_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_handle_method.setClickable(false);
                QUERYDISTRUBITE_CALL(userId,Long.valueOf(alarmId));
            }
        });








//        if (type == 5){
//            linear_shuoming.setVisibility(View.GONE);
//            btn_confirm.setVisibility(View.GONE);
//            linear_handle_method.setVisibility(View.GONE);
//        } else {
//            linear_shuoming.setVisibility(View.VISIBLE);
//            btn_confirm.setVisibility(View.VISIBLE);
//            linear_handle_method.setVisibility(View.VISIBLE);
//        }


//        if (roleId == 3 && status == 1){
//            linear_shuoming.setVisibility(View.VISIBLE);
//            linear_handle_method.setVisibility(View.VISIBLE);
//            btn_confirm.setVisibility(View.VISIBLE);
//            tv_content_tittle.setText("处理内容：");
//
//            btn_confirm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (propertyId != 0){
//                        SENDMLIST_CALL(userId,propertyId,Long.valueOf(alarmId),et_content.getText().toString());
//                    }else if (propertyId == 0) {
//                        Toast.makeText(ProblemDetailActivity.this, "请选择处理方式", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }

//        if (roleId == 2 && status == 1){
//            linear_shuoming.setVisibility(View.GONE);
//            linear_handle_method.setVisibility(View.GONE);
//            btn_confirm.setVisibility(View.GONE);
//        }



//        if (status == 2){
//            linear_shuoming.setVisibility(View.GONE);
//            linear_handle_method.setVisibility(View.GONE);
//            btn_confirm.setVisibility(View.GONE);
//        }

//        if (status == 3){
//            linear_isComplete.setVisibility(View.VISIBLE);
//            linear_handle_method.setVisibility(View.GONE);
//            btn_confirm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    CONFIRM_CALL(userId,Long.valueOf(alarmId),completed,et_content.getText().toString().trim()," ");
//                }
//            });
//        }

//        if (status == 4){
//            linear_shuoming.setVisibility(View.GONE);
//            linear_handle_method.setVisibility(View.GONE);
//            btn_confirm.setVisibility(View.GONE);
//        }





        QUERYPRODETAIL_CALL(Long.valueOf(alarmId));



    }

//    private void CONFIRM_CALL(long userId,long alarmId,int confirmResult,String confirmContent,String pics){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(MyConstants.BASEURL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
//        Call<String> call = request.CONFIRM_CALL(userId, alarmId, confirmResult, confirmContent, pics);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()){
//                    Toast.makeText(ProblemDetailActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
//                    finish();
//                }else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }

    private void SENDMLIST_CALL(long userId,long propertyId,long alarmId,String postil){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.SENDMLIST_CALL(userId, propertyId, alarmId, postil);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if (loginModel.getResultValue()){
                        Toast.makeText(ProblemDetailActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(ProblemDetailActivity.this,loginModel.getMessage(),Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(ProblemDetailActivity.this,"提交成功",Toast.LENGTH_SHORT).show();

                }else {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void QUERYDISTRUBITE_CALL(long userId,long alarmId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.QUERYDISTRUBITE_CALL(alarmId,userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    final DepInfoModel depInfoModel = new Gson().fromJson(response.body(),DepInfoModel.class);
                    final int size_picker_1 = depInfoModel.getDepList().size();

                    View view = LayoutInflater.from(ProblemDetailActivity.this).inflate(R.layout.number_picker_item,null);
                    final NumberPicker picker_1 = view.findViewById(R.id.picker_1);
                    final NumberPicker picker_2 = view.findViewById(R.id.picker_2);
                    picker_1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                    picker_2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                    final List<String> list_picker1 = new ArrayList<>();
                    for (int i = 0; i < size_picker_1; i ++){
                        list_picker1.add(depInfoModel.getDepList().get(i).getDepartmentName());
                    }

                    String[] picker_1_value = list_picker1.toArray(new String[list_picker1.size()]);
                    picker_1.setDisplayedValues(picker_1_value);
                    picker_1.setMinValue(0);
                    picker_1.setMaxValue(picker_1_value.length - 1);


                    List<String> list_picker2 = new ArrayList<>();
                    for (int i = 0; i < depInfoModel.getDepList().get(0).getActorList().size(); i ++){
                        list_picker2.add(depInfoModel.getDepList().get(0).getActorList().get(i).getName());
                    }

                    String[] picker_2_value = list_picker2.toArray(new String[list_picker2.size()]);
                    picker_2.setDisplayedValues(picker_2_value);
                    picker_2.setMinValue(0);
                    picker_2.setMaxValue(picker_2_value.length - 1);

                    picker_1.setOnScrollListener(new NumberPicker.OnScrollListener() {
                        @Override
                        public void onScrollStateChange(NumberPicker numberPicker, int i) {

                            if (0 < i && i < size_picker_1){
                                List<String> list_picker2 = new ArrayList<>();

                                for (int j = 0; j < depInfoModel.getDepList().get(i).getActorList().size(); j ++){
                                    list_picker2.add(depInfoModel.getDepList().get(i).getActorList().get(j).getName());
                                }
                                String[] picker_2_value = list_picker2.toArray(new String[list_picker2.size()]);
                                picker_2.setDisplayedValues(picker_2_value);
                                picker_2.setMinValue(0);
                                picker_2.setMaxValue(picker_2_value.length - 1);
                            }

                        }
                    });

                    picker_1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int i) {
                            if (0 < i && i < size_picker_1){
                                List<String> list_picker2 = new ArrayList<>();

                                for (int j = 0; j < depInfoModel.getDepList().get(i).getActorList().size(); j ++){
                                    list_picker2.add(depInfoModel.getDepList().get(i).getActorList().get(j).getName());
                                }
                                String[] picker_2_value = list_picker2.toArray(new String[list_picker2.size()]);
                                picker_2.setDisplayedValues(picker_2_value);
                                picker_2.setMinValue(0);
                                picker_2.setMaxValue(picker_2_value.length - 1);
                            }
                        }

                    });


                    AlertDialog mAlertDialog = new AlertDialog.Builder(ProblemDetailActivity.this)
                            .setView(view)
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    propertyId = depInfoModel.getDepList().get(picker_1.getValue()).getActorList().get(picker_2.getValue()).getId();
                                    tv_handle_method.setText(depInfoModel.getDepList().get(picker_1.getValue()).getDepartmentName() + " -- " + depInfoModel.getDepList().get(picker_1.getValue()).getActorList().get(picker_2.getValue()).getName());
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create();
                    mAlertDialog.show();
                    tv_handle_method.setClickable(true);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void QUERYPRODETAIL_CALL(final long alarmId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.QUERYPRODETAIL_CALL(alarmId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    QueryProDetailModel queryProDetailModel = new Gson().fromJson(response.body(),QueryProDetailModel.class);
//                    Toast.makeText(ProblemDetailActivity.this,queryProDetailModel.getAlarmImgList().get(0).getPic(),Toast.LENGTH_SHORT).show();

                    tv_id.setText(queryProDetailModel.getAlarmNumber());
                    tv_content.setText(queryProDetailModel.getAlarmContent().get(0).getContent());
                    tv_location.setText(queryProDetailModel.getAlarmContent().get(0).getLocation());
                    tv_type.setText(queryProDetailModel.getAlarmContent().get(0).getCategoryName());
                    tv_time.setText(queryProDetailModel.getAlarmTimeStr());

//                    if (status == 5){
//                        tv_content.setText(queryProDetailModel.getLastAssist());
//                    }

                    if (status == 4 || status == 3){
                        linear_finish_time.setVisibility(View.VISIBLE);
                        tv_finish_time.setText(queryProDetailModel.getMlistFinishTimeStr());
                    }else {
                        linear_finish_time.setVisibility(View.GONE);
                    }

                    final List<ImgListModel> alarmImgList = queryProDetailModel.getAlarmImgList();

                    if (alarmImgList.size() == 0){
                        img_1.setVisibility(View.GONE);
                        img_2.setVisibility(View.GONE);
                        img_3.setVisibility(View.GONE);
                        img_4.setVisibility(View.GONE);
                        img_5.setVisibility(View.GONE);

                    }else if (alarmImgList.size() == 1){
                        img_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_1.setVisibility(View.VISIBLE);
                        img_1.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(0).getPic()));
                        img_2.setVisibility(View.GONE);
                        img_3.setVisibility(View.GONE);
                        img_4.setVisibility(View.GONE);
                        img_5.setVisibility(View.GONE);
                    }
                    else if (alarmImgList.size() == 2){
                        img_1.setVisibility(View.VISIBLE);
                        img_2.setVisibility(View.VISIBLE);
                        img_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_1.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(0).getPic()));
                        img_2.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(1).getPic()));
                        img_3.setVisibility(View.GONE);
                        img_4.setVisibility(View.GONE);
                        img_5.setVisibility(View.GONE);
                    }else if (alarmImgList.size() == 3){
                        img_1.setVisibility(View.VISIBLE);
                        img_2.setVisibility(View.VISIBLE);
                        img_3.setVisibility(View.VISIBLE);
                        img_1.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(0).getPic()));
                        img_2.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(1).getPic()));
                        img_3.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(2).getPic()));
                        img_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_4.setVisibility(View.GONE);
                        img_5.setVisibility(View.GONE);
                    }else if (alarmImgList.size() == 4){
                        img_1.setVisibility(View.VISIBLE);
                        img_2.setVisibility(View.VISIBLE);
                        img_3.setVisibility(View.VISIBLE);
                        img_4.setVisibility(View.VISIBLE);
                        img_1.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(0).getPic()));
                        img_2.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(1).getPic()));
                        img_3.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(2).getPic()));
                        img_4.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(3).getPic()));
                        img_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(3).getId());
                                startActivity(intent);
                            }
                        });
                        img_5.setVisibility(View.GONE);
                    }else if (alarmImgList.size() == 5){
                        img_1.setVisibility(View.VISIBLE);
                        img_2.setVisibility(View.VISIBLE);
                        img_3.setVisibility(View.VISIBLE);
                        img_4.setVisibility(View.VISIBLE);
                        img_5.setVisibility(View.VISIBLE);
                        img_1.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(0).getPic()));
                        img_2.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(1).getPic()));
                        img_3.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(2).getPic()));
                        img_4.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(3).getPic()));
                        img_5.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(4).getPic()));
                        img_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(3).getId());
                                startActivity(intent);
                            }
                        });
                        img_5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(4).getId());
                                startActivity(intent);
                            }
                        });
                    }


                    if (status != 7){
                        linear_finish_pics.setVisibility(View.GONE);
                    }else {
                        linear_finish_pics.setVisibility(View.VISIBLE);
                        final List<ImgListModel> confirmImgList = queryProDetailModel.getConfirmImgList();

                        if (confirmImgList.size() == 0){
                            img_11.setVisibility(View.GONE);
                            img_22.setVisibility(View.GONE);
                            img_33.setVisibility(View.GONE);
                            img_44.setVisibility(View.GONE);
                            img_55.setVisibility(View.GONE);

                        }else if (confirmImgList.size() == 1){
                            img_11.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(0).getId());
                                    startActivity(intent);
                                }
                            });
                            img_11.setVisibility(View.VISIBLE);
                            img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(0).getPic()));
                            img_22.setVisibility(View.GONE);
                            img_33.setVisibility(View.GONE);
                            img_44.setVisibility(View.GONE);
                            img_55.setVisibility(View.GONE);
                        }
                        else if (confirmImgList.size() == 2){
                            img_11.setVisibility(View.VISIBLE);
                            img_22.setVisibility(View.VISIBLE);
                            img_11.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(0).getId());
                                    startActivity(intent);
                                }
                            });
                            img_22.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(1).getId());
                                    startActivity(intent);
                                }
                            });
                            img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(0).getPic()));
                            img_22.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(1).getPic()));
                            img_33.setVisibility(View.GONE);
                            img_44.setVisibility(View.GONE);
                            img_55.setVisibility(View.GONE);
                        }else if (confirmImgList.size() == 3){
                            img_11.setVisibility(View.VISIBLE);
                            img_22.setVisibility(View.VISIBLE);
                            img_33.setVisibility(View.VISIBLE);
                            img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(0).getPic()));
                            img_22.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(1).getPic()));
                            img_33.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(2).getPic()));
                            img_11.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(0).getId());
                                    startActivity(intent);
                                }
                            });
                            img_22.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(1).getId());
                                    startActivity(intent);
                                }
                            });
                            img_33.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(2).getId());
                                    startActivity(intent);
                                }
                            });
                            img_44.setVisibility(View.GONE);
                            img_55.setVisibility(View.GONE);
                        }else if (confirmImgList.size() == 4){
                            img_11.setVisibility(View.VISIBLE);
                            img_22.setVisibility(View.VISIBLE);
                            img_33.setVisibility(View.VISIBLE);
                            img_44.setVisibility(View.VISIBLE);
                            img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(0).getPic()));
                            img_22.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(1).getPic()));
                            img_33.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(2).getPic()));
                            img_44.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(3).getPic()));
                            img_11.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(0).getId());
                                    startActivity(intent);
                                }
                            });
                            img_22.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(1).getId());
                                    startActivity(intent);
                                }
                            });
                            img_33.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(2).getId());
                                    startActivity(intent);
                                }
                            });
                            img_44.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(3).getId());
                                    startActivity(intent);
                                }
                            });
                            img_55.setVisibility(View.GONE);
                        }else if (confirmImgList.size() == 5){
                            img_11.setVisibility(View.VISIBLE);
                            img_22.setVisibility(View.VISIBLE);
                            img_33.setVisibility(View.VISIBLE);
                            img_44.setVisibility(View.VISIBLE);
                            img_55.setVisibility(View.VISIBLE);
                            img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(0).getPic()));
                            img_22.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(1).getPic()));
                            img_33.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(2).getPic()));
                            img_44.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(3).getPic()));
                            img_55.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(confirmImgList.get(4).getPic()));
                            img_11.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(0).getId());
                                    startActivity(intent);
                                }
                            });
                            img_22.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(1).getId());
                                    startActivity(intent);
                                }
                            });
                            img_33.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(2).getId());
                                    startActivity(intent);
                                }
                            });
                            img_44.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(3).getId());
                                    startActivity(intent);
                                }
                            });
                            img_55.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProblemDetailActivity.this,ImageActivity.class);
                                    intent.putExtra("pic",confirmImgList.get(4).getId());
                                    startActivity(intent);
                                }
                            });
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
