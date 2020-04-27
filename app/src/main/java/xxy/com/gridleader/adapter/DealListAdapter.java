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

import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.ProblemDetailActivity;
import xxy.com.gridleader.model.AlarmModel;

/**
 * Created by Betty Li on 2018/1/10.
 */

public class DealListAdapter extends BaseAdapter{
    private List<AlarmModel> alarmModels;
    private Context context;

    public DealListAdapter(List<AlarmModel> alarmModels, Context context){
        this.alarmModels = alarmModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return alarmModels.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.deal_list_item,null);


        TextView tv_problem_num = view.findViewById(R.id.tv_problem_num);
        TextView tv_pro_time = view.findViewById(R.id.tv_pro_time);
        TextView tv_pro_type = view.findViewById(R.id.tv_pro_type);
        TextView tv_pro_content = view.findViewById(R.id.tv_pro_content);
        TextView tv_location = view.findViewById(R.id.tv_location);
        ImageView img_detail = view.findViewById(R.id.img_detail);

        tv_problem_num.setText(alarmModels.get(i).getAlarmNumber());
        tv_pro_time.setText(alarmModels.get(i).getAlarmTimeStr());
        tv_pro_type.setText(alarmModels.get(i).getAlarmContent().get(0).getCategoryName());
        tv_pro_content.setText(alarmModels.get(i).getAlarmContent().get(0).getContent());
        tv_location.setText(alarmModels.get(i).getAlarmContent().get(0).getLocation());

        final String alarmId = alarmModels.get(i).getId();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProblemDetailActivity.class);
                intent.putExtra("alarmId",alarmId);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
