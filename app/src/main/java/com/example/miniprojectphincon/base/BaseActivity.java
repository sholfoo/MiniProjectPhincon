package com.example.miniprojectphincon.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.miniprojectphincon.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected FragmentManager getBaseFragmentManager() {
        return getSupportFragmentManager();
    }

    public MaterialDialog showGlobalAlertMessage(String message) {
        return new MaterialDialog.Builder(this)
                .content(message)
                .contentColorRes(R.color.colorAccent)
                .show();
    }
}
