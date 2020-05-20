package com.stoplovingnever.nest.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.app.Config;
import com.stoplovingnever.nest.app.EndPoints;
import com.stoplovingnever.nest.app.MyApplication;
import com.stoplovingnever.nest.gcm.GcmIntentService;
import com.stoplovingnever.nest.gcm.NotificationUtils;
import com.stoplovingnever.nest.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.Telephony.Mms.Addr.CONTACT_ID;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{


    /*

    TO send text message without recorded

    SmsManager sms = SmsManager.getDefault();
sms.sendTextMessage(phoneNumber, null, message, null, null);

<uses-permission android:name="android.permission.SEND_SMS" />


TO delete msg in Kitkat


If msg is in out box

getContentResolver().delete(Uri.parse("content://sms/outbox"), "address = ? and body = ?", new String[] {etsendernumber.getText().toString(),etmessagebody.getText().toString()});

If message is in sent items.

getContentResolver().delete(Uri.parse("content://sms/sent"), "address = ? and body = ?", new String[] {etsendernumber.getText().toString(),etmessagebody.getText().toString()});
     */

    String user_valid;
    Boolean version_valid,forced_update;

    public static MainActivity mainactivity;
    LayoutInflater inflater;

    private String TAG = MainActivity.class.getSimpleName();
public static AppBarLayout appBarLayout;
    public static int in=1;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
public static TabLayout tabLayout;
    //GlideDrawableImageViewTarget imageViewTarget;
    TextView chats,user_name,like_details;
    NavigationView navigationView, navigationViewBottom;
    DrawerLayout drawer;
   public static Toolbar toolbar;
    static TextView tv;
    static View toastLayout;
static Context context;
   public static FloatingActionButton new_crush;
    View header;
    static ActionBarDrawerToggle toggle;
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    private InterstitialAd interstitial;
    final static String text = "I LOVE YOU! \uD83D\uDE0D❤\uD83D\uDC98\n" +
            "The most beatiful words one would have ever spelled.\n" +
            "A felling of heart which is hardly expressed.\n" +
            "Everyone at some point of their life have come across these words \n" +
            "murmoring in their heart,without having the courage to express it.\n" +
            "But from now expressing love is not that hard!\n" +
            "NEST❤ (NeverStopLoving) allows you to save your love as a secret!\uD83D\uDC7C\uD83C\uDFFB\n" +
            "Hint them,give them clues \uD83D\uDC8C and build their\n" +
            "curiosity.Make them wondering about you.\uD83D\uDC93And finally get matched when\n" +
            "your crush adds you too! \uD83D\uDC91\n" +
            "Give your love story a chance to happen.\n" +
            "Express your love with NEST   https://play.google.com/store/apps/details?id=com.stoplovingnever.nest\n" +
            "for android users.\n" +
            "Forward this message to all those people who you care for,as I did. \uD83D\uDCE2\n" +
            "Whatsapp is free don't dump this in your inbox,let this words of love \uD83D\uDC9E fly \n" +
            "all over the world.\uD83C\uDF0D";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Check for login session. If not logged in launch
         * login activity
         * */
        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            launchLoginActivity();
        }
        in=1;
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setupToolbar(R.id.toolbar,"Never Stop Loving");
      //  setupImage(R.id.toolbar);
        appBarLayout=(AppBarLayout)findViewById(R.id.appbar);
        new_crush=(FloatingActionButton)findViewById(R.id.crush);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
        header=navigationView.getHeaderView(0);
        user_name=(TextView)header.findViewById(R.id.user_name);
        user_name.setText(MyApplication.getInstance().getPrefManager().getUser().getName());

        like_details=(TextView)header.findViewById(R.id.like_details);

        // menu=navigationView.getMenu();
        //credit=(TextView)header.findViewById(R.id.nav_credit_left);
      //   credit=(ClipData.Item) menu.findItem(R.id.nav_credit_left);

        mainactivity=this;

        getMsgLeftHere();



        //credit.setText("Message Left "+MyApplication.getInstance().getPrefManager().getMsgLeft());

        //new change

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

         tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
       // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
     //   tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

       // tabLayout.setTabMode(TabLayout.MODE_FIXED);
         inflater=getLayoutInflater();
        toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.custom_toast_layout));
        tv=(TextView)toastLayout.findViewById(R.id.custom_toast_message);

        //new change ends
        context=getApplicationContext();
        chats =(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_chats));
       // initializeCountDrawer();
