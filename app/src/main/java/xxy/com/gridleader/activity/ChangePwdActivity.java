package xxy.com.gridleader.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.model.UpdatePsdModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

public class ChangePwdActivity extends AppCompatActivity {
    private SharedPreferences loginSP;
    private long userId;
    private EditText et_old_pwd,et_new_pwd_1,et_new_pwd_2;
    private Button btn_confirm;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        initUi();
    }

    private void initUi(){
        loginSP = getSharedPreferences("loginSP",MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);

        et_old_pwd = findViewById(R.id.et_old_pwd);
        et_new_pwd_1 = findViewById(R.id.et_new_pwd_1);
        et_new_pwd_2 = findViewById(R.id.et_new_pwd_2);
        btn_confirm = findViewById(R.id.btn_confirm);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old_pwd = et_old_pwd.getText().toString();
                String new_pwd_1 = et_new_pwd_1.getText().toString();
                String new_pwd_2 = et_new_pwd_2.getText().toString();

                if (old_pwd.equals("") || new_pwd_1.equals("") || new_pwd_2.equals("")){
                    Toast.makeText(ChangePwdActivity.this,"输入不能为空！",Toast.LENGTH_SHORT).show();
                }else if (!new_pwd_1.equals(new_pwd_2)){
                    Toast.makeText(ChangePwdActivity.this,"两次输入新密码不一样！",Toast.LENGTH_SHORT).show();
                }
//                else if (!isRightPwd(new_pwd_1)){
//                    Toast.makeText(ChangePwdActivity.this,"新密码格式不正确！",Toast.LENGTH_SHORT).show();
//                }
                else {

                    UPDATEPSD_CALL(userId, MyConstants.GETSHA1(old_pwd), MyConstants.GETSHA1(new_pwd_1));
                }
            }
        });

    }

    private void UPDATEPSD_CALL(long userId,String oldPassword,String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.UPDATEPSD_CALL(userId, oldPassword, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    UpdatePsdModel updatePsdModel = new Gson().fromJson(response.body(),UpdatePsdModel.class);
                    if (updatePsdModel.getResultValue()){
                        if (updatePsdModel.getMessage()!=null && !updatePsdModel.getMessage().equals(""))
                            Toast.makeText(ChangePwdActivity.this,updatePsdModel.getMessage(),Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(ChangePwdActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        if (updatePsdModel.getMessage()!=null && !updatePsdModel.getMessage().equals(""))
                            Toast.makeText(ChangePwdActivity.this,updatePsdModel.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private boolean isRightPwd(String pwd) {
        String regex = "[a-zA-Z0-9_-]{6,20}";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }
}
