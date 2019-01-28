package cn.itcast.bookreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import cn.itcast.bookreader.R;
import cn.itcast.bookreader.ReadActivity;
import cn.itcast.bookreader.adapter.BookListAdapter;
import cn.itcast.bookreader.view.MenuPopupwindow;

public class Fragment_1 extends Fragment{
    ListView lv_books;
    BookListAdapter adapter;
    MenuPopupwindow mPopWindow = new MenuPopupwindow();
    String sql = "select * from book where t_id='01'";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container,false);
        adapter = new BookListAdapter(getActivity(), getContext(), sql);
        lv_books = (ListView) view.findViewById(R.id.lv_books);
        lv_books.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv_books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter != null){
                    String bookName = (String)adapter.getItem(position);
//                    Log.i("点击的书名：",bookName+getActivity().toString()+view.toString());
                    Intent intent = new Intent(getActivity(), ReadActivity.class);
                    intent.putExtra("bookName", bookName);
                    getActivity().startActivity(intent);
                }
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }
}
