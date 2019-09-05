package com.example.ocrecognition.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.ocrecognition.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final Intent main = new Intent(this,MainActivity.class);
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(main, ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this).toBundle());
                SplashScreenActivity.this.finish();
            }
        };
        new Handler().postDelayed(runnable,3000);
    }
}
