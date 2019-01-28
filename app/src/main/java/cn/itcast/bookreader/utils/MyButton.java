package cn.itcast.bookreader.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by Administrator on 2018/6/26.
 */

public class MyButton extends Button{

    private int index = -1;
    private String bookName;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName){
        this.bookName = bookName;
    }

    public MyButton(Context context) {
        super(context);
        // TODO: do something here if you want
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO: do something here if you want
    }

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO: do something here if you want
    }
}

