package cn.itcast.bookreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.itcast.bookreader.db.DBManager;
import cn.itcast.bookreader.utils.UserData;

public class LoginRegisterActivity extends Activity implements View.OnClickListener{
    private View welcome_view, bottomSheet_login, bottomSheet_register;
    private BottomSheetBehavior behavior_login, behavior_register;
    private Button btn_toLogin, btn_toRegister, btn_login, btn_ok, btn_cancle;
    private TextView tv_forget, tv_register;
    private EditText et_lgusername, et_lgpassword, et_rgusername, et_rgpassword, et_rgpassword2, et_rgphone;
    private UserData userData = new UserData();
    private DBManager dbManager = new DBManager(userData);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        initView();
    }
    public void initView(){
        welcome_view = findViewById(R.id.welcome_view);
        bottomSheet_login = findViewById(R.id.bottom_sheet_login);
        bottomSheet_register = findViewById(R.id.bottom_sheet_regiter);
        behavior_login = BottomSheetBehavior.from(bottomSheet_login);
        behavior_register = BottomSheetBehavior.from(bottomSheet_register);
        behavior_login.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior_register.setState(BottomSheetBehavior.STATE_HIDDEN);
        btn_toLogin= (Button) findViewById(R.id.btn_toLogin);
        btn_toRegister= (Button) findViewById(R.id.btn_toRegister);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_register = (TextView) findViewById(R.id.tv_register);
        btn_toLogin.setOnClickListener(this);
        btn_toRegister.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        et_lgusername = (EditText) findViewById(R.id.et_lgusername);
        et_lgpassword = (EditText) findViewById(R.id.et_lgpassword);
        et_rgusername = (EditText) findViewById(R.id.et_rgusername);
        et_rgpassword = (EditText) findViewById(R.id.et_rgpassword);
        et_rgpassword2 = (EditText) findViewById(R.id.et_rgpassword2);
        et_rgphone = (EditText) findViewById(R.id.et_rgphone);
        // 监听Bottom Sheets回调的状态，可以通过setBottomSheetCallback来实现，
        // onSlide方法是拖拽中的回调，根据slideOffset可以做一些动画 onStateChanged方法可以监听到状态的改变
        behavior_login.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                Log.i("newState",""+newState);
                if (newState == 2) {
                    welcome_view.setVisibility(View.GONE);
                }
                if (newState == 4) {
                    welcome_view.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                behavior_login.setHideable(true);
//                behavior.setPeekHeight(0);
            }
        });
        behavior_register.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                Log.i("newState",""+newState);
                if (newState == 2) {
                    welcome_view.setVisibility(View.GONE);
                }
                if (newState == 4 || newState == 5) {
                    welcome_view.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                behavior_register.setHideable(true);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_toLogin:
//                Log.i("首页登录按钮","ok"+behavior_login.getState());
//                welcome_view.setVisibility(View.GONE);
                et_lgusername.setText("");
                et_lgpassword.setText("");
                behavior_login.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btn_toRegister:
//                Log.i("首页注册按钮","ok"+behavior_register.getState());
                et_rgusername.setText("");
                et_rgpassword.setText("");
                et_rgpassword2.setText("");
                et_rgphone.setText("");
                behavior_register.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btn_login:
                checkLogin();
                break;
            case R.id.btn_ok:   //确认注册，有更改数据库操作
                checkRegister();
                break;
            case R.id.btn_cancle:
                behavior_register.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.tv_forget:
                showAlertDialog();
                break;
            case R.id.tv_register:
                behavior_login.setState(BottomSheetBehavior.STATE_HIDDEN);
                et_rgusername.setText("");
                et_rgpassword.setText("");
                et_rgpassword2.setText("");
                et_rgphone.setText("");
                behavior_register.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }
    public void checkLogin(){
        String lgusername = et_lgusername.getText().toString().trim();
        String lgpassword = et_lgpassword.getText().toString().trim();;
        if(lgusername.length() == 0){
            Toast.makeText(this, "账号不能为空！", Toast.LENGTH_SHORT).show();
        }else if(lgpassword.length() == 0){
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            userData.setLgusername(lgusername);
            userData.setLgpassword(lgpassword);
            //检查用户是否存在
            int result = dbManager.findUserByNameAndPwd(this);
            //用户已经存在时返回，给出提示文字
            if (result == 1) { //返回1说明用户名和密码均正确
                Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show(); //登录成功提示
                saveUserLoginState(lgusername);
                Intent intent = new Intent(LoginRegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();//关闭当前登录界面，否则在主界面按后退键还会回到登录界面
            } else if (result == 0) {
                Toast.makeText(this, "账号或密码错误！", Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }
    }
    public void checkRegister(){
        String rgusername = et_rgusername.getText().toString().trim();
        String rgpassword = et_rgpassword.getText().toString().trim();;
        String rgpassword2 = et_rgpassword2.getText().toString().trim();;
        String rgphone = et_rgphone.getText().toString().trim().trim();;
        if(rgusername.length() == 0){
            Toast.makeText(this, "账号不能为空！", Toast.LENGTH_SHORT).show();
        }else if(rgpassword.length() == 0){
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        }else if (rgpassword2.length() == 0){
            Toast.makeText(this, "请确认密码！", Toast.LENGTH_SHORT).show();
        }else if (! rgpassword.equals(rgpassword2)) {
            Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
        }else if (rgphone.length() == 0 || rgphone.length() < 7 || rgphone.length() > 11){
            Toast.makeText(this, "请输入7-11位数字的电话！", Toast.LENGTH_SHORT).show();
        }else{
            userData.setRgusername(rgusername);
            userData.setRgpassword(rgpassword);
            userData.setRgpassword2(rgpassword2);
            userData.setRgphone(rgphone);
//            Log.i("setUserData",rgusername+rgpassword+rgphone);
            int count = dbManager.findUserByName(this);
            if(count>0){
                Toast.makeText(this, "该用户名已被注册！",Toast.LENGTH_SHORT).show();
            }else{
                dbManager.insertUser(this);
//                Toast.makeText(this, "注册成功！",Toast.LENGTH_SHORT).show();
                behavior_register.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    }
    public void saveUserLoginState(String userName){
        int user_id = dbManager.getUserId(this, userName);
        //SharedPreferences 保存数据的实现代码
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", user_id);
        editor.putBoolean("isLogin", true);
        //将用户信息和登录状态保存到其中
        editor.commit();
    }
    public void showAlertDialog(){
        //确认注销的对话框
        AlertDialog.Builder logoutDialog = new AlertDialog.Builder(this);
        logoutDialog.setTitle("遇到问题？请联系我们的技术人员获取帮助哦！");
//        logoutDialog.setIcon(R.drawable.my);
        logoutDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击“确认”后的操作
//                Log.i("点击了AlertDialog","确认");
                behavior_login.setState(BottomSheetBehavior.STATE_HIDDEN);
                welcome_view.setVisibility(View.VISIBLE);
            }
        });
        logoutDialog.create().show();
    }
}
