package xxy.com.gridleader.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.GongdanDetailActivity;
import xxy.com.gridleader.model.DealModel;
import xxy.com.gridleader.model.LoginModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

/**
 * Created by Betty Li on 2018/1/10.
 */

public class GongdanListAdapter extends BaseAdapter{
    private List<DealModel> dealModels;
    private Context context;
    private long userId;


    public GongdanListAdapter(List<DealModel> dealModels, Context context,long userId){
        this.dealModels = dealModels;
        this.context = context;
        this.userId = userId;
    }

    @Override
    public int getCount() {
        return dealModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.gongdan_list_item,null);

        TextView tv_problem_num = view.findViewById(R.id.tv_problem_num);
        TextView tv_pro_time = view.findViewById(R.id.tv_pro_time);
        TextView tv_pro_type = view.findViewById(R.id.tv_pro_type);
        TextView tv_pro_content = view.findViewById(R.id.tv_pro_content);
        TextView tv_location = view.findViewById(R.id.tv_location);
        final Button btn_jiedan = view.findViewById(R.id.btn_jiedan);
        final ImageView img_mark = view.findViewById(R.id.img_mark);

        tv_problem_num.setText(dealModels.get(i).getMlistNum());
        tv_pro_time.setText(dealModels.get(i).getAlarmTimeStr());
        tv_pro_type.setText(dealModels.get(i).getSysName());
        tv_pro_content.setText(dealModels.get(i).getContent());
        tv_location.setText(dealModels.get(i).getLocation());


        btn_jiedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RECEIVE_CALL(userId,dealModels.get(i).getId());
            }
        });

        int status = dealModels.get(i).getStatus();

        if (status == 1){
            btn_jiedan.setVisibility(View.VISIBLE);
            view.setClickable(false);
        }
        else{
            btn_jiedan.setVisibility(View.GONE);
            view.setClickable(true);
        }

        if (status == 1){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GongdanDetailActivity.class);
                    long id = dealModels.get(i).getId();
                    long alarmId = dealModels.get(i).getAlarmId();

                    intent.putExtra("status","1");
                    intent.putExtra("id",String.valueOf(id));
                    intent.putExtra("alarmId",String.valueOf(alarmId));
                    intent.putExtra("bianhao",dealModels.get(i).getMlistNum());
                    intent.putExtra("miaoshu",dealModels.get(i).getContent());
                    intent.putExtra("weizhi",dealModels.get(i).getLocation());
                    intent.putExtra("leixin",dealModels.get(i).getSysName());
                    intent.putExtra("shijian",dealModels.get(i).getAlarmTimeStr());
                    context.startActivity(intent);
                }
            });
        }


        if (status == 2){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GongdanDetailActivity.class);
                    long id = dealModels.get(i).getId();
                    long alarmId = dealModels.get(i).getAlarmId();

                    intent.putExtra("status","2");
                    intent.putExtra("id",String.valueOf(id));
                    intent.putExtra("alarmId",String.valueOf(alarmId));
                    intent.putExtra("bianhao",dealModels.get(i).getMlistNum());
                    intent.putExtra("miaoshu",dealModels.get(i).getContent());
                    intent.putExtra("weizhi",dealModels.get(i).getLocation());
                    intent.putExtra("leixin",dealModels.get(i).getSysName());
                    intent.putExtra("shijian",dealModels.get(i).getAlarmTimeStr());
                    context.startActivity(intent);
                }
            });
        }

        if (status == 3){
            img_mark.setVisibility(View.VISIBLE);
            btn_jiedan.setVisibility(View.GONE);
            view.setClickable(true);
            img_mark.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GongdanDetailActivity.class);
                    long id = dealModels.get(i).getId();
                    long alarmId = dealModels.get(i).getAlarmId();

                    intent.putExtra("status","3");
                    intent.putExtra("alarmId",String.valueOf(alarmId));
                    intent.putExtra("id",String.valueOf(id));
                    intent.putExtra("bianhao",dealModels.get(i).getMlistNum());
                    intent.putExtra("miaoshu",dealModels.get(i).getContent());
                    intent.putExtra("weizhi",dealModels.get(i).getLocation());
                    intent.putExtra("leixin",dealModels.get(i).getSysName());
                    intent.putExtra("shijian",dealModels.get(i).getAlarmTimeStr());
                    context.startActivity(intent);
                }
            });

        }


        return view;
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
                        Toast.makeText(context,"接单成功",Toast.LENGTH_SHORT).show();
                        Activity activity = (Activity) context;
                        activity.finish();
                    }else {
                        Toast.makeText(context,loginModel.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

}
