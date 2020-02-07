package com.nsut.spotsepsis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import lecho.lib.hellocharts.view.LineChartView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.nsut.spotsepsis.GraphUtils.GraphInitialisation;
import com.nsut.spotsepsis.GraphUtils.GraphInitialisationParametersClass;
import com.nsut.spotsepsis.retrofit.Calls.ReportDataOCRReadCall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private LinearLayout homeTestLayout;
    private LinearLayout scanLabReportLayout;
    private LinearLayout recommendationsLayout;
    private Spinner parametersSpinner;
    private LineChartView sepsisTrendChartView;
    private LineChartView parametersTrendChartView;

    public final static int PICK_PHOTO_CODE = 1046;
    public final static int UPLOAD_REPORT_CODE = 1047;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hiding the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        homeTestLayout = findViewById(R.id.homeTestLayout);
        scanLabReportLayout = findViewById(R.id.scanLabReportLayout);
        recommendationsLayout = findViewById(R.id.recommendationsLayout);
        parametersSpinner = findViewById(R.id.parametersSpinner);
        sepsisTrendChartView = findViewById(R.id.sepsisTrendChartView);
        parametersTrendChartView = findViewById(R.id.parametersTrendChartView);

        isStoragePermissionGranted();
        isCameraPermissionGranted();

        homeTestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start showing slides for taking different required test.
                startActivity(new Intent(MainActivity.this, ScanBodyActivity.class));
            }
        });

        scanLabReportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send request to OCR Api

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, PICK_PHOTO_CODE);
                }
            }
        });

        recommendationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecommendationsActivity.class));
            }
        });

        // Set Parameters spinners
        setSpinner();


        // Static initialisation of Sepsis Trend Graph
        new GraphInitialisation(this, sepsisTrendChartView).plotData();
        // Static initialisation of 40 Parameters Trend Graph
        new GraphInitialisationParametersClass(this, parametersTrendChartView).plotData();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri photoUri = data.getData();

                    System.out.println("Passed URI : "+photoUri);
                    // Do something with the photo based on Uri
                    Intent intent = new Intent(MainActivity.this, UploadingOCRReportDetails.class);
                    intent.putExtra("Uri", String.valueOf(photoUri));
                    startActivityForResult(intent, UPLOAD_REPORT_CODE);

//                    ReportDataOCRReadCall reportDataOCRReadCall = new ReportDataOCRReadCall(photoUri, MainActivity.this);
//                    reportDataOCRReadCall.execute();
//                    try {
//                        Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
//                    } catch (IOException e) {
//                        Log.d("TAG", "Error selecting image from storage.");
//                        e.printStackTrace();
//                    }
                    Log.d("TAG", "URL of photoUri is : " + photoUri);
                }
                break;
            case UPLOAD_REPORT_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    boolean isUploadedSuccessfully = Objects.requireNonNull(data.getExtras()).getBoolean("isUploadedSuccessfully");

                    // Update graph is isUploadedSuccessfully is true otherwise no need.

                }
        }
    }

    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
        }
    }

    public void isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 2);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private void setSpinner() {
        ArrayList<String> parametersList = getParameters();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, parametersList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parametersSpinner.setAdapter(dataAdapter);
    }

    private ArrayList<String> getParameters() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("O2 Saturation");
        categories.add("Temperature");
        categories.add("Hiv Test");
        categories.add("etCO2");
        return categories;
    }

}
