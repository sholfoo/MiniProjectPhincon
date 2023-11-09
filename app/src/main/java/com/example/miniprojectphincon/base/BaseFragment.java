package com.example.miniprojectphincon.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.miniprojectphincon.R;
import com.example.miniprojectphincon.network.ApiService;

import javax.inject.Inject;

public class BaseFragment extends Fragment {
    protected Context mContext;
    protected LayoutInflater mInflater;

    @Inject ApiService mApiService;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public void finish() {
        if (isAdded())
            getActivity().finish();
    }

    public MaterialDialog showGlobalAlertMessage(String message) {
        return new MaterialDialog.Builder(mContext)
                .content(message)
                .contentColorRes(R.color.colorAccent)
                .show();
    }

    protected boolean validateInput(EditText editText) {
        String input = editText.getText().toString().trim();
        if (input.isEmpty()) {
            editText.setError(getString(R.string.error_field_required));
            requestFocus(editText);
            return false;
        } else {
            editText.setError(null);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            );
        }
    }

//    private void goToLoginScreen(Activity activity) {
//        new LocalDataLogin().clearSession(mContext);
//
//        Intent finishIntent = new Intent(activity, LoginActivity.class);
//        finishIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        finishIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        finishIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(finishIntent);
//        finish();
//    }


}
