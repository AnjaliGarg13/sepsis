package com.nsut.spotsepsis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nsut.spotsepsis.HeartRateMeasure.ImageProcessing;

import java.util.Random;
import java.util.concurrent.Callable;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ScanBodyActivity extends AppCompatActivity {

    private ImageView scanBodyImageView;
    private static int progressStatus;
    private ProgressBar horizontalProgressBar;
    private Button startMeasurementButton;
    private LinearLayout heartRateDisplayLayout;
    private TextView heartBeatDisplayTextView;
    private TextView informationTextView;
    private Handler handler = new Handler();

    private static String initialText = "Place your index finger on camera lens and press start";
    private static String duringMeasurement = "Stay still and don't remove your finger from camera lens.";


    private SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static PowerManager.WakeLock wakeLock = null;

    private static boolean isRunning = false;

    @Override
    public void onBackPressed() {
        if(!isRunning){
            super.onBackPressed();
        }
        else{
            Toast.makeText(this, "In Progress..", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        progressStatus = 0;
        heartRateDisplayLayout.setVisibility(View.GONE);
        horizontalProgressBar.setVisibility(View.GONE);
        scanBodyImageView.setVisibility(View.GONE);
        informationTextView.setText(initialText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hiding the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_scan_body);

        scanBodyImageView = findViewById(R.id.scanBodyImageView);
        horizontalProgressBar = findViewById(R.id.horizontalProgressBar);
        startMeasurementButton = findViewById(R.id.startMeasurementButton);
        heartRateDisplayLayout = findViewById(R.id.heartRateDisplayLayout);
        heartBeatDisplayTextView= findViewById(R.id.heartRateDisplayTextView);
        informationTextView = findViewById(R.id.informationTextView);

        horizontalProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));

        init();

        Glide.with(this).asGif().load(R.drawable.body_scan).into(scanBodyImageView);



        preview = findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // WakeLock Initialization : Forces the phone to stay On
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Tag:DoNotDimScreen");
        }

        startMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = true;
                updateProgressBar();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        camera = Camera.open();
        camera.setDisplayOrientation(90);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (wakeLock.isHeld())
            wakeLock.release();
//        camera.setPreviewCallback(null);
//        camera.stopPreview();
//        camera.release();
//        camera = null;
    }

    private void updateProgressBar(){
        new Thread(new Runnable() {
            public void run() {
                postToHandler(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        onStartMeasurement();
                        return null;
                    }
                });

                while (progressStatus <= 20) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            horizontalProgressBar.setProgress(progressStatus);
                            heartBeatDisplayTextView.setText(String.valueOf(generateRandomNumber()));
                        }
                    });
                    try {
                        // Sleep for 500 milliseconds.
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                postToHandler(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        onStopMeasurement();
                        return null;
                    }
                });
            }
        }).start();
    }


    private int generateRandomNumber(){
        final int min = 65;
        final int max = 90;
        return new Random().nextInt((max - min) + 1) + min;
    }

    private void onStartMeasurement(){
        isRunning = true;
        progressStatus = 0;

        startMeasurementButton.setEnabled(false);
        startMeasurementButton.setText("Please Wait..");
        heartRateDisplayLayout.setVisibility(View.VISIBLE);
        horizontalProgressBar.setVisibility(View.VISIBLE);
        scanBodyImageView.setVisibility(View.VISIBLE);
        horizontalProgressBar.setProgress(progressStatus);
        informationTextView.setText(duringMeasurement);

        previewHolder.addCallback(surfaceCallback);
    }

    private void onStopMeasurement(){
        isRunning = false;
        progressStatus = 0;
        startMeasurementButton.setEnabled(true);
        startMeasurementButton.setText("START");
        heartRateDisplayLayout.setGravity(Gravity.CENTER);
        horizontalProgressBar.setVisibility(View.GONE);
        scanBodyImageView.setVisibility(View.GONE);
        informationTextView.setText(initialText);
        if(wakeLock.isHeld()) {
            wakeLock.release();
        }
        if(!isRunning){
            camera.stopPreview();
            // This below statement was crashing the app.
//        previewHolder.addCallback(null);
            camera.release();
        }
    }

    private void postToHandler(final Callable<Void> methodToCall){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    methodToCall.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private  SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
            } catch (Throwable t) {
                Log.e("PreviewDemo", "Exception in setPreviewDisplay()", t);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }

            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }



}
