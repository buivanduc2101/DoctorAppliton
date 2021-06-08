package com.sh.doctorapplication.ui.activity.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.network.request.ChangePasswordRequest;
import com.sh.doctorapplication.ui.activity.LoginActivity;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnChangePassword;
    private ProgressDialog progressDialog;
    private EditText edtOldPassword, edtNewPassword, edtConfirmNewPassword;

    private UserDetail userLogin;
    private ApiService apiService;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đổi mật khẩu");
        initViews();
        initData();
    }

    private void initData() {
        preferences = new MySharedPreferences(this);
        userLogin = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);

        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private void initViews() {
        edtOldPassword = this.findViewById(R.id.edtOldPassword);
        edtNewPassword = this.findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = this.findViewById(R.id.edtConfirmNewPassword);
        btnChangePassword = this.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(this);

        progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnChangePassword) {
            String oldPassword = edtOldPassword.getText().toString();
            String newPassword = edtNewPassword.getText().toString();
            String confirmNewPassword = edtConfirmNewPassword.getText().toString();

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(this, "Cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(this, "Mật khẩu mới không trùng nhau", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!oldPassword.equals(userLogin.getPatient().getUser().getPassword())) {
                Toast.makeText(this, "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show();
                return;
            }

            if (NetworkUtils.haveNetwork(ChangePasswordActivity.this)) {
                showProgressDialog();

                ChangePasswordRequest request = ChangePasswordRequest.builder()
                        .userId(userLogin.getPatient().getUser().getId())
                        .newPassword(newPassword.trim())
                        .build();

                Call<UserDetail> callLogin = apiService.changePassword(request);
                callLogin.enqueue(new Callback<UserDetail>() {
                    @Override
                    public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            preferences.clearAllData();
                            Toast.makeText(ChangePasswordActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                            Intent mIntent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            startActivity(mIntent);
                            finish();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                        }
                        hiddenProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<UserDetail> call, Throwable t) {
                        Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                        hiddenProgressDialog();
                    }
                });
            } else {
                Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
                hiddenProgressDialog();
            }
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