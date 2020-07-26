package xxy.com.gridleader.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.fankui.AlbumActivity;
import xxy.com.gridleader.fankui.Bimp;
import xxy.com.gridleader.fankui.FileUtils;
import xxy.com.gridleader.fankui.GalleryActivity;
import xxy.com.gridleader.fankui.ImageItem;
import xxy.com.gridleader.fankui.PublicWay;
import xxy.com.gridleader.fankui.Res;
import xxy.com.gridleader.model.AllCategoryListModel;
import xxy.com.gridleader.model.AllCategoryModel;
import xxy.com.gridleader.model.ImgListModel;
import xxy.com.gridleader.model.LoginModel;
import xxy.com.gridleader.model.QueryProDetailModel;
import xxy.com.gridleader.model.SystemModuleModel;
import xxy.com.gridleader.utils.ApiStrategy;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;
import xxy.com.gridleader.utils.WeiboDialogUtils;

public class PictureActivity extends AppCompatActivity {
    private LinearLayout linear_detail, linear_choose_place, linear_choose_type, linear_choose_minitype, linear_text_place;
    private double latitude;
    private double longitude;
    private SharedPreferences loginSP;
    private long userId, areaId;
    private Button btn_confirm;
    private ImageView img_back, mDisplayVoicePlay;
    private GridView noScrollgridview = null;
    private GridAdapter adapter = null;
    //    private GridView grid_view_type;
    private View parentView = null;
    private PopupWindow pop = null;
    private LinearLayout ll_popup, mDisplayVoiceLayout;
    public static Bitmap bimap = null;
    public static long addCategoryId, addSysModuleId;
    private TextView tv_choose_place;
    private TextView mDisplayVoiceTime;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Spinner grid_view_minitype, grid_view_type;

    private static final int MAX_TIME = 60;// 最长录音时间
    private static final int MIN_TIME = 2;// 最短录音时间

    private static final int RECORD_NO = 0; // 不在录音
    private static final int RECORD_ING = 1; // 正在录音
    private static final int RECORD_ED = 2; // 完成录音
    private int mRecord_State = 0; // 录音的状态
    private float mRecord_Time;// 录音的时间
    private double mRecord_Volume;// 麦克风获取的音量值
    private boolean mPlayState; // 播放状态
    private int mPlayCurrentPosition;// 当前播放的时间
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Horusch/audio";// 录音存储路径
    private String mRecordPath;// 录音的存储名称

    private ProgressBar mDisplayVoiceProgressBar;

    private MediaPlayer mMediaPlayer;

    RecordUtil mRecordUtil;
    Bell bell = null;
    Handler mHandler = new Handler();

    private EditText suggestion, tv_text_place;

    private ArrayList<View> listViews = null;
    private String imageS = null;
    private String text = "";
    private static AlertDialog dialog;
    public static PictureActivity instance = null;
    private TextView tv_head, tv_id, tv_content, tv_location, tv_type, tv_time, tv_finish_time, tv_uncomplete, tv_completed, tv_dealcontent;
    private Button bt_record;
    private EditText et_content;
    private ImageView img_1, img_2, img_3, img_4, img_5, img_11, img_21, img_31, img_41, img_51;
    public static String source, alarmId;
    private int completed = 0;
    private Dialog dialogUtils;
    private List<Long> categoryIdList = new ArrayList<>();
    private List<String> categoryStr = new ArrayList<>();
    private List<String> categoryCodeStr = new ArrayList<>();
    private List<Long> sysModuleIdList = new ArrayList<>();
    private List<String> sysModuleStr = new ArrayList<>();
    private int selectIndex = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Res.init(this);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
        instance = this;
        dialog = new AlertDialog.Builder(PictureActivity.this).create();
        setContentView(parentView);

        Init();


        Intent intent = getIntent();
        if (intent.getStringExtra("source") != null) {
            source = intent.getStringExtra("source");
        }
        if (intent.getStringExtra("alarmId") != null) {
            alarmId = intent.getStringExtra("alarmId");
        }


