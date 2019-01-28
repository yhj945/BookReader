package cn.itcast.bookreader.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.itcast.bookreader.R;
import cn.itcast.bookreader.db.DBManager;
import cn.itcast.bookreader.utils.CollectionList;

/**
 * Created by Administrator on 2018/6/30.
 */

public class CollectionListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private DBManager dbManager = new DBManager();
    private String b_name,b_image;
    private ArrayList collectionlist;
    private CollectionList book;

    public CollectionListAdapter(Context context, int user_id){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        collectionlist = dbManager.getCollectionList(context, user_id);
        book = new CollectionList(b_name, b_image);
//        Log.i("收藏书本数",""+collectionlist.size());
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return collectionlist.size()+1;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        CollectionList book = (CollectionList) collectionlist.get(arg0);
        String bookname = book.getName();
        return bookname;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //根据布局文件获取View返回值
            convertView = layoutInflater.inflate(R.layout.item_list_mylike, null);
        }
        TextView tv_bookname = (TextView) convertView.findViewById(R.id.tv_like_book_name) ;
        ImageView iv_bookimg = (ImageView) convertView.findViewById(R.id.iv_like_book);
        if (position < collectionlist.size()){
            book = (CollectionList) collectionlist.get(position);
            String b_name = book.getName();
            String b_image = book.getImage();
            int resid = context.getResources().getIdentifier(b_image, "drawable", context.getPackageName());
            iv_bookimg.setImageResource(resid);
            tv_bookname.setText(b_name);
        }else{
            iv_bookimg.setImageResource(R.drawable.add);
            tv_bookname.setText("");
        }
        return convertView;
    }
}
