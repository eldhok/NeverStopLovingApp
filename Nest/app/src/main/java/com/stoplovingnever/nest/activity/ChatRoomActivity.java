package com.stoplovingnever.nest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.adapter.ChatRoomThreadAdapter;
import com.stoplovingnever.nest.app.Config;
import com.stoplovingnever.nest.app.EndPoints;
import com.stoplovingnever.nest.app.MyApplication;
import com.stoplovingnever.nest.gcm.NotificationUtils;
import com.stoplovingnever.nest.model.Message;

public class ChatRoomActivity extends AppCompatActivity {

    private String TAG = ChatRoomActivity.class.getSimpleName();

    private String chatRoomId,rid,installed,rechrg,msg_left,title,number;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private Button btnSend;
    String last_msg="",last_msg_t="",last_msg_s="";
    public static Bitmap b;
    public static int color;
    public static String imtext;
   /// public static String date="";
    public  static int flag;
    Context context;
    static TextView tv;
    static  int msg_flag;
    static View toastLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputMessage = (EditText) findViewById(R.id.message);
        btnSend = (Button) findViewById(R.id.btn_send);

        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chat_room_id");
         title = intent.getStringExtra("name");
        imtext=title.substring(0,1).toUpperCase();
        //b=null;
        msg_left=intent.getStringExtra("msg_left");
        rechrg=intent.getStringExtra("rechrg");
        installed=intent.getStringExtra("inst");
        number=intent.getStringExtra("number");
       // installed=intent.getStringExtra("installed");

        Log.e(TAG, "rechrg 2 "+rechrg);
        Log.e(TAG, "msg_left 2 "+msg_left);
        Log.e(TAG, "inst 2 "+installed);

        LayoutInflater inflater=getLayoutInflater();
        toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.custom_toast_layout));
        tv=(TextView)toastLayout.findViewById(R.id.custom_toast_message);

