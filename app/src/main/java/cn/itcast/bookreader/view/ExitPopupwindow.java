package cn.itcast.bookreader.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import cn.itcast.bookreader.LoginRegisterActivity;
import cn.itcast.bookreader.R;
import cn.itcast.bookreader.ReadActivity;
import cn.itcast.bookreader.WelcomeActivity;
import cn.itcast.bookreader.db.DBManager;

/**
 * Created by Administrator on 2018/7/8.
 */

public class ExitPopupwindow {
    private PopupWindow mPopWindow;
    private Activity myActivity=null;
    private int user_id;
    private RelativeLayout rl_exit_login, rl_exit_system;

    public void showPopupWindow(final Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);
        myActivity=activity;

        //设置contentView
        View contentView = LayoutInflater.from(activity).inflate(R.layout.exit_popupwindow, null);
        rl_exit_login = (RelativeLayout) contentView.findViewById(R.id.rl_exit_login);
        rl_exit_system = (RelativeLayout) contentView.findViewById(R.id.rl_exit_system);
        rl_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopWindow.isShowing()) {
                    mPopWindow.dismiss();// 关闭
                }
                SharedPreferences sharedPreferences = myActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("user_id", 0);
                editor.putBoolean("isLogin", false);
                editor.commit();
                Intent intent = new Intent(myActivity, LoginRegisterActivity.class);
                myActivity.startActivity(intent);
                System.exit(0);
//                Log.i("点击退出菜单","退出登录");
            }
        });
        rl_exit_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopWindow.isShowing()) {
                    mPopWindow.dismiss();// 关闭
                }
//                Log.i("点击退出菜单","关闭程序");
                myActivity.finish();
                System.exit(0); //退出程序
            }
        });

        //新建一个PopupWindow
        mPopWindow = new PopupWindow(contentView);
        //设置PopupWindow的宽度
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow的高度
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow能否获得焦点，如果不加这个，Grid不会响应ItemClick
        mPopWindow.setFocusable(true);
        // 设置PopupWindow是否能响应外部点击事件
        mPopWindow.setOutsideTouchable(true);
        //设置PopupWindow的视图内容
//        mPopWindow.setContentView(contentView);
        // 设置PopupWindow的背景,如果不设置，无法使用setTouchInterceptor
        mPopWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity,R.color.colorbg)));
        //设置滑入滑出动画
        mPopWindow.setAnimationStyle(R.style.animTranslate);
        //设置弹窗出现后让背景变暗，并在弹窗消失后让背景还原
        //popWindow消失监听方法
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f, myActivity);
            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(activity).inflate(R.layout.fragment_layout, null);
        mPopWindow.showAtLocation(rootview, Gravity.CENTER|Gravity.BOTTOM, 0, 0);
        setBackgroundAlpha(0.3f, myActivity);
        // 如果setTouchInterceptor返回true的话，touch事件将被拦截
        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
        mPopWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //这里处理，当点击gridview以外区域的时候，菜单关闭
                if (event.getY()<0){
                    if (mPopWindow.isShowing())
                        mPopWindow.dismiss();
                }
                return false;
            }
        });
    }
    //设置背景透明度
    private void setBackgroundAlpha(float bgAlpha, Activity activity){
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(layoutParams);
    }

}
