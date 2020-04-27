package xxy.com.gridleader.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.adapter.ProgressListAdapter;
import xxy.com.gridleader.model.LogListModel;
import xxy.com.gridleader.model.QueryProgressModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

public class ProgressActivity extends AppCompatActivity {
    private String alarmId;
    private ImageView img_back;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        initUi();
    }

    private void initUi(){
        Intent intent = getIntent();
        alarmId = intent.getStringExtra("alarmId");


        lv = findViewById(R.id.lv);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        String s = "2018-01-10 19:05";
//        Toast.makeText(ProgressActivity.this,s.substring(11),Toast.LENGTH_SHORT).show();

        QUERYPROGRESS_CALL(Long.valueOf(alarmId));
    }

    private void QUERYPROGRESS_CALL(long alarmId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.QUERYPROGRESS_CALL(alarmId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    QueryProgressModel queryProgressModel = new Gson().fromJson(response.body(),QueryProgressModel.class);
//                    Toast.makeText(ProgressActivity.this,"" + queryProgressModel.getLogList().get(0).getCreateByName(),Toast.LENGTH_SHORT).show();
                    List<LogListModel> logListModels = queryProgressModel.getLogList();
                    ProgressListAdapter adapter = new ProgressListAdapter(logListModels,ProgressActivity.this);
                    lv.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}
