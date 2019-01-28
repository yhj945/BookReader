package cn.itcast.bookreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.bookreader.R;
import cn.itcast.bookreader.utils.BookList;
import cn.itcast.bookreader.utils.CollectionList;
import cn.itcast.bookreader.utils.UserData;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/6/30.
 */

public class DBManager {
    private ArrayList<BookList> booklist, searchResult;
    private ArrayList<CollectionList> collectionList;
    private UserData userData;

    public DBManager(){
        super();
    }
    public DBManager(UserData userData) {
        this.userData = userData;
    }

    public void insertUser(Context context){
        String username = userData.getRgusername();
        String password = userData.getRgpassword();
        String phone = userData.getRgphone();
        try{
            SQLdm s = new SQLdm();
            SQLiteDatabase db = s.openDatabase(context);
            ContentValues values = new ContentValues();
            values.put("u_name",username);
            values.put("u_passwd",password);
            values.put("u_phone",phone);
            long num1 = db.insert("User",null,values);
            Log.i("数据库记录", "插入了" + num1 + "数据");
            db.close();
            Toast.makeText(context, "注册成功！", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "出现异常，请重新注册！", Toast.LENGTH_SHORT).show();
        }
    }

    //根据用户名找用户，可以判断注册时用户名是否已经存在
    public int findUserByName(Context context){
        String rguserName = userData.getRgusername();
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        int result=0;
        Cursor cursor=db.rawQuery("select * from User where u_Name='"+rguserName+"'", null);
        if(cursor!=null){
            result=cursor.getCount();
        }
        cursor.close();
        db.close();
        return result;
    }
    //根据用户名和密码找用户，用于登录
    public int findUserByNameAndPwd(Context context){
        String lguserName = userData.getLgusername();
        String lgpasswd = userData.getLgpassword();
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        int result=0;
        Cursor cursor = db.rawQuery("select * from User where u_name='"+lguserName+"' and u_passwd='"+lgpasswd+"'", null);
        if(cursor!=null){
            result=cursor.getCount();
//            Log.i(TAG,"findUserByNameAndPwd , result="+result);
        }
        cursor.close();
        db.close();
        return result;
    }
    public int getUserId(Context context, String userName){
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        int user_id = 0;
        Cursor cursor = db.rawQuery("select u_id from User where u_name='"+userName+"'", null);
        while (cursor.moveToNext()) {
            user_id = cursor.getInt(cursor.getColumnIndex("u_id"));
        }
        db.close();
        cursor.close();
        return user_id;
    }

