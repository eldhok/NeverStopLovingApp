package com.stoplovingnever.nest.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.app.Config;
import com.stoplovingnever.nest.app.EndPoints;
import com.stoplovingnever.nest.app.MyApplication;
import com.stoplovingnever.nest.model.User;

import static android.provider.Telephony.Mms.Addr.CONTACT_ID;
import static com.stoplovingnever.nest.R.id.b1;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
   // private EditText inputName, inputEmail;
   // private TextInputLayout inputLayoutName, inputLayoutEmail;
   // private Button btnEnter;
   // ActionBar actionBar;
    TextView t1;
    ImageView tv_info_confirm;
    LinearLayout l1,l2;
     ;
  static EditText ed1,ed2,ed3;
    public static String no,name,code,code_recived,otp_tittle;
   static Button btn,login;
    String phoneNo;
    String country_code;
    GlideDrawableImageViewTarget imageViewTarget;
    static TextView tv;
    static View toastLayout;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Check for login session. It user is already logged in
         * redirect him to main activity
         * */

        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, Splash.class));
            finish();
        }

        setContentView(R.layout.activity_setup_phone_number);

       // actionBar = getSupportActionBar();

       // actionBar.setDisplayShowTitleEnabled(false);
       // actionBar.hide();
        context=getApplicationContext();

        btn = (Button)findViewById(b1);
        login = (Button)findViewById(R.id.login);


        ed1 = (EditText) findViewById(R.id.no);
        ed2 = (EditText) findViewById(R.id.name);
        ed3 = (EditText) findViewById(R.id.conf_code);

        l1=(LinearLayout) findViewById(R.id.layout_sms);
       // activity_main=(LinearLayout) findViewById(R.id.activity_main);

        l2=(LinearLayout) findViewById(R.id.layout_otp);
       // l3=(LinearLayout) findViewById(R.id.layout_edit_mobile);
        t1=(TextView) findViewById(R.id.txt_edit_mobile);
        //tv_country=(TextView) findViewById(R.id.tv_country);

        tv_info_confirm=(ImageView) findViewById(R.id.tv_info_confirm);
        imageViewTarget = new GlideDrawableImageViewTarget(tv_info_confirm);

        Glide.with(this).load(R.drawable.loveheartfly).into(imageViewTarget);

        LayoutInflater inflater = getLayoutInflater();
        toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.custom_toast_layout));
        tv=(TextView)toastLayout.findViewById(R.id.custom_toast_message);

    }

    /**
     * logging in user. Will make http post request with name, email
     * as parameters
     */


    public static boolean isNetworkAvailable(Context ct)
    {
        //Log.w("myApp", "iside network available");

        ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    public void tv_country(View view) {


        tv.setText("We are Sorry that Nest is \n currently only available in India");
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toast.show();

    }
    public void loginclk(View view){

        if(isNetworkAvailable(getApplicationContext())){
            if(!( ed1.length() == 0 || ed1.equals("") || ed1 == null || ed2.length() == 0 || ed2.equals("") || ed2 == null))
            {



                no=ed1.getText().toString();
                name=ed2.getText().toString();
                if(no.length()==10)
                {
                    no="+91"+no;
                }
                try {
                    // phone must begin with '+'
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(no, "");
                    int countryCode = numberProto.getCountryCode();
                    long nationalNumber = numberProto.getNationalNumber();
                    phoneNo = String.valueOf(nationalNumber);
                    country_code = String.valueOf(countryCode);
                    Log.i("code", "code " + countryCode);
                    Log.i("code", "national number " + nationalNumber);
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }
                if(phoneNo!=null) {
                    l1.setVisibility(View.GONE);
                   // activity_main.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    l2.setVisibility(View.VISIBLE);
                  //  l3.setVisibility(View.VISIBLE);
                    t1.setText(no);

//disable click for button


                    login.setEnabled(false);
                    login.setClickable(false);



                    //Write Code to Send OTP to mobile number
                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            EndPoints.OTP, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "response: " + response);

                            try {
                                JSONObject obj = new JSONObject(response);

                                // check for error flag
                                if (obj.getBoolean("error") == false) {
                                    // user successfully logged in

                                    JSONObject userObj = obj.getJSONObject("user");
                                    code_recived = userObj.getString("otp");

                                    otp_tittle = userObj.getString("otp_tittle");



                                } else {
                                    // login error - simply toast the message
                                   // Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                              //  Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                           // call retrieve contacts here

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                           // Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", name);


                            params.put("no", phoneNo);
                            if(country_code!=null)
                            params.put("code", country_code);
                            else
                                params.put("code", "91");


                            Log.e(TAG, "params: " + params.toString());
                            return params;
                        }
                    };

                    //Adding request to request queue
                    MyApplication.getInstance().addToRequestQueue(strReq);
                }

           }

        }else
        {
         //   Toast.makeText(LoginActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();

            tv.setText("Check your Internet Connection !");
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastLayout);
            toast.show();
        }
    }

    public void no_change(View view){

        //activity_main.setBackgroundColor(getResources().getColor(R.color.white));



        login.setEnabled(true);
        login.setClickable(true);

        btn.setEnabled(true);
        btn.setClickable(true);



        l2.setVisibility(View.GONE);
       // l3.setVisibility(View.GONE);
        l1.setVisibility(View.VISIBLE);
    }


