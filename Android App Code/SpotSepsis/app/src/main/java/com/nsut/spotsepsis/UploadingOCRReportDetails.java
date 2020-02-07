package com.nsut.spotsepsis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nsut.spotsepsis.resultInterface.FileUploadResultInterface;
import com.nsut.spotsepsis.retrofit.Calls.ReportDataOCRReadCall;

public class UploadingOCRReportDetails extends AppCompatActivity implements FileUploadResultInterface {

    private ImageView uploadStatusImageView;
    private Uri photoUri;


    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hiding the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_uploading_ocrreport_details);

        uploadStatusImageView = findViewById(R.id.uploadStatusImageView);


        Glide.with(this).asGif().load(R.drawable.loader).into(uploadStatusImageView);

        textView2 = findViewById(R.id.textView2);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showAlertDialog(false);
//            }
//        },3000);


        System.out.println("Recieved URI : "+Uri.parse(getIntent().getStringExtra("Uri")));

        photoUri = Uri.parse(getIntent().getStringExtra("Uri"));

        ReportDataOCRReadCall reportDataOCRReadCall = new ReportDataOCRReadCall(photoUri, this, uploadStatusImageView, textView2);
        reportDataOCRReadCall.execute();

    }

    private void showAlertDialog(boolean isUploadedSuccessfully) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.file_uploaded_alert_layout, null);
        ImageView uploadStatusImageView = dialogLayout.findViewById(R.id.uploadStatusImageView);
        TextView uploadStatusTextView = dialogLayout.findViewById(R.id.uploadStatusTextView);
        TextView statusDescTextView = dialogLayout.findViewById(R.id.statusDescTextView);
        TextView actionButtonTextView = dialogLayout.findViewById(R.id.actionButtonTextView);
        TextView cancelAlertDialogButton = dialogLayout.findViewById(R.id.cancelAlertDialogButton);
        final Intent intent = new Intent();
        if(isUploadedSuccessfully){
            Toast.makeText(UploadingOCRReportDetails.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
            uploadStatusImageView.setImageResource(R.drawable.upload_success);
            uploadStatusTextView.setText("yay!!");
            statusDescTextView.setText("Your report is successfully uploaded.");
            actionButtonTextView.setText("OK, Cool");
            actionButtonTextView.setBackgroundColor(Color.parseColor("#43A047"));
            cancelAlertDialogButton.setVisibility(View.GONE);
            actionButtonTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "Ok, Cool clicked!!");
                    intent.putExtra("isUploadedSuccessfully", true);
                    setResult(MainActivity.UPLOAD_REPORT_CODE, null);
                    finish();
                }
            });
        }
        else{
            Toast.makeText(UploadingOCRReportDetails.this, "Could not upload", Toast.LENGTH_SHORT).show();
            uploadStatusImageView.setImageResource(R.drawable.upload_failure);
            uploadStatusTextView.setText("oh snap");
            statusDescTextView.setText("Some error occurred while uploading the lab report.\nPlease ensure that you have properly working internet connection.");
            actionButtonTextView.setText("TRY AGAIN");
            actionButtonTextView.setBackgroundColor(Color.parseColor("#FF1744"));
            actionButtonTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ReportDataOCRReadCall reportDataOCRReadCall = new ReportDataOCRReadCall(photoUri, this, uploadStatusImageView);
//                    reportDataOCRReadCall.execute();
                }
            });
            cancelAlertDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("isUploadedSuccessfully", false);
                    setResult(MainActivity.UPLOAD_REPORT_CODE, null);
                    finish();
                }
            });
        }
        builder.setView(dialogLayout);
        builder.show();
    }

    @Override
    public void fileUploadStatus(boolean isUploadedSuccessfully) {
        showAlertDialog(isUploadedSuccessfully);
    }
}
