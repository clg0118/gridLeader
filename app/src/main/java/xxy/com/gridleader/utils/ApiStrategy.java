package xxy.com.gridleader.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 */
public class ApiStrategy {
    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 20000;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 20000;

    public static OkHttpClient okHttpClient;

    public static OkHttpClient getApiService() {
        if (okHttpClient == null) {
            synchronized (ApiStrategy.class) {
                if (okHttpClient == null) {
                    new ApiStrategy();
                }
            }
        }
        return okHttpClient;
    }

    private ApiStrategy() {
        //创建一个OkHttpClient并设置超时时间
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                //失败重连
                .retryOnConnectionFailure(true)
                .build();
    }

}
