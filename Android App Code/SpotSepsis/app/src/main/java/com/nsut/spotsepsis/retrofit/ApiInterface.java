package com.nsut.spotsepsis.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nsut.spotsepsis.models.Recommendation;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST("/upload_lab_report")
    Call<JsonObject> getOcrResult(@Part MultipartBody.Part file);

    @POST("/")
    Call<JsonArray> getRecommendations();

}
