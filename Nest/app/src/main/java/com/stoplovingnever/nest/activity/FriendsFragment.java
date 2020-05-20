package com.stoplovingnever.nest.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.adapter.FriendsAdapter;
import com.stoplovingnever.nest.app.EndPoints;
import com.stoplovingnever.nest.app.MyApplication;
import com.stoplovingnever.nest.helper.HidingScrollListener;
import com.stoplovingnever.nest.helper.SimpleDividerItemDecoration;
import com.stoplovingnever.nest.model.FriendList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.stoplovingnever.nest.R.id.swipeRefreshLayout;
import static com.stoplovingnever.nest.activity.CrushFragment.getContactsDetails;


public class FriendsFragment extends Fragment{

    private static String TAG = MainActivity.class.getSimpleName();
    // private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //private static final int SECOND_ACTIVITY_RESULT_CODE = 153;
    public static ArrayList<FriendList> chatRoomArrayList;
    private static FriendsAdapter mAdapter;
    public static RecyclerView recyclerView;
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
    LinearLayoutManager layoutManager;
    static RelativeLayout content_main;
public static Boolean refresh;
    private Handler handler = new Handler();
    public static final String CONTACT_ID = ContactsContract.Contacts._ID;
    public static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

    public static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    public static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    SwipeRefreshLayout mSwipeRefreshLayout;
    static TextView tv;
    static View toastLayout;
    AsyncTaskRunner runner;
    static  ArrayList<String> numbers;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(
                R.layout.content_friend, container, false);


        recyclerView = (RecyclerView)rv.findViewById(R.id.recycler_view);

       // new_crush=(FloatingActionButton)rv.findViewById(R.id.crush);



/*
        toastLayout_crush = inflater.inflate(R.layout.app_bar_main, (ViewGroup) rv.findViewById(R.id.crush));
        new_crush=(FloatingActionButton)toastLayout_crush.findViewById(R.id.crush);

*/

        emptyElement=(TextView)rv.findViewById(R.id.emptyElement);
        content_main=(RelativeLayout)rv.findViewById(R.id.content_main);
        context=getActivity();
        //  in=1;
        /**
         * Broadcast receiver calls in two scenarios
         * 1. nest registration is completed
         * 2. when new push notification is received
         * */

        mSwipeRefreshLayout = (SwipeRefreshLayout) rv.findViewById(swipeRefreshLayout);

        //LayoutInflater inflater = getLayoutInflater();
        toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) rv.findViewById(R.id.custom_toast_layout));
        tv=(TextView)toastLayout.findViewById(R.id.custom_toast_message);

        chatRoomArrayList = new ArrayList<>();
        mAdapter = new FriendsAdapter(context, chatRoomArrayList);
         layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                context.getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new FriendsAdapter.RecyclerTouchListener(context.getApplicationContext(), recyclerView, new FriendsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity


                FriendList chatRoom = chatRoomArrayList.get(position);
                TextView name = (TextView) view.findViewById(R.id.name);
                Intent intent = new Intent(context, ProfileScreenXMLUIDesign.class);

                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("crush", chatRoom.getCrush());
                intent.putExtra("id", chatRoom.getId());

                intent.putExtra("like", chatRoom.getLike());
                intent.putExtra("number", chatRoom.getNumber());



                if(chatRoom.getBit()!=null) {
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    //Compress it before sending it to minimize the size and quality of bitmap.
                    // chatRoom.getBit().getBitmap().compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    chatRoom.getBit().compress(Bitmap.CompressFormat.PNG, 100, bStream);

                    byte[] byteArray = bStream.toByteArray();

                    intent.putExtra("image", byteArray);
                }
                intent.putExtra("flag", chatRoom.getFlag());


                // ChatRoomActivity.date="";
                getActivity().startActivityForResult(intent, 215);

                //  startActivity(intent);



            }

            @Override
            public void onLongClick(View view, int position) {
                //code for blocking the person
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




        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int ydy = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = dy - ydy;
                ydy = dy;
                boolean shouldRefresh = (layoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                        && (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) && offset > 30;
                if (shouldRefresh) {
                    //swipeRefreshLayout.setRefreshing(true);
                    //Refresh to load data here.
                    return;
                }
                boolean shouldPullUpRefresh = layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getChildCount() - 1
                        && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING && offset < -30;
                if (shouldPullUpRefresh) {
                    //swipeRefreshLayout.setRefreshing(true);
                    //refresh to load data here.
                    return;
                }
                mSwipeRefreshLayout.setRefreshing(false);
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

            fetchfriends();
        }
        else
        {
           // Toast.makeText(context, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            tv.setText("Check your Internet Connection");
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
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
/*
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });


*/



        mSwipeRefreshLayout.setOnRefreshListener(new
                                                        SwipeRefreshLayout.OnRefreshListener() {

                                                            @Override
                                                            public void onRefresh() {
                                                                // get the new data from you data source
                                                                // TODO : request data here
            /* our swipeRefreshLayout needs to be notified when the data is
               returned in order for it to stop the animation */
                                                                refresh=true;
                                                                if(isNetworkAvailable(context.getApplicationContext())){
                                                                fetchfriends();

                                                                    tv.setText("Refresh Done");
                                                                    Toast toast = new Toast(context);
                                                                    toast.setDuration(Toast.LENGTH_SHORT);
                                                                    toast.setView(toastLayout);
                                                                    toast.show();
                                                                    mSwipeRefreshLayout.setRefreshing(false);
/*
                                                                    runner = new AsyncTaskRunner();
                                                                    runner.execute("");
                                                                    */
                                                                }
                                                                else {
                                                                    tv.setText("No Internet !");
                                                                    Toast toast = new Toast(context);
                                                                    toast.setDuration(Toast.LENGTH_SHORT);
                                                                    toast.setView(toastLayout);
                                                                    toast.show();
                                                                    refresh=false;

                                                                }

                                                                handler.post(refreshing);
                                                            }
                                                        });
        // sets the colors used in the refresh animation
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));


