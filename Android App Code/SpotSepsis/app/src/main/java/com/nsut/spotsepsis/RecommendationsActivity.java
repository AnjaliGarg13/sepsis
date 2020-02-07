package com.nsut.spotsepsis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nsut.spotsepsis.adapters.RecommendationsAdapter;
import com.nsut.spotsepsis.models.Recommendation;
import com.nsut.spotsepsis.resultInterface.RecommendationsResultInterface;
import com.nsut.spotsepsis.retrofit.Calls.GetRecommendationsCall;

import java.util.ArrayList;

public class RecommendationsActivity extends AppCompatActivity implements RecommendationsResultInterface {

    private ImageView loaderImageView;
    private RecyclerView recommendationRecyclerView;
    private RecommendationsAdapter recommendationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hiding the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_recommendations);

        loaderImageView = findViewById(R.id.loaderImageView);
        recommendationRecyclerView = findViewById(R.id.recommendationRecyclerView);

//        GetRecommendationsCall getRecommendationsCall = new GetRecommendationsCall(this, loaderImageView);
//        getRecommendationsCall.recommendationsResultInterface = this;
//        getRecommendationsCall.execute();

        final ArrayList<Recommendation> recommendationArrayList = prepareList();


        // Remove below code when calling to live api.
        Glide.with(this).asGif().load(R.drawable.loader).into(loaderImageView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loaderImageView.setVisibility(View.GONE);
                recommendationsAdapter = new RecommendationsAdapter(recommendationArrayList, RecommendationsActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RecommendationsActivity.this);
                recommendationRecyclerView.setLayoutManager(mLayoutManager);
                recommendationRecyclerView.setItemAnimator(new DefaultItemAnimator());
                recommendationRecyclerView.setAdapter(recommendationsAdapter);
            }
        },4000);

    }

    @Override
    public void recommendationsResult(ArrayList<Recommendation> recommendationArrayList) {

        loaderImageView.setVisibility(View.GONE);
        if(recommendationArrayList !=null && recommendationArrayList.size()>0){

            recommendationArrayList = prepareList();

            recommendationsAdapter = new RecommendationsAdapter(recommendationArrayList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recommendationRecyclerView.setLayoutManager(mLayoutManager);
            recommendationRecyclerView.setItemAnimator(new DefaultItemAnimator());
            recommendationRecyclerView.setAdapter(recommendationsAdapter);
        }
        else{
            // Show empty screen or a message - some error occurred, try again.
        }
    }

    private ArrayList<Recommendation> prepareList(){
        ArrayList<Recommendation> recommendationArrayList = new ArrayList<>();
        Recommendation recommendation;
        ArrayList<Integer> imagesUrl = new ArrayList<>();
        imagesUrl.add(R.drawable.temp_one);
        imagesUrl.add(R.drawable.temp_two);
        imagesUrl.add(R.drawable.temp_three);
        imagesUrl.add(R.drawable.temp_four);
        imagesUrl.add(R.drawable.temp_five);
        imagesUrl.add(R.drawable.temp_one);
        imagesUrl.add(R.drawable.temp_two);
        imagesUrl.add(R.drawable.temp_three);
        imagesUrl.add(R.drawable.temp_four);
        imagesUrl.add(R.drawable.temp_five);


        for(int i=0; i<10; i++){
            recommendation = new Recommendation(
                    imagesUrl.get(i),
                    "Lifestyle assessment questionnaire ",
                    "Make our recommendation more accurate with the lifestyle assessment! Just a few questions will help us find out what stresses you out the most so we can personalize our advice.",
                    "Vaccination",
                    "https://www.androidhive.info/2016/01/android-working-with-recycler-view/"
            );
            recommendationArrayList.add(recommendation);
        }
        return recommendationArrayList;
    }

}
