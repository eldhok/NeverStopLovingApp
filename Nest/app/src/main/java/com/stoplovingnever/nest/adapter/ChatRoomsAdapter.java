package com.stoplovingnever.nest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.app.MyApplication;
import com.stoplovingnever.nest.model.ChatRoom;

import static java.sql.Types.NULL;


public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private static String today;
 //   public static int i=1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message,timestamp, count;
        public TextView myImageViewText;
        public ImageView myImageView,online_indicator;
    //   LayerDrawable bgDrawable;
    //    GradientDrawable shape;


        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            count = (TextView) view.findViewById(R.id.count);
            myImageViewText = (TextView) view.findViewById(R.id.myImageViewText);
            myImageView = (ImageView) view.findViewById(R.id.myImageView);

            // bgDrawable = (LayerDrawable)myImageView.getBackground();
            //  shape = (GradientDrawable)bgDrawable.findDrawableByLayerId(R.id.shape);
            online_indicator = (ImageView) view.findViewById(R.id.online_indicator);

            //    shape.mutate();

          //  Drawable box = ContextCompat.getDrawable(mContext, R.drawable.grid_elemt_style).mutate();




        }
    }


    public ChatRoomsAdapter(Context mContext, ArrayList<ChatRoom> chatRoomArrayList) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_rooms_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatRoom chatRoom = chatRoomArrayList.get(position);

      //  LayerDrawable layerDrawable = (LayerDrawable)holder.myImageView.getBackground().getCurrent();
        //GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.shape).getCurrent();

        //  holder.myImageView.setImageDrawable(null);
       // holder.myImageViewText.setText("");
        //holder.name.setText("S"+chatRoom.getS_id()+ "M"+ MyApplication.getInstance().getPrefManager().getUser().getId()+"R"+chatRoom.getR_id());

        holder.name.setText(chatRoom.getName());

        if (chatRoom.getSatus().equals("1"))
        {
            holder.name.setText(chatRoom.getName()+" "+chatRoom.getI());

        }


