package com.ctg.fitgram.network;

/**
 * Created by syeds on 11/27/2017.
 */

import android.content.SharedPreferences;

import com.ctg.fitgram.model.Data;
import com.ctg.fitgram.model.DataUser;
import com.ctg.fitgram.model.Response;
import com.ctg.fitgram.model.ResponseTimeline;
import com.ctg.fitgram.model.User;
import com.ctg.fitgram.model.UserResponseModel;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.Part;



public interface RetrofitInterface
{

    @POST("users")
    Observable<Response> register(@Body User user);

    @POST("authenticate")
    Observable<Response> login();

    @GET("users/{email}")
    Observable<User> getProfile(@Path("email") String email);

    @PUT("users/{email}")
    Observable<Response> changePassword(@Path("email") String email, @Body User user);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);

    //for getting list of friends
    @GET("users/{email}/friend_list")
    //Call<Response> findOne(@Path("email") String email);
    Call<Response>  friendList(@Path("email") String email);


    //for searching for friends
      @GET("find_friends/{email}")
      Call<UserResponseModel> findFriends(@Path("email") String email);

    //for adding a friend with email
    @PUT("users/{emailOfUser}/make_friend/{emailofFriend}")
    Call<Response> makeFriend(@Path("emailOfUser") String user,@Path("emailofFriend") String email);

    //for updating user
    @FormUrlEncoded
    @PUT("users/update/{email}")
    Call<Response> updateUser(@Field("name") String name,@Field("age") Integer age,@Field("city") String city, @Path("email") String email);


    //for putting rewards and achivements
    @FormUrlEncoded
    @PUT("users/ar/{email}")
    Call<Response> updateUsersAR(@Field("rewards") Integer rewards,@Field("achievements") Integer achievements, @Path("email") String email);

    //for putting user activity
    @FormUrlEncoded
    @PUT("users/updateActivity/{email}")
    Call<Response> updateUserActivity(@Path("email") String email,@Field("date") Integer date,@Field("walkingSteps") Integer walkingSteps,@Field("walkingDistance") Integer walkingDistance,@Field("runningTime") Integer runningTime,@Field("runningDistance") Integer runningDistance,@Field("bikingTime") Integer bikingTime,@Field("bikingDistance") Integer bikingDistance,@Field("loc") JSONObject loc );
    //Call<Response> updateUserActivity(@Path("email") String email, @Field("date") Integer date, @Field("walkingSteps") Integer walkingSteps, @Field("walkingDistance") Integer walkingDistance, @Field("runningTime") Integer runningTime, @Field("runningDistance") Integer runningDistance, @Field("bikingTime") Integer bikingTime, @Field("bikingDistance") Integer bikingDistance, @Field("loc")ArrayList loc);

   //for getting activity
    @GET("/users/{email}/activity")
    Call<ResponseTimeline> getUserActivity(@Path("email") String email);

//for getting user
    @GET("/users")
    Call<Response> find();

    //for image
    @Multipart
    @POST("/upload/images/{email}")
    Call<Response> uploadImage(@Part MultipartBody.Part image ,@Path("email") String email);





}