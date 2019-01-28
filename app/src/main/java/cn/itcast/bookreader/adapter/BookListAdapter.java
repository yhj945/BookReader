package cn.itcast.bookreader.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.itcast.bookreader.db.DBManager;
import cn.itcast.bookreader.utils.BookList;
import cn.itcast.bookreader.utils.MyButton;
import cn.itcast.bookreader.utils.MyOnClickListener;

import cn.itcast.bookreader.R;

//import android.view.LayoutInflater;

/**
 * Created by Administrator on 2018/6/25.
 */
//自定义Adapter
public class BookListAdapter extends BaseAdapter{

    private Activity activity;
    private Context context;
    private LayoutInflater layoutInflater;
    private MyButton bt_addlike = null;
    private DBManager dbManager = new DBManager();
    private ArrayList booklist;
    private BookList book;
    private String b_name,b_author,b_image;

    public BookListAdapter(Activity activity, Context context, String sql){
        this.activity = activity;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
//        dbManager.addbook(context);
        booklist = dbManager.booklist(context, sql);
        book = new BookList(b_name, b_author, b_image);
//        Log.i("该类书本数",""+booklist.size());
    }
    //清空数据列表
    public void clear(){
        booklist.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return booklist.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        BookList book = (BookList) booklist.get(arg0);
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
            convertView = layoutInflater.inflate(R.layout.item_list_books, null);
        }
        ImageView iv_image = (ImageView) convertView.findViewById(R.id.imgv_book);
        TextView tv_book = (TextView) convertView.findViewById(R.id.tv_bookname);
        TextView tv_author = (TextView) convertView.findViewById(R.id.tv_authorname);
        bt_addlike = (MyButton) convertView.findViewById(R.id.bt_addlike);
//        从booklist中取出一行数据，position相当于数组下标,可以实现逐行取数据
        book = (BookList) booklist.get(position);
//        Log.i("获取到的",book.getName());
        String b_name = book.getName();
        String b_author = book.getAuthor();
        String b_image = book.getImage();
//        Log.i("当前position",""+position);
//        Log.i("获取到的信息","书名："+b_name+",作者："+b_author+",图片："+b_image);
        int resid = context.getResources().getIdentifier(b_image, "drawable", context.getPackageName());
//        Log.i("resid",""+resid);
        iv_image.setImageResource(resid);
        tv_book.setText(b_name);
        tv_author.setText(b_author);
        bt_addlike.setBookName(b_name);
        bt_addlike.setOnClickListener(MyOnClickListener.getInstance(activity, context));
        return convertView;
    }

}

