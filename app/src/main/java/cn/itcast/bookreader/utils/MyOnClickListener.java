package cn.itcast.bookreader.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import cn.itcast.bookreader.view.MenuPopupwindow;

/**
 * Created by Administrator on 2018/6/26.
 */

public class MyOnClickListener implements View.OnClickListener {
    private Activity activity;
    private Context context;
    private static MyOnClickListener instance = null;
    private static boolean imageFlag = false;
    MenuPopupwindow mPopWindow = new MenuPopupwindow();

    private MyOnClickListener(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public static MyOnClickListener getInstance(Activity activity, Context context) {
        if (instance == null)
            instance = new MyOnClickListener(activity, context) ;
        return instance;
    }
    @Override
    public void onClick(View view) {
        String bookName = ((MyButton)view).getBookName();
        mPopWindow.showPopupWindow(activity, context, bookName);
//        Log.i("点击按钮弹出菜单：",""+bookName);
        imageFlag = true;
    }
    public static boolean getImageFlag(){
        return imageFlag;
    }
    public static void setImageFlag(boolean b){
        imageFlag = b;
    }
}