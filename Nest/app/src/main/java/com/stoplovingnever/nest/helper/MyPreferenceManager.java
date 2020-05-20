package com.stoplovingnever.nest.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.stoplovingnever.nest.model.User;

import java.util.ArrayList;

/**
 * Created by Lincoln on 07/01/16.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "androidhive_gcm";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_FCM = "fcmkeynest";
    private static final String KEY_FRIENDS = "friends_no";
    private static final String KEY_MSG_COUNT = "m_c";
    private static final String KEY_MSG_LEFT = "msg_left";
    private static final String KEY_RCG = "rcg_amt";
    private static final String KEY_LIKES = "likes";
    private static final String KEY_CON_RE = "contre";
    private static final String INST = "inst";
    private static final String PROFILE = "profile";
    private static final String MSG_COUNT = "msg_count";


    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void storeFcm(String key) {
        editor.putString(KEY_FCM, key);
        editor.commit();

    }

    public String getFcm() {
        if (pref.getString(KEY_USER_ID, null) != null) {

            return pref.getString(KEY_FCM, null);
        }
        return null;
    }


 public void storeContRe(String key) {
        editor.putString(KEY_CON_RE, key);
        editor.commit();

    }

    public String getContRe() {
        if (pref.getString(KEY_USER_ID, null) != null) {

            return pref.getString(KEY_CON_RE, null);
        }
        return null;
    }


    public void storeInst(String key) {
        editor.putString(INST, key);
        editor.commit();

    }

    public String getInst() {
        if (pref.getString(KEY_USER_ID, null) != null) {

            return pref.getString(INST, null);
        }
        return null;
    }


    public void storeProfile(String key) {
        editor.putString(PROFILE, key);
        editor.commit();

    }

    public String getProfile() {
        if (pref.getString(KEY_USER_ID, null) != null) {

            return pref.getString(PROFILE, null);
        }
        return null;
    }public void storeMsgC(String key) {
        editor.putString(MSG_COUNT, key);
        editor.commit();

    }

    public String getMsgC() {
        if (pref.getString(KEY_USER_ID, null) != null) {

            return pref.getString(MSG_COUNT, null);
        }
        return null;
    }


    public void storeLikes(String key) {
        editor.putString(KEY_LIKES, key);
        editor.commit();

    }

    public String getLikes() {
        if (pref.getString(KEY_USER_ID, null) != null) {

            return pref.getString(KEY_LIKES, "0");
        }
        return null;
    }




    public void storeMsgLeft(String msgleft) {
        editor.putString(KEY_MSG_LEFT, msgleft);
        editor.commit();

    }

    public String getMsgLeft() {
        if (pref.getString(KEY_USER_ID, null) != null) {

            return pref.getString(KEY_MSG_LEFT, "0");
        }
        return null;
    }
    public void storeRechrge(String rcrg) {
        editor.putString(KEY_RCG, rcrg);
        editor.commit();

    }

    public String getRechrge() {
        if (pref.getString(KEY_USER_ID, null) != null) {

            return pref.getString(KEY_RCG, null);
        }
        return null;
    }

    public void storeMsgCount(String chat_id,String key) {
        editor.putString(KEY_MSG_COUNT+chat_id, key);
        editor.commit();

    }

    public String getMsgCount(String chat_id) {
        if (pref.getString(KEY_USER_ID, null) != null) {

            return pref.getString(KEY_MSG_COUNT+chat_id,"0");
        }
        return null;
    }


    public void storefriends(ArrayList<String> set) {
       // editor.putStringSet(KEY_FRIENDS, set);
        editor.putString(KEY_FRIENDS, ObjectSerializer.serialize(set));
        editor.commit();

    }
    public ArrayList<String> getfriends() {
       // return pref.getStringSet(KEY_FRIENDS, null);

            return (ArrayList<String>) ObjectSerializer.deserialize(pref.getString(KEY_FRIENDS, ObjectSerializer.serialize(new ArrayList<String>())));
    }

    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " + user.getEmail());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, email;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);

            User user = new User(id, name, email);
            return user;
        }
        return null;
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
