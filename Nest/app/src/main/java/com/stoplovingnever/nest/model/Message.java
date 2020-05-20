package com.stoplovingnever.nest.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Message implements Serializable {
    String id, message, createdAt,msg_id;

    //User user;

    public Message() {
    }

    public Message(String id, String message, String createdAt, String msg_id,User user) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.msg_id = msg_id;
       // this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMsgId() {
        return msg_id;
    }

    public void setMsgId(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }



    /*
 public User getUser() {
        return user;
    }

  public void setUser(User user) {
      this.user = user;
  }
  */
}
