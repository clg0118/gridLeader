package xxy.com.gridleader.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.model.LoginModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;
import xxy.com.gridleader.utils.WeiboDialogUtils;

public class LoginActivity extends AppCompatActivity {
    private EditText et_account,et_pwd;
    private Button btn_login;
    private SharedPreferences loginSP;
    private ProgressDialog pd = null;
    private SharedPreferences.Editor editor;
    private Dialog mWeiboDialog;
//    private String apkUrl = "http://192.168.18.206:8080/spleader.apk";
    private String apkUrl = "http://spxc.sipac.gov.cn:80/spleader.apk";
    private String UPDATE_SERVERAPK = "spleader.apk";
    private long length = 2;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
    }

    private void initUi(){
        loginSP = getSharedPreferences("loginSP",MODE_PRIVATE);
        editor = loginSP.edit();

        int roleId = loginSP.getInt("roleId", -1);
        Integer type = loginSP.getInt("type",-1);

        if (type == 1){
            Intent intent = new Intent(LoginActivity.this,ThridGridMainActivity.class);
            startActivity(intent);
            finish();
        }else if (type == 4){
            Intent intent = new Intent(LoginActivity.this,LeaderMainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (type == 2){
            Intent intent = new Intent(LoginActivity.this,SecondMainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (type == -1){
            et_account = findViewById(R.id.et_account);
            et_pwd = findViewById(R.id.et_pwd);
            btn_login = findViewById(R.id.btn_login);

            et_account.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (et_account.getText() == null || et_account.getText().toString().trim().equals("") || et_pwd.getText() == null || et_pwd.getText().toString().trim().equals("")) {
                        btn_login.setClickable(false);
                    } else {
                        btn_login.setClickable(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            et_pwd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (et_account.getText() == null || et_account.getText().toString().trim().equals("") || et_pwd.getText() == null || et_pwd.getText().toString().trim().equals("")) {
                        btn_login.setClickable(false);
                    } else {

                        btn_login.setClickable(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(LoginActivity.this, "加载中...");
                    String userName = et_account.getText().toString();
                    String password = MyConstants.GETSHA1(et_pwd.getText().toString());
                    LoginCall(userName,password);
                }
            });


            btn_login.setClickable(false);

            validVersion("0.9.8");
        }


    }

    private void validVersion(final String localversion) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);

        Call<String> call = request.VALID_VERSION();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if ( loginModel.getResultValue() && loginModel != null ) {
                        String version = loginModel.getVersion();
                        if (!localversion.equals(version)) {
                            doNewVersionUpdate(version, localversion);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(LoginActivity.this,MyConstants.CONNECT_FAILED,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoginCall(String userName,String password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);

//        Call<String> call = request.LOGIN_CALL(userName,password,2);  //3级传2

        Call<String> call = request.LOGIN_CALL(userName,password,"0.9.8");  //领导和2级传1
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if ( loginModel.getResultValue() && loginModel != null ){
                        Integer roleId = loginModel.getRoleId();
                        Integer type = loginModel.getUser().getType();
                        editor.putLong("userId",loginModel.getUser().getId());
                        editor.putString("name",loginModel.getUser().getName());
                        editor.putInt("type",loginModel.getUser().getType());
                        String version = loginModel.getVersion();

//                        Toast.makeText(LoginActivity.this,"" + loginModel.getUser().getId(),Toast.LENGTH_SHORT).show();

//                        JPushInterface.resumePush(getApplicationContext());
                        if (JPushInterface.isPushStopped(LoginActivity.this)){
                            JPushInterface.resumePush(LoginActivity.this);
                            JPushInterface.setAlias(LoginActivity.this,String.valueOf(loginModel.getUser().getId()),null);
                        }else {
                            JPushInterface.init(getApplicationContext());
                            JPushInterface.setAlias(getApplicationContext(),String.valueOf(loginModel.getUser().getId()),null);
                        }



                        editor.putString("pic",loginModel.getUser().getPic());
                        if (loginModel.getPointList() != null && loginModel.getPointList().size() > 0){
                            if(loginModel.getPointList().get(0).getName() != null) editor.putString("areaName",loginModel.getPointList().get(0).getName());
                            if((Long) loginModel.getPointList().get(0).getId() != null) editor.putLong("areaId",loginModel.getPointList().get(0).getId());
                            if(loginModel.getUser().getJobNumber()!= null) editor.putString("jobNumber",loginModel.getUser().getJobNumber());
                        }

                        if (roleId != null)
                            editor.putInt("roleId",loginModel.getRoleId());
                        else{
                        }

                        editor.apply();
                        if (type == 1){
                            Intent intent = new Intent(LoginActivity.this,ThridGridMainActivity.class);
                            startActivity(intent);
                            finish();
                        }else if (type ==4){
                            Intent intent = new Intent(LoginActivity.this,LeaderMainActivity.class);
                            startActivity(intent);
                            finish();
                            WeiboDialogUtils.closeDialog(mWeiboDialog);
                        }
                        else if(type == 2){
                            Intent intent = new Intent(LoginActivity.this,SecondMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(mWeiboDialog);

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(LoginActivity.this,MyConstants.CONNECT_FAILED,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
            * 更新版本
	 */
    public void doNewVersionUpdate(String version,String versionLocal) {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        System.out.println("------------------doNewVersionUpdate--------------------------");
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本：V");
        sb.append(versionLocal);
        sb.append(",发现版本：V");
        sb.append(version);
        sb.append("\n" + "为了保证您的使用体验,马上升级到最新版本吧!");
        Dialog dialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setTitle("版本更新")
                .setMessage(sb.toString()).setCancelable(false)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd = new ProgressDialog(LoginActivity.this);
                        pd.setTitle("正在下载");
                        pd.setMessage("正在更新，请稍后。。。");
                        pd.setCancelable(false);
                        // ProgressDialog.STYLE_HORIZONTAL可以设置百分比，圆圈的不行。
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        downFile(apkUrl);
                    }
                }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // startHomeActivity();
                    }
                }).create();
        // 显示更新框
        dialog.show();
    }

    /**
     * 下载apk
     */
    public void downFile(final String url) {
        pd.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        // verifyStoragePermissions(AboutUsActivity.this);
                        File file = new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] b = new byte[1024];
                        int charb = -1;
                        while ((charb = is.read(b)) != -1) {
                            fileOutputStream.write(b, 0, charb);
                            count += charb;
                            pd.setMax(Integer.parseInt(String.valueOf(length)));
                            pd.setProgress(count);
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            pd.cancel();
            update();
            finish();
        }
    };

    /**
     * 下载完成，通过handler将下载对话框取消
     */
    public void down() {
        new Thread() {
            public void run() {
                Message message = handler1.obtainMessage();
                handler1.sendMessage(message);
            }
        }.start();
    }

    /**
     * 安装应用
     */
    public void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        System.out.println("-------------------------update-------------------------------");
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK)),
                "application/vnd.android.package-archive");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            install(this);
        }else{
            startActivity(intent);
        }
    }

    public static void install(Context context) {
        File file= new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "spleader.apk");
        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
        Uri apkUri =
                FileProvider.getUriForFile(context, "xxy.com.gridleader.fileprovider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
