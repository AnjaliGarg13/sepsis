package com.nsut.spotsepsis.retrofit.Calls;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nsut.spotsepsis.R;
import com.nsut.spotsepsis.models.Recommendation;
import com.nsut.spotsepsis.resultInterface.RecommendationsResultInterface;
import com.nsut.spotsepsis.retrofit.ApiInterface;
import com.nsut.spotsepsis.utils.GenerateRetrofitInstance;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class GetRecommendationsCall extends AsyncTask<Void, Void, ArrayList<Recommendation>> {

    public RecommendationsResultInterface recommendationsResultInterface = null;
    private final static String TAG = "Log";
    private Context context;
    private ImageView loaderImageView;

    public GetRecommendationsCall(Context context, ImageView loaderImageView){
        this.context = context;
        this.loaderImageView = loaderImageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Glide.with(context).asGif().load(R.drawable.loader).into(loaderImageView);
        Log.d(TAG, "Starting call to OCR Read Data");
    }

    @Override
    protected ArrayList<Recommendation> doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground call to OCR Read Data");
        ApiInterface apiInterface = GenerateRetrofitInstance.getApiInterface();

        Call<JsonArray> getOcrResult = apiInterface.getRecommendations();
        JsonArray recommendationJsonArray = null;
        ArrayList<Recommendation> recommendationArrayList = new ArrayList<>();

        try {
            Response<JsonArray> response = getOcrResult.execute();
            System.out.println("Response is : "+response);
            System.out.println("Response body is : "+response.body());

            if(response.body() != null) {
                recommendationJsonArray = response.body();
                recommendationArrayList = new Gson().fromJson(recommendationJsonArray, new TypeToken<List<Recommendation>>(){}.getType());
                System.out.println("isUploadedSuccessfully : " + recommendationArrayList);
            }
        } catch (IOException exception){
            exception.printStackTrace();
        }
        return recommendationArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Recommendation> recommendations) {
        super.onPostExecute(recommendations);
        recommendationsResultInterface.recommendationsResult(recommendations);
    }
}