/*
        if(chatRoom.getS_id().equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {

            holder.name.setText(chatRoom.getR_name());
           // holder.myImageViewText.setText(Character.toUpperCase((chatRoom.getR_name()).substring(0,1)));
             holder.myImageViewText.setText(((chatRoom.getR_name()).substring(0,1)).toUpperCase());



        }
        else
        {
            //holder.name.setText("Crush"+Integer.toString(i));
            holder.name.setText("Crush");

            holder.myImageViewText.setText(Integer.toString(i));

            i++;
        }
        if(chatRoom.getSatus().equals("2"))
        {

            if(chatRoom.getS_id().equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {

                holder.name.setText(chatRoom.getR_name());
            //   holder.myImageViewText.setText(Character.toUpperCase((chatRoom.getR_name()).charAt(0)));
                 holder.myImageViewText.setText(((chatRoom.getR_name()).substring(0,1)).toUpperCase());


            }
            else
            {
                holder.name.setText(chatRoom.getS_name());
            //    holder.myImageViewText.setText(Character.toUpperCase((chatRoom.getS_name()).charAt(0)));
                    holder.myImageViewText.setText(((chatRoom.getS_name()).substring(0,1)).toUpperCase());

            }
        }


*/


        if(chatRoom.getLast_msg_s_id().equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {
            holder.message.setText("You : "+chatRoom.getLast_msg());
        }
        else
        {
            holder.message.setText(chatRoom.getLast_msg());
        }





        if (chatRoom.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
            holder.message.setTextColor(Color.BLACK);
        } else {

           holder.count.setVisibility(View.GONE);
           // holder.message.setTextColor(getResources().getColor(R.color.colorTextInfo));
            holder.message.setTextColor(Color.parseColor("#9b9b9b"));
         //  holder.count.setText(String.valueOf(0));
          //  holder.count.setVisibility(View.VISIBLE);

        }





        holder.timestamp.setText(getTimeStamp(chatRoom.getTimestamp()));

        //edit new to hide color selection
/*

        holder.myImageView.setImageBitmap(chatRoom.getBit());


        if(chatRoom.getFlag()==1)//means photo present
        {
            holder.myImageViewText.setText("");

        }
        else
        {
            holder.myImageViewText.setText(chatRoom.getI());


        }
*/

        //edit new to show image from drawable


        if(chatRoom.getFlag()==1)//means photo present
        {
            // holder.myImageViewText.setText("");
            holder.myImageView.setBackgroundResource(NULL);

            holder.myImageView.setImageBitmap(chatRoom.getBit());


        }
        else
        {
            holder.myImageView.setImageBitmap(null);

            holder.myImageView.setBackgroundResource(R.drawable.contact);

            // holder.myImageViewText.setText(chatRoom.getI());


        }





        //edit new ends here

/*

        bit=chatRoom.getBit();
        if(bit!=null)
        {
           // Log.e(TAG, "S "+chatRoom.getS_name()+ " R "+chatRoom.getR_name());

            holder.myImageViewText.setText("");
         //   holder.myImageView.setBackgroundResource(NULL);

           // holder.myImageView.setBackgroundDrawable(bit.getBitmap());

            holder.myImageView.setImageBitmap(bit);



        }
        else
        {

            Bitmap bmp=Bitmap.createBitmap(10,10,Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(bmp);
            canvas.drawColor(chatRoom.getColor());
            holder.myImageView.setImageBitmap(bmp);

            //holder.myImageView.setBackgroundColor(chatRoom.getColor());
           // Log.e(TAG, "S "+chatRoom.getS_name()+ " R "+chatRoom.getR_name()+ " N "+no);
          //  holder.myImageView.setBackgroundDrawable(null);
           // holder.shape.setColor(chatRoom.getColor());

          //  holder.myImageView.setBackgroundResource(R.drawable.grid_elemt_style);



            //old

            // holder.myImageView.setBackgroundColor(MaterialColorPalette.getRandomColor("400"));
             // holder.shape.setColor(MaterialColorPalette.getRandomColor("400"));

           // holder.shape.mutate();
           // holder.shape.setColor(chatRoom.getColor());

          //  Drawable box = ContextCompat.getDrawable(mContext, R.drawable.grid_elemt_style).mutate();

           // gradientDrawable.setColor(0);


           // holder.shape.setColor(chatRoom.getColor());
        //    holder.myImageView.setBackgroundColor(chatRoom.getColor());

            Log.e(TAG, "S "+chatRoom.getS_name()+ " R "+chatRoom.getR_name()+" "+chatRoom.getColor());




        }

     */


        if(chatRoom.getInst()==1)
        {
            holder.online_indicator.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.online_indicator.setVisibility(View.GONE);


        }
       // Random rnd = new Random();
        //int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));



       // holder.shape.setColor(getMatColor("500"));
       // holder.shape.setColor(MaterialColorPalette.getRandomColor("400"));
       // Drawable boxBorder = ContextCompat.getDrawable(mContext, R.drawable.markbox_border).mutate();

    }

    @Override
    public int getItemCount() {
        return chatRoomArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
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
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }



    public int getMatColor(String typeColor)
    {


        Random rnd = new Random();

        int rand=0 + rnd.nextInt(17 - 0 + 1);
        int c=Color.parseColor("#e51c23");
        switch(rand)
        {
            case 0:
                c=Color.parseColor("#e51c23");
                break;
            case 1:c=Color.parseColor("#e91e63");
                break;
            case 2:c=Color.parseColor("#9c27b0");
                break;
            case 3:c=Color.parseColor("#673ab7");
                break;
            case 4:c=Color.parseColor("#3f51b5");
                break;
            case 5:c=Color.parseColor("#5677fc");
                break;
            case 6:c=Color.parseColor("#03a9f4");
                break;
            case 7:c=Color.parseColor("#00bcd4");
                break;
            case 8:c=Color.parseColor("#009688");
                break;

            case 9:c=Color.parseColor("#4fc3f7");
                break;
            case 10:c=Color.parseColor("#8bc34a");
                break;
            case 11:c=Color.parseColor("#cddc39");
                break;
            case 12:c=Color.parseColor("#ffeb3b");
                break;
            case 13:c=Color.parseColor("#ff9800");
                break;
            case 14:c=Color.parseColor("#ff5722");
                break;
            case 15:c=Color.parseColor("#039be5");
                break;

            case 16:c=Color.parseColor("#4dd0e1");
                break;
            case 17:c=Color.parseColor("#607d8b");
                break;

        }







        return c;





    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ChatRoomsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ChatRoomsAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
