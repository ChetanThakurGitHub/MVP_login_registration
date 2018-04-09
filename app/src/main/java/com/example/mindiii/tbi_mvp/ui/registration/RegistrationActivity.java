package com.example.mindiii.tbi_mvp.ui.registration;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mindiii.tbi_mvp.R;
import com.example.mindiii.tbi_mvp.model.UserFullDetail;
import com.example.mindiii.tbi_mvp.session.Session;
import com.example.mindiii.tbi_mvp.ui.home.HomeActivity;
import com.example.mindiii.tbi_mvp.util.Constant;
import com.example.mindiii.tbi_mvp.util.Utils;
import com.example.mindiii.tbi_mvp.vollyemultipart.AppHelper;
import com.example.mindiii.tbi_mvp.vollyemultipart.VolleyMultipartRequest;
import com.example.mindiii.tbi_mvp.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends Activity implements View.OnClickListener,RegistrationView{

    private EditText et_for_fullname, et_for_email, et_for_password, et_for_cpassword;
    private Session session;
    private ImageView iv_profile_image;
    private Bitmap profileImageBitmap;
    private RegistrationPresenter registrationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
        session = new Session(this);
        registrationPresenter= new RegistrationPresenterImpl(this,new RegistrationIntractorImpl());
    }

    private void initView() {
        findViewById(R.id.layout_for_signup).setOnClickListener(this);
        et_for_fullname = findViewById(R.id.et_for_fullname);
        et_for_email = findViewById(R.id.et_for_email);
        et_for_password = findViewById(R.id.et_for_password);
        et_for_cpassword = findViewById(R.id.et_for_cpassword);
        iv_profile_image = findViewById(R.id.iv_profile_image);
        findViewById(R.id.btn_for_signup).setOnClickListener(this);
        findViewById(R.id.layout_for_userImg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_for_signup:
                onBackPressed();
                break;
            case R.id.btn_for_signup:
                registrationPresenter.validationCondition(et_for_fullname.getText().toString(),et_for_email.getText().toString(),et_for_password.getText().toString(),et_for_cpassword.getText().toString());
                break;
            case R.id.layout_for_userImg:
                selectImage();
                break;
        }
    }

    private void selectImage() {

        final CharSequence[] items = {getString(R.string.text_take_photo), getString(R.string.text_chose_gellery), getString(R.string.text_cancel)};
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistrationActivity.this);
        alert.setTitle(getString(R.string.text_add_photo));
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.text_take_photo))) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(
                                    new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constant.MY_PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, Constant.CAMERA);
                        }
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, Constant.CAMERA);
                    }
                } else if (items[item].equals(getString(R.string.text_chose_gellery))) {

                    if (Build.VERSION.SDK_INT >= 23) {

                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, Constant.GALLERY);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, Constant.GALLERY);
                    }
                } else if (items[item].equals(getString(R.string.text_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.GALLERY && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            try {
                profileImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                if (profileImageBitmap != null) {
                    iv_profile_image.setPadding(0, 0, 0, 0);
                    iv_profile_image.setImageBitmap(profileImageBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (requestCode == Constant.CAMERA && resultCode == RESULT_OK) {
                profileImageBitmap = (Bitmap) data.getExtras().get("data");
                if (profileImageBitmap != null) {
                    iv_profile_image.setPadding(0, 0, 0, 0);
                    iv_profile_image.setImageBitmap(profileImageBitmap);
                }
            }
        }
    }


    private void doRegistration(final String fullName, final String email, final String password, final String cpassword) {

        if (Utils.isNetworkAvailable(this)) {

            final Dialog pDialog = new Dialog(this);
            Constant.myDialog(this, pDialog);
            pDialog.show();

            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.URL_WITHOUT_LOGIN + "userRegistration", new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String data = new String(response.data);

                    try {
                        JSONObject jsonObject = new JSONObject(data);

                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("success")) {

                            String userDetail = jsonObject.getString("userDetail");

                            UserFullDetail userFullDetail = new Gson().fromJson(userDetail, UserFullDetail.class);
                            userFullDetail.password = password;

                            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));

                        } else {
                            Constant.snackbar(et_for_cpassword, message);
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
                    Constant.snackbar(et_for_cpassword, networkResponse + "");
                    pDialog.dismiss();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", fullName);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("confirm_password", cpassword);
                    params.put("userType", "1");

                    if (profileImageBitmap == null) {
                        params.put("profileImage", "");
                    }
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    if (profileImageBitmap != null) {
                        params.put("profileImage", new VolleyMultipartRequest.DataPart("profilePic.jpg", AppHelper.getFileDataFromDrawable(profileImageBitmap), "image/jpeg"));
                    }
                    return params;
                }
            };
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(RegistrationActivity.this).addToRequestQueue(multipartRequest);
        } else {
            Constant.snackbar(et_for_cpassword, getResources().getString(R.string.check_net_connection));
        }
    }

    @Override
    public void setFullnameError() {
        Constant.snackbar(et_for_cpassword, getString(R.string.fullname_v));
    }

    @Override
    public void setFullMinimumError() {
        Constant.snackbar(et_for_cpassword, getString(R.string.fullname_required));
    }

    @Override
    public void setEmailError() {
        Constant.snackbar(et_for_cpassword, getString(R.string.email_v));
    }

    @Override
    public void setEmailErrorV() {
        Constant.snackbar(et_for_cpassword, getString(R.string.valid_email));
    }

    @Override
    public void setPasswordError() {
        Constant.snackbar(et_for_cpassword, getString(R.string.password_v));
    }

    @Override
    public void setPasswordReqError() {
        Constant.snackbar(et_for_cpassword, getString(R.string.password_required));
    }

    @Override
    public void setConfirmPasswordError() {
        Constant.snackbar(et_for_cpassword, getString(R.string.cpassword_v));
    }

    @Override
    public void setConfirmPasswordReqError() {
        Constant.snackbar(et_for_cpassword, getString(R.string.cpassword_required));
    }

    @Override
    public void setPasswordMatchError() {
        Constant.snackbar(et_for_cpassword, getString(R.string.password_match));
    }

    @Override
    protected void onDestroy() {
        registrationPresenter.onDistroy();
        super.onDestroy();
    }

    @Override
    public void setNevigetToHome() {
        doRegistration(et_for_fullname.getText().toString(), et_for_email.getText().toString(), et_for_password.getText().toString(), et_for_cpassword.getText().toString().trim());

    }
}
