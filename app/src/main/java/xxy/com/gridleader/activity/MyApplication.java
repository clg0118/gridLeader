package xxy.com.gridleader.activity;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Betty Li on 2018/1/16.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
