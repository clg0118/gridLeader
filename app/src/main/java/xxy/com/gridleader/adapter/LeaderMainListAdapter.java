package xxy.com.gridleader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.ProblemDetailActivity;
import xxy.com.gridleader.activity.ProblemListActivity;
import xxy.com.gridleader.model.AlarmModel;

/**
 * Created by Betty Li on 2018/1/10.
 */

public class LeaderMainListAdapter extends BaseAdapter {
    private List<AlarmModel> alarmModels;
    private Context context;

    public LeaderMainListAdapter(List<AlarmModel> alarmModels,Context context){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.leader_main_list_item,null);
        TextView tv_tittle = view.findViewById(R.id.tv_tittle);
        TextView tv_community_name = view.findViewById(R.id.tv_community_name);
        TextView tv_time = view.findViewById(R.id.tv_time);

        tv_tittle.setText(String.valueOf(alarmModels.get(i).getAlarmContent().get(0).getContent()));
        tv_community_name.setText(String.valueOf(alarmModels.get(i).getAlarmContent().get(0).getLocation()));
        tv_time.setText(String.valueOf(alarmModels.get(i).getAlarmTimeStr()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProblemDetailActivity.class);
                intent.putExtra("alarmId",alarmModels.get(i).getId());
                intent.putExtra("status",String.valueOf(alarmModels.get(i).getStatus()));
                context.startActivity(intent);
            }
        });
        return view;
    }
}
