package cn.itcast.bookreader.utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/4.
 */

public class BookList {
    String name;
    String author;
    String image;
//    ArrayList<> booklist;

    public BookList(){
        super();
    }

    public BookList(String name, String author, String image){
        this.name = name;
        this.author = author;
        this.image = image;
    }
//    public String toString(){
//        return name;
//    }
    public String getName(){
        return name;
    }

    public String getAuthor(){
        return author;
    }

    public String getImage(){
        return image;
    }
}