        if (source.equals("main")) {
            linear_detail.setVisibility(View.GONE);
            tv_head.setText("上报问题");
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogUtils = WeiboDialogUtils.createLoadingDialog(PictureActivity.this, "加载中...");
//                    if (tv_choose_place.getText().toString().trim().equals("")){
//                        Toast.makeText(PictureActivity.this, "请选择事发位置", Toast.LENGTH_SHORT).show();
//                        WeiboDialogUtils.closeDialog(dialogUtils);
//                    }else
                    if (suggestion.getText().toString().equals("")) {
                        Toast.makeText(PictureActivity.this, "请填写问题内容", Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(dialogUtils);
                    } else if (tv_text_place.getText().toString().equals("")) {
                        Toast.makeText(PictureActivity.this, "请填写事件发生位置", Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(dialogUtils);
                    } else {
//                        String[] alarmImgForAndroid = new String[Bimp.tempSelectBitmap.size()];
                        File file1 = null;
                        File file2 = null;
                        File file3 = null;
                        File file4 = null;
                        File file5 = null;
//                            MultipartBody.Part body =
                        HashMap<String, String> nameMap = new HashMap<>();
                        for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//                            alarmImgForAndroid[i] = Bitmap2StrByBase64(Bimp.tempSelectBitmap.get(i).getBitmap());
//                            File file = new File(Bimp.tempSelectBitmap.get(i).getImagePath());
//                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
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
                        DOADDALARM_CALL(userId, areaId, suggestion.getText().toString().trim(), tv_text_place.getText().toString(), categoryIdList.get(grid_view_minitype.getSelectedItemPosition()), longitude, latitude, file1, file2, file3, file4, file5,nameMap);

//                        DOADDALARM_CALL(userId, areaId, suggestion.getText().toString().trim(), "12345", categoryIdList.get(grid_view_minitype.getSelectedItemPosition()), longitude, latitude, file1, file2, file3, file4, file5);
                        Bimp.tempSelectBitmap.clear();

                    }
                }
            });
        } else if (source.equals("adapter")) {
            linear_detail.setVisibility(View.VISIBLE);
            linear_choose_place.setVisibility(View.GONE);
            linear_text_place.setVisibility(View.GONE);
            linear_choose_type.setVisibility(View.GONE);
            linear_choose_minitype.setVisibility(View.GONE);
            suggestion.setVisibility(View.GONE);
            tv_head.setText("待确认");
            QUERYPRODETAIL_CALL(Long.valueOf(alarmId));


            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogUtils = WeiboDialogUtils.createLoadingDialog(PictureActivity.this, "加载中...");
//                    String[] alarmImgForAndroid = new String[Bimp.tempSelectBitmap.size()];
                    File file1 = null;
                    File file2 = null;
                    File file3 = null;
                    File file4 = null;
                    File file5 = null;

                    HashMap<String, String> nameMap = new HashMap<>();
                    for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//                            alarmImgForAndroid[i] = Bitmap2StrByBase64(Bimp.tempSelectBitmap.get(i).getBitmap());
//                            File file = new File(Bimp.tempSelectBitmap.get(i).getImagePath());
//                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
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
                    CONFIRM_CALL(userId, Long.valueOf(alarmId), completed, et_content.getText().toString().trim(), file1, file2, file3, file4, file5,  nameMap);
                    Bimp.tempSelectBitmap.clear();
                }
            });

        }


        QUERYTYPE();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


    }

    @SuppressLint("ClickableViewAccessibility")
    public void Init() {
        tv_head = findViewById(R.id.tv_head);
        tv_id = findViewById(R.id.tv_id);
        tv_content = findViewById(R.id.tv_content);
        tv_dealcontent = findViewById(R.id.tv_dealcontent);
        tv_location = findViewById(R.id.tv_location);
        tv_type = findViewById(R.id.tv_type);
        tv_time = findViewById(R.id.tv_time);
        tv_finish_time = findViewById(R.id.tv_finish_time);
        bt_record = findViewById(R.id.voice_record_btn);
        et_content = findViewById(R.id.et_content);
        tv_text_place = findViewById(R.id.tv_text_place);
        img_1 = findViewById(R.id.img_1);
        img_2 = findViewById(R.id.img_2);
        img_3 = findViewById(R.id.img_3);
        img_4 = findViewById(R.id.img_4);
        img_5 = findViewById(R.id.img_5);
        img_11 = findViewById(R.id.img_11);
        img_21 = findViewById(R.id.img_21);
        img_31 = findViewById(R.id.img_31);
        img_41 = findViewById(R.id.img_41);
        img_51 = findViewById(R.id.img_51);
        grid_view_minitype = findViewById(R.id.grid_view_minitype);
        mDisplayVoiceLayout = (LinearLayout) findViewById(R.id.voice_display_voice_layout);
        mDisplayVoicePlay = (ImageView) findViewById(R.id.voice_display_voice_play);
        mDisplayVoiceProgressBar = (ProgressBar) findViewById(R.id.voice_display_voice_progressbar);
        mDisplayVoiceLayout.setVisibility(View.GONE);
        mDisplayVoiceTime = (TextView) findViewById(R.id.voice_display_voice_time);
        bt_record.setVisibility(View.GONE);


        bt_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    // 开始录音
                    case MotionEvent.ACTION_DOWN:
                        if (mRecord_State != RECORD_ING) {
                            // 修改录音状态
                            mRecord_State = RECORD_ING;
                            // 设置录音保存路径
                            mRecordPath = PATH + UUID.randomUUID().toString()
                                    + ".amr";
                            // 实例化录音工具类
                            mRecordUtil = new RecordUtil(mRecordPath);
                            try {
                                // 开始录音
                                mRecordUtil.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            new Thread(new Runnable() {

                                public void run() {
                                    // 初始化录音时间
                                    mRecord_Time = 0;
                                    while (mRecord_State == RECORD_ING) {
                                        // 大于最大录音时间则停止录音
                                        if (mRecord_Time >= MAX_TIME) {
                                            mRecordHandler.sendEmptyMessage(0);
                                        } else {
                                            try {
                                                // 每隔200毫秒就获取声音音量并更新界面显示
                                                Thread.sleep(200);
                                                mRecord_Time += 0.2;
                                                if (mRecord_State == RECORD_ING) {
                                                    mRecord_Volume = mRecordUtil
                                                            .getAmplitude();
                                                    mRecordHandler
                                                            .sendEmptyMessage(1);
                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }).start();
                        }
                        break;
                    // 停止录音
                    case MotionEvent.ACTION_UP:
                        if (mRecord_State == RECORD_ING) {
                            // 修改录音状态
                            mRecord_State = RECORD_ED;
                            try {
                                // 停止录音
                                mRecordUtil.stop();
                                // 初始录音音量
                                mRecord_Volume = 0;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // 如果录音时间小于最短时间
                            if (mRecord_Time <= MIN_TIME) {
                                // 显示提醒
                                Toast.makeText(PictureActivity.this, "录音时间过短",
                                        Toast.LENGTH_SHORT).show();
                                // 修改录音状态
                                mRecord_State = RECORD_NO;
                                // 修改录音时间
                                mRecord_Time = 0;
                                // 修改显示界面
                            } else {
                                // 录音成功,则显示录音成功后的界面
                                mDisplayVoiceLayout.setVisibility(View.VISIBLE);
                                mDisplayVoicePlay
                                        .setImageResource(R.drawable.globle_player_btn_play);
                                mDisplayVoiceProgressBar.setMax((int) mRecord_Time);
                                mDisplayVoiceProgressBar.setProgress(0);
                                mDisplayVoiceTime.setText((int) mRecord_Time + "″");
                            }
                        }
                        break;
                }
                return false;
            }
        });

        mDisplayVoicePlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 播放录音
                if (!mPlayState) {
                    mMediaPlayer = new MediaPlayer();
                    try {
                        // 添加录音的路径
                        mMediaPlayer.setDataSource(mRecordPath);
                        // 准备
                        mMediaPlayer.prepare();
                        // 播放
                        mMediaPlayer.start();
                        // 根据时间修改界面
                        new Thread(new Runnable() {

                            public void run() {

                                mDisplayVoiceProgressBar
                                        .setMax((int) mRecord_Time);
                                mPlayCurrentPosition = 0;
                                while (mMediaPlayer.isPlaying()) {
                                    mPlayCurrentPosition = mMediaPlayer
                                            .getCurrentPosition() / 1000;
                                    mDisplayVoiceProgressBar
                                            .setProgress(mPlayCurrentPosition);
                                }
                            }
                        }).start();
                        // 修改播放状态
                        mPlayState = true;
                        // 修改播放图标
                        mDisplayVoicePlay
                                .setImageResource(R.drawable.globle_player_btn_stop);

                        mMediaPlayer
                                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    // 播放结束后调用
                                    public void onCompletion(MediaPlayer mp) {
                                        // 停止播放
                                        mMediaPlayer.stop();
                                        // 修改播放状态
                                        mPlayState = false;
                                        // 修改播放图标
                                        mDisplayVoicePlay
                                                .setImageResource(R.drawable.globle_player_btn_play);
                                        // 初始化播放数据
                                        mPlayCurrentPosition = 0;
                                        mDisplayVoiceProgressBar
                                                .setProgress(mPlayCurrentPosition);
                                    }
                                });

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mMediaPlayer != null) {
                        // 根据播放状态修改显示内容
                        if (mMediaPlayer.isPlaying()) {
                            mPlayState = false;
                            mMediaPlayer.stop();
                            mDisplayVoicePlay
                                    .setImageResource(R.drawable.globle_player_btn_play);
                            mPlayCurrentPosition = 0;
                            mDisplayVoiceProgressBar
                                    .setProgress(mPlayCurrentPosition);
                        } else {
                            mPlayState = false;
                            mDisplayVoicePlay
                                    .setImageResource(R.drawable.globle_player_btn_play);
                            mPlayCurrentPosition = 0;
                            mDisplayVoiceProgressBar
                                    .setProgress(mPlayCurrentPosition);
                        }
                    }
                }
            }
        });


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


        linear_choose_type = findViewById(R.id.linear_choose_type);
        linear_choose_minitype = findViewById(R.id.linear_choose_minitype);
        linear_choose_place = findViewById(R.id.linear_choose_place);
        linear_text_place = findViewById(R.id.linear_text_place);
        linear_detail = findViewById(R.id.linear_detail);

        loginSP = getSharedPreferences("loginSP", MODE_PRIVATE);
        userId = loginSP.getLong("userId", -1);
        areaId = loginSP.getLong("areaId", -1);


        btn_confirm = findViewById(R.id.btn_confirm);
        grid_view_type = findViewById(R.id.grid_view_type);
        tv_choose_place = findViewById(R.id.tv_choose_place);
        tv_text_place = findViewById(R.id.tv_text_place);


        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bimp.tempSelectBitmap.clear();
                finish();

            }
        });

        suggestion = (EditText) findViewById(R.id.suggestion);
        suggestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                suggestion.setCursorVisible(true);
            }
        });
        suggestion.setText(text);

        pop = new PopupWindow(PictureActivity.this);

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
                intent.putExtra("way", "photo");
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(PictureActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
                    ActivityCompat.requestPermissions(PictureActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            0);
                } else {
                    //有权限，直接拍照
                    Intent intent = new Intent(PictureActivity.this,
                            AlbumActivity.class);
                    System.out.println("___________suggestion_______________" + suggestion.getText().toString());

                    intent.putExtra("text", suggestion.getText().toString());
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
        adapter = new GridAdapter(this);
        System.out.println("_______adapter_____2____" + adapter);
        //adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                PictureActivity.this
                                        .getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(PictureActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                    //finish();
                } else {

                    System.out.println("___________suggestion_______________" + suggestion.getText().toString());

                    Intent intent = new Intent(PictureActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    intent.putExtra("text", suggestion.getText().toString());
                    startActivity(intent);

                    finish();
                }
            }
        });

        tv_choose_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(PictureActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(PictureActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(PictureActivity.this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                    ActivityCompat.requestPermissions(PictureActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 888);
                } else {
                    Intent intent = new Intent(PictureActivity.this, MapViewActivity.class);
                    startActivityForResult(intent, TAKE_LOCATION);
                }

            }
        });

        //二级下拉框监听
        grid_view_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MyConstants.BASEURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                NetWorkRequest request = retrofit.create(NetWorkRequest.class);
                addSysModuleId = sysModuleIdList.get(position);
                Call<String> call = request.QUERYTYPEBYSYSMODULID(addSysModuleId);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            AllCategoryListModel allCategoryListModel = new Gson().fromJson(response.body(), AllCategoryListModel.class);
                            // 设置二级下拉列表的选项内容适配器
                            categoryIdList.clear();
                            categoryStr.clear();
                            for (AllCategoryModel itemModel : allCategoryListModel.getAllCategoryList()) {
                                categoryStr.add(itemModel.getCode() + "(" + itemModel.getName() + ")");
                                categoryIdList.add(itemModel.getId());
                            }
                            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(PictureActivity.this, android.R.layout.simple_spinner_item, categoryStr);
                            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            grid_view_minitype.setAdapter(categoryAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(PictureActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
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
    private static final int TAKE_LOCATION = 0x000002;

    public void photo() {
        if (ContextCompat.checkSelfPermission(PictureActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    TAKE_PICTURE);
        } else {
            //有权限，直接拍照
//            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//            Date d1 = new Date();
//            String t1 = format.format(d1);
//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                    .toString()
//                    + File.separator
//                    + "Download"
//                    + File.separator
//                    + String.valueOf(Math.round(Math.random() * 10000))
//                    + "-" + t1
//                    + "lam.jpg"); // 定义File类对象
//            if (!file.getParentFile().exists()) { // 父文件夹不存在
//                file.getParentFile().mkdirs(); // 创建文件夹
//            }
//            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            openCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//            Uri uri = Uri.fromFile(file);
//            // 获取拍照后未压缩的原图片，并保存在uri路径中
//            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            startActivityForResult(openCameraIntent, 2);
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
                Intent intent = new Intent(PictureActivity.this, MapViewActivity.class);
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
            case TAKE_LOCATION:
                if (data != null && resultCode == RESULT_OK) {
                    if (data.getStringExtra("location") != null) {
                        String location = data.getStringExtra("location");
                        tv_choose_place.setText(location);
                    }
                    if (data.getStringExtra("latitude") != null && data.getStringExtra("longitude") != null) {
                        latitude = Double.valueOf(data.getStringExtra("latitude"));
                        longitude = Double.valueOf(data.getStringExtra("longitude"));
                    }
                }
                break;


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
                Intent intent = new Intent(PictureActivity.this,
                        AlbumActivity.class);
                System.out.println("___________suggestion_______________" + suggestion.getText().toString());

                intent.putExtra("text", suggestion.getText().toString());
                startActivity(intent);
                break;

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (int i = 0; i < PublicWay.activityList.size(); i++) {
                if (null != PublicWay.activityList.get(i)) {
                    PublicWay.activityList.get(i).finish();
                }
            }
            Bimp.tempSelectBitmap.clear();


            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//            int in = Bimp.tempSelectBitmap.size();
//            System.out.println("______bitmap______________" + in + "_______i______" + i);
//            if (imageS == null) {
//                imageS = Bitmap2StrByBase64(Bimp.tempSelectBitmap.get(i).getBitmap());
//                System.out.println("________s__=null_______" + imageS + "__________" + i);
//            } else {
//                imageS = imageS + "," + Bitmap2StrByBase64(Bimp.tempSelectBitmap.get(i).getBitmap());
//                System.out.println("________s_________" + imageS + "__________" + i);
//            }
//
//        }
    }

    private void DOADDALARM_CALL(long userId, long monitorPointId, String addContent,
                                 String addLocation, long addCategoryId, double longitude,
                                 double latitude, File file1, File file2, File file3,
                                 File file4, File file5,HashMap<String,String> nameMap) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .client(ApiStrategy.getApiService())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);

//        Call<String> call = request.DOADDALARM_CALL(userId, monitorPointId, addContent, addLocation, addCategoryId, longitude, latitude, file1,file2,file3,file4,file5);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", userId + "")
                .addFormDataPart("monitorPointId", monitorPointId + "")
                .addFormDataPart("addContent", addContent)
                .addFormDataPart("addLocation", addLocation)
                .addFormDataPart("addCategoryId", addCategoryId + "")
                .addFormDataPart("longitude", longitude + "")
                .addFormDataPart("latitude", latitude + "");
        if (file1 != null && !TextUtils.isEmpty(nameMap.get("uploadFile1"))) {
            if (TextUtils.equals(nameMap.get("uploadFile1"), file1.getName())) {
                builder.addFormDataPart("uploadFile1",
                        file1.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file1));
            }
        }
        if (file2 != null && !TextUtils.isEmpty(nameMap.get("uploadFile2"))) {
            if (TextUtils.equals(nameMap.get("uploadFile2"), file2.getName())) {
                builder.addFormDataPart("uploadFile2",
                        file2.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file2));
            }
        }
        if (file3 != null && !TextUtils.isEmpty(nameMap.get("uploadFile3"))) {
            if (TextUtils.equals(nameMap.get("uploadFile3"), file3.getName())) {
                builder.addFormDataPart("uploadFile3",
                        file3.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file3));
            }
        }
        if (file4 != null && !TextUtils.isEmpty(nameMap.get("uploadFile4"))) {
            if (TextUtils.equals(nameMap.get("uploadFile4"), file4.getName())) {
                builder.addFormDataPart("uploadFile4",
                        file4.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file4));
            }
        }
        if (file5 != null && !TextUtils.isEmpty(nameMap.get("uploadFile5"))) {
            if (TextUtils.equals(nameMap.get("uploadFile5"), file5.getName())) {
                builder.addFormDataPart("uploadFile5",
                        file5.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file5));
            }
        }
        RequestBody requestBody = builder.build();
        Call<String> call = request.DOADDALARM_CALL1(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    LoginModel loginModel = new Gson().fromJson(response.body(), LoginModel.class);
                    if (loginModel.getResultValue()) {
                        Toast.makeText(PictureActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        Bimp.tempSelectBitmap.clear();
                        WeiboDialogUtils.closeDialog(dialogUtils);


                        finish();
                    } else {
                        Toast.makeText(PictureActivity.this, loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(dialogUtils);
                    }

                } else {
                    WeiboDialogUtils.closeDialog(dialogUtils);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PictureActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void QUERYTYPE() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);

        Call<String> call = request.QUERYTYPE();  //领导和2级传1
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    AllCategoryListModel allCategoryListModel = new Gson().fromJson(response.body(), AllCategoryListModel.class);
                    addCategoryId = allCategoryListModel.getAllCategoryList().get(0).getId();
                    addSysModuleId = allCategoryListModel.getSysModuleList().get(0).getId();
                    for (SystemModuleModel moduleModel : allCategoryListModel.getSysModuleList()) {
                        sysModuleIdList.add(moduleModel.getId());
                        sysModuleStr.add(moduleModel.getModuleName());
                    }
                    ArrayAdapter<String> moduleAdapter = new ArrayAdapter<String>(PictureActivity.this, android.R.layout.simple_spinner_item, sysModuleStr);
                    moduleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    grid_view_type.setAdapter(moduleAdapter);
//                    ReportGridAdapter adapter2 = new ReportGridAdapter(allCategoryListModel.getAllCategoryList(),allCategoryListModel.getSysModuleList(), PictureActivity.this);
                    for (AllCategoryModel itemModel : allCategoryListModel.getAllCategoryList()) {
                        categoryStr.add(itemModel.getCode() + "(" + itemModel.getName() + ")");
                        categoryIdList.add(itemModel.getId());
                        categoryCodeStr.add(itemModel.getCode());
                    }
                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(PictureActivity.this, android.R.layout.simple_spinner_item, categoryStr);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    grid_view_minitype.setAdapter(categoryAdapter);
                    View view = LayoutInflater.from(PictureActivity.this).inflate(R.layout.report_grid_item, null);
                    int width = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    int height = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    view.measure(width, height);
//        view.getMeasuredWidth(); // 获取宽度
                    int item_height = view.getMeasuredHeight(); // 获取高度

//                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) grid_view_type.getLayoutParams();
//                    int size = allCategoryListModel.getAllCategoryList().size();
//                    if (size % 3 == 0) {
//                        params.height = item_height * (size / 3);
//                        grid_view_type.setLayoutParams(params);
//                    } else {
//                        params.height = item_height * (size / 3 + 1);
//                        grid_view_type.setLayoutParams(params);
//                    }
//
//                    grid_view_type.setAdapter(adapter2);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
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

    private void QUERYPRODETAIL_CALL(final long alarmId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.QUERYPRODETAIL_CALL(alarmId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    QueryProDetailModel queryProDetailModel = new Gson().fromJson(response.body(), QueryProDetailModel.class);
//                    Toast.makeText(ProblemDetailActivity.this,queryProDetailModel.getAlarmImgList().get(0).getPic(),Toast.LENGTH_SHORT).show();

                    tv_id.setText(queryProDetailModel.getAlarmNumber());
                    tv_content.setText(queryProDetailModel.getAlarmContent().get(0).getContent());
                    tv_dealcontent.setText(queryProDetailModel.getDeptDealContent());
                    tv_location.setText(queryProDetailModel.getAlarmContent().get(0).getLocation());
                    tv_type.setText(queryProDetailModel.getAlarmContent().get(0).getCategoryName());
                    tv_time.setText(queryProDetailModel.getAlarmTimeStr());

                    int status = 3;

                    tv_finish_time.setText(queryProDetailModel.getMlistFinishTimeStr());


                    final List<ImgListModel> alarmImgList = queryProDetailModel.getAlarmImgList();

                    if (alarmImgList.size() == 0) {
                        img_1.setVisibility(View.GONE);
                        img_2.setVisibility(View.GONE);
                        img_3.setVisibility(View.GONE);
                        img_4.setVisibility(View.GONE);
                        img_5.setVisibility(View.GONE);

                    } else if (alarmImgList.size() == 1) {
                        img_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_1.setVisibility(View.VISIBLE);
                        img_1.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(0).getPic()));
                        img_2.setVisibility(View.GONE);
                        img_3.setVisibility(View.GONE);
                        img_4.setVisibility(View.GONE);
                        img_5.setVisibility(View.GONE);
                    } else if (alarmImgList.size() == 2) {
                        img_1.setVisibility(View.VISIBLE);
                        img_2.setVisibility(View.VISIBLE);
                        img_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_1.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(0).getPic()));
                        img_2.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(1).getPic()));
                        img_3.setVisibility(View.GONE);
                        img_4.setVisibility(View.GONE);
                        img_5.setVisibility(View.GONE);
                    } else if (alarmImgList.size() == 3) {
                        img_1.setVisibility(View.VISIBLE);
                        img_2.setVisibility(View.VISIBLE);
                        img_3.setVisibility(View.VISIBLE);
                        img_1.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(0).getPic()));
                        img_2.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(1).getPic()));
                        img_3.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(alarmImgList.get(2).getPic()));
                        img_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_4.setVisibility(View.GONE);
                        img_5.setVisibility(View.GONE);
                    } else if (alarmImgList.size() == 4) {
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
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(3).getId());
                                startActivity(intent);
                            }
                        });
                        img_5.setVisibility(View.GONE);
                    } else if (alarmImgList.size() == 5) {
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
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(3).getId());
                                startActivity(intent);
                            }
                        });
                        img_5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(4).getId());
                                startActivity(intent);
                            }
                        });
                    }

                    final List<ImgListModel> deptImgList = queryProDetailModel.getDeptImgList();

                    if (deptImgList.size() == 0) {
                        img_11.setVisibility(View.GONE);
                        img_21.setVisibility(View.GONE);
                        img_31.setVisibility(View.GONE);
                        img_41.setVisibility(View.GONE);
                        img_51.setVisibility(View.GONE);

                    } else if (deptImgList.size() == 1) {
                        img_11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_11.setVisibility(View.VISIBLE);
                        img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(0).getPic()));
                        img_21.setVisibility(View.GONE);
                        img_31.setVisibility(View.GONE);
                        img_41.setVisibility(View.GONE);
                        img_51.setVisibility(View.GONE);
                    } else if (deptImgList.size() == 2) {
                        img_11.setVisibility(View.VISIBLE);
                        img_21.setVisibility(View.VISIBLE);
                        img_11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_21.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(0).getPic()));
                        img_21.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(1).getPic()));
                        img_31.setVisibility(View.GONE);
                        img_41.setVisibility(View.GONE);
                        img_51.setVisibility(View.GONE);
                    } else if (deptImgList.size() == 3) {
                        img_11.setVisibility(View.VISIBLE);
                        img_21.setVisibility(View.VISIBLE);
                        img_31.setVisibility(View.VISIBLE);
                        img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(0).getPic()));
                        img_21.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(1).getPic()));
                        img_31.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(2).getPic()));
                        img_11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_21.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_31.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", alarmImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_41.setVisibility(View.GONE);
                        img_51.setVisibility(View.GONE);
                    } else if (deptImgList.size() == 4) {
                        img_11.setVisibility(View.VISIBLE);
                        img_21.setVisibility(View.VISIBLE);
                        img_31.setVisibility(View.VISIBLE);
                        img_41.setVisibility(View.VISIBLE);
                        img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(0).getPic()));
                        img_21.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(1).getPic()));
                        img_31.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(2).getPic()));
                        img_41.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(3).getPic()));
                        img_11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_21.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_31.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_41.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(3).getId());
                                startActivity(intent);
                            }
                        });
                        img_51.setVisibility(View.GONE);
                    } else if (deptImgList.size() == 5) {
                        img_11.setVisibility(View.VISIBLE);
                        img_21.setVisibility(View.VISIBLE);
                        img_31.setVisibility(View.VISIBLE);
                        img_41.setVisibility(View.VISIBLE);
                        img_51.setVisibility(View.VISIBLE);
                        img_11.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(0).getPic()));
                        img_21.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(1).getPic()));
                        img_31.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(2).getPic()));
                        img_41.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(3).getPic()));
                        img_51.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(deptImgList.get(4).getPic()));
                        img_11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(0).getId());
                                startActivity(intent);
                            }
                        });
                        img_21.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(1).getId());
                                startActivity(intent);
                            }
                        });
                        img_31.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(2).getId());
                                startActivity(intent);
                            }
                        });
                        img_41.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(3).getId());
                                startActivity(intent);
                            }
                        });
                        img_51.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PictureActivity.this, ImageActivity.class);
                                intent.putExtra("pic", deptImgList.get(4).getId());
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

    private void CONFIRM_CALL(long userId, long alarmId, int confirmResult, String confirmContent, File file1, File file2, File file3,
                              File file4, File file5, HashMap<String, String> nameMap) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .client(ApiStrategy.getApiService())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
