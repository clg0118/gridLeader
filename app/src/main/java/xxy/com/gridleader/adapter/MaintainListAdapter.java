package xxy.com.gridleader.adapter;

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
import xxy.com.gridleader.activity.ProblemDetailActivity;
import xxy.com.gridleader.model.LoginModel;
import xxy.com.gridleader.model.MaintainResultModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

/**
 * Created by XPS13 on 2018/11/14.
 */

public class MaintainListAdapter extends BaseAdapter {

    List<MaintainResultModel> mlist;
    Context context;
    private long userId;
    private onItemClick mOnItemClick;

    public MaintainListAdapter(List<MaintainResultModel> mlist, Context context, long userId, onItemClick itemClick) {
        this.mlist = mlist;
        this.context = context;
        this.userId = userId;
        this.mOnItemClick = itemClick;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.maintain_list_item, null);


        TextView tv_maintain_num = view.findViewById(R.id.tv_maintain_num);
        TextView tv_maintain_time = view.findViewById(R.id.tv_maintain_time);
        TextView tv_maintain_type = view.findViewById(R.id.tv_maintain_type);
        TextView tv_maintain_content = view.findViewById(R.id.tv_maintain_content);
        TextView tv_maintain_location = view.findViewById(R.id.tv_maintain_location);
        TextView tv_maintain_status = view.findViewById(R.id.tv_maintain_status);
        final Button btn_jiedan = view.findViewById(R.id.btn_jiedan);
        final ImageView img_mark = view.findViewById(R.id.img_mark);

        tv_maintain_num.setText(mlist.get(i).getMlistNum());
        tv_maintain_time.setText(mlist.get(i).getUpdateDtStr());
        tv_maintain_type.setText(mlist.get(i).getCategoryName());
        tv_maintain_content.setText(mlist.get(i).getContent());
        tv_maintain_location.setText(mlist.get(i).getLocation());
        tv_maintain_status.setText(mlist.get(i).getStatusStr());
        final long mlistId = mlist.get(i).getId();

        final long alarmId = mlist.get(i).getAlarmId();

        btn_jiedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RECEIVE_CALL(userId, mlist.get(i).getId(), i);
            }
        });

        final int status = mlist.get(i).getStatus();

        if (status == 1) {
            btn_jiedan.setVisibility(View.VISIBLE);
            view.setClickable(false);
        } else {
            btn_jiedan.setVisibility(View.GONE);
            view.setClickable(true);
        }

        if (status == 1) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GongdanDetailActivity.class);
                    long id = mlist.get(i).getId();
                    long alarmId = mlist.get(i).getAlarmId();

                    intent.putExtra("status", String.valueOf(status));
                    intent.putExtra("alarmId", String.valueOf(alarmId));
                    intent.putExtra("id", String.valueOf(id));
                    intent.putExtra("bianhao", mlist.get(i).getMlistNum());
                    intent.putExtra("miaoshu", mlist.get(i).getContent());
                    intent.putExtra("weizhi", mlist.get(i).getLocation());
                    intent.putExtra("leixin", mlist.get(i).getCategoryName());
                    intent.putExtra("shijian", mlist.get(i).getUpdateDtStr());
                    context.startActivity(intent);
                }
            });
        }

        if (status == 6 || status == 8 || status == 9) {
            img_mark.setVisibility(View.VISIBLE);
            btn_jiedan.setVisibility(View.GONE);
            view.setClickable(true);
            img_mark.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GongdanDetailActivity.class);
                    long id = mlist.get(i).getId();
                    long alarmId = mlist.get(i).getAlarmId();

                    intent.putExtra("status", String.valueOf(status));
                    intent.putExtra("alarmId", String.valueOf(alarmId));
                    intent.putExtra("id", String.valueOf(id));
                    intent.putExtra("bianhao", mlist.get(i).getMlistNum());
                    intent.putExtra("miaoshu", mlist.get(i).getContent());
                    intent.putExtra("weizhi", mlist.get(i).getLocation());
                    intent.putExtra("leixin", mlist.get(i).getCategoryName());
                    intent.putExtra("shijian", mlist.get(i).getUpdateDtStr());
                    context.startActivity(intent);
                }
            });

        }
        if (status == 2 || status == 3 || status == 4 || status == 5 || status == 7) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProblemDetailActivity.class);
                    long id = mlist.get(i).getId();
                    long alarmId = mlist.get(i).getAlarmId();
                    intent.putExtra("alarmId", String.valueOf(alarmId));
                    intent.putExtra("status", String.valueOf(status));
                    context.startActivity(intent);
                }
            });
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClick != null) {
                    mOnItemClick.click(mlist.get(i));
                }
            }

        });

        return view;
    }

    private void RECEIVE_CALL(long userId, long mlistId, final int index) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.RECEIVE_CALL(userId, mlistId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    LoginModel loginModel = new Gson().fromJson(response.body(), LoginModel.class);
                    if (loginModel.getResultValue()) {
                        Toast.makeText(context, "接单成功", Toast.LENGTH_SHORT).show();
                        mlist.get(index).setStatus(6);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    public interface onItemClick {
        void click(MaintainResultModel item);
    }
}