public static void automsg(String message)
{
    Log.e("MAIN", "OTP Detected "+message );


    //ed3.setText(Integer.parseInt(message));
    btn.setEnabled(true);
    btn.setClickable(true);
    ed3.setText(message);

    btn.performClick();
}



    public void b1(View view){
        //pb.setVisibility(View.VISIBLE);
        //code is the one user entered
       // Toast.makeText(getApplicationContext(), "button clk", Toast.LENGTH_LONG).show();
        code=ed3.getText().toString();
        if(code_recived!=null && code!=null) {
            if (code_recived.equals(code)) {


                if (isNetworkAvailable(getApplicationContext())) {

                    tv.setText("OTP Success");
                    Toast toast = new Toast(context);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();
                    //Before that add user details to main_user table

                  //  MyApplication.getInstance().getPrefManager().storefriends(FriendsFragment.getAll(getApplicationContext()));

                    btn.setEnabled(false);
                    btn.setClickable(false);


                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            EndPoints.LOGIN, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "response: " + response);

                            try {
                                JSONObject obj = new JSONObject(response);

                                // check for error flag
                                if (obj.getBoolean("error") == false) {
                                    // user successfully logged in
                                    Log.e(TAG, "got in" );
                                    JSONObject userObj = obj.getJSONObject("user");
                                    User user = new User(userObj.getString("id"),
                                            userObj.getString("name"),
                                            userObj.getString("no"));

                                    // storing user in shared preferences
                                    MyApplication.getInstance().getPrefManager().storeUser(user);


                                    sendRegistrationToServer(MyApplication.getInstance().getPrefManager().getFcm());




                                    //call refresh friends number details here
                                    fetchfriends_list();





                                    // start main activity
                                    startActivity(new Intent(getApplicationContext(), Splash.class));
                                    finish();

                                } else {
                                    // login error - simply toast the message
                                  //  Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                Log.e(TAG, "json parsing error: " + e.getMessage());
                               // Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                           // Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", name);

                            params.put("no", phoneNo);

                            params.put("code",country_code);

                            Log.e(TAG, "params: " + params.toString());
                            return params;
                        }
                    };

                    //Adding request to request queue
                    MyApplication.getInstance().addToRequestQueue(strReq);






// for testing php

/*

                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            EndPoints.LOGIN, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "response: " + response);


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                            Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", name);

                            params.put("no", phoneNo);
                            if(country_code!=null)
                                params.put("code",country_code);
                            else
                                params.put("code","91");

                            Log.e(TAG, "params: " + params.toString());
                            return params;
                        }
                    };

                    //Adding request to request queue
                    MyApplication.getInstance().addToRequestQueue(strReq);

*/




                } else {
                   // Toast.makeText(LoginActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    tv.setText("Check your Internet Connection !");
                    Toast toast = new Toast(context);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(toastLayout);
                    toast.show();
                }


            } else {
                //Toast.makeText(LoginActivity.this, "OTP is wrong", Toast.LENGTH_SHORT).show();
                //pb.setVisibility(View.GONE);
                tv.setText("OTP Wrong !");
                Toast toast = new Toast(context);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(toastLayout);
                toast.show();
            }
        }


    }




    private void sendRegistrationToServer(final String token) {

        // checking for valid login session
        User user = MyApplication.getInstance().getPrefManager().getUser();
        if (user == null) {
            // TODO
            // user not found, redirecting him to login screen
            Log.e(TAG, "user is null");

            return;
        }

        String endPoint = EndPoints.USER.replace("_ID_", user.getId());

        Log.e(TAG, "endpoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.PUT,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        // broadcasting token sent to server
                        Intent registrationComplete = new Intent(Config.SENT_TOKEN_TO_SERVER);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                    } else {
                       // Toast.makeText(getApplicationContext(), "Unable to send nest registration id to our sever. " + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                   // Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
              //  Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gcm_registration_id", token);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }



     public static void fetchfriends_list() {



        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CHAT_FRIENDS_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e("Main", "response: fetchfriends_list" + response);

                try {
                    JSONObject obj = new JSONObject(response);


                    // check for error flag
                    if (obj.getBoolean("error") == false) {

                        JSONArray chatRoomsArray = obj.getJSONArray("friend_no");

                        ArrayList<String> num=new ArrayList<>();
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            //JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                            num.add(chatRoomsArray.get(i).toString());
                            Log.e("Main", "number: " + chatRoomsArray.get(i).toString());

                        }

                        MyApplication.getInstance().getPrefManager().storefriends(num);

                        Log.e("Main", "number from sp: " + MyApplication.getInstance().getPrefManager().getfriends().toString());

                        Log.e("Main", "End Reached ");


                    } else {
                        // error in fetching chat rooms
                   //    Toast.makeText(context.getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                 //   Log.e(TAG, "json parsing error: " + e.getMessage());
                   // Toast.makeText(context.getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


                // subscribing to all chat room topics
                // subscribeToAllTopics();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
              // Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
              //  Toast.makeText(context.getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();



               ArrayList<String> numbers = getAll(context);

                JSONArray list = new JSONArray();

                for (int i = 0; i < numbers.size(); i++)
                {



                    try {
                        // phone must begin with '+'
                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(numbers.get(i), "");
                        Log.e("Main", "number without code: " + String.valueOf(numberProto.getNationalNumber()));

                        list.put(String.valueOf(numberProto.getNationalNumber()));
                    } catch (NumberParseException e) {
                        System.err.println("NumberParseException was thrown: " + e.toString());
                    }

                  //  list.put(numbers.get(i));

                }


                //numbers=getAll(context);

                  //  Log.e("Main", "Contacts" + getAll(context).toString());

/*
                int i=0;

                for(String object: numbers){
                    params.put("friend["+(i++)+"]", object);
                    // you first send both data with same param name as friendnr[] ....  now send with params friendnr[0],friendnr[1] ..and so on
                }
                */
               // params.put("friend_no", Integer.toString(i-1));

                params.put("friend_no", list.toString());

                //params.add(new BasicNameValuePair("list", list.toString()));


                    return params;
            }
        };


        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);



    }





    public static ArrayList<String> getAll(Context context){
        ContentResolver cr = context.getContentResolver();

        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{FriendsFragment.PHONE_NUMBER, FriendsFragment.PHONE_CONTACT_ID},
                null,
                null,
                null
        );
        if(pCur != null){
            if(pCur.getCount() > 0) {
                HashMap<Integer, ArrayList<String>> phones = new HashMap<>();
                while (pCur.moveToNext()) {
                    Integer contactId = pCur.getInt(pCur.getColumnIndex(FriendsFragment.PHONE_CONTACT_ID));
                    ArrayList<String> curPhones = new ArrayList<>();
                    if (phones.containsKey(contactId)) {
                        curPhones = phones.get(contactId);
                    }


/*


 try {
                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(pCur.getString(pCur.getColumnIndex(FriendsFragment.PHONE_NUMBER)), "");
                    curPhones.add(String.valueOf(numberProto.getNationalNumber()));

                    } catch (NumberParseException e) {
                        System.err.println("NumberParseException was thrown: " + e.toString());
                    }

*/

                    curPhones.add(pCur.getString(pCur.getColumnIndex(FriendsFragment.PHONE_NUMBER)));

                    phones.put(contactId, curPhones);
                }
                Cursor cur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{CONTACT_ID, FriendsFragment.HAS_PHONE_NUMBER},
                        FriendsFragment.HAS_PHONE_NUMBER + " > 0",
                        null,null);
                if (cur != null) {
                    if (cur.getCount() > 0) {
                        ArrayList<String> contacts = new ArrayList<>();
                        while (cur.moveToNext()) {
                            int id = cur.getInt(cur.getColumnIndex(CONTACT_ID));
                            if(phones.containsKey(id)) {

                                //remove country code from here
                                contacts.addAll(phones.get(id));
                            }
                        }
                        return contacts;
                    }
                    cur.close();
                }
            }
            pCur.close();
        }
        return null;
    }




}
