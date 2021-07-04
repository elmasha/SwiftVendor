package com.gas.swiftel.Model;

public class StkQuey {

    private String MerchantRequestID;
    private String stkCallback;
    private String CheckoutRequestID;
    private int ResponseCode;
    private String ResultDesc;
    private String ResponseDescription;
    private String ResultCode;

    public String getMerchantRequestID() {
        return MerchantRequestID;
    }

    public String getStkCallback() {
        return stkCallback;
    }

    public String getCheckoutRequestID() {
        return CheckoutRequestID;
    }

    public int getResponseCode() {
        return ResponseCode;
    }

    public String getResultDesc() {
        return ResultDesc;
    }

    public String getResponseDescription() {
        return ResponseDescription;
    }

    public String getResultCode() {
        return ResultCode;
    }
}
