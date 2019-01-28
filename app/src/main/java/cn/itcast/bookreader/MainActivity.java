package cn.itcast.bookreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity; // 注意这里我们导入的V4的包，不要导成app的包了
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.itcast.bookreader.fragment.Fragment_collection;
import cn.itcast.bookreader.fragment.Fragment_home;
import cn.itcast.bookreader.fragment.Fragment_personal;
import cn.itcast.bookreader.fragment.Fragment_search;

/**
 * 主页面内容
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    // 定义4个Fragment对象
    private Fragment_home fg1;
    private Fragment_collection fg2;
    private Fragment_search fg3;
    private Fragment_personal fg4;
    // 帧布局对象，用来存放Fragment对象
    private FrameLayout frameLayout;
    // 定义每个选项中的相关控件
    private RelativeLayout firstLayout;
    private RelativeLayout secondLayout;
    private RelativeLayout thirdLayout;
    private RelativeLayout fourthLayout;
    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;
    private ImageView fourthImage;
    private TextView firstText;
    private TextView secondText;
    private TextView thirdText;
    private TextView fourthText;
    // 定义几个颜色
    private int white = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;
    private int dark = 0xff000000;
    // 定义FragmentManager对象管理器
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        //取sharedpreferences中的数据
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", 0);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
//        Log.i("main登录信息",user_id+","+isLogin);

        initView(); // 初始化界面控件
        setChioceItem(0); // 初始化页面加载时显示第一个选项卡
//        String userName = getIntent().getStringExtra("userName");
//        Log.i("登录的userName",userName);
    }
    /**
     * 初始化页面
     */
    private void initView() {
// 初始化底部导航栏的控件
        firstImage = (ImageView) findViewById(R.id.first_image);
        secondImage = (ImageView) findViewById(R.id.second_image);
        thirdImage = (ImageView) findViewById(R.id.third_image);
        fourthImage = (ImageView) findViewById(R.id.fourth_image);
        firstText = (TextView) findViewById(R.id.first_text);
        secondText = (TextView) findViewById(R.id.second_text);
        thirdText = (TextView) findViewById(R.id.third_text);
        fourthText = (TextView) findViewById(R.id.fourth_text);
        firstLayout = (RelativeLayout) findViewById(R.id.first_layout);
        secondLayout = (RelativeLayout) findViewById(R.id.second_layout);
        thirdLayout = (RelativeLayout) findViewById(R.id.third_layout);
        fourthLayout = (RelativeLayout) findViewById(R.id.fourth_layout);
        firstLayout.setOnClickListener(MainActivity.this);
        secondLayout.setOnClickListener(MainActivity.this);
        thirdLayout.setOnClickListener(MainActivity.this);
        fourthLayout.setOnClickListener(MainActivity.this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_layout:
                setChioceItem(0);
                break;
            case R.id.second_layout:
                setChioceItem(1);
                break;
            case R.id.third_layout:
                setChioceItem(2);
                break;
            case R.id.fourth_layout:
                setChioceItem(3);
                break;
            default:
                break;
        }
    }
    /**
     * 设置点击选项卡的事件处理
     *
     * @param index 选项卡的标号：0, 1, 2, 3
     */
    public void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        clearChioce(); // 清空, 重置选项, 隐藏所有Fragment
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                firstImage.setImageResource(R.drawable.home2); //需要的话自行修改
                firstText.setTextColor(dark);
                firstLayout.setBackgroundColor(gray);
                // 如果fg1为空，则创建一个并添加到界面上
                if (fg1 == null) {
                    fg1 = new Fragment_home();
                    fragmentTransaction.add(R.id.content, fg1);
                } else {
                // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(fg1);
                }
                break;
            case 1:
                secondImage.setImageResource(R.drawable.like2);
                secondText.setTextColor(dark);
                secondLayout.setBackgroundColor(gray);
//                if (fg2 == null) {
//                    fg2 = new Fragment_collection();
//                    fragmentTransaction.add(R.id.content, fg2);
//                } else {
//                    fragmentTransaction.show(fg2);
//                }
                //由于Fragment_like中的数据需要动态更新，所以重新加载
                fg2 = new Fragment_collection();
                fragmentTransaction.add(R.id.content, fg2);
                break;
            case 2:
                thirdImage.setImageResource(R.drawable.search2);
                thirdText.setTextColor(dark);
                thirdLayout.setBackgroundColor(gray);
                if (fg3 == null) {
                    fg3 = new Fragment_search();
                    fragmentTransaction.add(R.id.content, fg3);
                } else {
                    fragmentTransaction.show(fg3);
                }
                break;
            case 3:
                fourthImage.setImageResource(R.drawable.my2);
                fourthText.setTextColor(dark);
                fourthLayout.setBackgroundColor(gray);
                if (fg4 == null) {
                    fg4 = new Fragment_personal();
                    fragmentTransaction.add(R.id.content, fg4);
                } else {
                    fragmentTransaction.show(fg4);
                }
                break;
        }
        fragmentTransaction.commit(); // 提交
    }
    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChioce() {
        firstImage.setImageResource(R.drawable.home);
        firstText.setTextColor(gray);
        firstLayout.setBackgroundColor(white);
        secondImage.setImageResource(R.drawable.like);
        secondText.setTextColor(gray);
        secondLayout.setBackgroundColor(white);
        thirdImage.setImageResource(R.drawable.search);
        thirdText.setTextColor(gray);
        thirdLayout.setBackgroundColor(white);
        fourthImage.setImageResource(R.drawable.my);
        fourthText.setTextColor(gray);
        fourthLayout.setBackgroundColor(white);
    }
    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) {
            fragmentTransaction.hide(fg1);
        }
        if (fg2 != null) {
//            fragmentTransaction.hide(fg2);
            fragmentTransaction.remove(fg2);
        }
        if (fg3 != null) {
            fragmentTransaction.hide(fg3);
        }
        if (fg4 != null) {
            fragmentTransaction.hide(fg4);
        }
    }
}
