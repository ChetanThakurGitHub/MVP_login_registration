package com.example.mindiii.tbi_mvp.ui.login;

/**
 * Created by mindiii on 2/4/18.
 */

public interface LoginView {
    void showProgress();
    void hiderProgress();
    void setUserNameError();
    void setUserNameVError();
    void setPasswordError();
    void navigateToHome();
    void navigateToRegistration();
}
