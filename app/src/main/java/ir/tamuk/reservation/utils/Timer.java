package ir.tamuk.reservation.utils;

import static androidx.navigation.Navigation.findNavController;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import ir.tamuk.reservation.R;

public class Timer {

    private long Time_Left = 120000;// 2min or 120s
    private long timeLeftInMilliSeconds = Time_Left;
    private CountDownTimer countDownTimer ;

    public void startTimer(TextView textView){
        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliSeconds = l;
                updateTimer(textView);

            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    public void cancelTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }

    public void stopTimer(TextView textView){
        timeLeftInMilliSeconds = Time_Left; //120s
        updateTimer(textView);

    }
    //showTimer
    public String updateTimer(TextView textTimer){
        int min = (int) timeLeftInMilliSeconds / 60000;
        int sec = (int) timeLeftInMilliSeconds % 60000/ 1000;
        String timeLeftText;

        Log.d("ghazal", "UpdateTimer: min ->" + min + " sec ->" + sec );

        timeLeftText = ""+ min;
        timeLeftText += ":";
        if (sec<10) timeLeftText += "0";
        timeLeftText += sec;

        textTimer.setText(timeLeftText);

        return timeLeftText;

    }
}
