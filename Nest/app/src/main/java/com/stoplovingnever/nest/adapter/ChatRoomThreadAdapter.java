package com.stoplovingnever.nest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.activity.ChatRoomActivity;
import com.stoplovingnever.nest.model.Message;

import static java.sql.Types.NULL;


public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = ChatRoomThreadAdapter.class.getSimpleName();
public static int f=0;
    private String userId;
    private int SELF = 100;
    private static String today;
    private Context mContext;
    private ArrayList<Message> messageArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp,imText,tv_date;
        ImageView image_chat;
     //  LayerDrawable bgDrawable;
     //   GradientDrawable shape;
//LinearLayout l,date;


        public ViewHolder(View view) {
            super(view);


                tv_date = (TextView) itemView.findViewById(R.id.tv_date);


                message = (TextView) itemView.findViewById(R.id.message);
                timestamp = (TextView) itemView.findViewById(R.id.timestamp);
                imText = (TextView) itemView.findViewById(R.id.imText);

                image_chat = (ImageView) itemView.findViewById(R.id.image_chat);

               // bgDrawable = (LayerDrawable) image_chat.getBackground();
                //shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape);


        //    l = (LinearLayout) itemView.findViewById(R.id.l);
        //    date = (LinearLayout) itemView.findViewById(R.id.date);



        }
    }


    public ChatRoomThreadAdapter(Context mContext, ArrayList<Message> messageArrayList, String userId) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_holder_me, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_holder_you, parent, false);
        }



        return new ViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);



        if (message.getId().equals(userId)) {

            return SELF;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

       // LayerDrawable layerDrawable = (LayerDrawable) ((ViewHolder) holder).image_chat.getBackground().getCurrent();
       // GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.shape).getCurrent();


        Message message = messageArrayList.get(position);
        ((ViewHolder) holder).message.setText(message.getMessage());

      //  String date=getDay(message.getCreatedAt());
       // Log.e(TAG, "DATE : " +date);
/*
        if(!(ChatRoomActivity.date.equals(date)))
        {
            ((ViewHolder) holder).tv_date.setText(date);
            ((ViewHolder) holder).date.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).l.setVisibility(View.GONE);

        }
        */
       // if (message.getUser().getName() != null)
       //     timestamp = message.getUser().getName() + ", " + timestamp;
//else {
            Log.e(TAG, "getting inside");

            String timestamp = getTimeStamp(message.getCreatedAt());

          //  ((ViewHolder) holder).date.setVisibility(View.GONE);
          //  ((ViewHolder) holder).l.setVisibility(View.VISIBLE);


           /*
            if (ChatRoomActivity.b != null) {
                ((ViewHolder) holder).imText.setText("");

                ((ViewHolder) holder).image_chat.setImageBitmap(ChatRoomActivity.b);

            } else {

                ((ViewHolder) holder).image_chat.setBackgroundDrawable(null);

                ((ViewHolder) holder).shape.setColor(ChatRoomActivity.color);

                ((ViewHolder) holder).image_chat.setBackgroundResource(R.drawable.grid_el);

                ((ViewHolder) holder).imText.setText(ChatRoomActivity.imtext);


                Log.e(TAG, "color chat : " + Integer.toString(ChatRoomActivity.color));


            }
*/




        if (ChatRoomActivity.flag==0)//image not present in Contact
        {
            ((ViewHolder) holder).imText.setText("");

            ((ViewHolder) holder).image_chat.setImageBitmap(null);

            ((ViewHolder) holder).image_chat.setBackgroundResource(R.drawable.contact);


        }
        else
        {


            ((ViewHolder) holder).imText.setText("");
            ((ViewHolder) holder).image_chat.setBackgroundResource(NULL);
            ((ViewHolder) holder).image_chat.setImageBitmap(ChatRoomActivity.b);


        }

        //for setting up colored image in chat
        //starts
        /*
        ((ViewHolder) holder).image_chat.setImageBitmap(ChatRoomActivity.b);

        if (ChatRoomActivity.flag==0)
        {
            ((ViewHolder) holder).imText.setText(ChatRoomActivity.imtext);

        }
        else
        {
            ((ViewHolder) holder).imText.setText("");


        }
*/
        //ends here


            ((ViewHolder) holder).timestamp.setText(timestamp);


      //  }
        //ChatRoomActivity.date=date;

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";


        try {

            /*
        today = today.length() < 2 ? "0" + today : today;


            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
*/

           //new

            Date date = format.parse(dateStr);

            SimpleDateFormat todayFormat = new SimpleDateFormat("hh:mm a");
            timestamp = todayFormat.format(date);

          //  timestamp=time;
          //  timestamp="5:04pm";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public static String getDay(String dateStr) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";
        try {

      /*
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();

            */
            //new
            Date date = format.parse(dateStr);

            SimpleDateFormat todayFormat = new SimpleDateFormat("dd-MMMM-yyyy");
            timestamp = todayFormat.format(date);

            //  timestamp=time;
            //  timestamp="5:04pm";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }
}

