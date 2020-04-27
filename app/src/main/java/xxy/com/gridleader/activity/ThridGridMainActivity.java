package xxy.com.gridleader.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import xxy.com.gridleader.R;
import xxy.com.gridleader.fragment.MaintainFragement;
import xxy.com.gridleader.fragment.MeFragment;
import xxy.com.gridleader.fragment.ThirdMainFragment;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.view.ThirdTabWidget;

public class ThridGridMainActivity extends AppCompatActivity implements ThirdTabWidget.OnTabSelectedListener {
    private TextView tv_head;
    private ThirdTabWidget mTabWidget;
    private MeFragment meFragment;
    private MaintainFragement maintainFragement;
    private FragmentManager mFragmentManager;
    private ThirdMainFragment thirdMainFragment;
    private String flag;
    private int mIndex = 0;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid_grid_main);
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
//
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
//            case MyConstants.THIRD_MAIN_FRAGMENT_INDEX:
//                if (null == thirdMainFragment) {
//                    thirdMainFragment = new ThirdMainFragment();
//                    transaction.add(R.id.center_layout,thirdMainFragment);
//                    tv_head.setText("首页");
//                } else {
//                    transaction.show(thirdMainFragment);
//                    tv_head.setText("首页");
//                }
//                break;

            case MyConstants.THIRD_maintain_FRAGMENT_INDEX:
                if (null == maintainFragement){
                    maintainFragement = new MaintainFragement();
                    transaction.add(R.id.center_layout,maintainFragement);
                    tv_head.setText("工单");
                }else {
                    transaction.show(maintainFragement);
                    tv_head.setText("工单");
                }
                break;
            case MyConstants.THIRD_ME_FRAGMENT_INDEX:
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
        if (null != thirdMainFragment) {
            transaction.hide(thirdMainFragment);
            thirdMainFragment = null;
        }
        if (null != maintainFragement) {
            transaction.hide(maintainFragement);
            maintainFragement = null;
        }
        if (null != meFragment){
            transaction.hide(meFragment);
            meFragment = null;
        }
    }
}
