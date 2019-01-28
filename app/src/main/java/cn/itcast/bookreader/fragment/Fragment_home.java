package cn.itcast.bookreader.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import cn.itcast.bookreader.adapter.BookListAdapter;
import cn.itcast.bookreader.adapter.PagerAdapter;
import cn.itcast.bookreader.view.SlideTabView;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.bookreader.fragment.Fragment_1;
import cn.itcast.bookreader.fragment.Fragment_2;
import cn.itcast.bookreader.fragment.Fragment_3;
import cn.itcast.bookreader.fragment.Fragment_4;
import cn.itcast.bookreader.fragment.Fragment_5;
import cn.itcast.bookreader.R;

/**
 * Created by Administrator on 2018/6/28.
 * 第一页面
 */
public class Fragment_home extends Fragment {
    private SlideTabView tabView;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private List<String> strings;
    private List<Fragment> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        tabView = (SlideTabView) view.findViewById(R.id.tab);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        strings = new ArrayList<>();
        list = new ArrayList<>();

        list.add(new Fragment_1());
        list.add(new Fragment_2());
        list.add(new Fragment_3());
        list.add(new Fragment_4());
        list.add(new Fragment_5());
        adapter = new PagerAdapter(getFragmentManager(), list);
        viewPager.setAdapter(adapter);
        strings.add("小说");
        strings.add("文学");
        strings.add("历史");
        strings.add("军事");
        strings.add("社科");
        tabView.initTab(strings, viewPager);
        return view;
    }

}