//        Call<String> call = request.CONFIRM_CALL(userId, alarmId, confirmResult, confirmContent, alarmImgForAndroid);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", userId + "")
                .addFormDataPart("alarmId", alarmId + "")
                .addFormDataPart("confirmResult", confirmResult + "")
                .addFormDataPart("confirmContent", confirmContent);
        if (file1 != null && !TextUtils.isEmpty(nameMap.get("uploadFile1"))) {
            if (TextUtils.equals(nameMap.get("uploadFile1"), file1.getName())) {
                builder.addFormDataPart("uploadFile1",
                        file1.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file1));
            }
        }
        if (file2 != null && !TextUtils.isEmpty(nameMap.get("uploadFile2"))) {
            if (TextUtils.equals(nameMap.get("uploadFile2"), file2.getName())) {
                builder.addFormDataPart("uploadFile2",
                        file2.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file2));
            }
        }
        if (file3 != null && !TextUtils.isEmpty(nameMap.get("uploadFile3"))) {
            if (TextUtils.equals(nameMap.get("uploadFile3"), file3.getName())) {
                builder.addFormDataPart("uploadFile3",
                        file3.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file3));
            }
        }
        if (file4 != null && !TextUtils.isEmpty(nameMap.get("uploadFile4"))) {
            if (TextUtils.equals(nameMap.get("uploadFile4"), file4.getName())) {
                builder.addFormDataPart("uploadFile4",
                        file4.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file4));
            }
        }
        if (file5 != null && !TextUtils.isEmpty(nameMap.get("uploadFile5"))) {
            if (TextUtils.equals(nameMap.get("uploadFile5"), file5.getName())) {
                builder.addFormDataPart("uploadFile5",
                        file5.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file5));
            }
        }
        RequestBody requestBody = builder.build();

        Call<String> call = request.CONFIRM_CALL1(requestBody);
