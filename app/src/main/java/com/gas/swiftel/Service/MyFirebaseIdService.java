package com.gas.swiftel.Service;

import com.gas.swiftel.Model.Token;
import com.gas.swiftel.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseIdService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Common.currentToken = FirebaseInstanceId.getInstance().getToken();
        upDateTokenService(Common.currentToken);

    }




    private void upDateTokenService(String currentToken) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tokens = db.collection("Tokens");
        CollectionReference vendorRef = db.collection("Gas_Vendor");

        String refreshtoken = FirebaseInstanceId.getInstance().getToken();
        Map<String,Object> UpdateToken = new HashMap<>();
        UpdateToken.put("token_id",refreshtoken);


        Token token = new Token(currentToken);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            tokens.document(uid).set(token);
            vendorRef.document(uid).update(UpdateToken);
        }
    }
}
