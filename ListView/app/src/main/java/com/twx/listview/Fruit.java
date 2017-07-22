package com.twx.listview;

/**
 * Created by twx on 2017/2/27.
 */

public class Fruit {
    private String name;
    private int imageId;

    public  Fruit(String name,int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public  String getName(){
        return  name;
    }
    public  int getImageId(){
        return  imageId;
    }
}
