package cn.itcast.bookreader.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
import android.support.v4.app.Fragment;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import cn.itcast.bookreader.MainActivity;
import cn.itcast.bookreader.ReadActivity;
import java.util.ArrayList;
import java.util.HashMap;
import cn.itcast.bookreader.R;
import cn.itcast.bookreader.db.DBManager;

/**
 * Created by Administrator on 2018/6/29.
 */

public class MenuPopupwindow {
//    private GridView menuGrid;
    private PopupWindow mPopWindow;
    private Activity myActivity=null;
    private String bookName;
    private int user_id;
    private DBManager dbManager = new DBManager();

    public void showPopupWindow(final Activity activity, final Context context, final String bookname) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);
        myActivity=activity;
        bookName = bookname;
        final SimpleAdapter adapter;

        /** 菜单图片 **/
        int[] menu_image_array = { R.drawable.read,
                R.drawable.like, R.drawable.delete,
                R.drawable.share , R.drawable.more2,};
        /** 菜单文字 **/
        String[] menu_name_array = { "阅读", "收藏",
                "移除收藏", "分享", "更多" };
        //设置contentView
        View contentView = LayoutInflater.from(activity).inflate(R.layout.popup_window_layout, null);
        //填充数据到GridView菜单
        GridView menuGrid = (GridView) contentView.findViewById(R.id.gridview);
        adapter = getMenuAdapter(menu_name_array, menu_image_array, context);
        menuGrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //设置各个控件的点击响应
        menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Log.i("点击了菜单选项","id:"+id+",position:"+position);
                if (mPopWindow.isShowing()) {
                    mPopWindow.dismiss();// 关闭
                }
                switch (position){
                    case 0:
//                        Log.i("点击阅读：",bookName);
                        Intent intent = new Intent(myActivity, ReadActivity.class);
                        intent.putExtra("bookName", bookName);//传送要打开的txt文件路径pathData.get(position)
                        myActivity.startActivity(intent);
                        break;
                    case 1:
//                        Log.i("点击收藏：",bookName);
                        new Thread(){
                            public void run(){
                                Looper.prepare();
                                    dbManager.insertCollection(context, bookName, user_id);
                                Looper.loop();
                            }
                        }.start();
                        break;
                    case 2:
//                        Log.i("点击移除：",bookName);
                        new Thread(){
                            public void run(){
                                Looper.prepare();
                                dbManager.deleteCollection(context, bookName, user_id);
                                Looper.loop();
                            }
                        }.start();
                        break;
                    case 3:
//                        Log.i("点击分享：",bookName);
                        share();
                        break;
                    case 4:
//                        Log.i("点击更多",bookName);
                        Toast.makeText(context, "没有更多啦！", Toast.LENGTH_SHORT).show();
                }

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
//                Log.d("Demo", "popupWindow::onTouch >>> view: "
//                        + v + ", event: " + event);
//                Log.d("popupWindow::onTouch",""+event.getY());
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
    /**
     * 构造菜单Adapter
     *
     * @param menuNameArray
     *            名称
     * @param imageResourceArray
     *            图片
     * @return SimpleAdapter
     */
    private SimpleAdapter getMenuAdapter(String[] menuNameArray, int[] imageResourceArray, Context context) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < menuNameArray.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", imageResourceArray[i]);
            map.put("itemText", menuNameArray[i]);
            data.add(map);
        }
        SimpleAdapter simperAdapter = new SimpleAdapter(context, data,
                R.layout.item_menu, new String[] { "itemImage", "itemText" },
                new int[] { R.id.item_menuimage, R.id.item_menutext });
        return simperAdapter;
    }
    public void share(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您阅读一本很好看的书《"+bookName+"》！");
        intent.setType("text/plain");
        myActivity.startActivity(Intent.createChooser(intent,"share"));
    }
}
