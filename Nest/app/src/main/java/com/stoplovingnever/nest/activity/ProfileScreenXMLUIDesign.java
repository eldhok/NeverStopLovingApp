package com.stoplovingnever.nest.activity;

/**
 * Created by eldho on 9/3/17.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.app.EndPoints;
import com.stoplovingnever.nest.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import static com.stoplovingnever.nest.activity.CrushFragment.isNetworkAvailable;



public class ProfileScreenXMLUIDesign extends AppCompatActivity {
String crush,like,number,name,r_id;
    Bitmap b;
    static TextView tv;
    static View toastLayout;
    int flag;
    Context context;
    TextView like_count,crush_count,user_profile_name;
    ImageView user_profile_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_design_profile_screen_xml_ui_design);



        Intent intent = getIntent();
        crush = intent.getStringExtra("crush");
        like = intent.getStringExtra("like");
        number = intent.getStringExtra("number");
        name = intent.getStringExtra("name");
        r_id = intent.getStringExtra("id");

        crush_count=(TextView)findViewById(R.id.crush_count);
        like_count=(TextView)findViewById(R.id.like_count);
        user_profile_name=(TextView)findViewById(R.id.user_profile_name);

        user_profile_photo=(ImageView)findViewById(R.id.user_profile_photo);

        if(getIntent().hasExtra("image"))
        {
            //ImageView previewThumbnail = new ImageView(this);
            b = BitmapFactory.decodeByteArray( getIntent().getByteArrayExtra("image"),0,getIntent().getByteArrayExtra("image").length);
            // previewThumbnail.setImageBitmap(b);
        }
        flag=intent.getIntExtra("flag",0);

        user_profile_name.setText(name);
        like_count.setText(like);
        crush_count.setText(crush);


        if(flag==1)//means photo present
        {
            // holder.myImageViewText.setText("");
          //  user_profile_photo.setBackgroundResource(R.drawable.profile_circular_border_imageview);

           // Drawable drawable =new BitmapDrawable(b);
           // user_profile_photo.setImageDrawable(drawable);

           // user_profile_photo.setImageResource(R.drawable.contact);
           // user_profile_photo.setImageDrawable(new BitmapDrawable(getResources(), b));


            user_profile_photo.setImageBitmap(b);


        }
        else
        {
            //user_profile_photo.setImageBitmap(null);
           // user_profile_photo.setBackgroundResource(R.drawable.profile_circular_border_imageview);

            user_profile_photo.setImageResource(R.drawable.contact);

           // user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.contact));

            // holder.myImageViewText.setText(chatRoom.getI());


        }
        context=getApplicationContext();

        LayoutInflater inflater = getLayoutInflater();
        toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.custom_toast_layout));
        tv=(TextView)toastLayout.findViewById(R.id.custom_toast_message);




        if (MyApplication.getInstance().getPrefManager().getProfile() == null)
        {


            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            builder.setTitle("PROFILE");
            builder.setMessage("Love shows the number of Crush's that "+name+" recieved.\n\nLike shows the number of People who likes "+name+" as a Good person.\n" +
                    "\nClick on the Like button to say Anonymously that you also likes "+name+".\n( Your Identity won't be revealed to "+name+" on Liking. )\n"
            );

            //String positiveText = getString();
            builder.setPositiveButton("GOT IT",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic



                MyApplication.getInstance().getPrefManager().storeProfile("1");

                dialog.cancel();

                        }
                    });




            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            // display dialog
            dialog.show();

        }
    }

    public void crush(View v)
    {




    }


    public void like(View v) {
        if (isNetworkAvailable(context)) {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.LIKE, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    // Log.e(TAG, "response: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);

                        // check for error flag
                        if (obj.getBoolean("error") == false) {
                            // user successfully logged in

                            like_count.setText(Integer.toString(Integer.parseInt(like) + 1));
                            tv.setText("You Liked "+name);
                            Toast toast = new Toast(context);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(toastLayout);
                            toast.show();

                        } else {
                            // login error - simply toast the message
                            // Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                            tv.setText("Permission Denied !");
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


                    params.put("s_id", MyApplication.getInstance().getPrefManager().getUser().getId());

                    params.put("r_id", r_id);


                    // Log.e(TAG, "params: " + params.toString());
                    return params;
                }
            };

            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
        }
    }
}