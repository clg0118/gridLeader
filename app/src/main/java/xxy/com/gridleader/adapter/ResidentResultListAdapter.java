package xxy.com.gridleader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.QueryResidentActivity;
import xxy.com.gridleader.model.ResidentResultModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

/**
 * Created by Betty Li on 2018/4/26.
 */

public class ResidentResultListAdapter  extends BaseAdapter {
    private List<ResidentResultModel> residentList;
    private Context context;

    public ResidentResultListAdapter(List<ResidentResultModel> residentList, Context context){
        this.residentList = residentList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return residentList.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.resident_list_item,null);
        TextView tv_r_room = view.findViewById(R.id.tv_r_room);
        TextView tv_r_name = view.findViewById(R.id.tv_r_name);
        TextView tv_r_number = view.findViewById(R.id.tv_r_number);
        TextView tv_r_garden = view.findViewById(R.id.tv_r_garden);
        ImageView img_r_detail = view.findViewById(R.id.img_r_detail);

        tv_r_room.setText(residentList.get(i).getRoomNumber());
        tv_r_name.setText(residentList.get(i).getName());
        tv_r_number.setText(residentList.get(i).getId_number());
        tv_r_garden.setText(residentList.get(i).getGardenName());

        final Long residentId = residentList.get(i).getId();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MyConstants.BASEURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                NetWorkRequest request = retrofit.create(NetWorkRequest.class);
                Call<String> call = request.DETAIL_RESIDENT(residentId);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(context, QueryResidentActivity.class);
                            intent.putExtra("response",response.body());
                            context.startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
        });

        return view;
    }
}
