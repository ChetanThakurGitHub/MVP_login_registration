package com.example.mindiii.tbi_mvp.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mindiii.tbi_mvp.R;
import com.example.mindiii.tbi_mvp.ui.home.HomeActivity;
import com.example.mindiii.tbi_mvp.model.UserFullDetail;
import com.example.mindiii.tbi_mvp.ui.registration.RegistrationActivity;
import com.example.mindiii.tbi_mvp.util.Constant;
import com.example.mindiii.tbi_mvp.util.Utils;
import com.example.mindiii.tbi_mvp.vollyemultipart.VolleyMultipartRequest;
import com.example.mindiii.tbi_mvp.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements View.OnClickListener,LoginView {

    private EditText username;
    private EditText password;
    private LoginPresenter loginPrasenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        findViewById(R.id.btn_for_login).setOnClickListener(this);
        findViewById(R.id.layout_for_signup).setOnClickListener(this);

        loginPrasenter = new LoginPresenterImpl(this,new LoginIntractorImpl());
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.btn_for_login:
               loginPrasenter.validationCondition(username.getText().toString(),password.getText().toString());
               break;
           case R.id.layout_for_signup:
               navigateToRegistration();
               break;
       }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hiderProgress() {

    }

    @Override
    public void setUserNameError() {
        Toast.makeText(this, R.string.email_v, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserNameVError() {
        Toast.makeText(this, "Wrong email", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPasswordError() {
        Toast.makeText(this, R.string.password_v, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        doLogin(username.getText().toString(),password.getText().toString());
    }

    @Override
    public void navigateToRegistration() {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    @Override
    protected void onDestroy() {
        loginPrasenter.onDistroy();
        super.onDestroy();
    }

    public void doLogin(final String email, final String password) {

        if (Utils.isNetworkAvailable(this)) {

            final Dialog pDialog = new Dialog(this);
            Constant.myDialog(this, pDialog);
            pDialog.show();

            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.URL_WITHOUT_LOGIN + "userLogin", new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String data = new String(response.data);

                    try {
                        JSONObject jsonObject = new JSONObject(data);

                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject userDetail = jsonObject.getJSONObject("userDetail");

                            UserFullDetail userFullDetail = new Gson().fromJson(userDetail.toString(), UserFullDetail.class);
                            userFullDetail.password = password;
                            if (userFullDetail.status.equals("1")) {

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();

                            } else {
                               // Constant.snackbar(password, "Your account has been inactivated by admin, please contact to activate");
                            }

                        } else {
                            //Constant.snackbar(password, message);
                        }

                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                    }
                    pDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                   // Constant.snackbar(mainLayout, networkResponse + "");
                    pDialog.dismiss();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("email", email);
                    params.put("password", password);
                    params.put("userType", "1");

                    return params;
                }
            };

            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(multipartRequest);
        } else {
            //Constant.snackbar(mainLayout, getResources().getString(R.string.check_net_connection));
        }
    }
}
