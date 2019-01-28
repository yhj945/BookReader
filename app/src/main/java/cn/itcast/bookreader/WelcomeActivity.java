package cn.itcast.bookreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class WelcomeActivity extends AppCompatActivity {
    private final long SPLASH_LENGTH = 2000;
    Handler handler = new Handler();
    private int user_id;
    private Boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //取sharedpreferences中的数据
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
//        Log.i("welcome登录信息",user_id+","+isLogin);
        handler.postDelayed(new Runnable() {  //使用handler的postDelayed实现延时跳转
            public void run() {
                if (user_id != 0 && isLogin) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();//关闭当前登录界面，否则在主界面按后退键还会回到登录界面
                }else{
                    Intent intent = new Intent(WelcomeActivity.this, LoginRegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_LENGTH);//2秒后跳转至应用主界面MainActivity
    }
}
