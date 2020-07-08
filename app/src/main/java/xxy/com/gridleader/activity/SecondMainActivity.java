package xxy.com.gridleader.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import xxy.com.gridleader.R;
import xxy.com.gridleader.fragment.AllProblemFragment;
import xxy.com.gridleader.fragment.GridFragment;
import xxy.com.gridleader.fragment.MeFragment;
import xxy.com.gridleader.fragment.SecondMainFragment;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.view.SecondTabWidget;
import xxy.com.gridleader.view.TabWidget;

public class SecondMainActivity extends AppCompatActivity implements TabWidget.OnTabSelectedListener {
    private TextView tv_head;
    private SecondTabWidget mTabWidget;
    private MeFragment meFragment;
    private AllProblemFragment allProblemFragment;
    private FragmentManager mFragmentManager;
    private SecondMainFragment secondMainFragment;
    private GridFragment gridFragment;
    private String flag;
    private int mIndex = 0;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);
        initUi();
    }

    private void initUi(){
        tv_head = (TextView) findViewById(R.id.tv_head);
        mFragmentManager = getSupportFragmentManager();
        mTabWidget =  findViewById(R.id.tab_widget);
        mTabWidget.setOnTabSelectedListener(this);
        if (flag == null) {
            onTabSelected(mIndex);
            mTabWidget.setTabsDisplay(this, mIndex);
        } else {
            int result = Integer.valueOf(flag);
            onTabSelected(result);
            mTabWidget.setTabsDisplay(this, result);
            flag = null;
        }
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
            case MyConstants.THIRD_MAIN_FRAGMENT_INDEX:
                if (null == secondMainFragment) {
                    secondMainFragment = new SecondMainFragment();
                    transaction.add(R.id.center_layout,secondMainFragment);
                    tv_head.setText("首页");
                } else {
                    transaction.show(secondMainFragment);
                    tv_head.setText("首页");
                }
                break;

            case MyConstants.ALL_PROBLEM_FRAGMENT_INDEX:
                if (null == allProblemFragment){
                    allProblemFragment = new AllProblemFragment();
                    transaction.add(R.id.center_layout,allProblemFragment);
                    tv_head.setText("问题");
                }else {
                    transaction.show(allProblemFragment);
                    tv_head.setText("问题");
                }
                break;
            case 2:
                if (null == gridFragment){
                    gridFragment = new GridFragment();
                    transaction.add(R.id.center_layout,gridFragment);
                    tv_head.setText("我的巡查（今日）");
                }else {
                    transaction.show(gridFragment);
                    tv_head.setText("我的巡查");
                }
                break;
            case 3:
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
        if (null != secondMainFragment) {
            transaction.hide(secondMainFragment);
            secondMainFragment = null;
        }
        if (null != allProblemFragment) {
            transaction.hide(allProblemFragment);
            allProblemFragment = null;
        }
        if (null != meFragment){
            transaction.hide(meFragment);
            meFragment = null;
        }
        if (null != gridFragment){
            transaction.hide(gridFragment);
            gridFragment = null;
        }
    }
}