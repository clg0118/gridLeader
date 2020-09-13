package xxy.com.gridleader.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.fankui.Album2Activity;
import xxy.com.gridleader.fankui.Bimp;
import xxy.com.gridleader.fankui.FileUtils;
import xxy.com.gridleader.fankui.Gallery2Activity;
import xxy.com.gridleader.fankui.ImageItem;
import xxy.com.gridleader.fankui.Res;
import xxy.com.gridleader.model.ImgListModel;
import xxy.com.gridleader.model.LoginModel;
import xxy.com.gridleader.model.QueryMlistDetailModel;
import xxy.com.gridleader.utils.ApiStrategy;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

public class GongdanDetailActivity extends AppCompatActivity {
    private SharedPreferences loginSP;
    private long userId;
    private long id,alarmId;
    private int status,flag = -1;
    private String bianhao,miaoshu,weizhi,leixin,shijian;
    private TextView tv_content_tittle,tv_liucheng,tv_id,tv_content,tv_location,tv_type,tv_shijian,tv_mdeal,tv_mback,tv_minvalid,tv_mdelay,tv_maccept;
    private EditText et_content,tv_delayNum;
    private Button btn_confirm;
    private ImageView img_back;
    private LinearLayout linear_chulifangshi,linera_delay;
    private ImageView img_1,img_2,img_3,img_4,img_5;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private GridView noScrollgridview = null;
    private GridAdapter adapter = null;
    private View parentView = null;
    public static GongdanDetailActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Res.init(this);
        parentView = getLayoutInflater().inflate(R.layout.activity_gongdan_detail, null);
        setContentView(parentView);
        instance = this;
        initUi();
    }

    void initUi(){
        MyConstants.LOADING(GongdanDetailActivity.this,200);

        img_1 = findViewById(R.id.img_1);
        img_2 = findViewById(R.id.img_2);
        img_3 = findViewById(R.id.img_3);
        img_4 = findViewById(R.id.img_4);
        img_5 = findViewById(R.id.img_5);

        loginSP = getSharedPreferences("loginSP",MODE_PRIVATE);
        userId = loginSP.getLong("userId",-1);

        tv_content_tittle = findViewById(R.id.tv_content_tittle);
        linear_chulifangshi = findViewById(R.id.linear_chulifangshi);
        img_back = findViewById(R.id.img_back);
        btn_confirm = findViewById(R.id.btn_confirm);
        et_content = findViewById(R.id.et_content);
        tv_id = findViewById(R.id.tv_id);
        tv_content = findViewById(R.id.tv_content);
        tv_location = findViewById(R.id.tv_location);
        tv_type = findViewById(R.id.tv_type);
        tv_shijian = findViewById(R.id.tv_shijian);
        tv_liucheng = findViewById(R.id.tv_liucheng);
        tv_mdeal = findViewById(R.id.tv_mdeal);
        tv_mback = findViewById(R.id.tv_mback);
        tv_minvalid = findViewById(R.id.tv_minvalid);
        tv_mdelay = findViewById(R.id.tv_mdelay);
        tv_delayNum = findViewById(R.id.tv_delayNum);
        tv_maccept = findViewById(R.id.tv_maccept);
        linera_delay = findViewById(R.id.linera_delay);

        Intent intent = getIntent();

        alarmId = Long.valueOf(intent.getStringExtra("alarmId"));
        status = Integer.valueOf(intent.getStringExtra("status"));
        id = Long.valueOf(intent.getStringExtra("id"));
        bianhao = intent.getStringExtra("bianhao");
        miaoshu = intent.getStringExtra("miaoshu");
        weizhi = intent.getStringExtra("weizhi");
        leixin = intent.getStringExtra("leixin");
        shijian = intent.getStringExtra("shijian");

        tv_id.setText(bianhao);
        tv_content.setText(miaoshu);
        tv_location.setText(weizhi);
        tv_type.setText(leixin);
        tv_shijian.setText(shijian);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_liucheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(GongdanDetailActivity.this,ProgressActivity.class);
                intent1.putExtra("alarmId",String.valueOf(alarmId));
                startActivity(intent1);
            }
        });

        tv_mdeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_mdeal.setBackgroundResource(R.drawable.bg_chosen);
                tv_mdeal.setTextColor(Color.WHITE);
                linera_delay.setVisibility(View.GONE);
                flag = 0;
                tv_maccept.setTextColor(Color.BLACK);
                tv_maccept.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mback.setTextColor(Color.BLACK);
                tv_mback.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mdelay.setTextColor(Color.BLACK);
                tv_mdelay.setBackgroundResource(R.drawable.bg_unchosen);
                tv_minvalid.setTextColor(Color.BLACK);
                tv_minvalid.setBackgroundResource(R.drawable.bg_unchosen);
            }
        });

        tv_mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_mback.setBackgroundResource(R.drawable.bg_chosen);
                tv_mback.setTextColor(Color.WHITE);
                linera_delay.setVisibility(View.GONE);
                flag = 1;
                tv_maccept.setTextColor(Color.BLACK);
                tv_maccept.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mdeal.setTextColor(Color.BLACK);
                tv_mdeal.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mdelay.setTextColor(Color.BLACK);
                tv_mdelay.setBackgroundResource(R.drawable.bg_unchosen);
                tv_minvalid.setTextColor(Color.BLACK);
                tv_minvalid.setBackgroundResource(R.drawable.bg_unchosen);
            }
        });

        tv_minvalid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_minvalid.setBackgroundResource(R.drawable.bg_chosen);
                tv_minvalid.setTextColor(Color.WHITE);
                linera_delay.setVisibility(View.GONE);
                flag = 2;
                tv_maccept.setTextColor(Color.BLACK);
                tv_maccept.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mdeal.setTextColor(Color.BLACK);
                tv_mdeal.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mdelay.setTextColor(Color.BLACK);
                tv_mdelay.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mback.setTextColor(Color.BLACK);
                tv_mback.setBackgroundResource(R.drawable.bg_unchosen);
            }
        });

        tv_mdelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_mdelay.setBackgroundResource(R.drawable.bg_chosen);
                tv_mdelay.setTextColor(Color.WHITE);
                linera_delay.setVisibility(View.VISIBLE);
                flag = 3;
                tv_maccept.setTextColor(Color.BLACK);
                tv_maccept.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mdeal.setTextColor(Color.BLACK);
                tv_mdeal.setBackgroundResource(R.drawable.bg_unchosen);
                tv_minvalid.setTextColor(Color.BLACK);
                tv_minvalid.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mback.setTextColor(Color.BLACK);
                tv_mback.setBackgroundResource(R.drawable.bg_unchosen);
            }
        });

        tv_maccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_maccept.setBackgroundResource(R.drawable.bg_chosen);
                tv_maccept.setTextColor(Color.WHITE);
                linera_delay.setVisibility(View.GONE);
                flag = 4;
                tv_mdelay.setTextColor(Color.BLACK);
                tv_mdelay.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mdeal.setTextColor(Color.BLACK);
                tv_mdeal.setBackgroundResource(R.drawable.bg_unchosen);
                tv_minvalid.setTextColor(Color.BLACK);
                tv_minvalid.setBackgroundResource(R.drawable.bg_unchosen);
                tv_mback.setTextColor(Color.BLACK);
                tv_mback.setBackgroundResource(R.drawable.bg_unchosen);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == -1) {
                    Toast.makeText(GongdanDetailActivity.this,"请选择一种处理方式",Toast.LENGTH_SHORT).show();
                }else if (flag == 0){
                    if(et_content.getText().toString().trim().length() > 200) {
                        Toast.makeText(GongdanDetailActivity.this,"内容最多填写200字",Toast.LENGTH_SHORT).show();
                    }
//                    String[] alarmImgForAndroid = new String[Bimp.tempSelectBitmap.size()];
                    File file1 = null;
                    File file2 = null;
                    File file3 = null;
                    File file4 = null;
                    File file5 = null;

                    HashMap<String, String> nameMap = new HashMap<>();
                    for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//                        alarmImgForAndroid[i] = Bitmap2StrByBase64(Bimp.tempSelectBitmap.get(i).getBitmap());
//                        File file = new File(Bimp.tempSelectBitmap.get(i).getImagePath());
//                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        if (i == 0) {
                            String fileName1 = System.currentTimeMillis() + ".PNG";
                            nameMap.put("uploadFile1", fileName1);
                            file1 = Bimp.tempSelectBitmap.get(0).getFile(fileName1);
//                                file1 = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
                        } else if (i == 1) {
//                                file2 = new File(Bimp.tempSelectBitmap.get(1).getImagePath());
                            String fileName2 = System.currentTimeMillis() + ".PNG";
                            nameMap.put("uploadFile2", fileName2);
                            file2 = Bimp.tempSelectBitmap.get(1).getFile(fileName2);
//                                file2 = MultipartBody.Part.createFormData("files", file.getName(), requestFile);

                        } else if (i == 2) {
                            String fileName3 = System.currentTimeMillis() + ".PNG";
                            nameMap.put("uploadFile3", fileName3);
                            file3 = Bimp.tempSelectBitmap.get(2).getFile(fileName3);

                        } else if (i == 3) {
                            String fileName4 = System.currentTimeMillis() + ".PNG";
                            nameMap.put("uploadFile4", fileName4);
                            file4 = Bimp.tempSelectBitmap.get(3).getFile(fileName4);

                        } else if (i == 4) {
                            String fileName5 = System.currentTimeMillis() + ".PNG";
                            nameMap.put("uploadFile5", fileName5);
                            file5 = Bimp.tempSelectBitmap.get(4).getFile(fileName5);
                        }
                    }
                    MAINTAIN_DEAL(userId,id,et_content.getText().toString().trim(),file1,file2,file3,file4,file5,nameMap);
                    Bimp.tempSelectBitmap.clear();
                }else if (flag == 1){
                    MAINTAIN_BACK(userId,id,et_content.getText().toString().trim());
                }else if (flag == 2){
                    MAINTAIN_INVISLID(userId,id,et_content.getText().toString().trim());
                }else if (flag == 3){
                    if(tv_delayNum.getText().equals("")){
                        Toast.makeText(GongdanDetailActivity.this,"请填写延期天数",Toast.LENGTH_SHORT).show();
                    }else if(!tv_delayNum.getText().toString().matches("^[+]{0,1}(\\d+)$|^[+]{0,1}(\\d+\\.\\d+)$")) {
                        Toast.makeText(GongdanDetailActivity.this,"延期天数请填写正整数",Toast.LENGTH_SHORT).show();
                    }else{
                        MAINTAIN_DELAY(userId,id,et_content.getText().toString().trim(),Long.parseLong(tv_delayNum.getText().toString()));
                    }
                }else if (flag == 4){
                    RECEIVE_CALL(userId,id);
                }
            }
        });

        pop = new PopupWindow(GongdanDetailActivity.this);

        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);



        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                Intent intent = getIntent();
                intent.putExtra("way","photo");
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(GongdanDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
                    ActivityCompat.requestPermissions(GongdanDetailActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            0);
                } else {
                    //有权限，直接拍照
                    Intent intent = new Intent(GongdanDetailActivity.this,
                            Album2Activity.class);

                    intent.putExtra("text", et_content.getText().toString());
                    startActivity(intent);
                }

                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();

            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        System.out.println("_______adapter_____1____" + adapter);

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GongdanDetailActivity.GridAdapter(this);
        System.out.println("_______adapter_____2____" + adapter);
        //adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                GongdanDetailActivity.this
                                        .getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(GongdanDetailActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                    //finish();
                } else {

                    System.out.println("___________suggestion_______________" + et_content.getText().toString());

                    Intent intent = new Intent(GongdanDetailActivity.this,
                            Gallery2Activity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    intent.putExtra("text", et_content.getText().toString());
                    intent.putExtra("alarmId",alarmId);
                    intent.putExtra("status",status);
                    intent.putExtra("id",id);
                    intent.putExtra("bianhao",bianhao);
                    intent.putExtra("miaoshu",miaoshu);
                    intent.putExtra("weizhi",weizhi);
                    intent.putExtra("leixin",leixin);
                    intent.putExtra("shijian",shijian);

                    startActivity(intent);

                    finish();
                }
            }
        });

        if(status == 1) {
            tv_mdeal.setVisibility(View.GONE);
            tv_mdelay.setVisibility(View.GONE);
            noScrollgridview.setVisibility(View.GONE);
        }else {
            tv_mdeal.setVisibility(View.VISIBLE);
            tv_mdelay.setVisibility(View.VISIBLE);
            noScrollgridview.setVisibility(View.VISIBLE);
            tv_mback.setVisibility(View.GONE);
            tv_maccept.setVisibility(View.GONE);
            tv_minvalid.setVisibility(View.GONE);
        }

        if (status == 2){
            tv_content_tittle.setText("处理内容：");
            linear_chulifangshi.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.GONE);
        }

        QUERYPRODETAIL_CALL(id);

    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 5) {
                return 5;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 5) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void MAINTAIN_DEAL(long userId,long id,String mark,File file1, File file2, File file3,
                               File file4, File file5,HashMap<String,String> nameMap){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .client(ApiStrategy.getApiService())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("actUserId", userId + "")
                .addFormDataPart("id", id + "")
                .addFormDataPart("dealContent", mark);
        if (file1 != null) {
//            if (TextUtils.equals(nameMap.get("uploadFile1"), file1.getName())) {
                builder.addFormDataPart("uploadFile1",
                        file1.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file1));
//            }
        }
        if (file2 != null) {
//            if (TextUtils.equals(nameMap.get("uploadFile2"), file2.getName())) {
                builder.addFormDataPart("uploadFile2",
                        file2.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file2));
