package xxy.com.gridleader.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import xxy.com.gridleader.R;
import xxy.com.gridleader.activity.AboutUsActivity;
import xxy.com.gridleader.activity.ChangePwdActivity;
import xxy.com.gridleader.activity.LoginActivity;
import xxy.com.gridleader.utils.MyConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    private TextView tv_quit;
    private SharedPreferences loginSP;
    private SharedPreferences.Editor editor;
    private ImageView img_pic;
    private TextView tv_user_name,tv_user_id,tv_community_name;
    private RelativeLayout layout_change_pwd,layout_about_us;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
    }

    private void initUi(){
        loginSP = getContext().getSharedPreferences("loginSP", Context.MODE_PRIVATE);
        editor = loginSP.edit();
        String pic = loginSP.getString("pic","");
        String name = loginSP.getString("name","");
        String areaName = loginSP.getString("areaName","");
        String jobNumber = loginSP.getString("jobNumber","");

        img_pic = getView().findViewById(R.id.img_pic);
        if (!pic.equals(""))
            img_pic.setImageBitmap(MyConstants.CONVERTSTRINGTOICON(pic));

        tv_user_name = getView().findViewById(R.id.tv_user_name);
        tv_user_name.setText(name);

        tv_user_id = getView().findViewById(R.id.tv_user_id);
        if (!jobNumber.equals(""))
            tv_user_id.setText(jobNumber);

        tv_community_name = getView().findViewById(R.id.tv_community_name);
        tv_community_name.setText(areaName);
        if (tv_community_name.getText().toString().trim() .equals(""))
            tv_community_name.setVisibility(View.GONE);

        layout_change_pwd = getView().findViewById(R.id.layout_change_pwd);
        layout_about_us = getView().findViewById(R.id.layout_about_us);
        layout_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangePwdActivity.class);
                startActivity(intent);
            }
        });
        layout_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        tv_quit = getView().findViewById(R.id.tv_quit);
        tv_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Light_Dialog_Alert)
                        .setMessage("确定退出吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quitLogin();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setCancelable(true)
                        .show();
            }
        });
    }

    private void quitLogin(){
        editor.clear();
        editor.apply();

        JPushInterface.stopPush(getContext());

        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
