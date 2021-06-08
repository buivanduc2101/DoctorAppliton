package com.sh.doctorapplication.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.network.request.LoginRequest;
import com.sh.doctorapplication.ui.activity.doctor.DoctorDashboardActivity;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressDialog progressDialog;
    private EditText edtUsername, edtPassword;

    private ApiService apiService;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        checkUserInSharePreference();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void checkUserInSharePreference() {
        if (preferences == null) {
            preferences = new MySharedPreferences(LoginActivity.this);
        }
        if (apiService == null) {
            apiService = RetrofitClient.getClient().create(ApiService.class);
        }
        UserDetail userDetail = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);
        if (userDetail != null) {
            if (userDetail.getDoctor() != null) {
                startActivity(new Intent(LoginActivity.this, DoctorDashboardActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            finish();
        }
    }

    private void initView() {
        tvRegister = this.findViewById(R.id.tvRegisterLogin);
        edtUsername = this.findViewById(R.id.edtUsernameLogin);
        edtPassword = this.findViewById(R.id.edtPassLogin);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnLogin = this.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        btnLogin.setTransformationMethod(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRegisterLogin: {
                Intent mIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.btnLogin: {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.vui_long_nhap_day_du_thong_tin), Toast.LENGTH_SHORT).show();
                    return;
                }
                checkUserLogin(username, password);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                break;
            }
        }
    }

    private void checkUserLogin(String username, String password) {
        if (NetworkUtils.haveNetwork(LoginActivity.this)) {
            showProgressDialog();

            LoginRequest request = LoginRequest.builder()
                    .username(username)
                    .password(password)
                    .build();
            Call<UserDetail> callLogin = apiService.login(request);
            callLogin.enqueue(new Callback<UserDetail>() {
                @Override
                public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDetail userDetail = response.body();
                        preferences.putUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN, userDetail);
                        if (userDetail.getDoctor() != null) {
                            startActivity(new Intent(LoginActivity.this, DoctorDashboardActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.taikhoan_matkhau_khong_dung), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onFailure(Call<UserDetail> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.dang_nhap_thai_bai), Toast.LENGTH_SHORT).show();
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
            hiddenProgressDialog();
        }
    }

    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hiddenProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
