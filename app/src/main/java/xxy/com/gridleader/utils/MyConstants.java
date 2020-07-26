package xxy.com.gridleader.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by Betty Li on 2018/1/8.
 */

public class MyConstants {

    public static final String BASEURL = "http://112.4.75.18:80/grid_omw/";
//    public static final String BASEURL = "http://spxc.sipac.gov.cn:80/grid_omw/";
//    public static final String BASEURL = "http://192.168.18.206:8080/grid_omw/";

    public static final int MAIN_FRAGMENT_INDEX = 0;
    public static final int STATISTICS_FRAGMENT_INDEX = 1;
    public static final int ME_FRAGMENT_INDEX = 2;

    public static final int THIRD_MAIN_FRAGMENT_INDEX = 0;
    public static final int ALL_PROBLEM_FRAGMENT_INDEX = 1;
    public static final int THIRD_maintain_FRAGMENT_INDEX = 0;
    public static final int THIRD_ME_FRAGMENT_INDEX = 1;



    public static final ArrayList<Integer> COLORS = new ArrayList<>();

    static {
        COLORS.add(Color.parseColor("#17606F"));
        COLORS.add(Color.parseColor("#23888D"));
        COLORS.add(Color.parseColor("#0FAA90"));
        COLORS.add(Color.parseColor("#55AE40"));
    }


    public static final String NETWORK_UNABLE = "当前没有可用网络！";
    public static final String CONNECT_FAILED = "无法连接服务器！";


    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static void LOADING(Context context, final long millis){
        final Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(context,"加载中...");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }
        });
        thread.start();
    }


    //SHA1加密
    public static String GETSHA1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }


    public static Bitmap CONVERTSTRINGTOICON(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
