package cn.itcast.bookreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.itcast.bookreader.db.DBManager;
import cn.itcast.bookreader.view.ReadView;

public class ReadActivity extends FragmentActivity {
    private ReadView readView;
    private TextView tv_title;
    private RelativeLayout rl_my_read_menu, rl_my_read_back;
    private DBManager dbManager = new DBManager();
    private LinearLayout mNightView;
    private boolean isNight;
    private String bookName, bookFile;
    private int pageNum;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        initView();
    }
    @Override
    //按HOME或BACK键回调用onStop方法
    protected void onStop(){
        isNight = getModeState();
        //返回或回主页就把夜间模式关闭
        if (isNight){
            removeNight();
        }
        super.onStop();
    }
    @Override
    //从onStop回到Activity会调用
    protected void onRestart(){
        super.onRestart();
        isNight = getModeState();
        //从其它页面重新回到该Activity就恢复原有的日/夜间模式
        if (isNight){
            setNight();
        }
    }
    private void initView(){
        user_id = getUserId();
        isNight = getModeState();
        //获取上次设置的模式，并设置当前模式
        if (isNight){
            setNight();
        }
        readView = (ReadView) findViewById(R.id.read_view);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_my_read_back = (RelativeLayout) findViewById(R.id.rl_my_read_back);
        rl_my_read_menu = (RelativeLayout) findViewById(R.id.rl_my_read_menu);
        bookName = getIntent().getStringExtra("bookName");//获取传进来的书本名字
        bookFile = dbManager.bookfile(ReadActivity.this,bookName);
        //获取书签pageNum
        pageNum = dbManager.getBookmark(ReadActivity.this, user_id, bookName);
        openBook(bookName, bookFile);
        rl_my_read_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_my_read_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);   //打开菜单
            }
        });
    }

    private void openBook(String bookName, String bookFile){
        if(bookName != null){//如果有传进数据
            int index = bookName.lastIndexOf(File.separator);//用来截取文本的名字的第一个参数
            String name = bookName.substring(index+1, bookName.length());//截取文本名字
//            Log.i("打开阅读的书名",""+name+bookFile);
            tv_title.setText(name); //设置标题为书本名字
            try {
                InputStream in = getResources().getAssets().open(bookFile); //文件输出流
//                Log.i("已经得到输出流",""+name+bookFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8")); //缓冲读取文件数据
                String line = "" ;//记录每一行数据
                String content = "" ;
                int x = 0;
                while((line = br.readLine()) != null && content.length() < 40000){//如果还有下一行数据执行,该处有设置最多读40000字
                    content += line + "\n" ;
                    x++;
                }
//                Log.i("内容字数",""+content.length());
                readView.setText(content, pageNum);
                in.close();//关闭文件输出流
                br.close();//关闭缓冲区
            } catch (Exception e) { //抛出异常
                Toast.makeText(ReadActivity.this, "没有此文件！", Toast.LENGTH_SHORT).show();//提示异常
                finish();//直接关闭界面
            }
        }
        readView.setOnItemSelectListener(new ReadView.OnItemSelectListener() {
            @Override
            public void onItemSelect(int index) {
                if (index==0){
                    readView.PageDn(ReadActivity.this);
                }else{
                    readView.PageUp(ReadActivity.this);
                }
            }
        });
    }

    //点击菜单按钮，弹出菜单
    public void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        final PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Log.i("点击了菜单4：","进入菜单事件"+item.getTitle());
                switch (item.getTitle().toString()){
                    case "日/夜间模式":
                        isNight = getModeState();
                        if (isNight){
                            removeNight();
                            saveModeState(false);
                        }else {
                            setNight();
                            saveModeState(true);
                        }
                        break;
                    case "保存书签":
                        int user_id = getUserId();
                        //获取SharedPreferences中保存的书签
                        int getPageNum = getPageNum();
//                        Log.i("pageNum", ""+getPageNum);
                        dbManager.saveBookmark(ReadActivity.this, user_id, bookName, getPageNum);
                        break;
                    case "分享":
                        share();
                        break;
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }
    //关闭夜间模式
    public void removeNight(){
        WindowManager localWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        localWindowManager.removeView(mNightView);
    }
    //开启夜间模式
    public void setNight(){
        // 保护眼睛模式的核心代码
        WindowManager localWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        this.mNightView = new LinearLayout(getApplicationContext());
        //不让悬浮窗获取焦点
        this.mNightView.setFocusable(false);
        this.mNightView.setFocusableInTouchMode(false);
        this.mNightView.setBackgroundColor(Color.argb(153, 0, 8, 13));
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        //支持透明度
        localLayoutParams.format = PixelFormat.RGBA_8888;
        localLayoutParams.gravity = Gravity.CENTER;
        localLayoutParams.x = 0;
        localLayoutParams.y = 0;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        localWindowManager.addView(this.mNightView, localLayoutParams);
    }

    public int getUserId(){
        //取sharedpreferences中的数据
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", 0);
        return user_id;
    }
    public void saveModeState(boolean isNight){
        //SharedPreferences 保存数据的实现代码
        SharedPreferences sharedPreferences = this.getSharedPreferences("mode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isNight", isNight);
        //将日/夜间模式状态保存到其中
        editor.commit();
    }
    public boolean getModeState(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("mode", Context.MODE_PRIVATE);
        boolean isNight = sharedPreferences.getBoolean("isNight", false);
        return isNight;
    }
    public int getPageNum(){
        SharedPreferences sharedPreferences =this.getSharedPreferences("pageNum", Context.MODE_PRIVATE);
        int pageNum = sharedPreferences.getInt("pageNum", 1); //默认值为1
        return pageNum;
    }
    public void share(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您阅读一本很好看的书《"+bookName+"》！");
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"share"));
    }
}