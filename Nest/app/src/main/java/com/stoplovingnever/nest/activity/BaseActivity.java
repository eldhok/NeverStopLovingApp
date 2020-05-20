package com.stoplovingnever.nest.activity;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import com.stoplovingnever.nest.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//import static com.stoplovingnever.nest.adapter.ChatRoomsAdapter.i;

/**
 * Created by Dytstudio.
 */

public class BaseActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView title;
    ImageView tv_image;
    GlideDrawableImageViewTarget imageViewTarget;

    public final void changeTitle(int toolbarId, String titlePage){
        toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);

        title = (TextView) toolbar.findViewById(R.id.tv_title);
        title.setText(Html.fromHtml("<font color='#FFFFFF'><i><strong>"+titlePage+"</strong></i></font>"));
        getSupportActionBar().setTitle("");
    }
    public final void setupToolbar(int toolbarId, String titlePage){
        toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);

        title = (TextView) toolbar.findViewById(R.id.tv_title);
        title.setText(titlePage);
       // title.setText(Html.fromHtml("<font color='#FFFFFF'><i><strong>"+titlePage+"</strong></i></font>"));

        getSupportActionBar().setTitle("");
    }

    public final void setupImage(int toolbarId){
        toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);

        tv_image = (ImageView) toolbar.findViewById(R.id.tv_image);
       // tv_image.setBackgroundResource(R.drawable.loveheartfly);
        imageViewTarget = new GlideDrawableImageViewTarget(tv_image);

        Glide.with(this).load(R.drawable.loveheartfly).into(imageViewTarget);
        getSupportActionBar().setTitle("");
    }


    public void setupToolbarWithUpNav(int toolbarId, String titlePage, @DrawableRes int res){
        toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);

        title = (TextView) toolbar.findViewById(R.id.tv_title);
        title.setText(titlePage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(res);
        getSupportActionBar().setTitle("");
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
