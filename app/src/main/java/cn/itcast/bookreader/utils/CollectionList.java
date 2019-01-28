package cn.itcast.bookreader.utils;

/**
 * Created by Administrator on 2018/7/6.
 */

public class CollectionList {
    String name;
    String image;

    public CollectionList(){
        super();
    }

    public CollectionList(String name, String image){
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
