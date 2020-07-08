package xxy.com.gridleader.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import xxy.com.gridleader.R;
import xxy.com.gridleader.fragment.LeaderMainFragment;
import xxy.com.gridleader.fragment.MeFragment;
import xxy.com.gridleader.fragment.StatisticFragment;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.view.TabWidget;

public class LeaderMainActivity extends AppCompatActivity implements TabWidget.OnTabSelectedListener {
    private TextView tv_head;
    private TabWidget mTabWidget;
    private MeFragment meFragment;
    private StatisticFragment statisticFragment;
    private FragmentManager mFragmentManager;
    private LeaderMainFragment mainFragment;
    private String flag;
    private int mIndex = 0;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_main);
        initUi();
    }

    private void initUi(){
        tv_head = (TextView) findViewById(R.id.tv_head);
        mFragmentManager = getSupportFragmentManager();
        mTabWidget = (TabWidget) findViewById(R.id.tab_widget);
        mTabWidget.setOnTabSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onTabSelected(int index) {
        transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case MyConstants.MAIN_FRAGMENT_INDEX:
                if (null == mainFragment) {
                    mainFragment = new LeaderMainFragment();
                    transaction.add(R.id.center_layout,mainFragment);
                    tv_head.setText("首页");
                } else {
                    transaction.show(mainFragment);
                    tv_head.setText("首页");
                }
                break;

            case MyConstants.STATISTICS_FRAGMENT_INDEX:
                if (null == statisticFragment) {
                    statisticFragment = new StatisticFragment();
                    transaction.add(R.id.center_layout, statisticFragment);
                    tv_head.setText("统计");
                } else {
                    transaction.show(statisticFragment);
                    tv_head.setText("统计");
                }
                break;
            case MyConstants.ME_FRAGMENT_INDEX:
                if (null == meFragment){
                    meFragment = new MeFragment();
                    transaction.add(R.id.center_layout,meFragment);
                    tv_head.setText("我的");
                }else {
                    transaction.show(meFragment);
                    tv_head.setText("我的");
                }
                break;

            default:
                break;
        }
        mIndex = index;
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != mainFragment) {
            transaction.hide(mainFragment);
            mainFragment = null;
        }
        if (null != statisticFragment) {
            transaction.hide(statisticFragment);
            statisticFragment = null;
        }

        if (null != meFragment){
            transaction.hide(meFragment);
            meFragment = null;
        }
    }
}
