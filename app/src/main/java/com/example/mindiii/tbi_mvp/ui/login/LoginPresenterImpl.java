package com.example.mindiii.tbi_mvp.ui.login;

public class LoginPresenterImpl implements LoginPresenter,LoginIntractor.OnLoginFinishedListener{

    private LoginView loginView;
    private LoginIntractor loginIntractor;

    public LoginPresenterImpl(LoginView loginView, LoginIntractor loginIntractor){
        this.loginView = loginView;
        this.loginIntractor = loginIntractor;
    }

    @Override
    public void validationCondition(String username, String password) {
        loginIntractor.login(username,password,this);
    }

    @Override
    public void onDistroy() {
        loginView = null;
    }

    @Override
    public void onUsernameError() {
        if (loginView != null){
            loginView.setUserNameError();
        }
    }

    @Override
    public void onUsernameVError() {
        if (loginView != null){
            loginView.setUserNameVError();
        }
    }

    @Override
    public void onPassowrdError() {
        if (loginView != null){
            loginView.setPasswordError();
        }
    }

    @Override
    public void onSuccess() {
            if (loginView != null){
        loginView.navigateToHome();
            }
    }

    @Override
    public void onNavigator() {
        loginView.navigateToRegistration();
    }


}
