package com.example.mindiii.tbi_mvp.ui.registration;

public interface RegistrationPresenter {

    void validationCondition(String fullname, String email, String password,String cPassword );

    void onDistroy();

}
