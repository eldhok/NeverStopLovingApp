package com.stoplovingnever.nest.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.adapter.ChatRoomsAdapter;
import com.stoplovingnever.nest.app.EndPoints;
import com.stoplovingnever.nest.app.MyApplication;
import com.stoplovingnever.nest.helper.HidingScrollListener;
import com.stoplovingnever.nest.helper.SimpleDividerItemDecoration;
import com.stoplovingnever.nest.model.ChatRoom;
import com.stoplovingnever.nest.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.stoplovingnever.nest.activity.MainActivity.in;


public class CrushFragment extends Fragment {
    public static final int AUTH_REQUEST = 1001;

    private static String TAG = MainActivity.class.getSimpleName();
   // private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //private static final int SECOND_ACTIVITY_RESULT_CODE = 153;

    public static ArrayList<ChatRoom> chatRoomArrayList;
    private static ChatRoomsAdapter mAdapter;
    private static RecyclerView recyclerView;
    static TextView emptyElement;
    // private static final int RESULT_PICK_CONTACT = 95045;
    static String phoneNo = null ;
    static String name = null;
    static String country_code=null;
   // static FloatingActionButton new_crush;
    static Bitmap bit,bmp;
    static Canvas canvas;
    private static Context context = null;
    //GlideDrawableImageViewTarget imageViewTarget;

    static RelativeLayout content_main;

    //View toastLayout_crush;
    static TextView tv;
    static View toastLayout;
    ChatRoom chatRoom;
    String chat_id;

    private Paint p = new Paint();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(
                R.layout.content_main, container, false);

        Log.e(TAG, "Called onCreateView ");

        recyclerView = (RecyclerView)rv.findViewById(R.id.recycler_view);



/*
        toastLayout_crush = inflater.inflate(R.layout.app_bar_main, (ViewGroup) rv.findViewById(R.id.abl));
        new_crush=(FloatingActionButton)toastLayout_crush.findViewById(R.id.crush);
        new_crush.setVisibility(View.GONE);
*/
        //new_crush=(FloatingActionButton)rv.findViewById(R.id.crush);



        emptyElement=(TextView)rv.findViewById(R.id.emptyElement);
        content_main=(RelativeLayout)rv.findViewById(R.id.content_main);
        context=getActivity();
      //  in=1;
        /**
         * Broadcast receiver calls in two scenarios
         * 1. nest registration is completed
         * 2. when new push notification is received
         * */


        toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) rv.findViewById(R.id.custom_toast_layout));
        tv=(TextView)toastLayout.findViewById(R.id.custom_toast_message);

        chatRoomArrayList = new ArrayList<>();
        mAdapter = new ChatRoomsAdapter(context, chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                context.getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(context.getApplicationContext(), recyclerView, new ChatRoomsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                ChatRoom chatRoom = chatRoomArrayList.get(position);
                TextView name = (TextView) view.findViewById(R.id.name);
                Intent intent = new Intent(context, ChatRoomActivity.class);

                Log.e(TAG, "count 3 " + chatRoom.getUnreadCount());
                Log.e(TAG, "count 4 " + Integer.toString(Integer.parseInt(MyApplication.getInstance().getPrefManager().getMsgCount(chatRoom.getChat_id()))+chatRoom.getUnreadCount()));

                MyApplication.getInstance().getPrefManager().storeMsgCount(chatRoom.getChat_id(),Integer.toString(Integer.parseInt(MyApplication.getInstance().getPrefManager().getMsgCount(chatRoom.getChat_id()))+chatRoom.getUnreadCount()));
                chatRoomArrayList.get(position).setUnreadCount(0);

                if (chatRoom.getS_id() == MyApplication.getInstance().getPrefManager().getUser().getId())
                {
                    intent.putExtra("rid", chatRoom.getR_id());
                }
                else {
                    intent.putExtra("rid", chatRoom.getS_id());
                }
                intent.putExtra("chat_room_id", chatRoom.getChat_id());

                if(chatRoom.getS_id().equals( MyApplication.getInstance().getPrefManager().getUser().getId())) {
                    intent.putExtra("name", name.getText());
                    intent.putExtra("r_id", chatRoom.getR_id());
                }
                else
                {
                    intent.putExtra("name", name.getText());
                    intent.putExtra("r_id", chatRoom.getS_id());
                }

                if(chatRoom.getBit()!=null) {
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    //Compress it before sending it to minimize the size and quality of bitmap.
                    // chatRoom.getBit().getBitmap().compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    chatRoom.getBit().compress(Bitmap.CompressFormat.PNG, 100, bStream);

                    byte[] byteArray = bStream.toByteArray();

                    intent.putExtra("image", byteArray);
                }

                intent.putExtra("number", chatRoom.getNumber());

                intent.putExtra("flag", chatRoom.getFlag());
                intent.putExtra("inst", Integer.toString(chatRoom.getInst()));
                intent.putExtra("msg_left", MyApplication.getInstance().getPrefManager().getMsgLeft());
                intent.putExtra("rechrg", MyApplication.getInstance().getPrefManager().getRechrge());



               // Log.e(TAG, "rechrg 1 "+chatRoom.getRechrg());
              //  Log.e(TAG, "msg_left 1 "+chatRoom.getMsg_left());
                Log.e(TAG, "inst 1 "+chatRoom.getInst());

//else
                {
                    //send the random color obtained
                    //   intent.putExtra("color", chatRoom.getColor());
                    //  Log.e(TAG, "color 1"+Integer.toString(chatRoom.getColor()));



                }
                // ChatRoomActivity.date="";
                getActivity().startActivityForResult(intent, 153);

                //  startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                //code for blocking the person




/*
                 chatRoom = chatRoomArrayList.get(position);
                chat_id=chatRoom.getChat_id();





                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //builder.setMessage("Are you sure you want to block the person ?")
                builder.setMessage(Html.fromHtml(" "+"<font color='#E91E63'>Are you sure you want to block "+chatRoom.getName()+" ?</font>"))
                        .setCancelable(false)
                        //.setPositiveButton(Html.fromHtml("YES"), new DialogInterface.OnClickListener() {
                            .setPositiveButton(Html.fromHtml(" "+"<font color='#E91E63'> YES </font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if(isNetworkAvailable(context.getApplicationContext()))
                                {
                                    block();
                                    //crush_mesg(5);
                                    //Toast.makeText(MainActivity.this, "You has blocked this person", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    //Toast.makeText(MainActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                    tv.setText("Check your Internet Connection");
                                    Toast toast = new Toast(context);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(toastLayout);
                                    toast.show();
                                }

                            }
                        })
                        //.setNegativeButton(Html.fromHtml("NO"), new DialogInterface.OnClickListener() {
                            .setNegativeButton(Html.fromHtml(" "+"<font color='#E91E63'> NO </font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


*/



            }
        }));
        recyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }
            @Override
            public void onShow() {
                showViews();
            }
        });

        /**
         * Always check for google play services availability before
         * proceeding further with GCM
         * */
        // if (checkPlayServices()) {
        // registerGCM();

        if (isNetworkAvailable(context))
        {

            fetchChatRooms();
        }
        else
        {
       // Toast.makeText(context, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            tv.setText("Check your Internet Connection");
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastLayout);
            toast.show();
        }

          /*
            emptyElement.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Log.e(TAG, "got inside 1");
            fetchChatRooms();
            Log.e(TAG, "got inside 2");
            if (chatRoomArrayList.isEmpty())
            {
                emptyElement.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Log.e(TAG, "got inside 3");
            }
            */
        // }



        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT){

                    chatRoom = chatRoomArrayList.get(position);

                    chat_id=chatRoom.getChat_id();




                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                    builder.setTitle("Do you want to Block "+chatRoom.getName()+" ?!");
                    builder.setMessage("We are Really Sorry for any trouble caused by "+chatRoom.getName()+".\nIf You want to Continue Blocking "+chatRoom.getName()+", \nPlease press BLOCK ");

                    //String positiveText = getString();
                    builder.setPositiveButton("BLOCK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic
                                    if(isNetworkAvailable(context.getApplicationContext()))
                                    {
                                        block();
                                        //crush_mesg(5);
                                        //Toast.makeText(MainActivity.this, "You has blocked this person", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        //Toast.makeText(MainActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                        tv.setText("Check your Internet Connection");
                                        Toast toast = new Toast(context);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(toastLayout);
                                        toast.show();
                                    }
                                }
                            });

                   // String negativeText = getString(android.R.string.cancel);
                    builder.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // negative button logic
                                    mAdapter.notifyDataSetChanged();

                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    // display dialog
                    dialog.show();

                  /*

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    //builder.setMessage("Are you sure you want to block the person ?")
                    builder.setMessage(Html.fromHtml(" "+"<font color='#E91E63'>Are you sure you want to block "+chatRoom.getName()+" ?</font>"))
                            .setCancelable(false)
                            //.setPositiveButton(Html.fromHtml("YES"), new DialogInterface.OnClickListener() {
                            .setPositiveButton(Html.fromHtml(" "+"<font color='#E91E63'> YES </font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if(isNetworkAvailable(context.getApplicationContext()))
                                    {
                                        block();
                                        //crush_mesg(5);
                                        //Toast.makeText(MainActivity.this, "You has blocked this person", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        //Toast.makeText(MainActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                        tv.setText("Check your Internet Connection");
                                        Toast toast = new Toast(context);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(toastLayout);
                                        toast.show();
                                    }

                                }
                            })
                            //.setNegativeButton(Html.fromHtml("NO"), new DialogInterface.OnClickListener() {
                            .setNegativeButton(Html.fromHtml(" "+"<font color='#E91E63'> NO </font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    mAdapter.notifyDataSetChanged();

                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

*/

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;

                    //Paint p = new Paint();

                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if (dX > 0) {
            /* Set your color for positive displacement */

                        p.setColor(getResources().getColor(R.color.colorAccent));

                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);

                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_block_white);

                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
