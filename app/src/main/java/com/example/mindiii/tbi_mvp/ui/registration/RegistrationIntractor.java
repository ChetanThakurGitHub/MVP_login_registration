package com.example.mindiii.tbi_mvp.ui.registration;

import android.app.Activity;

/**
 * Created by mindiii on 5/4/18.
 */

public interface RegistrationIntractor {
    interface onRegitrationFinishedListener{
        void onFullnameError();
        void onFullnameMinError();
        void onEmailError();
        void onEmailErrorVali();
        void onPasswordError();
        void onPasswordRequ();
        void onCPasswordError();
        void onCPasswordReqError();
        void onPasswordMatchError();
        void onSuccess();
        void onNavigator();
    }

    void registration(String fullname, String email, String password, String cPassword , onRegitrationFinishedListener listener);
}