/*

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml(" "+"<font color='#FFFFFF'><i><strong> Never Stop Loving </strong></i></font>"));
        //toolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(toolbar);
*/


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // nest successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    // subscribeToGlobalTopic();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // nest registration id is stored in our server's MySQL
                    //  Log.e(TAG, "GCM registration id is sent to our server");

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };





//to give giff in imageview
/*
        imageViewTarget = new GlideDrawableImageViewTarget(new_crush);

        Glide.with(this).load(R.drawable.lovemixing).into(imageViewTarget);
*/

//upto here


        if (MyApplication.getInstance().getPrefManager().getContRe() == null) {

            fetchfriends_list();




            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            builder.setTitle("MESSAGES");
            builder.setMessage("Hi "+MyApplication.getInstance().getPrefManager().getUser().getName()+", you have 5 free messages left in your Account.\n" +
                    "Use these messages wisely to send Clues to your Crush's number Anonymously, thus building up their Curiosity\n\nAlso SHARE nest to all Whatsapp group your crush belongs to, this will" +
                    " make them hear about Nest !"
            );

            //String positiveText = getString();
            builder.setPositiveButton("GOT IT",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic
                            dialog.cancel();


                        }
                    });
            builder.setNeutralButton("SHARE",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // negative button logic
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

                            dialog.cancel();
                        }
                    });




            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            // display dialog
            dialog.show();


            MyApplication.getInstance().getPrefManager().storeContRe("1");
        }


        //user blocked due to bad activity
       user_valid();



        if(isNetworkAvailable(getApplicationContext())){

            interstitial = new InterstitialAd(this);

            interstitial.setAdUnitId("ca-app-pub-8792494833623334/4333507403");
            //nw ad id : ca-app-pub-8792494833623334/1114611803
// Create ad request

            AdRequest adRequest = new AdRequest.Builder()
//   .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();


            // Attempt loading ad for interstitial
            interstitial.loadAd(adRequest);


            interstitial.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded()
                {
                    //	Log.w("myApp","ad loaded");
                }
                @Override
                public void onAdClosed() {



                    moveTaskToBack(true);


                    // super.onAdClosed();
                }

            });


        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       // if (pendingIntroAnimation)
        {
           // pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }


    public static void startIntroAnimation() {
        new_crush.setTranslationY(2 * context.getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

       // float actionbarSize = 56 * context.getResources().getDisplayMetrics().density;

        float actionbarSize = 112 * context.getResources().getDisplayMetrics().density;
      //  toolbar.setTranslationY(-actionbarSize);
       // tabLayout.setTranslationY(-actionbarSize);
        appBarLayout.setTranslationY(-actionbarSize);
      //  drawer.setTranslationY(-actionbarSize);
        //getInboxMenuItem().getActionView().setTranslationY(-actionbarSize);
/*
        toolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
                */
        appBarLayout.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    public static void startContentAnimation() {
        new_crush.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
       // feedAdapter.updateItems(true);
    }

    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_CHATROOM) {
          /*
            Message message = (Message) intent.getSerializableExtra("message");
            String chatRoomId = intent.getStringExtra("chat_room_id");

            if (message != null && chatRoomId != null) {
                updateRow(chatRoomId, message);
            }
            */
        } else if (type == Config.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            // Message message = (Message) intent.getSerializableExtra("message");
            // Toast.makeText(getApplicationContext(), "New push: " + message.getMessage(), Toast.LENGTH_LONG).show();
            Message message = (Message) intent.getSerializableExtra("message");
            String chatRoomId = intent.getStringExtra("chat_room_id");
            String status=intent.getStringExtra("status");
            if(status.equals("3"))
            {
                if (message != null && chatRoomId != null)
                {
                    CrushFragment.updateRow(chatRoomId, message);
                }
            }
            else
            {

                if (message != null && chatRoomId != null)
                {
                    //    updateRowcrush(chatRoomId, message);
                    //Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    // getApplicationContext().startActivity(i);
                    Log.e(TAG, "before calling  chatrooms ");

                    CrushFragment.fetchChatRooms();

                    Log.e(TAG, "after calling  chatrooms ");

                }
            }
        }


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


                            if(num.size()==0)
                            {
                                Log.e("Main", "Equal to Zero ! ");

/*
                               FriendsFragment.content_main.setBackgroundResource(R.drawable.background);
                                    // imageViewTarget = new GlideDrawableImageViewTarget(emptyElement);
                                    // Glide.with(MainActivity.this).load(R.drawable.love).into(imageViewTarget);
                                FriendsFragment.emptyElement.setVisibility(View.VISIBLE);
                                FriendsFragment.recyclerView.setVisibility(View.GONE);
*/

                            }
                            else
                            {
                                Log.e("Main", "Not equal to Zero ! ");


                                FriendsFragment.fetchfriends();
/*
                                FriendsFragment.content_main.setBackgroundResource(NULL);
                                FriendsFragment.emptyElement.setVisibility(View.GONE);
                                FriendsFragment.recyclerView.setVisibility(View.VISIBLE);
                                */
                            }



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
                        Log.e("Main", "uniq number with code: " +  numbers.get(i));

                        Log.e("Main", "uniq number without code: " + String.valueOf(numberProto.getNationalNumber()));

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



    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new CrushFragment(), "Crush list");
        adapter.addFragment(new FriendsFragment(), "Friends");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {

            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(contactPickerIntent, 1525);
        }
        else if(id==R.id.nav_logout)
        {
            MyApplication.getInstance().logout();
        }
        else if(id==R.id.nav_add_credit)
        {
            //call funtion to add paytm credits to account
            //onStartTransaction();
            tv.setText("Coming Soon...");
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastLayout);
            toast.show();

        }
        else if (id==R.id.nav_refresh_contacts)
        {

            tv.setText("Contacts Refreshing...");
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastLayout);
            toast.show();

            fetchfriends_list();
        }
        else if (id==R.id.nav_share)
        {




            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
            builder.setTitle("Share Via Whatsapp");
            builder.setMessage("Share the following message to all Groups your Crush belongs to \nthis will make them know about Nest and download it,\nand let the birds of Nest fly !\n\n" +
                    text+"\n");

            //String positiveText = getString();
            builder.setPositiveButton("SHARE",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic

                            PackageManager pm=getPackageManager();
                            try {

                                Intent waIntent = new Intent(Intent.ACTION_SEND);
                                waIntent.setType("text/plain");


                                PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                                //Check if package exists or not. If not then code
                                //in catch block will be called
                                waIntent.setPackage("com.whatsapp");

                                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                                startActivity(Intent.createChooser(waIntent, "Share with"));

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






        }
        else if (id==R.id.compliant)
        {
            //show popup

           // LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = inflater.inflate(R.layout.user_input_dialog_box, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
            alertDialogBuilderUserInput.setView(mView);

            final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
            final EditText compliant_id = (EditText) mView.findViewById(R.id.comp_id);
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            // ToDo get user input here

if (!compliant_id.getText().toString().equals("")) {


    StringRequest strReq = new StringRequest(Request.Method.POST,
            EndPoints.COMPLIANT, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.e(TAG, "response: " + response);

            try {
                JSONObject obj = new JSONObject(response);

                // check for error
                if (obj.getBoolean("error") == false) {


                    tv.setText("Compliant Registered Successfully\nAnd We are Sorry for what happened !");
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
        }
    }) {

        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();

            params.put("msg", userInputDialogEditText.getText().toString());
            params.put("s_no", MyApplication.getInstance().getPrefManager().getUser().getEmail());
            params.put("compliant_id", compliant_id.getText().toString());

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
else
{
    tv.setText("Enter Compliant ID");
    Toast toast = new Toast(context);
    toast.setDuration(Toast.LENGTH_SHORT);
    toast.setView(toastLayout);
    toast.show();
}

                        }
                    })

                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }



    /**
     * Handles new push notification
     */


    // Subscribing to all chat room topics
    // each topic name starts with `topic_` followed by the ID of the chat room
    // Ex: topic_1, topic_2
    /*
    private void subscribeToAllTopics() {
        for (ChatRoom cr : chatRoomArrayList) {

            Intent intent = new Intent(this, GcmIntentService.class);
            intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
          //  intent.putExtra(GcmIntentService.TOPIC, "topic_" + cr.getId());
            startService(intent);
        }
    }
    */




    //real paytm starts
    //commented due to security issues for now from google play


/*
    public void onStartTransaction() {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();


        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);


        paramMap.put("ORDER_ID",orderId);
        paramMap.put("MID","Making42857475470515");
        paramMap.put("CUST_ID","CUST"+MyApplication.getInstance().getPrefManager().getUser().getId());
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID","Retail");
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("TXN_AMOUNT","20");
        paramMap.put("THEME","merchant");
       // paramMap.put("EMAIL","eldho.kuriyan@gmail.com");
        paramMap.put("EMAIL","");
        paramMap.put("MOBILE_NO", MyApplication.getInstance().getPrefManager().getUser().getEmail());


        Log.e("Main", "Number for paytm: " + MyApplication.getInstance().getPrefManager().getUser().getEmail());



        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
               "https://52.26.199.108/Paytm_App_Checksum_Kit_PHP-master/generateChecksum.php",
              "https://52.26.199.108/Paytm_App_Checksum_Kit_PHP-master/verifyChecksum.php");
               // EndPoints.G_C,EndPoints.V_C);



        Service.initialize(Order, Merchant, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                        //Toast.makeText(getApplicationContext(), "Ui/Webview error occured.", Toast.LENGTH_LONG).show();

                        tv.setText("Please try Again !");
                        Toast toast = new Toast(context);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(toastLayout);
                        toast.show();

                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.



                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    EndPoints.CHAT_PAYMENT, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                  //  Log.e(TAG, "response: block " + response);

                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        // check for error flag
                                        if (obj.getBoolean("error") == false) {
                                            // user successfully logged in


                                            tv.setText("Payment Success");
                                            Toast toast = new Toast(context);
                                            toast.setDuration(Toast.LENGTH_LONG);
                                            toast.setView(toastLayout);
                                            toast.show();

                                            getMsgLeftHere();

                                        } else {
                                            // login error - simply toast the message
                                            // Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();

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

                                    params.put("id", MyApplication.getInstance().getPrefManager().getUser().getId());


                                    // Log.e(TAG, "params: " + params.toString());
                                    return params;
                                }
                            };

                            //Adding request to request queue
                            MyApplication.getInstance().addToRequestQueue(strReq);





                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                       // Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);

                        tv.setText("Sorry, Please try again !");
                        Toast toast = new Toast(context);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(toastLayout);
                        toast.show();
                      //  Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                        recreate();
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                       // Toast.makeText(getBaseContext(), "No Internet connection.", Toast.LENGTH_LONG).show();
                        tv.setText("No Internet connection !");
                        Toast toast = new Toast(context);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(toastLayout);
                        toast.show();

                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                        Toast.makeText(getBaseContext(), "Client Authentication Failed.", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                });
    }


*/


    private void launchLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        in=1;

        Log.e(TAG, "onResume in MainActivity ");

        // chatRoomArrayList=null;
       // fetchChatRooms();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        NotificationUtils.clearNotifications();




        if(isNetworkAvailable(getApplicationContext())){
            AdRequest adRequest = new AdRequest.Builder()
                    //   .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();


            // Attempt loading ad for interstitial
            interstitial.loadAd(adRequest);


        }

        //user_valid();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    // starting the service to register with GCM

    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }




    public void user_valid()
    {

        //Write Code to Send OTP to mobile number
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.USER_VALID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // user successfully logged in
                         user_valid=obj.getString("installed");
                         version_valid=obj.getBoolean("version");
                         forced_update=obj.getBoolean("forced");


                        if (user_valid.equals("3"))
                        {



                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                            builder.setTitle("BLOCKED !");
                            builder.setMessage("You are Currently BLOCKED from using NEST, due to some Complaints we received.");


                            AlertDialog dialog = builder.create();
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            // display dialog
                            dialog.show();


                        }


                        if (!version_valid)
                        {











                                            if (forced_update)
                                            {



                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                                                builder.setTitle("UPDATE NEST !");
                                                builder.setMessage("You are Currently using an old version of NEST, Please update to new Version for a greater Experience.");

                                                builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // TODO Auto-generated method stub

                                                    }
                                                });

                                              /*
                                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // TODO Auto-generated method stub

                                                    }
                                                });
                                                */

                                                final AlertDialog dialog = builder.create();
                                                dialog.setCancelable(false);
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();
                                                //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(View v)
                                                    {

                                                        if(isNetworkAvailable(context.getApplicationContext()))
                                                        {
                                                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                            try {
                                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                            }
                                                        }
                                                        else{
                                                            //Toast.makeText(MainActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                                            tv.setText("Check your Internet Connection");
                                                            Toast toast = new Toast(context);
                                                            toast.setDuration(Toast.LENGTH_LONG);
                                                            toast.setView(toastLayout);
                                                            toast.show();


                                                        }

                                                        /*
                                                        Boolean wantToCloseDialog = false;
                                                        //Do stuff, possibly set wantToCloseDialog to true then...
                                                        if(wantToCloseDialog)
                                                            dialog.dismiss();
                                                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.


                                                    */

                                                    }
                                                });
