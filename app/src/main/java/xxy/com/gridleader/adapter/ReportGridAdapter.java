package xxy.com.gridleader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.PictureActivity;
import xxy.com.gridleader.model.AllCategoryModel;
import xxy.com.gridleader.model.SystemModuleModel;

/**
 * Created by Betty Li on 2018/1/14.
 */

public class ReportGridAdapter extends BaseAdapter{
    public static List<TextView> textViews = new ArrayList<>();
    private List<AllCategoryModel> allCategoryModels;
    private List<SystemModuleModel> sysModuleList;
    private Context context;
    private long addSysModuleId;

    public ReportGridAdapter(List<AllCategoryModel> allCategoryModels,List<SystemModuleModel> sysModuleList,Context context){
        this.allCategoryModels = allCategoryModels;
        this.sysModuleList = sysModuleList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return allCategoryModels.size();
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

        view = LayoutInflater.from(context).inflate(R.layout.report_grid_item,null);
        final TextView tv_content = view.findViewById(R.id.tv_content);

        if (i == 0){
            tv_content.setTextColor(Color.WHITE);
            tv_content.setBackgroundResource(R.drawable.bg_chosen);
        }


        textViews.add(tv_content);

        tv_content.setText(sysModuleList.get(i).getModuleName());

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PictureActivity.addSysModuleId = sysModuleList.get(i).getId();
//                for (TextView textView : textViews){
//                    textView.setBackgroundResource(R.drawable.bg_unchosen);
//                    textView.setTextColor(Color.parseColor("#D3D3D3"));
//                }
//                tv_content.setTextColor(Color.WHITE);
//                tv_content.setBackgroundResource(R.drawable.bg_chosen);
//            }
//        });

        return view;
    }


}
