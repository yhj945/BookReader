package cn.itcast.bookreader.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.itcast.bookreader.LoginRegisterActivity;
import cn.itcast.bookreader.R;
import cn.itcast.bookreader.SettingActivity;
import cn.itcast.bookreader.db.DBManager;
import cn.itcast.bookreader.view.ExitPopupwindow;

/**
 * Created by Administrator on 2018/6/28.
 */

public class Fragment_personal extends Fragment implements View.OnClickListener{
    private TextView tv_userName, tv_userId, tv_my_phone_num;
    private RelativeLayout rl_my_settings_modify, rl_my_settings_user, rl_my_settings_exit;
    private int user_id;
    String u_name, u_phone;
    private boolean isLogin;
    private DBManager dbManager = new DBManager();
    private ArrayList userInfoList;
    private View view;
    private ExitPopupwindow exitPopupwindow = new ExitPopupwindow();
    private ReceiveBroadCast receiveBroadCast;

    @Override
    public void onAttach(Activity activity) {
        /** 注册广播 */
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateUserData");    //只有持有相同的action的接受者才能接收此广播
        activity.registerReceiver(receiveBroadCast, filter);
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_personal, container, false);
        //取sharedpreferences中的数据
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
//        Log.i("welcome登录信息",user_id+","+isLogin);
        initView();
        return view;
    }
    public void initView(){
        tv_userName = (TextView) view.findViewById(R.id.tv_userName);
        tv_userId = (TextView) view.findViewById(R.id.tv_userId);
        tv_my_phone_num = (TextView) view.findViewById(R.id.tv_my_phone_num);
        //获取用户信息
        userInfoList = dbManager.getUserInfoList(getContext(), user_id);
//        String u_name = userInfoList.get(0).toString();
        u_name = String.valueOf(userInfoList.get(0));
//        Log.i("u_name",""+u_name);
        int u_id = Integer.valueOf(userInfoList.get(1).toString());
//        Log.i("u_id",""+u_id);
        u_phone = String.valueOf(userInfoList.get(2));
//        Log.i("u_phone",""+u_phone);
        tv_userName.setText(""+u_name);
        tv_userId.setText("ID："+u_id);
        tv_my_phone_num.setText(""+u_phone);

        rl_my_settings_modify = (RelativeLayout) view.findViewById(R.id.rl_my_settings_modify);
        rl_my_settings_user = (RelativeLayout) view.findViewById(R.id.rl_my_settings_user);
        rl_my_settings_exit = (RelativeLayout) view.findViewById(R.id.rl_my_settings_exit);
        rl_my_settings_modify.setOnClickListener(this);
        rl_my_settings_user.setOnClickListener(this);
        rl_my_settings_exit.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_my_settings_modify:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("user_id", user_id);
                getActivity().startActivity(intent);
                break;
            case R.id.rl_my_settings_user:
                showAlertDialog();
                break;
            case R.id.rl_my_settings_exit:
                exitPopupwindow.showPopupWindow(getActivity());
                break;
        }
    }
    public void showAlertDialog(){
        //确认注销的对话框
        AlertDialog.Builder logoutDialog = new AlertDialog.Builder(getActivity());
        logoutDialog.setTitle("确定注销当前账号吗？");
//        logoutDialog.setIcon(R.drawable.my);
        logoutDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // 点击“确认”后的操作
//                Log.i("点击了AlertDialog","确认");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("user_id", 0);
                editor.putBoolean("isLogin", false);
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
                getActivity().startActivity(intent);
                System.exit(0);
            }
        });
        logoutDialog.setNegativeButton("返回", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击“返回”后的操作,这里不设置没有任何操作
//                Log.i("点击了AlertDialog","返回");
            }
        });
        logoutDialog.create().show();
    }
    class ReceiveBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //得到广播中得到的数据，并显示出来
            String user_name = intent.getExtras().getString("user_name");
            int user_id = intent.getExtras().getInt("user_id");
            String user_phone = intent.getExtras().getString("user_phone");
            tv_userName.setText(""+user_name);
            tv_userId.setText("ID："+user_id);
            tv_my_phone_num.setText(""+user_phone);
        }
    }
    /**
     *注销广播
     * */
    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(receiveBroadCast);
        super.onDestroyView();
    }

}