/*
                                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        Boolean wantToCloseDialog = true;
                                                        //Do stuff, possibly set wantToCloseDialog to true then...
                                                        if(wantToCloseDialog)
                                                            dialog.dismiss();
                                                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                                                    }
                                                });


*/







/*

                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                                                builder.setTitle("UPDATE NEST !");
                                                builder.setMessage("You are Currently using an old version of NEST, Please update to new Version for a greater Experience.");


                                                builder.setNeutralButton("UPDATE",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // positive button logic
                                                                if(isNetworkAvailable(context.getApplicationContext()))
                                                                {
                                                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                                    try {
                                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                                    }
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


                                                AlertDialog dialog = builder.create();
                                                dialog.setCancelable(false);
                                                dialog.setCanceledOnTouchOutside(false);

                                                // display dialog


                                                dialog.show();

*/
                                            }
                                            else
                                            {

/*
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                                                builder.setTitle("UPDATE NEST !");
                                                builder.setMessage("You are Currently using an old version of NEST, Please update to new Version for a greater Experience.");

                                                builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // TODO Auto-generated method stub

                                                    }
                                                });


                                                builder.setNegativeButton("LATER", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // TODO Auto-generated method stub

                                                    }
                                                });


                                                final AlertDialog dialog = builder.create();
                                                dialog.setCancelable(false);
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();
                                                //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(View v)
                                                    {

                                                        if(isNetworkAvailable(context.getApplicationContext()))
                                                        {
                                                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                            try {
                                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                            }
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

                                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        dialog.dismiss();

                                                    }
                                                });











*/














                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                                                builder.setTitle("UPDATE NEST !");
                                                builder.setMessage("You are Currently using an old version of NEST, Please update to new Version for a greater Experience.");
                                                builder.setPositiveButton("UPDATE",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // positive button logic
                                                                if(isNetworkAvailable(getApplicationContext()))
                                                                {
                                                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                                    try {
                                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                                    }
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
                                                builder.setNegativeButton("LATER",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // negative button logic

                                                                dialog.cancel();
                                                            }
                                                        });

                                                AlertDialog dialog = builder.create();
                                               // dialog.setCancelable(false);
                                                //dialog.setCanceledOnTouchOutside(false);
                                                // display dialog
                                                dialog.show();







                                            }



                        }




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
                params.put("id",MyApplication.getInstance().getPrefManager().getUser().getId());
                String version="";



                try
                {

                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionName;
                }
                catch (PackageManager.NameNotFoundException e)
                {
                    e.printStackTrace();
                }

                params.put("version",version);


                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);



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
    public void getMsgLeftHere()
    {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CHAT_MSG_LEFT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: getMsgLeft " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // user successfully logged in


                      //  Log.e(TAG, "response: getMsgLeft 1" + obj.getString("rechrg"));
                       // Log.e(TAG, "response: getMsgLeft 2" + obj.getString("msg_left"));

                     /*
                        navigationView.getMenu().getItem(3).setTitle("Messages Left  "+obj.getString("msg_left"));

                        if(!obj.getString("likes").equals("0")) {
                            like_details.setText(obj.getString("likes") + " People Likes You");
                        }
                        else
                        {
                            like_details.setText("Lot of People likes You,\nBut they are not ready to reveal it with You.");

                        }
                        */
                        MyApplication.getInstance().getPrefManager().storeMsgLeft( obj.getString("msg_left"));
                        MyApplication.getInstance().getPrefManager().storeRechrge(obj.getString("rechrg"));
                        MyApplication.getInstance().getPrefManager().storeLikes(obj.getString("likes"));


                        navigationView.getMenu().getItem(3).setTitle("Messages Left  "+MyApplication.getInstance().getPrefManager().getMsgLeft());

                        if(Integer.parseInt(MyApplication.getInstance().getPrefManager().getLikes())>1) {
                            like_details.setText(MyApplication.getInstance().getPrefManager().getLikes() + " People Likes You");
                        }
                        else if (Integer.parseInt(MyApplication.getInstance().getPrefManager().getLikes())==1)
                        {
                            like_details.setText(MyApplication.getInstance().getPrefManager().getLikes() + " Person Likes You");

                        }
                        else
                        {
                            like_details.setText("Lot of People likes You,\nBut they are not ready to reveal it with You.");

                        }



                    } else {
                        // login error - simply toast the message
                        // Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();

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

                params.put("id",  MyApplication.getInstance().getPrefManager().getUser().getId());


                // Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);



    }



    public void pickContact(View v)
    {
        Log.e(TAG, "pickContact");
/*
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.TEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: WaytoSms " + response);


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

                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);


*/
        //original


        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, 1525);



    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // Log.e(TAG, " onactivity results 153");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1525:
                    Log.e(TAG, " inside case 1525");
                    CrushFragment.contactPicked(data);
                    break;
                case 153:
                    //code to refresh list
                    Log.e(TAG, " inside case 153");
                    CrushFragment.onback(data);
                    break;
            }

        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }



    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (interstitial != null && interstitial.isLoaded()) {

            interstitial.show();

        }
        else
        {


            moveTaskToBack(true);


        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (interstitial != null && interstitial.isLoaded()) {

                interstitial.show();

            }
            else
            {


                moveTaskToBack(true);

            }
            return true;
        }
        return true;
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
