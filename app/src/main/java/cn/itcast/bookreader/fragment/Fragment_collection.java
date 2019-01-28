package cn.itcast.bookreader.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import cn.itcast.bookreader.ReadActivity;
import cn.itcast.bookreader.adapter.CollectionListAdapter;
import cn.itcast.bookreader.MainActivity;
import cn.itcast.bookreader.R;
import cn.itcast.bookreader.db.DBManager;

/**
 * Created by Administrator on 2018/6/28.
 */

public class Fragment_collection extends Fragment{
    private RelativeLayout rl_nothings, rl_collectionView;
    private Button bt_add;
    private GridView gv_likebooks;
    private CollectionListAdapter adapter;
    private int count;
    private DBManager dbManager;
    private int user_id;
    private boolean isLogin;
    private String bookName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_collection, container, false);
        rl_nothings = (RelativeLayout) view.findViewById(R.id.rl_nothings);
        rl_collectionView = (RelativeLayout) view.findViewById(R.id.rl_collectionView);
        bt_add = (Button) view.findViewById(R.id.add_button_selector);
        gv_likebooks = (GridView) view.findViewById(R.id.gv_likebooks);
        //取sharedpreferences中的数据
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);
        isLogin = sharedPreferences.getBoolean("isLogin", false);
//        Log.i("main登录信息",user_id+","+isLogin);
        fillData();
        // 为按钮添加事件监听，点击跳转到MainActivity
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.setChioceItem(0);
            }
        });
        return view;
    }

    //用于填充数据，刷新界面
    public void fillData(){
        dbManager = new DBManager();
        count = dbManager.getCollectionCount(getContext(), user_id);
        if (count==0){
//            Log.i("count",""+count);
            rl_nothings.setVisibility(View.VISIBLE);   // 显示
            bt_add.setVisibility(View.VISIBLE);
            bt_add.setEnabled(true);
            gv_likebooks.setVisibility(View.GONE);
        }else if (count > 0){
//            Log.i("count",""+count);
            rl_nothings.setVisibility(View.GONE);   // 隐藏
            rl_collectionView.setVisibility(View.VISIBLE);
            bt_add.setVisibility(View.GONE);
            bt_add.setEnabled(false);
            adapter = new CollectionListAdapter(getActivity(), user_id);
            gv_likebooks.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            gv_likebooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (adapter != null){
                        if (position == parent.getChildCount() - 1) {
                            MainActivity mainActivity = (MainActivity)getActivity();
                            mainActivity.setChioceItem(0);
                        }else{
                            String bookName = (String) adapter.getItem(position);
//                            Log.i("点击的书名：", bookName + getActivity().toString() + view.toString());
                            Intent intent = new Intent(getActivity(), ReadActivity.class);
                            intent.putExtra("bookName", bookName);
                            getActivity().startActivity(intent);
                        }
                    }
                }
            });

            gv_likebooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position != parent.getChildCount() - 1) {
                        showAlertDialog(position);
                    }
                    return true;
                }
            });
        }
    }
    public void showAlertDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(getContext(), R.layout.dialog_menu, null);
        //设置对话框布局
        dialog.setView(dialogView);
        dialog.show();
        Button btn_delete = (Button) dialogView.findViewById(R.id.btn_delete);
        Button btn_sharel = (Button) dialogView.findViewById(R.id.btn_share);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookName = (String) adapter.getItem(position);
                dbManager.deleteCollection(getContext(), bookName, user_id);
                fillData();
                dialog.dismiss();
            }
        });
        btn_sharel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookName = (String) adapter.getItem(position);
                share();
                dialog.dismiss();
            }
        });
    }
    public void share(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您阅读一本很好看的书《"+bookName+"》！");
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"share"));
    }
}
