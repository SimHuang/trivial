package com.simhuang.trivial.service;

import com.simhuang.trivial.model.MashapeResults;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This service is used to generate random questions
 * when user selects random topic
 */
public interface MashapeService {

    @Headers({
            "Accept: application/json",
            "X-Mashape-Key: 2B22X5PJOBmsh8CKMX35DiazXEzrp131kmrjsndzoOExp07ku5"
    })
    @GET("/v1/query/trivia")
    Call<MashapeResults> getQuestions(@Query("count") int count);

    //Retrofit object
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ServiceURL.MASHAPE_SERVICE_ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
