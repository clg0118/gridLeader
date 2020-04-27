package xxy.com.gridleader.fankui;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.GongdanDetailActivity;

/**
 * 这个是进入相册显示所有图片的界面
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:47:15
 */
public class Album2Activity extends Activity {
    //显示手机里的所有图片的列表控件
    private GridView gridView;
    //当手机里没有图片时，提示用户没有图片的控件
    private TextView tv;
    //gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    //完成按钮
    private Button okButton;
    // 返回按钮
    private Button back;
    // 取消按钮
    private Button cancel;
    private Intent intent;
    // 预览按钮
    private Button preview;
    private Context mContext;
    private ArrayList<ImageItem> dataList;
    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    public static Bitmap bitmap;

    public ArrayList<ImageItem> SelectBitmap = Bimp.tempSelectBitmap;

    private HashMap<String, ImageItem> map = null;

    private ArrayList<HashMap<String, ImageItem>> mapList= new ArrayList<HashMap<String,ImageItem>>();

    private String text;

    private int size;

    private boolean flag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Res.getLayoutID("plugin_camera_album"));
        PublicWay.activityList.add(this);
        size = Bimp.tempSelectBitmap.size();
        for(int i = 0;i<size;i++){
            map = new HashMap<String, ImageItem>();
            map.put("pic",Bimp.tempSelectBitmap.get(i));
            mapList.add(map);
        }
        mContext = this;
        //注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        bitmap = BitmapFactory.decodeResource(getResources(),Res.getDrawableID("plugin_camera_no_pictures"));
        text = getIntent().getStringExtra("text");
        init();
        initListener();
        //这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();
    }


    //防止重复注册广播接收器
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    //    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterReceiver(broadcastReceiver);
//    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //mContext.unregisterReceiver(this);
            // TODO Auto-generated method stub
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    // 预览按钮的监听
    private class PreviewListener implements View.OnClickListener {
        public void onClick(View v) {
            if (size > 0) {
                intent.putExtra("position", "1");
                intent.setClass(Album2Activity.this, Gallery2Activity.class);
                intent.putExtra("text", text);
                startActivity(intent);
            }
        }

    }

    // 完成按钮的监听
    private class AlbumSendListener implements View.OnClickListener {
        public void onClick(View v) {
//            PictureActivity.instance.finish();
            Bimp.tempSelectBitmap.clear();
            for(int i=0;i<mapList.size();i++){
                Bimp.tempSelectBitmap.add(mapList.get(i).get("pic"));
            }

            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
            intent.setClass(mContext, GongdanDetailActivity.class);
            intent.putExtra("laiyuan", "Album2Activity");
            intent.putExtra("text", text);
            startActivity(intent);
            finish();
        }

    }

    //已经被禁用
    // 返回按钮监听
    private class BackListener implements View.OnClickListener {
        public void onClick(View v) {
			/*intent.setClass(AlbumActivity.this, ImageFile.class);
			startActivity(intent);*/
            //Bimp.tempSelectBitmap = SelectBitmap;
            finish();
        }
    }

    // 取消按钮的监听
    private class CancelListener implements View.OnClickListener {
        public void onClick(View v) {
            GongdanDetailActivity.instance.finish();
            //SelectBitmap.clear();
            //Bimp.tempSelectBitmap.clear();
            intent.setClass(mContext, GongdanDetailActivity.class);
            intent.putExtra("laiyuan", "Album2Activity");
            intent.putExtra("text", text);
            startActivity(intent);
            finish();

        }
    }



    // 初始化，给一些对象赋值
    private void init() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        contentList = helper.getImagesBucketList(false);
        dataList = new ArrayList<ImageItem>();
        for(int i = 0; i<contentList.size(); i++){
            dataList.addAll( contentList.get(i).imageList );
        }

        back = (Button) findViewById(Res.getWidgetID("back"));
        cancel = (Button) findViewById(Res.getWidgetID("cancel"));
        cancel.setOnClickListener(new CancelListener());
        back.setOnClickListener(new BackListener());
        preview = (Button) findViewById(Res.getWidgetID("preview"));
        preview.setOnClickListener(new PreviewListener());
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        gridView = (GridView) findViewById(Res.getWidgetID("myGrid"));
        gridImageAdapter = new AlbumGridViewAdapter(this,dataList,
                SelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        tv = (TextView) findViewById(Res.getWidgetID("myText"));
        gridView.setEmptyView(tv);
        okButton = (Button) findViewById(Res.getWidgetID("ok_button"));
        okButton.setText(Res.getString("finish")+"(" +size
                + "/"+PublicWay.num+")");
    }

    private void initListener() {

        gridImageAdapter
                .setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(final ToggleButton toggleButton,
                                            int position, boolean isChecked,Button chooseBt) {

                        System.out.println("_________isChecked_____________"+isChecked+size);

                        if (size == PublicWay.num) {
                            toggleButton.setChecked(false);
                            chooseBt.setVisibility(View.GONE);

                            for(int i=0;i<mapList.size();i++){
                                if(dataList.get(position).equals(mapList.get(i).get("pic"))){
                                    flag = true;
                                    size = size-1;
                                    mapList.remove(i);
                                    chooseBt.setVisibility(View.GONE);
                                    okButton.setText(Res.getString("finish")+"(" + size + "/"+PublicWay.num+")");
                                }else{
                                    flag = false;
                                }
                            }

                            if (!flag) {
                                Toast.makeText(Album2Activity.this, Res.getString("only_choose_num"),
                                        200).show();
                            }else{

                            }
                            return;

                        }

                        if (isChecked) {
                            System.out.println("______size____1______"+size);
                            size = size+1;
                            System.out.println("______size___2______"+size);
                            chooseBt.setVisibility(View.VISIBLE);
                            map = new HashMap<String, ImageItem>();
                            map.put("pic",dataList.get(position));
                            mapList.add(map);
                            //SelectBitmap.add(dataList.get(position));
                            okButton.setText(Res.getString("finish")+"(" + size
                                    + "/"+PublicWay.num+")");
                        } else {
                            System.out.println("______size____3_____"+size);
                            size = size-1;
                            System.out.println("______size____4____"+size);
                            for(int i=0;i<mapList.size();i++){
                                if(dataList.get(position).equals(mapList.get(i).get("pic"))){
                                    mapList.remove(i);
                                }
                            }
                            //SelectBitmap.remove(dataList.get(position));
                            chooseBt.setVisibility(View.GONE);
                            okButton.setText(Res.getString("finish")+"(" + size + "/"+PublicWay.num+")");
                        }
                        isShowOkBt();
                    }
                });

        okButton.setOnClickListener(new AlbumSendListener());

    }

    private boolean removeOneData(ImageItem imageItem) {
        if (SelectBitmap.contains(imageItem)) {
            SelectBitmap.remove(imageItem);
            okButton.setText(Res.getString("finish")+"(" +size + "/"+PublicWay.num+")");
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
        if (size > 0) {
            okButton.setText(Res.getString("finish")+"(" + size + "/"+PublicWay.num+")");
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText(Res.getString("finish")+"(" + size + "/"+PublicWay.num+")");
            preview.setPressed(false);
            preview.setClickable(false);
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
            preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            GongdanDetailActivity.instance.finish();
            System.out.println("___________SelectBitmap____________"+SelectBitmap+"__________"+Bimp.tempSelectBitmap);
            //SelectBitmap.clear();
            //Bimp.tempSelectBitmap = SelectBitmap;
            intent.setClass(mContext, GongdanDetailActivity.class);
            intent.putExtra("laiyuan", "Album2Activity");
            intent.putExtra("text", text);
            startActivity(intent);
            finish();

        }
        return false;

    }
    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }

}
