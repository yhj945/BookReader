package cn.itcast.bookreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.itcast.bookreader.db.DBManager;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private View bottomSheet_passwd, bottomSheet_data;
    private BottomSheetBehavior behavior_passwd, behavior_data;
    private RelativeLayout rl_back, rl_data, rl_passwd;
    private RelativeLayout settingsView, rl_back_data, rl_back_pwd;
    private EditText et_data_name, et_data_phone,et_passwd_oldpasswd,
            et_passwd_newpasswd, et_passwd_newpasswd2;
    private Button btn_saveData, btn_savePasswd;
    private int user_id;
    private DBManager dbManager = new DBManager();
    private int changeNum = 0; //EditText监听修改的次数
    private String newName, newPhone;
    private ArrayList userInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }
    public void initView(){
        user_id = getIntent().getIntExtra("user_id",0);//获取传进来的用户信息
        userInfoList = dbManager.getUserInfoList(this, user_id);
        newName = String.valueOf(userInfoList.get(0));
        newPhone = String.valueOf(userInfoList.get(2));
        settingsView = (RelativeLayout) findViewById(R.id.settingsView) ;
        rl_back = (RelativeLayout) findViewById(R.id.rl_my_settings_back);
        rl_data = (RelativeLayout) findViewById(R.id.rl_my_settings_data);
        rl_passwd = (RelativeLayout) findViewById(R.id.rl_my_settings_passwd);
        rl_back_data = (RelativeLayout) findViewById(R.id.rl_my_settings_back_data);
        rl_back_pwd = (RelativeLayout) findViewById(R.id.rl_my_settings_back_pwd);
        bottomSheet_data = findViewById(R.id.bottom_sheet_setting_data);
        bottomSheet_passwd = findViewById(R.id.bottom_sheet_setting_passwd);
        behavior_data = BottomSheetBehavior.from(bottomSheet_data);
        behavior_passwd = BottomSheetBehavior.from(bottomSheet_passwd);
        behavior_data.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior_passwd.setState(BottomSheetBehavior.STATE_HIDDEN);
        rl_back.setOnClickListener(this);
        rl_data.setOnClickListener(this);
        rl_passwd.setOnClickListener(this);
        rl_back_data.setOnClickListener(this);
        rl_back_pwd.setOnClickListener(this);
        behavior_data.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                Log.i("newState",""+newState);
                if (newState == 2) {
                    settingsView.setVisibility(View.GONE);
                }
                if (newState == 4 || newState == 5) {
                    settingsView.setVisibility(View.VISIBLE);
                    changeNum = 0;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                behavior_data.setHideable(true);

            }
        });
        behavior_passwd.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                Log.i("newState",""+newState);
                if (newState == 2) {
                    settingsView.setVisibility(View.GONE);
                }
                if (newState == 4 || newState == 5) {
                    settingsView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                behavior_passwd.setHideable(true);

            }
        });
        et_data_name = (EditText) findViewById(R.id.et_settings_data_name);
        et_data_phone = (EditText) findViewById(R.id.et_settings_data_phone);
        et_passwd_oldpasswd = (EditText) findViewById(R.id.et_settings_passwd_oldpasswd);
        et_passwd_newpasswd = (EditText) findViewById(R.id.et_settings_passwd_newpasswd);
        et_passwd_newpasswd2 = (EditText) findViewById(R.id.et_settings_passwd_newpasswd2);
        btn_saveData = (Button) findViewById(R.id.btn_saveData);
        btn_savePasswd = (Button) findViewById(R.id.btn_savePasswd);
        btn_saveData.setEnabled(false);
        btn_savePasswd.setEnabled(false);
        TextChange textChange=new TextChange();
        //添加edittext的输入监听
        et_data_name.addTextChangedListener(textChange);
        et_data_phone.addTextChangedListener(textChange);
        et_passwd_oldpasswd.addTextChangedListener(textChange);
        et_passwd_newpasswd.addTextChangedListener(textChange);
        et_passwd_newpasswd2.addTextChangedListener(textChange);

    }
    public void send() {
        //获取用户信息,并发送广播
        userInfoList = dbManager.getUserInfoList(this, user_id);
        newName = String.valueOf(userInfoList.get(0));
        newPhone = String.valueOf(userInfoList.get(2));
        Intent intent = new Intent("updateUserData");
        intent.putExtra("user_name", newName);
        intent.putExtra("user_id", user_id);
        intent.putExtra("user_phone", newPhone);
        sendBroadcast(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_my_settings_back:
//                Log.i("点击","返回");
                send();
                finish();
                break;
            case R.id.rl_my_settings_data:
//                Log.i("点击","资料");
                et_data_name.setText(newName);
                et_data_phone.setText(newPhone);
                behavior_data.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.rl_my_settings_passwd:
//                Log.i("点击","密码");
                et_passwd_oldpasswd.setText("");
                et_passwd_newpasswd.setText("");
                et_passwd_newpasswd2.setText("");
                behavior_passwd.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.rl_my_settings_back_data:
                behavior_data.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.rl_my_settings_back_pwd:
                behavior_passwd.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
        }
    }
    //设置edittext的输入监听
    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            Log.i("before改变","name:"+et_data_name.getText().toString()+",phone:"+et_data_phone.getText().toString());
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            Log.i("on改变","name:"+et_data_name.getText().toString()+",phone:"+et_data_phone.getText().toString());
        }
        @Override
        public void afterTextChanged(Editable s) {
            changeNum++;
//            Log.i("changeNum",""+changeNum);
//            Log.i("after改变","name:"+et_data_name.getText().toString()+",phone:"+et_data_phone.getText().toString());
            if (et_passwd_oldpasswd.length()>0 && et_passwd_newpasswd.length()>0 &&
                    et_passwd_newpasswd2.length()>0){
                final String oldPasswd = et_passwd_oldpasswd.getText().toString();
                final String newPasswd = et_passwd_newpasswd.getText().toString();
                final String newPasswd2 = et_passwd_newpasswd2.getText().toString();
                btn_savePasswd.setBackgroundColor(getResources().getColor(R.color.saveSelected));
                btn_savePasswd.setEnabled(true);
                btn_savePasswd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newPasswd.equals(newPasswd2)){
                            boolean isUpdate = dbManager.updatePasswd(SettingActivity.this, user_id, oldPasswd, newPasswd);
                            if (isUpdate){ //修改成功
                                SharedPreferences sharedPreferences = SettingActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("user_id", 0);
                                editor.putBoolean("isLogin", false);
                                editor.commit();
                                behavior_passwd.setState(BottomSheetBehavior.STATE_HIDDEN);
                                showAlertDialog();
                            }else{
                                behavior_passwd.setState(BottomSheetBehavior.STATE_HIDDEN);
                            }
//                            Log.i("密码修改", "ok");
                        }
                    }
                });
