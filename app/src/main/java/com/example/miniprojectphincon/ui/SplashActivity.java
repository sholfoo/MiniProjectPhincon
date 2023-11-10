package com.example.miniprojectphincon.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;

import com.example.miniprojectphincon.R;
import com.example.miniprojectphincon.base.BaseActivity;
import com.example.miniprojectphincon.databinding.ActivityDetailBinding;
import com.example.miniprojectphincon.databinding.ActivitySplashBinding;
import com.example.miniprojectphincon.ui.main.MainActivity;

public class SplashActivity extends BaseActivity {
    ActivitySplashBinding activitySplashBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, 0);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        super.onCreate(savedInstanceState);
        activitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(activitySplashBinding.getRoot());

        setupAnimation();

        new Handler().postDelayed(() -> {
            Animation slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            activitySplashBinding.imgText.startAnimation(slideOutLeft);
            
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }, 2000);
    }

    private void setupAnimation(){
        Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        activitySplashBinding.imgText.startAnimation(slideInRight);
        activitySplashBinding.imgLogo.animate().rotation(1110).setDuration(1000);
    }
}