/*
        if(installed.equals("0") && Integer.parseInt(msg_left)<1)
        {



            AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this, R.style.MyDialogTheme);
            builder.setTitle(title+" is not currently using Nest !");
            builder.setMessage(title+" is not currently using Nest, find some tricky way and let them hear about Nest. \nOr else use our Anonymous messaging feature to send text message to "+title+"'s Phone.\nCredits " +
                    "Left in your\nAccount is "+msg_left+"\nPlease kindly Recharge your Nest wallet using Paytm.\nPressing Cancel will Send messages to our servers and "+title+" will not know about these Messages untill the Start Using Nest");

            //String positiveText = getString();
            builder.setPositiveButton("RECHARGE",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic
                           MainActivity.mainactivity.onStartTransaction();


                        }
                    });

            // String negativeText = getString(android.R.string.cancel);
            builder.setNegativeButton("CANCEL",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // negative button logic

                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            // display dialog
            dialog.show();



        }
        else
        */
        if(installed.equals("0") && Integer.parseInt(msg_left)>0)
        {

            //toast

            tv.setText("Message balance left "+msg_left+"\nUse it Wisely !");
            Toast toast = new Toast(ChatRoomActivity.this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastLayout);
            toast.show();

        }




        if(getIntent().hasExtra("image"))
        {
            //ImageView previewThumbnail = new ImageView(this);
             b = BitmapFactory.decodeByteArray( getIntent().getByteArrayExtra("image"),0,getIntent().getByteArrayExtra("image").length);
          // previewThumbnail.setImageBitmap(b);
        }
        flag=intent.getIntExtra("flag",0);
       /*
        if(getIntent().hasExtra("color"))
        {

            //set color random and first letter of name
            color=intent.getIntExtra("color",1);
            Log.e(TAG, "color 2"+Integer.toString(color));

        }
        */
        rid=intent.getStringExtra("r_id");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (chatRoomId == null) {
           // Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }
        context=getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();

        // self user id is to identify the message owner
        String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId();

        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList, selfUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                   sendMessage();


            }
        });

        fetchChatThread();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        Log.e(TAG, " inside onback pressed 1");
        //finish();
        Log.e(TAG, " inside onback pressed 2");
        // put the String to pass back into an Intent and close this activity
      //Intent intent = new Intent();
       Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("last_msg", last_msg);
        intent.putExtra("chatRoomId", chatRoomId);
        intent.putExtra("last_msg_t", last_msg_t);
        intent.putExtra("last_msg_s", last_msg_s);

        setResult(RESULT_OK, intent);
      finish();
    }
    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     * */
    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");

        Log.e(TAG, " PUSHHHHHHHHHHHH");

        if(this.chatRoomId.equals(chatRoomId))
        {

            if (message != null && chatRoomId != null) {
                last_msg = message.getMessage();
                last_msg_t = message.getCreatedAt();
                last_msg_s = message.getId();
                messageArrayList.add(message);
                mAdapter.notifyDataSetChanged();
                if (mAdapter.getItemCount() > 1) {
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                }
            }

        }
    }

    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     * */
    private void sendMessage() {
        final String message = this.inputMessage.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
         //   Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();

            tv.setText("Enter a message");
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastLayout);
            toast.show();
            return;
        }



        //new starts

        Message m = new Message();
        m.setId(MyApplication.getInstance().getPrefManager().getUser().getId());
        m.setMessage("...");
        m.setCreatedAt("");

        //   message.setUser(user);

        messageArrayList.add(m);

        mAdapter.notifyDataSetChanged();

        if (mAdapter.getItemCount() > 1) {
            // scrolling to bottom of the recycler view
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
        }

        //new ends

        this.inputMessage.setText("");

        if (installed.equals("0") && Integer.parseInt(msg_left)>0)
        {


            AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this, R.style.MyDialogTheme);
            builder.setTitle(title+" is not currently using Nest !");
            builder.setMessage("Find some tricky way and let them hear about Nest. \nElse use our Anonymous messaging feature to send text message to "+title+"'s Phone.\n\n( SPAM messages are Strictly restricted,any Complaint from other side will result in Legal Actions against the Sender. )\n\nCredits " +
                    "Left in your\nAccount is : "+msg_left+"\nPress TEXT to send this message to "+title+"'s phone. \nPressing Server will Send messages to our servers and "+title+" will not know about these Messages untill they Start Using Nest !\n");

            //String positiveText = getString();
            builder.setPositiveButton("TEXT",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic


                            String endPoint = EndPoints.CHAT_ROOM_MESSAGE.replace("_ID_", chatRoomId);

                            Log.e(TAG, "endpoint: " + endPoint);

                            //

                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    endPoint, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG, "response: " + response);

                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        // check for error
                                        if (obj.getBoolean("error") == false) {
                                            JSONObject commentObj = obj.getJSONObject("message");

                                            String msgId = commentObj.getString("message_id");
                                            String commentText = last_msg = commentObj.getString("message");
                                            String createdAt = last_msg_t = commentObj.getString("created_at");
                                            last_msg_s = MyApplication.getInstance().getPrefManager().getUser().getId();
                                            //   JSONObject userObj = obj.getJSONObject("user");
                                            //   String userId = userObj.getString("user_id");
                                            //   String userName = userObj.getString("name");
                                            //   User user = new User(userId, userName, null);

                                            Message message = new Message();

                                            message.setId(last_msg_s);
                                            message.setMsgId(msgId);
                                            message.setMessage(commentText);
                                            message.setCreatedAt(createdAt);
                                            //   message.setUser(user);

                                            MyApplication.getInstance().getPrefManager().storeMsgCount(commentObj.getString("chat_room_id"), Integer.toString(Integer.parseInt(MyApplication.getInstance().getPrefManager().getMsgCount(commentObj.getString("chat_room_id"))) + 1));


                                            messageArrayList.remove(messageArrayList.size() - 1);


                                            messageArrayList.add(message);

                                            mAdapter.notifyDataSetChanged();
                                            if (mAdapter.getItemCount() > 1) {
                                                // scrolling to bottom of the recycler view
                                                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                                            }


                                            tv.setText("Message Sent Successfully to Phone");
                                            Toast toast = new Toast(context);
                                            toast.setDuration(Toast.LENGTH_LONG);
                                            toast.setView(toastLayout);
                                            toast.show();

                                        } else {
                                            //  Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        Log.e(TAG, "json parsing error: " + e.getMessage());
                                        // Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    NetworkResponse networkResponse = error.networkResponse;
                                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                                    //   Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    inputMessage.setText(message);
                                }
                            }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("user_id", MyApplication.getInstance().getPrefManager().getUser().getId());
                                    params.put("message", message);
                                    params.put("rid", rid);
                                    params.put("s_name", MyApplication.getInstance().getPrefManager().getUser().getName());
                                    params.put("s_number", MyApplication.getInstance().getPrefManager().getUser().getEmail());
                                    params.put("chat_id", chatRoomId);
                                    params.put("msg_flag", "1");

                                    Log.e(TAG, "Params: " + params.toString());


                                    return params;
                                }

                                ;
                            };


                            // disabling retry policy so that it won't make
                            // multiple http calls
                            int socketTimeout = 0;
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                            strReq.setRetryPolicy(policy);

                            //Adding request to request queue
                            MyApplication.getInstance().addToRequestQueue(strReq);




