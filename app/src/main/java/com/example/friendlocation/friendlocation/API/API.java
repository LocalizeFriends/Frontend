package com.example.friendlocation.friendlocation.API;

import com.example.friendlocation.friendlocation.JavaClasses.Model.ApiCall;
import com.example.friendlocation.friendlocation.JavaClasses.ListModels.FriendsLocationList;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Meeting;
import com.example.friendlocation.friendlocation.JavaClasses.ListModels.MeetupProposalList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

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
    private static String url = "http://192.168.1.100:3000";//"http://172.16.42.73:3000";//*/"https://localizefriends.ct8.pl";
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
        @POST("/api/location")
        Call<ApiCall> sendApiCall(@Field("fbtoken") String token, @Field("lat") double lat, @Field("lng") double lng );
        @FormUrlEncoded
        @POST("/api/meetup_proposal")
        Call<Meeting> sendMeetupProposal(@Field("fbtoken") String token, @Field("name") String name,
                                         @Field("timestamp_ms")long timestamp,
                                         @Field("place_name") String placeName,
                                         @Field("lng") double lng, @Field("lat") double lat,
                                         @Field("invite") String inviteList);
        @FormUrlEncoded
        @POST("/api/cloud_messaging_address")
        Call<String> sendMessagingAddress(@Field("fbtoken") String token,
                                          @Field("address") String address,
                                          @Field("expiration_time_ms") long expirationTimeMS);

        @FormUrlEncoded
        @POST("/api/meetup_proposal/{meetup_id}/cancel")
        Call<String> sendMeetingCancel(@Field("fbtoken") String token, @Field("value") Boolean value, @Path("meetup_id") int id);

        @FormUrlEncoded
        @POST("/api/meetup_proposal/{meetup_id}/accept")
        Call<String> sendMeetingAccept(@Field("fbtoken") String token, @Field("value") int value, @Path("meetup_id") int id);


        @GET("/api/meetup_proposals")
        Call<MeetupProposalList> getMeetings(@Query("fbtoken") String fbtoken);

        @GET("/api/friends_locations")
        Call<FriendsLocationList> getFriendsLocation(@Query("fbtoken") String fbtoken);

        @GET ("/api/friends_within_range")
        Call<FriendsLocationList> getFriendsWithinRange(@Query("fbtoken") String fbtoken,
                                                        @Query("lng") double lng,
                                                        @Query("lat") double lat,
                                                        @Query("radius") int meters);

    }
}