//            }
        }
        if (file3 != null) {
//            if (TextUtils.equals(nameMap.get("uploadFile3"), file3.getName())) {
                builder.addFormDataPart("uploadFile3",
                        file3.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file3));
//            }
        }
        if (file4 != null) {
//            if (TextUtils.equals(nameMap.get("uploadFile4"), file4.getName())) {
                builder.addFormDataPart("uploadFile4",
                        file4.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file4));
//            }
        }
        if (file5 != null) {
//            if (TextUtils.equals(nameMap.get("uploadFile5"), file5.getName())) {
                builder.addFormDataPart("uploadFile5",
                        file5.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file5));
//            }
        }
        RequestBody requestBody = builder.build();


//        Call<String> call = request.MAINTAIN_DEAL(userId, id, mark,visitContent);
        Call<String> call = request.MAINTAIN_DEAL1(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if (loginModel.getResultValue()){
                        Toast.makeText(GongdanDetailActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        Toast.makeText(GongdanDetailActivity.this,loginModel.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void MAINTAIN_BACK(long updateBy,long id,String dealContent){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.MAINTAIN_BACK(updateBy, id, dealContent);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if (loginModel.getResultValue()){
                        Toast.makeText(GongdanDetailActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        Toast.makeText(GongdanDetailActivity.this,loginModel.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void MAINTAIN_INVISLID(long updateBy,long id,String dealContent){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.MAINTAIN_INVALID(updateBy, id, dealContent);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if (loginModel.getResultValue()){
                        Toast.makeText(GongdanDetailActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(GongdanDetailActivity.this,loginModel.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void RECEIVE_CALL(long userId,long mlistId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.RECEIVE_CALL(userId,mlistId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if (loginModel.getResultValue()){
                        Toast.makeText(GongdanDetailActivity.this,"接单成功",Toast.LENGTH_SHORT).show();

                        finish();
                    }else {
                        Toast.makeText(GongdanDetailActivity.this,loginModel.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void MAINTAIN_DELAY(long updateBy,long id,String dealContent,long num){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.MAINTAIN_DELAY(updateBy, id, dealContent,num);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if (loginModel.getResultValue()){
                        Toast.makeText(GongdanDetailActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(GongdanDetailActivity.this,loginModel.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private void QUERYPRODETAIL_CALL(final long id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.MAINTAIN_DETAIL(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    QueryMlistDetailModel queryProDetailModel = new Gson().fromJson(response.body(),QueryMlistDetailModel.class);
//                    Toast.makeText(ProblemDetailActivity.this,queryProDetailModel.getAlarmImgList().get(0).getPic(),Toast.LENGTH_SHORT).show();

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
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
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
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
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
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
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
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
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
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(3).getId());
                                startActivity(intent);
                            }
                        });
                        img_5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GongdanDetailActivity.this,ImageActivity.class);
                                intent.putExtra("pic",alarmImgList.get(4).getId());
                                startActivity(intent);
                            }
                        });
                    }



                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    protected void onRestart() {

        //关闭线程防止出现ANR
//        Intent intent = getIntent();
//        String way = intent.getStringExtra("way");
//        if("photo".equals(way)) {
        adapter.notifyDataSetChanged();
//        }
        super.onRestart();
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        if (ContextCompat.checkSelfPermission(GongdanDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    TAKE_PICTURE);
        } else {
            //有权限，直接拍照
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == TAKE_PICTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            } else {
                Toast.makeText(this, "CAMERA PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (requestCode == 888) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                Intent intent = new Intent(GongdanDetailActivity.this, MapViewActivity.class);
                startActivity(intent);
            } else {
                // 没有获取到权限，做特殊处理
                Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 5 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
            case 0:
                Intent intent = new Intent(GongdanDetailActivity.this,
                        Album2Activity.class);
                System.out.println("___________suggestion_______________" + et_content.getText().toString());

                intent.putExtra("text", et_content.getText().toString());
                startActivity(intent);
                break;

        }
    }
}