//find all the people using the app
        return rv;

    }



    private final Runnable refreshing = new Runnable(){
        public void run(){
            try {
            /* TODO : isRefreshing should be attached to your data request status */
                if(refresh){
                    // re run the verification after 1 second
                    handler.postDelayed(this, 1000);
                }else{
                    // stop the animation after the data is fully loaded
                    mSwipeRefreshLayout.setRefreshing(false);
                    // TODO : update your list with the new data
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



    void refreshItems() {





        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                if(isNetworkAvailable(context.getApplicationContext())){


                    runner = new AsyncTaskRunner();
                    runner.execute("");
                }



            }
        }, 5000);



        /*
        LoginActivity.fetchfriends_list();
        fetchfriends();

        tv.setText("Refresh Done");
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastLayout);
        toast.show();
        mSwipeRefreshLayout.setRefreshing(false);
*/
        //onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void hideViews() {
        //MainActivity.appBarLayout.animate().translationY(-MainActivity.toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        MainActivity.toolbar.animate().translationY(-MainActivity.toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        //  MainActivity.tabLayout.animate().translationY(-MainActivity.toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) MainActivity.new_crush.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;

        MainActivity.new_crush.animate().translationY(MainActivity.new_crush.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        // MainActivity.appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

        MainActivity.toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        MainActivity.tabLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

        MainActivity.new_crush.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
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


     public static void fetchfriends() {


                if (numbers!=null)
                {
                    numbers.clear();

                }

             numbers = MyApplication.getInstance().getPrefManager().getfriends();

             Log.e(TAG, "Called chatrooms ");


    if (numbers.size()!=0) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CHAT_FRIENDS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "friends response here: " + response);
                  /*
                    content_main.setBackgroundResource(NULL);
                    emptyElement.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    */

                chatRoomArrayList.clear();
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // ChatRoomsAdapter.i=1;

                        JSONArray chatRoomsArray = obj.getJSONArray("friend_no");
                        //chatRoomArrayList=null;

                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                            FriendList fl = new FriendList();
                            fl.setName(chatRoomsObj.getString("name"));
                            fl.setCrush(chatRoomsObj.getString("crush"));
                            fl.setLike(chatRoomsObj.getString("like"));
                            fl.setNumber(chatRoomsObj.getString("number"));
                            fl.setId(chatRoomsObj.getString("id"));

                            fl.setI(((fl.getName()).substring(0, 1)).toUpperCase());

                            //Log.e(TAG, "bitmap 1.1");

                            //Log.e(TAG, "S "+chatRoom.getS_name()+ " R "+chatRoom.getR_name()+ " N "+no);
                            bit = null;
                            bit = getContactsDetails(context.getApplicationContext(), chatRoomsObj.getString("number"));
                            // Log.e(TAG, "bitmap "+chatRoomsObj.getString("number"));


                            if (bit != null) {
                                Log.e(TAG, "bitmap 1.1.1");

                                //cr.setBit(new BitmapDrawable(getApplicationContext().getResources(), (bit)));
                                fl.setBit(bit);
                                fl.setFlag(1);

                            }
                            //code to random color
                            else {
                                    /*
                                    bmp=Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888);

                                    canvas=new Canvas(bmp);
                                    canvas.drawColor(MaterialColorPalette.getRandomColor("500"));
                                    fl.setBit(bmp);
                                    */
                                fl.setFlag(0);

                                //here


                            }


                            chatRoomArrayList.add(fl);


                        }//for loop ends here

    /*
                            Collections.sort(chatRoomArrayList,new Comparator<FriendList>() {
                                @Override
                                public int compare(FriendList a, FriendList b) {
                                    return b.getCrush().compareTo(a.getCrush());
                                }
                            });
                            */
    /*
                            Collections.sort(chatRoomArrayList, new Comparator<FriendList>(){
                                public int compare(FriendList s1, FriendList s2) {
                                    return s1.getCrush().compareToIgnoreCase(s2.getCrush());
                                }
                            });
    */


                        if (chatRoomArrayList.size() == 0) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                            builder.setTitle("SHARE LOVE");
                            builder.setMessage("None of your Friends have started using NEST !\nBe the first one to Invite them to the Beautiful world of LOVE !\nShare the following message to all Groups your Crush belongs to \nthis will make them know about Nest and download it,\nand let the birds of Nest fly !\n\n" +
                                    MainActivity.text + "\n"
                            );

                            //String positiveText = getString();
                            builder.setPositiveButton("SHARE",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // positive button logic


                                            PackageManager pm = context.getPackageManager();
                                            try {

                                                Intent waIntent = new Intent(Intent.ACTION_SEND);
                                                waIntent.setType("text/plain");


                                                PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                                                //Check if package exists or not. If not then code
                                                //in catch block will be called
                                                waIntent.setPackage("com.whatsapp");

                                                waIntent.putExtra(Intent.EXTRA_TEXT, MainActivity.text);
                                                MainActivity.mainactivity.startActivity(Intent.createChooser(waIntent, "Share with"));

                                            } catch (PackageManager.NameNotFoundException e) {

                                                tv.setText("WhatsApp not Installed !");
                                                Toast toast = new Toast(context);
                                                toast.setDuration(Toast.LENGTH_SHORT);
                                                toast.setView(toastLayout);
                                                toast.show();
                                            }
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

                        tv.setText("Finished Refreshing !");
                        Toast toast = new Toast(context);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(toastLayout);
                        toast.show();
                    } else {
                        // error in fetching chat rooms
                        //  Toast.makeText(context.getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    // Toast.makeText(context.getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


                mAdapter.notifyDataSetChanged();

                // subscribing to all chat room topics
                // subscribeToAllTopics();

                refresh = false;
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


                JSONArray list = new JSONArray();

                for (int i = 0; i < numbers.size(); i++) {


                    list.put(numbers.get(i));

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


                Log.e(TAG, "paramss: " + params.toString());
                return params;
            }
        };


        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);

    }
    else
    {

/*
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle("SHARE LOVEE");
        builder.setMessage("None of your Friends have started using NEST\nBe the first one to Invite them to the Beautiful world of LOVE !\nShare the following message to all Groups your Crush belongs to \nthis will make them know about Nest and download it,\nand let the birds of Nest fly !\n\n" +
                MainActivity.text + "\n"
        );

        //String positiveText = getString();
        builder.setPositiveButton("SHARE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic


                        PackageManager pm = context.getPackageManager();
                        try {

                            Intent waIntent = new Intent(Intent.ACTION_SEND);
                            waIntent.setType("text/plain");


                            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                            //Check if package exists or not. If not then code
                            //in catch block will be called
                            waIntent.setPackage("com.whatsapp");

                            waIntent.putExtra(Intent.EXTRA_TEXT, MainActivity.text);
                            MainActivity.mainactivity.startActivity(Intent.createChooser(waIntent, "Share with"));

                        } catch (PackageManager.NameNotFoundException e) {

                            tv.setText("WhatsApp not Installed !");
                            Toast toast = new Toast(context);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(toastLayout);
                            toast.show();
                        }
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
*/

    }

    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        //   ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            //   publishProgress("Friends"); // Calls onProgressUpdate()

         //   MainActivity.fetchfriends_list();
            fetchfriends();

            Log.e("Main", "fetchfriends_list complete" );


            resp="1";
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
           // if(runner != null && runner .getStatus().equals(AsyncTask.Status.FINISHED))
            {

               // Log.e(TAG, "Runner Finished !");

               // fetchfriends();

                // progressDialog.dismiss();

                tv.setText("Refresh Done");
                Toast toast = new Toast(context);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(toastLayout);
                toast.show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
            Log.e(TAG, "Runner Status : " + runner.getStatus());


        }


/*
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context,
                    "Server Busy due to Overload !",
                    "Your Request is on the Way...");
        }


        @Override
        protected void onProgressUpdate(String... text) {
           // finalResult.setText(text[0]);

        }
*/
    }





}

