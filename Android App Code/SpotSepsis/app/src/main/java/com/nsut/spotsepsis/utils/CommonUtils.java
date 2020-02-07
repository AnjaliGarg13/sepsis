package com.nsut.spotsepsis.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.nsut.spotsepsis.R;

import androidx.appcompat.app.AlertDialog;

public class CommonUtils {

    public static ProgressDialog getProgressDialog(Context context, String title, String message){
        ProgressDialog progressDialog= new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.custom_progress_dailog);
        return progressDialog;
    }

}
