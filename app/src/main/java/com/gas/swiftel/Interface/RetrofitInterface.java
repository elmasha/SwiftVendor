package com.gas.swiftel.Interface;

import com.gas.swiftel.Model.ResponseStk;
import com.gas.swiftel.Model.Result;
import com.gas.swiftel.Model.ResultStk;
import com.gas.swiftel.Model.StkQuey;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/stk")
    Call<ResponseStk> stk_push(@Body Map<String, Object> pushStk);

    @POST("/stkActivate")
    Call<ResponseStk> stk_pushActivate(@Body Map<String, Object> pushStk);

    @POST("/stkRemit")
    Call<ResponseStk> stk_pushRemit(@Body Map<String, Object> pushStk);


    @POST("/stk_callback")
    Call<ResultStk>  getResponse();


    @GET("/")
    Call<Result>  getResult();

    @POST("/stk/query")
    Call<StkQuey>   stk_Query(@Body Map<String, Object> stkQuey);

    @POST("/stkActivate/query")
    Call<StkQuey>   stk_QueryActivate(@Body Map<String, Object> stkQuey);

    @POST("/stkRemit/query")
    Call<StkQuey>   stk_QueryRemit(@Body Map<String, Object> stkQuey);


}
