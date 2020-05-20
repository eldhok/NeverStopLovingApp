package com.stoplovingnever.nest.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class ChatRoom implements Serializable {
    String s_name,r_name,s_id,r_id,status,chat_id,last_msg,timestamp,last_msg_s_id,number,name,i,msg_left,rechrg;
    int unreadCount;
    Bitmap bit;
   int flag,inst;

    public ChatRoom() {
    }

    public ChatRoom(String s_name,String r_name,String s_id,String r_id,String status,String chat_id,String last_msg,String timestamp,String last_msg_s_id,int unreadCount,String number,Bitmap bit,String name,String i,int flag,int inst,String rechrg,String msg_left) {
        this.s_name = s_name;
        this.r_name = r_name;
        this.s_id = s_id;
        this.r_id = r_id;
        this.status = status;
        this.chat_id = chat_id;
        this.last_msg = last_msg;
        this.last_msg_s_id = last_msg_s_id;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
        this.number=number;
        this.bit=bit;
        this.flag=flag;
        this.name=name;
        this.i=i;
        this.inst=inst;
        this.msg_left=msg_left;
        this.rechrg=rechrg;

    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }


    public String getMsg_left() {
        return msg_left;
    }

    public void setMsg_left(String msg_left) {
        this.msg_left = msg_left;
    }
    public String getRechrg() {
        return rechrg;
    }

    public void setRechrg(String rechrg) {
        this.rechrg = rechrg;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) { this.s_id = s_id; }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) { this.r_id = r_id; }

    public String getSatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) { this.chat_id = chat_id; }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) { this.last_msg = last_msg; }

    public String getLast_msg_s_id() {
        return last_msg_s_id;
    }

    public void setLast_msg_s_id(String last_msg_s_id) { this.last_msg_s_id = last_msg_s_id; }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) { this.number = number; }

  /*
    public int getColor() {
        return color;
    }

    public void setColor(int color) { this.color = color; }
*/

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) { this.flag = flag; }



    public int getInst() {
        return inst;
    }

    public void setInst(int inst) { this.inst = inst; }



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