//        Call<String> call = request.CONFIRM_CALL(userId, alarmId, confirmResult, confirmContent, alarmImgForAndroid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    LoginModel loginModel = new Gson().fromJson(response.body(), LoginModel.class);
                    if (loginModel.getResultValue()) {
                        Toast.makeText(PictureActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        Bimp.tempSelectBitmap.clear();
                        WeiboDialogUtils.closeDialog(dialogUtils);

                        finish();
                    } else {
                        Toast.makeText(PictureActivity.this, loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(dialogUtils);
                    }

                } else {
                    WeiboDialogUtils.closeDialog(dialogUtils);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * 用来控制录音
     */
    Handler mRecordHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mRecord_State == RECORD_ING) {
                        // 修改录音状态
                        mRecord_State = RECORD_ED;
                        try {
                            // 停止录音
                            mRecordUtil.stop();
                            // 初始化录音音量
                            mRecord_Volume = 0;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mDisplayVoiceLayout.setVisibility(View.VISIBLE);
                        mDisplayVoicePlay
                                .setImageResource(R.drawable.globle_player_btn_play);
                        mDisplayVoiceProgressBar.setMax((int) mRecord_Time);
                        mDisplayVoiceProgressBar.setProgress(0);
                        mDisplayVoiceTime.setText((int) mRecord_Time + "″");
                    }
                    break;

                case 1:
                    break;
            }
        }

    };

}

