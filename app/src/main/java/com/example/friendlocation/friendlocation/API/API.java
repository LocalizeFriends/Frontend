package com.example.friendlocation.friendlocation.API;

import com.example.friendlocation.friendlocation.JavaClasses.ApiCall;
import com.example.friendlocation.friendlocation.JavaClasses.FriendsLocationList;
import com.example.friendlocation.friendlocation.JavaClasses.Meeting;
import com.example.friendlocation.friendlocation.JavaClasses.FriendLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;
import retrofit2.http.Query;

public class API {

    private static APIInterface apiInterface;
    private static String url = "http://192.168.0.160:3000";
    private static String baseUrl = "http://localhost:3000";
    public static APIInterface getClient() {
        if (apiInterface == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(
                            new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request().newBuilder()
                                            .addHeader("Accept", "application/json").build();
                                    return chain.proceed(request);
                                }
                            }).build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
            apiInterface = client.create(APIInterface.class);
        }
        return apiInterface;
    }

    public interface APIInterface {
        @FormUrlEncoded
        @POST("/api/Location")
        Call<ApiCall> sendApiCall(@Field("fbtoken") String token, @Field("lat") double lat, @Field("lng") double lng );
        @POST("/api/meetup_proposal")
        Call<Meeting> sendMeeting(@Body Meeting meeting);

        @GET("/api/meetings")
        Call<List<Meeting>> getMeetings();

        @GET("/api/friends_locations")
        Call<FriendsLocationList> getFriendsLocation(@Query("fbtoken") String fbtoken);
    }
}

