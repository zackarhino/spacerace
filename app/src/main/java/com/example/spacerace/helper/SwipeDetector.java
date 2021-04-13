package com.example.spacerace.helper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.spacerace.MainActivity;

/**
 * Modified from http://techin-android.blogspot.com/2011/11/swipe-event-in-android-scrollview.html
 * This class adds a custom Gesture recognizer so that you can swipe through the NestedScrollView/RecyclerView
 */
public class SwipeDetector implements View.OnTouchListener {

    private Activity activity;
    static final int MIN_DISTANCE = 60;
    private float downX, downY, upX, upY;

    public SwipeDetector(final Activity activity) {
        this.activity = activity;
    }

    public final void onRightToLeftSwipe() {
        Log.d("ScrollView","RightToLeftSwipe!");
        int i = MainActivity.bottomNavPosition;
        if(i < 3){
            i++;
            MainActivity.setBottomNavPosition(i, true);
        }
    }

    public void onLeftToRightSwipe(){
        Log.d("ScrollView", "LeftToRightSwipe!");
        int i = MainActivity.bottomNavPosition;
        if(i > 0){
            i--;
            MainActivity.setBottomNavPosition(i, true);
        }
    }

    public void onTopToBottomSwipe(){
        //Log.d("ScrollView", "onTopToBottomSwipe!");
    }

    public void onBottomToTopSwipe(){
        //Log.d("ScrollView", "onBottomToTopSwipe!");
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                //   return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > MIN_DISTANCE){
                    // left or right
                    if(deltaX < 0) { this.onLeftToRightSwipe(); return true; }
                    if(deltaX > 0) { this.onRightToLeftSwipe(); return true; }
                }

                // swipe vertical?
                if(Math.abs(deltaY) > MIN_DISTANCE){
                    // top or down
                    if(deltaY < 0) { this.onTopToBottomSwipe(); return true; }
                    if(deltaY > 0) { this.onBottomToTopSwipe(); return true; }
                }

                //     return true;
            }
        }
        return false;
    }
}