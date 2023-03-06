package com.example.popuparnews.models.api;

import com.example.popuparnews.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}
