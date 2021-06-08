package com.sh.doctorapplication.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.network.request.RegisterRequest;
import com.sh.doctorapplication.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private ProgressDialog progressDialog;
    private EditText edtName, edtAddress, edtPhone, edtUsername, edtPassword, edtBirthDay;

    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initInstance();
        clearData();
    }

    private void initInstance() {
        if (apiService == null) {
            apiService = RetrofitClient.getClient().create(ApiService.class);
        }
    }

    private void initViews() {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        edtName = this.findViewById(R.id.edtFullNameRegister);
        edtAddress = this.findViewById(R.id.edtAddressRegister);
        edtPhone = this.findViewById(R.id.edtPhoneNumberRegister);
        edtUsername = this.findViewById(R.id.edtUsernameRegister);
        edtPassword = this.findViewById(R.id.edtPassRegister);
        edtBirthDay = this.findViewById(R.id.edtBirthdayRegister);
        btnRegister = this.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
    }

    private void clearData() {
        edtName.setText("");
        edtAddress.setText("");
        edtPhone.setText("");
        edtUsername.setText("");
        edtPassword.setText("");
        edtBirthDay.setText("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            onClickRegister();
        }
    }

    private void onClickRegister() {
        String name = edtName.getText().toString();
        String address = edtAddress.getText().toString();
        String phoneNumber = edtPhone.getText().toString();
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String birthDay = edtBirthDay.getText().toString();

        if (name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest registerRequest = RegisterRequest.builder()
                .isDoctor(false)
                .fullName(name.trim())
                .address(address.trim())
                .password(password.trim())
                .mobile(phoneNumber.trim())
                .username(username.trim().toLowerCase())
                .birthDay(birthDay.trim())
                .build();

        if (NetworkUtils.haveNetwork(RegisterActivity.this)) {
            showProgressDialog();
            Call<UserDetail> callLogin = apiService.register(registerRequest);
            callLogin.enqueue(new Callback<UserDetail>() {
                @Override
                public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                        Intent mIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onFailure(Call<UserDetail> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
