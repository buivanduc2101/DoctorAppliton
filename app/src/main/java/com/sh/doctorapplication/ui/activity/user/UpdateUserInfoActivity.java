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
import com.sh.doctorapplication.network.request.PatientRequest;
import com.sh.doctorapplication.ui.activity.MainActivity;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;
import com.sh.doctorapplication.utils.StringFormatUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnConfirm;
    private ProgressDialog progressDialog;
    private EditText edtUsername, edtFullName, edtPhone, edtAddress, edtBirthDay;


    private ApiService apiService;
    private UserDetail userDetail;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập nhật");
        setContentView(R.layout.activity_update_user_info);

        initView();
        initData();
        setDataToView();
    }


    private void initData() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        preferences = new MySharedPreferences(this);
        userDetail = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);
    }

    private void setDataToView() {
        edtUsername.setEnabled(false);
        edtBirthDay.setEnabled(false);
        edtUsername.setText(userDetail.getPatient().getUser().getUsername());
        edtFullName.setText(userDetail.getPatient().getUser().getFullName());
        edtAddress.setText(userDetail.getPatient().getUser().getAddress());
        edtPhone.setText(userDetail.getPatient().getUser().getMobile());
        edtBirthDay.setText(userDetail.getPatient().getUser().getBirthDay());
    }

    private void initView() {
        edtUsername = this.findViewById(R.id.edtUsernameUpdateInfo);
        edtFullName = this.findViewById(R.id.edtFullNameUpdateInfo);
        edtPhone = this.findViewById(R.id.edtPhoneUpdateInfo);
        edtAddress = this.findViewById(R.id.edtAddressUpdateInfo);
        edtBirthDay = this.findViewById(R.id.edtBirthDayUpdateInfo);
        btnConfirm = this.findViewById(R.id.btnUpdateUserInfo);

        progressDialog = new ProgressDialog(UpdateUserInfoActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnConfirm.setOnClickListener(this);
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
        if (v.getId() == R.id.btnUpdateUserInfo) {
            onClickConfirmUpdate();
        }
    }

    private void onClickConfirmUpdate() {
        String fullName = edtFullName.getText().toString();
        String phone = edtPhone.getText().toString();
        String address = edtAddress.getText().toString();

        if (fullName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (NetworkUtils.haveNetwork(UpdateUserInfoActivity.this)) {
            showProgressDialog();

            PatientRequest request = PatientRequest.builder()
                    .address(address)
                    .mobile(phone)
                    .fullName(fullName)
                    .build();

            Call<UserDetail> callLogin = apiService.updatePatient(userDetail.getPatient().getUser().getId(), request);
            callLogin.enqueue(new Callback<UserDetail>() {
                @Override
                public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        preferences.putUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN, response.body());
                        Toast.makeText(UpdateUserInfoActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                        Intent mIntent = new Intent(UpdateUserInfoActivity.this, MainActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        Toast.makeText(UpdateUserInfoActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onFailure(Call<UserDetail> call, Throwable t) {
                    Toast.makeText(UpdateUserInfoActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(UpdateUserInfoActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
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
