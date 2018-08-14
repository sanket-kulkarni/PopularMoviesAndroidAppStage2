package com.example.sanketk.popularmoives.networking;

import com.example.sanketk.popularmoives.model.ApiResponse;
import com.example.sanketk.popularmoives.model.ReviewsResponse;
import com.example.sanketk.popularmoives.model.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    @GET("popular")
    Call<ApiResponse> getPopularMovies(@Query("api_key") String str);

    @GET("top_rated")
    Call<ApiResponse> getTopRatedMovies(@Query("api_key") String str);

    @GET("{id}/videos")
    Call<TrailersResponse> getMoviesTrailers(@Path("id")String id, @Query("api_key") String str);

    @GET("{id}/reviews")
    Call<ReviewsResponse> getMoviesReviews(@Path("id")String id, @Query("api_key") String str);
}
