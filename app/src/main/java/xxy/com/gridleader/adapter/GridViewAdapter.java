package xxy.com.gridleader.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.dinuscxj.progressbar.CircleProgressBar;

import java.text.DecimalFormat;
import java.util.List;

import xxy.com.gridleader.R;
import xxy.com.gridleader.model.GridViewModel;
import xxy.com.gridleader.utils.MyConstants;

/**
 * Created by Betty Li on 2018/1/8.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<GridViewModel> gridViewModels;
    private Context context;

    public GridViewAdapter(List<GridViewModel> gridViewModels,Context context){
        this.gridViewModels = gridViewModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gridViewModels.size();
    }

    @Override
    public Object getItem(int i) {
        return gridViewModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.grid_view_item,null);
        TextView tv_num = view.findViewById(R.id.tv_num);
        TextView tv_percent = view.findViewById(R.id.tv_percent);
        TextView tv_problem_name = view.findViewById(R.id.tv_problem_name);
        tv_num.setText(gridViewModels.get(i).getNum());
        tv_num.setTextColor(MyConstants.COLORS.get(2));

        DecimalFormat df   =new   DecimalFormat("#.00");
//        df.format(你要格式化的数字);
        tv_percent.setText(df.format(Float.valueOf(gridViewModels.get(i).getPercent()) *100) + "%");
        tv_problem_name.setText(gridViewModels.get(i).getName());

        final CircleProgressBar progress = view.findViewById(R.id.progress);
        progress.setProgressStartColor(MyConstants.COLORS.get(2));
        progress.setProgressEndColor(MyConstants.COLORS.get(2));
        float progressNum = Float.valueOf(gridViewModels.get(i).getPercent()) *100;
//        if (progressNum > (int) progressNum)
//            progressNum ++;
        int p = (int)Math.ceil(progressNum);
//        progress.setProgress(50);

        ValueAnimator animator = ValueAnimator.ofInt(0, p);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                int progress = (int) animation.getAnimatedValue();
                progress.setProgress((int) animation.getAnimatedValue());
            }
        });
//        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(1500);
        animator.start();

        return view;
    }
}
