package com.nsut.spotsepsis.retrofit.Calls;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.nsut.spotsepsis.OCRReportResult;
import com.nsut.spotsepsis.R;
import com.nsut.spotsepsis.retrofit.ApiInterface;
import com.nsut.spotsepsis.utils.GenerateRetrofitInstance;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class ReportDataOCRReadCall extends AsyncTask<Void, Void, JsonObject> {

    private final static String TAG = "Log";
    private File file;
    private Context context;
    private ImageView uploadStatusImageView;

    private TextView textView2;

    public ReportDataOCRReadCall(Uri photoURI, Context context, ImageView uploadStatusImageView, TextView textView2){
        this.file = createFileFromURI(photoURI);
        this.context = context;
        this.uploadStatusImageView = uploadStatusImageView;

        this.textView2 = textView2;
    }

    private File createFileFromURI(Uri photoURI){
        File dir = Environment.getExternalStorageDirectory();
        String path = dir.getAbsolutePath();
        System.out.println("My path : "+path);
        String pathURI = photoURI.getPath();
        System.out.println("PATH is : "+path+"/"+pathURI.substring(pathURI.indexOf("/")));
        return new File(Objects.requireNonNull(pathURI.substring(pathURI.indexOf("/",1))));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Glide.with(context).asGif().load(R.drawable.loader).into(uploadStatusImageView);
        Log.d(TAG, "Starting call to OCR Read Data");
    }

    @Override
    protected JsonObject doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground call to OCR Read Data");
        ApiInterface apiInterface = GenerateRetrofitInstance.getApiInterface();

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);

        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);

        Call<JsonObject> getOcrResult = apiInterface.getOcrResult(part);
        JsonObject isUploadedSuccessfully = null;
        try {
            Response<JsonObject> response = getOcrResult.execute();
            System.out.println("Response is : "+response);
            System.out.println("Response body is : "+response.body());

            if(response.body() != null) {
                isUploadedSuccessfully = response.body();
                System.out.println("isUploadedSuccessfully : " + isUploadedSuccessfully);
            }
        } catch (IOException exception){
            exception.printStackTrace();
        }
        return isUploadedSuccessfully;
    }

    @Override
    protected void onPostExecute(JsonObject ocrReport) {
        super.onPostExecute(ocrReport);

//        Add condn && ocrReport.get("isUploadedSuccessfully").getAsBoolean() also
        if(ocrReport !=null) {
            String result = "";
            String sepsisPrediction = "";
            result = String.valueOf(ocrReport.get("EtCO2")+"\n"+ocrReport.get("HIV Test")+"\n"+ocrReport.get("Heart Rate")+"\n"+ocrReport.get("O2Sat")+"\n"+ocrReport.get("Temperature"));
            sepsisPrediction = String.valueOf(ocrReport.get("risk"));
//            ocrApiTextView.setText(result+"\n"+sepsisPrediction);
//            reportDataOCRResult.ocrResult(ocrReport);
            Log.d(TAG, "Finishing call to OCR Read Data");
            String etCO2 = ocrReport.get("EtCO2").getAsString();
            String hivTest = ocrReport.get("HIV Test").getAsString();
            String heartRate = ocrReport.get("Heart Rate").getAsString();
            String O2sat = ocrReport.get("O2Sat").getAsString();
            String temperature = ocrReport.get("Temperature").getAsString();
            String sepsisPredictionVal = ocrReport.get("risk").getAsString();

            Intent intent = new Intent(context, OCRReportResult.class);
            intent.putExtra("etCO2", etCO2);
            intent.putExtra("hivTest", hivTest);
            intent.putExtra("heartRate", heartRate);
            intent.putExtra("O2sat", O2sat);
            intent.putExtra("temperature", temperature);
            intent.putExtra("sepsisPredictionVal", sepsisPredictionVal);

//            intent.putExtra("result", result+"\n"+sepsisPrediction);
            context.startActivity(intent);
        }
        else{
            uploadStatusImageView.setVisibility(View.GONE);

        }
    }
}
