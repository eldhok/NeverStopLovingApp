package com.stoplovingnever.nest.activity;

/**
 * Created by eldho on 16/3/17.
 */

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
        import android.support.annotation.FloatRange;
        import android.support.annotation.Nullable;
        import android.view.View;

import com.stoplovingnever.nest.R;
import com.stoplovingnever.nest.app.MyApplication;

import agency.tango.materialintroscreen.MaterialIntroActivity;
        import agency.tango.materialintroscreen.MessageButtonBehaviour;
        import agency.tango.materialintroscreen.SlideFragmentBuilder;
        import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        if (MyApplication.getInstance().getPrefManager().getInst() != null) {
            startActivity(new Intent(this, Splash.class));
            finish();
        }
        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        .image(R.drawable.loveheartfly)
                        .title("Express your Love with Nest !")
                        .description("No Love should remain untold !\nExpressing your Love is not that hard from now.")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Safe, Secret and Secure !");
                    }
                }, "Why Nest ?!"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.second_slide_background)
                .buttonsColor(R.color.second_slide_buttons)
                .title("How Nest Works ?")
                .description("Add your Crush's number to your Crush list.\nAnonymousley send Clues to their number about who you are and build up their Curiosity." +
                        "\nAnd both get Notified about the Love,when your crush adds you too to their Crush list.\nIt's that Simple to Express your Love !" +
                        "\nNest helps to remove the fear of being Rejected,\nby Expressing Love Secretly !")
                .build());

        //addSlide(new CustomSlide());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.third_slide_background)
                        .buttonsColor(R.color.third_slide_buttons)
                        .possiblePermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.INTERNET})
                        .neededPermissions(new String[]{Manifest.permission.WAKE_LOCK, Manifest.permission.READ_SMS,  Manifest.permission.GET_TASKS,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.GET_ACCOUNTS,Manifest.permission.VIBRATE})
                        .image(R.drawable.img_equipment)
                        .title("Permissions ")
                        .description("We need all these Permissions for the Smooth working of Nest,\nand We make sure that none of your data gets into Wrong hands.")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Try us!");
                    }
                }, "Tools"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.first_slide_buttons)
                .title("That's it")
                .description("Welcome to Never Stop Loving !\nStart your beautiful world of Love from now.\nDon't forget to Share about Nest with your Friends.\n\n" +"How Nest Works ?\n"+
                        "Add your Crush's number to your Crush list.\n" +
                        "Anonymousley send Clues to their number about who you are and build up their Curiosity.\n" +
                        "And both get Notified about the Love,when your crush adds you too to their Crush list.\nIt's that Simple to Express your Love !\n" +
                        "Nest helps to remove the fear of being Rejected,\n" +
                        "by Expressing Love Secretly !")
                .build());
    }

    @Override
    public void onFinish() {
        super.onFinish();

        MyApplication.getInstance().getPrefManager().storeInst("1");
        startActivity(new Intent(this, LoginActivity.class));


    }
}