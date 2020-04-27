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
import xxy.com.gridleader.model.CheckItemResultModel;

/**
 * Created by XPS13 on 2018/11/13.
 */

public class CheckListAdapter extends BaseAdapter{

    private List<CheckItemResultModel> checkListResult;
    private Context context;

    public CheckListAdapter(List<CheckItemResultModel> checkListResult,Context context) {
        this.checkListResult = checkListResult;
        this.context = context;
    }

    @Override
    public int getCount() {
        return checkListResult.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.check_list_item,null);

        TextView checkStartTime = view.findViewById(R.id.tv_check_starttime);
        TextView checkEndTime = view.findViewById(R.id.tv_check_endtime);

        checkStartTime.setText(checkListResult.get(i).getStartTime());
        checkEndTime.setText(checkListResult.get(i).getEndTime());

        final long acturalRecordId = checkListResult.get(i).getActuralRecordId();

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ProblemDetailActivity.class);
//                intent.putExtra("acturalRecordId",acturalRecordId);
//                context.startActivity(intent);
//            }
//        });
        return view;
    }
}
