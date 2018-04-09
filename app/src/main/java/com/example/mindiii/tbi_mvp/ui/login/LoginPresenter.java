package com.example.mindiii.tbi_mvp.ui.login;

/**
 * Created by mindiii on 2/4/18.
 */

public interface LoginPresenter {
    void validationCondition(String username ,String password);

    void onDistroy();
}
