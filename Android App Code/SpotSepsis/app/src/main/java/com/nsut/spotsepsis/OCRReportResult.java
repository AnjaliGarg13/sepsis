package com.nsut.spotsepsis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;

public class OCRReportResult extends AppCompatActivity {

    private TextView sepsisScoreTextView;
    private TextView temperatureTextView;
    private TextView oxygenSatTextView;
    private TextView heartRateTextView;
    private TextView etCo2TextView;
    private TextView hivTestTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hiding the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_ocr_report_result);

        Intent intent = getIntent();

        String etCO2 = intent.getStringExtra("etCO2");
        String hivTest = intent.getStringExtra("hivTest");
        String heartRate = intent.getStringExtra("heartRate");
        String O2sat = intent.getStringExtra("O2sat");
        String temperature = intent.getStringExtra("temperature");
        String sepsisPredictionVal = intent.getStringExtra("sepsisPredictionVal");

        sepsisScoreTextView = findViewById(R.id.sepsisScoreTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        oxygenSatTextView = findViewById(R.id.oxygenSatTextView);
        heartRateTextView = findViewById(R.id.heartRateTextView);
        etCo2TextView = findViewById(R.id.etCo2TextView);
        hivTestTextView = findViewById(R.id.hivTestTextView);

        sepsisScoreTextView.setText(sepsisPredictionVal);
        temperatureTextView.setText(temperature+" deg celsius");
        oxygenSatTextView.setText(O2sat+" mm Hg");
        heartRateTextView.setText(heartRate+" beats pe min");
        etCo2TextView.setText(etCO2+" mm Hg");
        hivTestTextView.setText(hivTest);

    }
}
