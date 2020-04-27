package xxy.com.gridleader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xxy.com.gridleader.R;
import xxy.com.gridleader.model.LogListModel;

/**
 * Created by Betty Li on 2018/1/10.
 */

public class ProgressListAdapter extends BaseAdapter{
    private List<LogListModel> logListModels;
    private Context context;

    public ProgressListAdapter(List<LogListModel> logListModels, Context context){
        this.logListModels = logListModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return logListModels.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.progress_list_item,null);

        TextView tv_time_1 = view.findViewById(R.id.tv_time_1);
        TextView tv_time_2 = view.findViewById(R.id.tv_time_2);
        TextView tv_explain = view.findViewById(R.id.tv_explain);
        TextView tv_creater = view.findViewById(R.id.tv_creater);
        ImageView image = view.findViewById(R.id.img);

        if (i == 0){
            image.setImageResource(R.drawable.img_progress_y);
            tv_time_1.setTextColor(Color.parseColor("#15AB92"));
            tv_explain.setTextColor(Color.parseColor("#15AB92"));
        }else {
            image.setImageResource(R.drawable.img_progress_n);
            tv_time_1.setTextColor(Color.BLACK);
            tv_explain.setTextColor(Color.BLACK);
        }

        tv_time_1.setText(logListModels.get(i).getCreateDtStr().substring(0,10));
        tv_time_2.setText(logListModels.get(i).getCreateDtStr().substring(11));
        tv_explain.setText(logListModels.get(i).getStatusExplain());
        tv_creater.setText(logListModels.get(i).getCreateByName());

        return view;
    }
}
