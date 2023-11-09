package com.example.miniprojectphincon.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.example.miniprojectphincon.R;
import com.example.miniprojectphincon.base.BaseActivity;
import com.example.miniprojectphincon.ui.main.MainActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                // Apply activity transition
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent,
//                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
//                finish();
//            } else {
//                // Swap without transition
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
//            }
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }, 2000);
    }
}
