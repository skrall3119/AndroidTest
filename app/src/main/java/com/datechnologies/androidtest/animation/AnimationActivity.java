package com.datechnologies.androidtest.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;

/**
 * Screen that displays the D & A Technologies logo.
 * The icon can be moved around on the screen as well as animated.
 */

public class AnimationActivity extends AppCompatActivity {

    //==============================================================================================
    // Class Properties
    //==============================================================================================

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context) {
        Intent starter = new Intent(context, AnimationActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        setTitle("Animation");
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Button doAnim = (Button) findViewById(R.id.doAnim);
        ImageView logo = (ImageView) findViewById(R.id.gpc_logo);

        doAnim.setOnClickListener(v -> {
            Animation fadeIn = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.fade_in);
            Animation fadeOut = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.fade_out);
            logo.startAnimation(fadeIn);
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    logo.startAnimation(fadeOut);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        });
        logo.setOnTouchListener((v, event) -> {
            float xDown = 0, yDown = 0;
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    xDown = event.getX();
                    yDown = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float movedX, movedY;
                    movedX = event.getX();
                    movedY = event.getY();

                    float distanceX = movedX - xDown;
                    float distanceY = movedY - yDown;

                    logo.setX(logo.getX() + distanceX - 400);
                    logo.setY(logo.getY() + distanceY - 50);
                    xDown = movedX;
                    yDown = movedY;
                    break;
            }
            return true;
        });


        // Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.
        // Add a ripple effect when the buttons are clicked

        // When the fade button is clicked, you must animate the D & A Technologies logo.
        // It should fade from 100% alpha to 0% alpha, and then from 0% alpha to 100% alpha

        // TODO: The user should be able to touch and drag the D & A Technologies logo around the screen.

        // TODO: Add a bonus to make yourself stick out. Music, color, fireworks, explosions!!!
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