    public int getResultByBidAndUid(Context context, int user_id, int b_id){
        String sql = "select * from Bookmark where u_id = '"+user_id+"' and b_id = '"+b_id+"'";
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
        int bm_id = 0;
        while (cursor.moveToNext()){
            bm_id = cursor.getInt(cursor.getColumnIndex("bm_id"));
        }
        cursor.close();
        db.close();
        return bm_id;
    }
    // 用于保存用户书签
    public boolean saveBookmark(Context context, int user_id, String bookName,int bm_page){
        boolean isSave = false;
        int b_id = getBookId(context, bookName);
        int bm_id = getResultByBidAndUid(context, user_id, b_id);
        if (bm_id == 0) {
            try {
                SQLdm s = new SQLdm();
                SQLiteDatabase db = s.openDatabase(context);
                ContentValues values = new ContentValues();
                values.put("u_id", user_id);
                values.put("b_id", b_id);
                values.put("bm_page", bm_page);
                long num1 = db.insert("Bookmark", null, values);
                Log.i("数据库记录", "插入了" + num1 + "数据");
                db.close();
                isSave = true;
                Toast.makeText(context, "保存书签成功！", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                isSave = false;
                Toast.makeText(context, "保存书签失败！", Toast.LENGTH_SHORT).show();
            }
        }else{
            try {
                SQLdm s = new SQLdm();
                SQLiteDatabase db = s.openDatabase(context);
                db.execSQL("update Bookmark set b_id='"+b_id+"' , bm_page='"+bm_page+"' where bm_id='"+bm_id+"' and u_id='"+user_id+"'");
                db.close();
                isSave = true;
                Toast.makeText(context, "保存书签成功！", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                isSave = false;
                Toast.makeText(context, "保存书签失败！", Toast.LENGTH_SHORT).show();
            }
        }
        return isSave;
    }
    //用于获取保存的书签
    public int getBookmark(Context context, int user_id, String bookName){
        int bm_page = 1;
        int b_id = getBookId(context, bookName);
        try{
            SQLdm s = new SQLdm();
            SQLiteDatabase db = s.openDatabase(context);
            Cursor cursor = db.rawQuery("select bm_page from Bookmark where u_id='"+user_id+"' and b_id='"+b_id+"'", null);
            while (cursor.moveToNext()) {
                bm_page = cursor.getInt(cursor.getColumnIndex("bm_page"));
            }
            db.close();
            cursor.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return bm_page;
    }

    public boolean updateData(Context context, int user_id, String newName, String newPhone){
        boolean isUpdate = false;
        try{
            SQLdm s = new SQLdm();
            SQLiteDatabase db = s.openDatabase(context);
            db.execSQL("update User set u_name='"+newName+"' , u_phone='"+newPhone+"' where u_id='"+user_id+"'");
            isUpdate = true;
            Toast.makeText(context, "修改资料成功！", Toast.LENGTH_SHORT).show();
            db.close();
        }catch (Exception e) {
            e.printStackTrace();
            isUpdate=false;
            Toast.makeText(context, "该用户名已被注册！", Toast.LENGTH_SHORT).show();
        }
        return isUpdate;
    }

    public boolean updatePasswd(Context context, int user_id, String oldPasswd, String newPasswd){
        boolean isUpdate = false;
        try{
            String u_passwd = "";
            SQLdm s = new SQLdm();
            SQLiteDatabase db = s.openDatabase(context);
            Cursor cursor = db.rawQuery("select u_passwd from User where u_id = '"+user_id+"'", null);
            while (cursor.moveToNext()) {
                u_passwd = cursor.getString(cursor.getColumnIndex("u_passwd"));
            }
            if (oldPasswd.equals(u_passwd)){
                db.execSQL("update User set u_passwd='"+newPasswd+"' where u_id='"+user_id+"'");
                isUpdate = true;
                Toast.makeText(context, "修改密码成功！", Toast.LENGTH_SHORT).show();
            }else{
                isUpdate = false;
                Toast.makeText(context, "原密码不正确！", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            db.close();
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "修改密码失败！", Toast.LENGTH_SHORT).show();
        }
        return isUpdate;
    }

    public ArrayList getUserInfoList(Context context, int user_id){
        ArrayList userInfo = new ArrayList();
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery("select * from User where u_id = '"+user_id+"'", null);
        while (cursor.moveToNext()) {
            String u_name = cursor.getString(cursor.getColumnIndex("u_name"));
            int u_id = cursor.getInt(cursor.getColumnIndex("u_id"));
            String u_phone = cursor.getString(cursor.getColumnIndex("u_phone"));
            userInfo.add(u_name);
            userInfo.add(u_id);
            userInfo.add(u_phone);
//            Log.i("得到用户信息",u_name+","+u_id+","+u_phone);
        }
        cursor.close();
        db.close();
        return userInfo;
    }

    public int getBookId(Context context, String bookName){
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        int book_id = 0;
        Cursor cursor = db.rawQuery("select b_id from Book where b_name='"+bookName+"'", null);
        while (cursor.moveToNext()) {
            book_id = cursor.getInt(cursor.getColumnIndex("b_id"));
        }
        db.close();
        cursor.close();
        return book_id;
    }

    public int getResultByNameAndUid(Context context, String bookName, int userId){
        int b_id = getBookId(context, bookName);
        String sql = "select * from Collection where u_id = '"+userId+"' and b_id = '"+b_id+"'";
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public void insertCollection(Context context, String bookName, int userId){
        int b_id = getBookId(context, bookName);
        int count = getResultByNameAndUid(context, bookName, userId);
        if (count == 0) {
            try {
                SQLdm s = new SQLdm();
                SQLiteDatabase db = s.openDatabase(context);
                ContentValues values = new ContentValues();
                values.put("u_id", userId);
                values.put("b_id", b_id);
                long num1 = db.insert("Collection", null, values);
                Log.i("数据库记录", "插入了" + num1 + "数据");
                db.close();
                Toast.makeText(context, "收藏成功！", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "收藏失败！", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "该书本已收藏过啦！", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCollection(Context context, String bookName, int userId){
        int b_id = getBookId(context, bookName);
        int count = getResultByNameAndUid(context, bookName, userId);
        if (count > 0) {
            try {
                SQLdm s = new SQLdm();
                SQLiteDatabase db = s.openDatabase(context);
                db.execSQL("delete from collection where b_id = '" + b_id + "' and u_id = '" + userId + "';");
                db.close();
                Toast.makeText(context, "取消收藏成功！", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "取消收藏失败！", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "你还没收藏该书本！", Toast.LENGTH_SHORT).show();
        }
    }

    public int getCollectionCount(Context context, int user_id){
        String sql = "select b_id from Collection where u_id = '"+user_id+"'";
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
//        cursor.moveToNext();
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public ArrayList<CollectionList> getCollectionList(Context context, int user_id){
        collectionList = new ArrayList<CollectionList>();
        String bid_sql ="select b_id from Collection where u_id = '"+user_id+"'";
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor bid_cursor = db.rawQuery(bid_sql, null);
        while (bid_cursor.moveToNext()){
            int b_id = bid_cursor.getInt(bid_cursor.getColumnIndex("b_id"));
            String sql = "select * from Book where b_id = '"+b_id+"'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                String b_name = cursor.getString(cursor.getColumnIndex("b_name"));
                String image = cursor.getString(cursor.getColumnIndex("b_image"));
                String b_image = image.substring(0, image.indexOf("."));
                CollectionList book = new CollectionList(b_name, b_image);
                collectionList.add(book);
            }
            cursor.close();
        }
        bid_cursor.close();
        Log.i("collectionList",""+collectionList.size());
        db.close();
        return collectionList;
    }
    //从book表中获取图书数据，加入booklist并返回
    public ArrayList<BookList> booklist(Context context, String sql){
        booklist = new ArrayList<BookList>();
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String b_name = cursor.getString(cursor.getColumnIndex("b_name"));
            String b_author = cursor.getString(cursor.getColumnIndex("b_author"));
            String image = cursor.getString(cursor.getColumnIndex("b_image"));
            String b_image = image.substring(0, image.indexOf("."));
            BookList book = new BookList(b_name, b_author, b_image);
            booklist.add(book);
        }
        db.close();
        cursor.close();
        return booklist;
    }

    public ArrayList<BookList> getSearchResult(Context context, String searchText){
        searchResult = new ArrayList<BookList>();
        String sql = "select * from Book where b_name like '%"+searchText+"%' or b_author like '%"+searchText+"%'";
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String b_name = cursor.getString(cursor.getColumnIndex("b_name"));
            String b_author = cursor.getString(cursor.getColumnIndex("b_author"));
            String image = cursor.getString(cursor.getColumnIndex("b_image"));
            String b_image = image.substring(0, image.indexOf("."));
            BookList book = new BookList(b_name, b_author, b_image);
            searchResult.add(book);
        }
        db.close();
        cursor.close();
        return searchResult;
    }

    public String bookfile(Context context, String bookname){
        String b_file = "";
        SQLdm s = new SQLdm();
        SQLiteDatabase db = s.openDatabase(context);
        String sql = "select * from book where b_name = '"+bookname+"'";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            b_file = cursor.getString(cursor.getColumnIndex("b_file"));
//            Log.i("bookfile",""+b_file+"长度："+b_file.length());
        }
        cursor.close();
        db.close();
        return b_file;
    }

}
