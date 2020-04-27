package xxy.com.gridleader.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.adapter.ResidentResultListAdapter;
import xxy.com.gridleader.model.ResidentResultListModel;
import xxy.com.gridleader.model.ResidentResultModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;
import xxy.com.gridleader.utils.WeiboDialogUtils;

public class ResidentResultsActivity extends AppCompatActivity {

    private ListView rlist;
    private TextView rlist_head;
    private ImageView img_rlist_back;
    private long userId;
    private SharedPreferences loginSP;
    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_results);
//        initUi();
    }

    @Override
    protected void onResume() {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ResidentResultsActivity.this,"加载中...");
        super.onResume();
        initUi();

    }

    private void initUi(){
        Intent intent = getIntent();
        int type = intent.getIntExtra("type",0);
        String number = intent.getStringExtra("number");
        String name = intent.getStringExtra("name");
        String room = intent.getStringExtra("room");


        loginSP = getSharedPreferences("loginSP",MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);

        RESIDENTRESULT_CALL(type,number,name,room,userId);

        rlist = findViewById(R.id.rlist);
        rlist_head = findViewById(R.id.rlist_head);
        img_rlist_back = findViewById(R.id.img_rlist_back);
        img_rlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void RESIDENTRESULT_CALL(int type,String number,String name,String room,long userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.QUERY_RESIDENT(type,number,name,room,userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    ResidentResultListModel residentListModel = new Gson().fromJson(response.body(), ResidentResultListModel.class);
                    List<ResidentResultModel> list = residentListModel.getResidentsList();
                    ResidentResultListAdapter adapter = new ResidentResultListAdapter(list,ResidentResultsActivity.this);
                    rlist.setAdapter(adapter);


                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