/*

                        //seperation

                        p.setColor(getResources().getColor(R.color.colorAccent));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.block_user);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
*/

                    }
                    /*
                    else {

                        p.setColor(getResources().getColor(R.color.colorAccent));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.block_user);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);

                        //seperation

                        p.setColor(getResources().getColor(R.color.colorAccent));

                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_block_white);

                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
*/
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);






        return rv;

    }

public void block()
{

    StringRequest strReq = new StringRequest(Request.Method.POST,
            EndPoints.BLOCK, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
             Log.e(TAG, "response: block " + response);

            try {
                JSONObject obj = new JSONObject(response);

                // check for error flag
                if (obj.getBoolean("error") == false) {
                    // user successfully logged in
                    tv.setText("You has blocked this person");
                    Toast toast = new Toast(context);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(toastLayout);
                    toast.show();


                    for (ChatRoom cr : chatRoomArrayList) {
                        if (cr.getChat_id().equals(chat_id)) {
                            int index = chatRoomArrayList.indexOf(cr);
                            chatRoomArrayList.remove(index);
                            break;
                        }
                    }
                    mAdapter.notifyDataSetChanged();




                } else {
                    // login error - simply toast the message
                    // Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    tv.setText("Sorry, Please try again !");
                    Toast toast = new Toast(context);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(toastLayout);
                    toast.show();
                }

            } catch (JSONException e) {
                //Log.e(TAG, "json parsing error: " + e.getMessage());
                //Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


            // call retrieve contacts here

        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            NetworkResponse networkResponse = error.networkResponse;
            // Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
            // Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }) {

        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<>();


          //  params.put("s_id", MyApplication.getInstance().getPrefManager().getUser().getId());

            params.put("chat_id", chat_id);


            // Log.e(TAG, "params: " + params.toString());
            return params;
        }
    };

    //Adding request to request queue
    MyApplication.getInstance().addToRequestQueue(strReq);




}

    private void hideViews() {
        //MainActivity.appBarLayout.animate().translationY(-MainActivity.toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        MainActivity.toolbar.animate().translationY(-MainActivity.toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
      //  MainActivity.tabLayout.animate().translationY(-MainActivity.toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));


        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) MainActivity.new_crush.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;

         MainActivity.new_crush.animate().translationY(MainActivity.new_crush.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
        //new_crush.setVisibility(View.GONE);

    }

    private void showViews() {
       // MainActivity.appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

        MainActivity.toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        MainActivity.tabLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

        MainActivity.new_crush.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    //    new_crush.setVisibility(View.VISIBLE);

    }


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

    /**
     * Updates the chat list unread count and the last message
     */
    public void updateRowcrush(String chatRoomId, Message message) {

        ChatRoom cr=new ChatRoom();
        cr.setLast_msg(message.getMessage());
        // cr.setUnreadCount(cr.getUnreadCount() + 1);
        cr.setInst(1);
        cr.setName("Crush");
        cr.setChat_id(chatRoomId);
        cr.setI(Integer.toString(in));

        MainActivity.in++;
/*
        bmp=Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888);

        canvas=new Canvas(bmp);
        canvas.drawColor(MaterialColorPalette.getRandomColor("500"));
        cr.setBit(bmp);
        */
        cr.setFlag(0);


        chatRoomArrayList.add(0, cr);


        mAdapter.notifyDataSetChanged();
    }
    public static void updateRow(String chatRoomId, Message message) {



        for (ChatRoom cr : chatRoomArrayList) {
            if (cr.getChat_id().equals(chatRoomId)) {
                int index = chatRoomArrayList.indexOf(cr);
                cr.setLast_msg(message.getMessage());
                // cr.setUnreadCount(cr.getUnreadCount() + 1);
                chatRoomArrayList.remove(index);
                chatRoomArrayList.add(0, cr);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * fetching the chat rooms by making http call
     */
    public static void fetchChatRooms() {

        Log.e(TAG, "Called chatrooms ");




        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CHAT_ROOMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
              /*
                content_main.setBackgroundResource(NULL);
                emptyElement.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                */
String msg_count;
                chatRoomArrayList.clear();
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // ChatRoomsAdapter.i=1;

                        JSONArray chatRoomsArray = obj.getJSONArray("chat");
                        //chatRoomArrayList=null;

                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                            ChatRoom cr = new ChatRoom();
                            cr.setS_name(chatRoomsObj.getString("s_name"));
                            cr.setR_name(chatRoomsObj.getString("r_name"));
                            cr.setS_id(chatRoomsObj.getString("s_id"));
                            cr.setR_id(chatRoomsObj.getString("r_id"));
                            cr.setStatus(chatRoomsObj.getString("status"));
                            cr.setChat_id(chatRoomsObj.getString("chat_id"));
                            cr.setLast_msg(chatRoomsObj.getString("last_msg"));
                            cr.setTimestamp(chatRoomsObj.getString("created_at"));
                            cr.setLast_msg_s_id(chatRoomsObj.getString("last_msg_sender_id"));

                          //  cr.setRechrg(chatRoomsObj.getString("rechrg"));
                          //  cr.setMsg_left(chatRoomsObj.getString("msg_left"));
                            //cr.setUnreadCount(0);
                            cr.setNumber(chatRoomsObj.getString("number"));
                            msg_count=chatRoomsObj.getString("msg_count");

                            Log.e(TAG, "count 1 " + msg_count);

                            cr.setUnreadCount(Integer.parseInt(msg_count)-Integer.parseInt(MyApplication.getInstance().getPrefManager().getMsgCount(cr.getChat_id())));
                            cr.setInst(Integer.parseInt(chatRoomsObj.getString("installed")));
                            //MyApplication.getInstance().getPrefManager().storeMsgCount(cr.getChat_id(),msg_count);
                            Log.e(TAG, "count 2 " + cr.getUnreadCount());

                            if(cr.getS_id().equals(MyApplication.getInstance().getPrefManager().getUser().getId()) || cr.getSatus().equals("2")) {

                                cr.setName(cr.getR_name());
                                // holder.myImageViewText.setText(Character.toUpperCase((chatRoom.getR_name()).substring(0,1)));
                                cr.setI(((cr.getR_name()).substring(0,1)).toUpperCase());



                            }
                            else
                            {
                                //holder.name.setText("Crush"+Integer.toString(i));
                                //holder.name.setText("Crush"+Integer.toString(i));
                                cr.setName("Crush");

                                cr.setI(Integer.toString(in));

                                in++;

                            }
                            if(cr.getSatus().equals("2"))
                            {

                                if(cr.getS_id().equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {

                                    cr.setName(cr.getR_name());
                                    //   holder.myImageViewText.setText(Character.toUpperCase((chatRoom.getR_name()).charAt(0)));
                                    cr.setI(((cr.getR_name()).substring(0,1)).toUpperCase());


                                }
                                else
                                {
                                    cr.setName(cr.getS_name());
                                    //    holder.myImageViewText.setText(Character.toUpperCase((chatRoom.getS_name()).charAt(0)));
                                    cr.setI(((cr.getS_name()).substring(0,1)).toUpperCase());

                                }
                            }






                            // cr.setColor(MaterialColorPalette.getRandomColor("400"));
                            // Log.e(TAG, "color 101"+Integer.toString(cr.getColor()));


                            if(chatRoomsObj.getString("number").equals("0")){
                                //edit for not doing random color
                                /*
                                bmp=Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888);

                                canvas=new Canvas(bmp);
                                canvas.drawColor(MaterialColorPalette.getRandomColor("500"));
                                cr.setBit(bmp);
                                */
                                cr.setFlag(0);
                                //here

                            }
                            else
                            {
                                Log.e(TAG, "bitmap 1.1");

                                //Log.e(TAG, "S "+chatRoom.getS_name()+ " R "+chatRoom.getR_name()+ " N "+no);
                                bit=null;
                                bit=getContactsDetails(context.getApplicationContext(),chatRoomsObj.getString("number"));
                                Log.e(TAG, "bitmap "+chatRoomsObj.getString("number"));


                                if(bit!=null) {
                                    Log.e(TAG, "bitmap 1.1.1");

                                    //cr.setBit(new BitmapDrawable(getApplicationContext().getResources(), (bit)));
                                    cr.setBit(bit);
                                    cr.setFlag(1);

                                }
                                else{
                                    //edit for not doing random color
                                    /*
                                    bmp=Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888);

                                    canvas=new Canvas(bmp);
                                    canvas.drawColor(MaterialColorPalette.getRandomColor("500"));
                                    cr.setBit(bmp);
                                    */
                                    cr.setFlag(0);

                                    //here


                                }
                            }


                            // Log.e(TAG, "S "+chatRoom.getS_name()+ " R "+chatRoom.getR_name()+ " N "+no);


/*

                            if(cr.getSatus().equals("2"))
                            {
                                Log.e(TAG, "bitmap 1.2");

                                int f=0;
                                for (ChatRoom crr : chatRoomArrayList) {
                                    if (crr.getChat_id().equals(cr.getChat_id())) {
                                        int index = chatRoomArrayList.indexOf(crr);


                                        f=1;
                                        crr.setS_name(cr.getS_name());
                                        crr.setR_name(cr.getR_name());
                                        crr.setS_id(cr.getS_id());
                                        crr.setR_id(cr.getR_id());
                                        crr.setStatus(cr.getSatus());
                                        crr.setChat_id(cr.getChat_id());
                                        crr.setLast_msg(cr.getLast_msg());
                                        crr.setTimestamp(cr.getTimestamp());
                                        crr.setLast_msg_s_id(cr.getLast_msg_s_id());
                                        crr.setUnreadCount(0);
                                        crr.setNumber(cr.getNumber());
                                        crr.setBit(cr.getBit());
                                        crr.setColor(cr.getColor());
                                        crr.setName(cr.getName());
                                        crr.setI(cr.getI());
                                        //here
                                        Log.e(TAG, "bitmap 1.3");

                                        chatRoomArrayList.remove(index);
                                        chatRoomArrayList.add(index, crr);

                                        break;
                                    }
                                }
                                if(f==0)
                                {
                                    chatRoomArrayList.add(cr);
                                }
                            }
                            else
                            {
                                //chatRoomArrayList.add(cr);
                                chatRoomArrayList.add(cr);
                            }

*/

                            chatRoomArrayList.add(cr);


                        }
                        if(chatRoomsArray.length()==0)
                        {



                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                            builder.setTitle("ADD CRUSH");
                            builder.setMessage("You have no Crush yet .\nSave someone in your crush list and give them clues !\nText message will be sent to their phone anonymously ." +
                                            "\nConfuse them intelligently\nwith your clues .\nClick on the crush button, add number and get into the new\n" +
                                            "World of Love !\n"
                                    );

                            //String positiveText = getString();
                            builder.setPositiveButton("ADD",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // positive button logic




                                            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                                            MainActivity.mainactivity.startActivityForResult(contactPickerIntent, 1525);
                                        }
                                    });

                            builder.setNeutralButton("CANCEL",
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




                            /*
                            content_main.setBackgroundResource(R.drawable.background);
                            // imageViewTarget = new GlideDrawableImageViewTarget(emptyElement);
                            // Glide.with(MainActivity.this).load(R.drawable.love).into(imageViewTarget);
                            emptyElement.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            */
                        }
                     /*
                        else
                        {
                            content_main.setBackgroundResource(NULL);
                            emptyElement.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        */


                    } else {
                        // error in fetching chat rooms
                       // Toast.makeText(context.getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                        tv.setText("Sorry, Please try again !");
                        Toast toast = new Toast(context);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(toastLayout);
                        toast.show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                  //  Toast.makeText(context.getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                mAdapter.notifyDataSetChanged();

                // subscribing to all chat room topics
                // subscribeToAllTopics();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
              //  Toast.makeText(context.getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",MyApplication.getInstance().getPrefManager().getUser().getId());


               // Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };


        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);



    }
/*
    // subscribing to global topic
    private void subscribeToGlobalTopic() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        startService(intent);
    }
    */


    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = Color.RED;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public static Bitmap getContactsDetails(Context context,String address) {
        //Bitmap bp = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
        Bitmap bp=null;




        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(address));

        // querying contact data store
        Cursor phones = context.getContentResolver().query(contactUri, null, null, null, null);


        while (phones.moveToNext()) {
            String image_uri = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            if (image_uri != null) {

                try {
                    bp = MediaStore.Images.Media
                            .getBitmap(context.getContentResolver(),
                                    Uri.parse(image_uri));

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        phones.close();
        return   bp;

    }

    public static void onback(Intent data)
    {


        for (ChatRoom cr : chatRoomArrayList) {
            if (cr.getChat_id().equals(data.getStringExtra("chatRoomId"))) {
                int index = chatRoomArrayList.indexOf(cr);
                cr.setLast_msg(data.getStringExtra("last_msg"));
                //cr.setUnreadCount(cr.getUnreadCount() + 1);
                cr.setTimestamp(data.getStringExtra("last_msg_t"));
                cr.setUnreadCount(0);
                cr.setLast_msg_s_id(data.getStringExtra("last_msg_s"));
                chatRoomArrayList.remove(index);
                chatRoomArrayList.add(0, cr);
                break;
            }
        }
        Log.e(TAG, " inside onback");

        mAdapter.notifyDataSetChanged();



    }








    public static void contactPicked(Intent data) {
        Cursor cursor = null;
        try {

            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);


            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);


            if (phoneNo.length() == 10) {
                phoneNo = "+91" + phoneNo;
            }


            try {
                // phone must begin with '+'
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNo, "");
                int countryCode = numberProto.getCountryCode();
                long nationalNumber = numberProto.getNationalNumber();
                phoneNo = String.valueOf(nationalNumber);
                country_code = String.valueOf(countryCode);
                Log.i("code", "code " + countryCode);
                Log.i("code", "national number " + nationalNumber);
            } catch (NumberParseException e) {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }
            // Set the value to the textviews
            //  crush_name.setText(name);
            //  crush_no.setText(phoneNo);

            if (isNetworkAvailable(context)) {

                tv.setText("Crush Request Sending...");
                Toast toast = new Toast(context);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(toastLayout);
                toast.show();
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        EndPoints.CRUSH_REQ, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " + response);

                        // Bitmap bit=null,bmp;

                  /*
                    content_main.setBackgroundResource(NULL);
                    emptyElement.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    */

                        try {
                            JSONObject obj = new JSONObject(response);
                            in = 1;
                            // check for error flag
                            if (obj.getBoolean("error") == false) {
                                JSONArray chatRoomsArray = obj.getJSONArray("crush");
                                for (int i = 0; i < chatRoomsArray.length(); i++) {
                                    JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                                    ChatRoom cr = new ChatRoom();
                                    cr.setS_name(chatRoomsObj.getString("s_name"));
                                    cr.setR_name(chatRoomsObj.getString("r_name"));
                                    cr.setS_id(chatRoomsObj.getString("s_id"));
                                    cr.setR_id(chatRoomsObj.getString("r_id"));
                                    cr.setStatus(chatRoomsObj.getString("status"));
                                    cr.setChat_id(chatRoomsObj.getString("chat_id"));
                                    cr.setLast_msg(chatRoomsObj.getString("last_msg"));
                                    cr.setTimestamp(chatRoomsObj.getString("created_at"));
                                    cr.setLast_msg_s_id(chatRoomsObj.getString("last_msg_sender_id"));
                                    cr.setUnreadCount(0);

                                   // cr.setRechrg(chatRoomsObj.getString("rechrg"));
                                   // cr.setMsg_left(chatRoomsObj.getString("msg_left"));



                                    cr.setInst(Integer.parseInt(chatRoomsObj.getString("installed")));
                                    MyApplication.getInstance().getPrefManager().storeMsgCount(chatRoomsObj.getString("chat_id"),Integer.toString(Integer.parseInt(MyApplication.getInstance().getPrefManager().getMsgCount(chatRoomsObj.getString("chat_id")))+1));


                                    // cr.setColor(MaterialColorPalette.getRandomColor("400"));

                                    cr.setNumber(chatRoomsObj.getString("number"));


                                    if (cr.getS_id().equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {

                                        cr.setName(cr.getR_name());
                                        // holder.myImageViewText.setText(Character.toUpperCase((chatRoom.getR_name()).substring(0,1)));
                                        cr.setI(((cr.getR_name()).substring(0, 1)).toUpperCase());


                                    } else {
                                        //holder.name.setText("Crush"+Integer.toString(i));
                                        //holder.name.setText("Crush"+Integer.toString(i));
                                        cr.setName("Crush");

                                        cr.setI(Integer.toString(in));

                                        in++;

                                    }
                                    if (cr.getSatus().equals("2")) {

                                        if (cr.getS_id().equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {

                                            cr.setName(cr.getR_name());
                                            //   holder.myImageViewText.setText(Character.toUpperCase((chatRoom.getR_name()).charAt(0)));
                                            cr.setI(((cr.getR_name()).substring(0, 1)).toUpperCase());


                                        } else {
                                            cr.setName(cr.getS_name());
                                            //    holder.myImageViewText.setText(Character.toUpperCase((chatRoom.getS_name()).charAt(0)));
                                            cr.setI(((cr.getS_name()).substring(0, 1)).toUpperCase());

                                        }
                                    }


                                    if (chatRoomsObj.getString("number").equals("0")) {
                                        //edit for not doing random color
                                        /*
                                        bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                                        canvas = new Canvas(bmp);
                                        canvas.drawColor(MaterialColorPalette.getRandomColor("500"));                                   // cr.setBit(null);
                                        cr.setBit(bmp);
                                        */
                                        cr.setFlag(0);

                                    } else {
                                        Log.e(TAG, "bitmap 1");
                                        bit = null;

                                        bit = getContactsDetails(context.getApplicationContext(), chatRoomsObj.getString("number"));
                                        if (bit != null) {

                                            cr.setBit(bit);
                                            cr.setFlag(1);
                                            // cr.setBit(new BitmapDrawable(getApplicationContext().getResources(), getCircleBitmap(bit)));

                                        } else {
                                            //edit for not doing random color
                                            /*

                                            bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                                            canvas = new Canvas(bmp);
                                            canvas.drawColor(MaterialColorPalette.getRandomColor("500"));
                                            cr.setBit(bmp);
                                            */
                                            cr.setFlag(0);
                                        }

                                    }


                                    // Log.e(TAG, "S "+chatRoom.getS_name()+ " R "+chatRoom.getR_name()+ " N "+no);


                                    if (cr.getSatus().equals("2")) {
                                        int f = 0;
                                        for (ChatRoom crr : chatRoomArrayList) {
                                            if (crr.getChat_id().equals(cr.getChat_id())) {
                                                int index = chatRoomArrayList.indexOf(crr);


                                                f = 1;
                                                crr.setS_name(cr.getS_name());
                                                crr.setR_name(cr.getR_name());
                                                crr.setS_id(cr.getS_id());
                                                crr.setR_id(cr.getR_id());
                                                crr.setStatus(cr.getSatus());
                                                crr.setChat_id(cr.getChat_id());
                                                crr.setLast_msg(cr.getLast_msg());
                                                crr.setTimestamp(cr.getTimestamp());
                                                crr.setLast_msg_s_id(cr.getLast_msg_s_id());
                                                crr.setUnreadCount(0);
                                                cr.setNumber(cr.getNumber());
                                                crr.setBit(cr.getBit());
                                                crr.setFlag(cr.getFlag());
                                                crr.setInst(cr.getInst());
                                               // crr.setMsg_left(cr.getMsg_left());
                                               // crr.setRechrg(cr.getRechrg());
                                                //  crr.setColor(cr.getColor());
                                                Log.e(TAG, "bitmap 2");
                                                crr.setName(cr.getName());
                                                crr.setI(cr.getI());

                                                chatRoomArrayList.remove(index);
                                                chatRoomArrayList.add(index, crr);

                                                break;
                                            }
                                        }
                                        if (f == 0) {
                                            chatRoomArrayList.add(cr);
                                        }
                                    } else {
                                        //chatRoomArrayList.add(cr);
                                        chatRoomArrayList.add(0, cr);
                                    }


                                }

                                if (chatRoomsArray.length() != 0) {
/*
                                    content_main.setBackgroundResource(NULL);
                                    emptyElement.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
 */
                                    tv.setText("Crush Added to List");


                                } else {

                                   // Toast.makeText(context.getApplicationContext(), "Sorry Crush Cannot be Added !", Toast.LENGTH_LONG).show();

                               /*
                                content_main.setBackgroundResource(R.drawable.background);
                                emptyElement.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                */
                                    tv.setText("Sorry Crush Cannot be Added !");


                                }


                                Toast toast = new Toast(context);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(toastLayout);
                                toast.show();
                            } else {
                                // error in fetching chat rooms
                               // Toast.makeText(context.getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, "json parsing error: " + e.getMessage());
                           // Toast.makeText(context.getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        mAdapter.notifyDataSetChanged();

                        // subscribing to all chat room topics
                        // subscribeToAllTopics();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                       // Toast.makeText(context.getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", MyApplication.getInstance().getPrefManager().getUser().getId());
                        params.put("c_no", phoneNo);

                        params.put("c_name", name);
                        params.put("user_name", MyApplication.getInstance().getPrefManager().getUser().getName());
                        params.put("country_code", country_code);

                        Log.e(TAG, "params: " + params.toString());
                        return params;
                    }
                };


                //Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(strReq);
            }
            else
            {
                //Toast.makeText(context, "Check your Internet Connection", Toast.LENGTH_SHORT).show();

                tv.setText("Check your Internet Connection");
                Toast toast = new Toast(context);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(toastLayout);
                toast.show();
            }


        }catch(Exception e){
                e.printStackTrace();
            }


    }

/*
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

*/

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_logout:
                MyApplication.getInstance().logout();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

*/



}
