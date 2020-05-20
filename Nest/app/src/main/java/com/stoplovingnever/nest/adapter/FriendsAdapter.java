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

import java.util.ArrayList;
import java.util.Random;


import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.model.FriendList;

import static java.sql.Types.NULL;


public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<FriendList> chatRoomArrayList;
    //   public static int i=1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,myImageViewText,crush_count,like_count;
        public ImageView myImageView;
        //   LayerDrawable bgDrawable;
        //    GradientDrawable shape;


        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            myImageViewText = (TextView) view.findViewById(R.id.myImageViewText);
            crush_count = (TextView) view.findViewById(R.id.crush_count);
            like_count = (TextView) view.findViewById(R.id.like_count);


            myImageView = (ImageView) view.findViewById(R.id.myImageView);

            // bgDrawable = (LayerDrawable)myImageView.getBackground();
            //  shape = (GradientDrawable)bgDrawable.findDrawableByLayerId(R.id.shape);

            //    shape.mutate();

            //  Drawable box = ContextCompat.getDrawable(mContext, R.drawable.grid_elemt_style).mutate();




        }
    }


    public FriendsAdapter(Context mContext, ArrayList<FriendList> chatRoomArrayList) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FriendList chatRoom = chatRoomArrayList.get(position);

        holder.name.setText(chatRoom.getName());
        holder.crush_count.setText(chatRoom.getCrush());
        holder.like_count.setText(chatRoom.getLike());






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




    }

    @Override
    public int getItemCount() {
        return chatRoomArrayList.size();
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
        private FriendsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FriendsAdapter.ClickListener clickListener) {
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
