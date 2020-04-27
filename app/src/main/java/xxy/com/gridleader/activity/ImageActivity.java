package xxy.com.gridleader.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.model.PicModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);



        Intent intent = getIntent();
        Long picId = intent.getLongExtra("pic",0);

        QUERYPRODETAIL_CALL(picId);

        TextView tv_bg = findViewById(R.id.tv_bg);

        tv_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void QUERYPRODETAIL_CALL(final long picId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.QUERYPIC(picId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    PicModel picModel = new Gson().fromJson(response.body(), PicModel.class);
                    ImageView img = findViewById(R.id.img);
                    img.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(picModel.getPic()));
                }
            };
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
