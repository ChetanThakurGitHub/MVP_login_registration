package com.example.mindiii.tbi_mvp.ui.login;

import com.example.mindiii.tbi_mvp.ui.registration.RegistrationActivity;

/**
 * Created by mindiii on 3/4/18.
 */

public interface LoginIntractor {

    interface OnLoginFinishedListener {

        void onUsernameError();

        void onUsernameVError();

        void onPassowrdError();

        void onSuccess();

        void onNavigator();
    }

    void login(String username, String password, OnLoginFinishedListener listener);
}
