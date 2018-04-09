package com.example.mindiii.tbi_mvp.ui.registration;

public class RegistrationPresenterImpl implements RegistrationPresenter ,RegistrationIntractor.onRegitrationFinishedListener{

    private RegistrationView registrationView;
    private RegistrationIntractor registrationIntractor;

    public RegistrationPresenterImpl(RegistrationView registrationView , RegistrationIntractor registrationIntractor){
        this.registrationView = registrationView;
        this.registrationIntractor = registrationIntractor;
    }

    @Override
    public void validationCondition(String fullname, String email, String password, String cPassword) {
            registrationIntractor.registration(fullname,email,password,cPassword,this);
    }

    @Override
    public void onDistroy() {
        registrationView = null;

    }

    @Override
    public void onFullnameError() {
        if (registrationView != null){
            registrationView.setFullnameError();
        }
    }

    @Override
    public void onFullnameMinError() {
        if (registrationView != null){
            registrationView.setFullMinimumError();
        }
    }

    @Override
    public void onEmailError() {
        if (registrationView != null){
            registrationView.setEmailError();
        }
    }

    @Override
    public void onEmailErrorVali() {
        if (registrationView != null){
            registrationView.setEmailErrorV();
        }
    }

    @Override
    public void onPasswordError() {
        if (registrationView != null){
            registrationView.setPasswordError();
        }
    }

    @Override
    public void onPasswordRequ() {
        if (registrationView != null){
            registrationView.setPasswordReqError();
        }
    }

    @Override
    public void onCPasswordError() {
        if (registrationView != null){
            registrationView.setConfirmPasswordError();
        }
    }

    @Override
    public void onCPasswordReqError() {
        if (registrationView != null){
            registrationView.setConfirmPasswordReqError();
        }
    }

    @Override
    public void onPasswordMatchError() {
        if (registrationView != null){
            registrationView.setPasswordMatchError();
        }
    }

    @Override
    public void onSuccess() {
        if (registrationView!= null) {
            registrationView.setNevigetToHome();
        }
    }

    @Override
    public void onNavigator() {
            registrationView.setNevigetToHome();
    }

}
