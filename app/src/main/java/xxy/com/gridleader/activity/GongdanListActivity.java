package xxy.com.gridleader.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.adapter.GongdanListAdapter;
import xxy.com.gridleader.model.DealListModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

public class GongdanListActivity extends AppCompatActivity {

    private ListView lv;
    private ImageView img_back;
    private SharedPreferences loginSP;
    private long userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongdan_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUi();
    }

    private void initUi(){
        loginSP = getSharedPreferences("loginSP",MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);

        lv = findViewById(R.id.lv);
        img_back = findViewById(R.id.img_back);



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        DEALLIST_CALL(userId,0,10000);
    }





    private void DEALLIST_CALL(final long userId, long start, long count){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.DEALLIST_CALL(userId, start, count);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    DealListModel dealListModel = new Gson().fromJson(response.body(),DealListModel.class);

                    GongdanListAdapter adapter = new GongdanListAdapter(dealListModel.getDealList(),GongdanListActivity.this,userId);
                    lv.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
