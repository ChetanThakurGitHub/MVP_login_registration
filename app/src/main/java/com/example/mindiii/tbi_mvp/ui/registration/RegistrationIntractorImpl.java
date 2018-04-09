package com.example.mindiii.tbi_mvp.ui.registration;


public class RegistrationIntractorImpl implements RegistrationIntractor {


    private String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


    @Override
    public void registration(String fullname, String email, String password, String cPassword, onRegitrationFinishedListener listener) {

        if (fullname.equalsIgnoreCase("")) {
            listener.onFullnameError();
            return;
        }
        if (fullname.length() < 3) {
            listener.onFullnameMinError();
            return;
        }

        if (email.equalsIgnoreCase("")) {
            listener.onEmailError();
            return;
        }

        if (!email.matches(emailPattern)) {
            listener.onEmailErrorVali();
            return;
        }

        if (password.equalsIgnoreCase("")) {
            listener.onPasswordError();
            return;
        }

        if (password.length()<6){
            listener.onPasswordRequ();
            return;
        }

        if (cPassword.equalsIgnoreCase("")) {
            listener.onCPasswordError();
            return;
        }

        if (password.length()<6){
            listener.onCPasswordReqError();
            return;
        }

        if(!password.matches(cPassword)){
            listener.onPasswordMatchError();
            return;
        }
        listener.onSuccess();
    }
}
