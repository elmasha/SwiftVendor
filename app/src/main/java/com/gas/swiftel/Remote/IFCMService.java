package com.gas.swiftel.Remote;

import com.gas.swiftel.Model.FCMResponse;
import com.gas.swiftel.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:Key=AAAAAlxFWgbY:APA91bENFqSyF5TrfnPuuIliA3lXEEsBf3rAn9U2JW7_gsu-pwoESFc04gXDCqh-nV2YmASi9Cu9_NvqHlhKLR1vqqrykq71LRS6oN9n_VRVmOi4ZG3cFaizdhyWV7V_MK_g4nF0_FQF"
    })

    @POST("fcm/send")
    Call<FCMResponse> sendMessgae(@Body Sender body);
}