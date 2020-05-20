package com.stoplovingnever.nest.model;

import android.graphics.Bitmap;

import java.io.Serializable;


public class FriendList implements Serializable {
    String number,name,crush,like,i,id;
    Bitmap bit;
    int flag;

    public FriendList() {
    }

    public FriendList(String number,String name,String crush,String like,String i,Bitmap bit,int flag,String id) {
        this.crush=crush;
        this.like=like;
        this.number=number;
        this.bit=bit;
        this.flag=flag;
        this.name=name;
        this.i=i;
        this.id=id;

    }



    public String getCrush() {
        return crush;
    }

    public void setCrush(String crush) { this.crush = crush; }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getLike() {
        return like;
    }

    public void setLike(String like) { this.like = like; }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) { this.number = number; }



    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) { this.flag = flag; }






    public Bitmap getBit() {
        return bit;
    }

    public void setBit(Bitmap bit) { this.bit = bit; }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getI() {
        return i;
    }

    public void setI(String i) { this.i = i; }

}