//                Log.i("密码","旧密码:"+oldPasswd+",新密码:"+newPasswd+",新密码2:"+newPasswd2);
            }else{
                btn_savePasswd.setBackgroundColor(getResources().getColor(R.color.save));
                btn_savePasswd.setEnabled(false);
//                Log.i("密码修改","有空白");
            }
            if (changeNum > 2){
                btn_saveData.setBackgroundColor(getResources().getColor(R.color.saveSelected));
                btn_saveData.setEnabled(true);
                newName = et_data_name.getText().toString();
                newPhone = et_data_phone.getText().toString();
                btn_saveData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = dbManager.updateData(SettingActivity.this, user_id, newName, newPhone);
                        if (isUpdate){ //修改成功
                            behavior_data.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                    }
                });
            }else{
                btn_saveData.setBackgroundColor(getResources().getColor(R.color.save));
                btn_saveData.setEnabled(false);
            }
        }
    }
    public void showAlertDialog(){
        //重新登录的对话框
        AlertDialog.Builder logoutDialog = new AlertDialog.Builder(this);
        logoutDialog.setTitle("密码已更改，请重新登录！");
//        logoutDialog.setIcon(R.drawable.my);
        logoutDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // 点击“确认”后的操作
//                Log.i("点击了AlertDialog","确认");
                Intent intent = new Intent(SettingActivity.this, LoginRegisterActivity.class);
                startActivity(intent);
                System.exit(0);
            }
        });
        logoutDialog.create().show();
    }
}