/*
                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    EndPoints.MSG, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG, "response: " + response);

                    if (Boolean.getBoolean(response))
                    {
                        tv.setText("Message Sent Successfully to Phone");
                        Toast toast = new Toast(context);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(toastLayout);
                        toast.show();
                    }



                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    NetworkResponse networkResponse = error.networkResponse;
                                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                                    //   Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id", MyApplication.getInstance().getPrefManager().getUser().getId());
                                    params.put("msg", message);
                                    params.put("number", number);
                                    Log.e(TAG, "Params: " + params.toString());


                                    return params;
                                }

                                ;
                            };


                            // disabling retry policy so that it won't make
                            // multiple http calls
                            int socketTimeout = 0;
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                            strReq.setRetryPolicy(policy);

                            //Adding request to request queue
                            MyApplication.getInstance().addToRequestQueue(strReq);
*/


                        }
                    });

            // String negativeText = getString(android.R.string.cancel);
            builder.setNegativeButton("SERVER",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // negative button logic

                            String endPoint = EndPoints.CHAT_ROOM_MESSAGE.replace("_ID_", chatRoomId);

                            Log.e(TAG, "endpoint: " + endPoint);

                            //

                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    endPoint, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG, "response: " + response);

                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        // check for error
                                        if (obj.getBoolean("error") == false) {
                                            JSONObject commentObj = obj.getJSONObject("message");

                                            String msgId = commentObj.getString("message_id");
                                            String commentText = last_msg = commentObj.getString("message");
                                            String createdAt = last_msg_t = commentObj.getString("created_at");
                                            last_msg_s = MyApplication.getInstance().getPrefManager().getUser().getId();
                                            //   JSONObject userObj = obj.getJSONObject("user");
                                            //   String userId = userObj.getString("user_id");
                                            //   String userName = userObj.getString("name");
                                            //   User user = new User(userId, userName, null);

                                            Message message = new Message();

                                            message.setId(last_msg_s);
                                            message.setMsgId(msgId);
                                            message.setMessage(commentText);
                                            message.setCreatedAt(createdAt);
                                            //   message.setUser(user);

                                            MyApplication.getInstance().getPrefManager().storeMsgCount(commentObj.getString("chat_room_id"), Integer.toString(Integer.parseInt(MyApplication.getInstance().getPrefManager().getMsgCount(commentObj.getString("chat_room_id"))) + 1));


                                            messageArrayList.remove(messageArrayList.size() - 1);


                                            messageArrayList.add(message);

                                            mAdapter.notifyDataSetChanged();
                                            if (mAdapter.getItemCount() > 1) {
                                                // scrolling to bottom of the recycler view
                                                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                                            }

                                        } else {
                                            //  Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        Log.e(TAG, "json parsing error: " + e.getMessage());
                                        // Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    NetworkResponse networkResponse = error.networkResponse;
                                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                                    //   Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    inputMessage.setText(message);
                                }
                            }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("user_id", MyApplication.getInstance().getPrefManager().getUser().getId());
                                    params.put("message", message);
                                    params.put("rid", rid);
                                    params.put("s_name", MyApplication.getInstance().getPrefManager().getUser().getName());
                                    params.put("s_number", MyApplication.getInstance().getPrefManager().getUser().getEmail());
                                    params.put("chat_id", chatRoomId);
                                    params.put("msg_flag", "0");

                                    Log.e(TAG, "Params: " + params.toString());


                                    return params;
                                }

                                ;
                            };


                            // disabling retry policy so that it won't make
                            // multiple http calls
                            int socketTimeout = 0;
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                            strReq.setRetryPolicy(policy);

                            //Adding request to request queue
                            MyApplication.getInstance().addToRequestQueue(strReq);

                            dialog.cancel();
                        }
                    });
            builder.setNeutralButton("RE-TYPE",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // negative button logic

                            messageArrayList.remove(messageArrayList.size() - 1);

                            mAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });



            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            // display dialog
            dialog.show();




        }
        else
        {




            String endPoint = EndPoints.CHAT_ROOM_MESSAGE.replace("_ID_", chatRoomId);

            Log.e(TAG, "endpoint: " + endPoint);

            //

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    endPoint, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);

                        // check for error
                        if (obj.getBoolean("error") == false) {
                            JSONObject commentObj = obj.getJSONObject("message");

                            String msgId = commentObj.getString("message_id");
                            String commentText = last_msg = commentObj.getString("message");
                            String createdAt = last_msg_t = commentObj.getString("created_at");
                            last_msg_s = MyApplication.getInstance().getPrefManager().getUser().getId();
                            //   JSONObject userObj = obj.getJSONObject("user");
                            //   String userId = userObj.getString("user_id");
                            //   String userName = userObj.getString("name");
                            //   User user = new User(userId, userName, null);

                            Message message = new Message();

                            message.setId(last_msg_s);
                            message.setMsgId(msgId);
                            message.setMessage(commentText);
                            message.setCreatedAt(createdAt);
                            //   message.setUser(user);

                            MyApplication.getInstance().getPrefManager().storeMsgCount(commentObj.getString("chat_room_id"), Integer.toString(Integer.parseInt(MyApplication.getInstance().getPrefManager().getMsgCount(commentObj.getString("chat_room_id"))) + 1));


                            messageArrayList.remove(messageArrayList.size() - 1);


                            messageArrayList.add(message);

                            mAdapter.notifyDataSetChanged();
                            if (mAdapter.getItemCount() > 1) {
                                // scrolling to bottom of the recycler view
                                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                            }




                        } else {
                            //  Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        // Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    //   Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    inputMessage.setText(message);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", MyApplication.getInstance().getPrefManager().getUser().getId());
                    params.put("message", message);
                    params.put("rid", rid);
                    params.put("s_name", MyApplication.getInstance().getPrefManager().getUser().getName());
                    params.put("s_number", MyApplication.getInstance().getPrefManager().getUser().getEmail());
                    params.put("chat_id", chatRoomId);
                    params.put("msg_flag", "1");

                    Log.e(TAG, "Params: " + params.toString());


                    return params;
                }

                ;
            };


            // disabling retry policy so that it won't make
            // multiple http calls
            int socketTimeout = 0;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            strReq.setRetryPolicy(policy);

            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);


        }
    }


    /**
     * Fetching all the messages of a single chat room
     * */
    private void fetchChatThread() {

        String endPoint = EndPoints.CHAT_THREAD.replace("_ID_", chatRoomId);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONArray commentsObj = obj.getJSONArray("messages");
                        String commentId="",commentText="",createdAt="";
                        for (int i = 0; i < commentsObj.length(); i++) {
                            JSONObject commentObj = (JSONObject) commentsObj.get(i);

                            commentId= commentObj.getString("s_id");
                             commentText = commentObj.getString("message");
                             createdAt = commentObj.getString("created_at");

                           // JSONObject userObj = commentObj.getJSONObject("user");
                         //   String userId = userObj.getString("user_id");
                         //   String userName = userObj.getString("username");
                        //    User user = new User(userId, userName, null);

                            Message message = new Message();
                            message.setId(commentId);
                            message.setMessage(commentText);
                            message.setCreatedAt(createdAt);
                         //   message.setUser(user);

                            messageArrayList.add(message);
                        }

                        last_msg= commentText;
                        last_msg_t= createdAt;
                        last_msg_s=commentId;


                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                      //  Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                   // Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", MyApplication.getInstance().getPrefManager().getUser().getId());
                params.put("chat_id", chatRoomId);
                Log.e(TAG, "Params: " + params.toString());

                return params;
            };
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

}
