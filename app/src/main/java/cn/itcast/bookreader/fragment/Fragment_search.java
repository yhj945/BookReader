package cn.itcast.bookreader.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import cn.itcast.bookreader.R;
import cn.itcast.bookreader.ReadActivity;
import cn.itcast.bookreader.adapter.BookListAdapter;

/**
 * Created by Administrator on 2018/6/28.
 */

public class Fragment_search extends Fragment implements SearchView.OnQueryTextListener{
    private SearchView searchView;
    private BookListAdapter adapter;
    private ListView lv_searchBook;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);
        searchView = (SearchView) view.findViewById(R.id.sv_search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        SpannableString spanText = new SpannableString("请输入书名或作者查询");
        // 设置字体大小
        spanText.setSpan(new AbsoluteSizeSpan(16, true), 0, spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        // 设置字体颜色
        spanText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        searchView.setQueryHint(spanText);
        return view;
    }
    @Override
    public boolean onQueryTextChange(String newText){
//        Log.i("onQueryTextChange","进入onQueryTextChange");
        fillData(newText);
        return false;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
//        Log.i("onQueryTextSubmit","进入onQueryTextSubmit");
        fillData(query);
        return false;
    }
    public void fillData(String searchText){
        if (searchText.length() != 0) {
//            Log.i("fillData", "fillData" + "," + searchText);
            String sql = "select * from Book where b_name like '%" + searchText + "%' or b_author like '%" + searchText + "%'";
            adapter = new BookListAdapter(getActivity(), getContext(), sql);
            lv_searchBook = (ListView) view.findViewById(R.id.lv_searchBook);
            lv_searchBook.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            lv_searchBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (adapter != null) {
                        String bookName = (String) adapter.getItem(position);
//                        Log.i("点击的书名：", bookName + getActivity().toString() + view.toString());
                        Intent intent = new Intent(getActivity(), ReadActivity.class);
                        intent.putExtra("bookName", bookName);
                        getActivity().startActivity(intent);
                    }
                }
            });
        }else{
            //清空数据
            //方法一
            lv_searchBook.setAdapter(null);
            //方法二,调用adapter容器中定义的的clear()方法
//            adapter.clear();
        }
    }
}
