package com.gas.swiftel;

import com.gas.swiftel.Remote.FCMClient;
import com.gas.swiftel.Remote.IFCMService;
import com.gas.swiftel.Remote.IGoogleAPI;
import com.gas.swiftel.Remote.RetrofitClient;

public class Common {


    public static String currentToken ="";
    public static final String  baseUrl = "https://maps.googleapis.com";
    public static String BASE_URL = "https://gasmpesa.herokuapp.com";

    public static IGoogleAPI getGoogleAPI(){
        return RetrofitClient.getClient(baseUrl).create(IGoogleAPI.class);
    }
    public static final String  fcmUrl = "https://fcm.googleapis.com";

    public static IFCMService getFCMService(){
        return  FCMClient.getClient(fcmUrl).create(IFCMService.class);
    }



    





}
