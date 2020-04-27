package xxy.com.gridleader.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.List;

import xxy.com.gridleader.R;
import xxy.com.gridleader.model.ThirdGridViewModel;
import xxy.com.gridleader.utils.MyConstants;

/**
 * Created by Betty Li on 2018/1/8.
 */

public class ThirdGridViewAdapter extends BaseAdapter {
    private List<ThirdGridViewModel> thirdGridViewModels;
    private Context context;

    public ThirdGridViewAdapter(List<ThirdGridViewModel> thirdGridViewModels, Context context){
        this.thirdGridViewModels = thirdGridViewModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return thirdGridViewModels.size();
    }

    @Override
    public Object getItem(int i) {
        return thirdGridViewModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.third_grid_item,null);
        TextView tv_tittle = view.findViewById(R.id.tv_tittle);
        TextView tv_change = view.findViewById(R.id.tv_change);
        ImageView img_change = view.findViewById(R.id.img_change);
        final CircleProgressBar progress = view.findViewById(R.id.progress);
        TextView tv_progress_num = view.findViewById(R.id.tv_progress_num);

        tv_tittle.setText(thirdGridViewModels.get(i).getTittle());

        if (Integer.valueOf(thirdGridViewModels.get(i).getChange()) > 0){
            img_change.setVisibility(View.VISIBLE);
            tv_change.setText("+" + thirdGridViewModels.get(i).getChange());
            img_change.setImageResource(R.drawable.img_increase);
        }else if (Integer.valueOf(thirdGridViewModels.get(i).getChange()) == 0){
            tv_change.setText("0");
            img_change.setVisibility(View.INVISIBLE);
        }else if (Integer.valueOf(thirdGridViewModels.get(i).getChange()) < 0){
            img_change.setVisibility(View.VISIBLE);
            tv_change.setText(thirdGridViewModels.get(i).getChange());
            img_change.setImageResource(R.drawable.img_decline);
        }
        tv_progress_num.setText(thirdGridViewModels.get(i).getProcess_num());

        progress.setProgressStartColor(MyConstants.COLORS.get(2));
        progress.setProgressEndColor(MyConstants.COLORS.get(2));
        int p;
        if (thirdGridViewModels.get(i).getTittle().equals("总计")){
            p = 100;
        }else {


            float progressNum = Float.valueOf(thirdGridViewModels.get(i).getPercent());
            p = (int)Math.ceil(progressNum *100);
        }

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
