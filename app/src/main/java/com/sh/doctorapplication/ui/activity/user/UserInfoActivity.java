package com.sh.doctorapplication.ui.activity.user;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.model.Patient;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import java.text.SimpleDateFormat;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Patient patient;
    private UserDetail userDetail;
    private ApiService apiService;
    private MySharedPreferences preferences;

    private TextView tvUsername, tvFullName, tvAddress, tvPhoneNumber, tvBirthday, tvUpdateInfo;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin cá nhân");
        setContentView(R.layout.activity_user_detail_info);
        initViews();
        initData();
    }

    private void initViews() {
        tvUsername = this.findViewById(R.id.tvUsernameUserInfo);
        tvFullName = this.findViewById(R.id.tvFullNameUserInfo);
        tvAddress = this.findViewById(R.id.tvAddressUserInfo);
        tvPhoneNumber = this.findViewById(R.id.tvPhoneUserInfo);
        tvBirthday = this.findViewById(R.id.tvBirthdayUserInfo);
        tvUpdateInfo = this.findViewById(R.id.tvUpdateUserInfo);

        tvUpdateInfo.setOnClickListener(this);
    }

    private void initData() {
        preferences = new MySharedPreferences(UserInfoActivity.this);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        userDetail = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);

        if (NetworkUtils.haveNetwork(UserInfoActivity.this)) {
            Call<Patient> callLogin = apiService.getPatient(userDetail.getPatient().getUser().getId());
            callLogin.enqueue(new Callback<Patient>() {
                @Override
                public void onResponse(Call<Patient> call, Response<Patient> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        patient = response.body();
                        setDataToView(patient);
                    }
                }

                @Override
                public void onFailure(Call<Patient> call, Throwable t) {
                    Toast.makeText(UserInfoActivity.this, getResources().getString(R.string.dang_nhap_thai_bai), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UserInfoActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataToView(Patient patient) {
        tvUsername.setText(patient.getUser().getUsername());
        tvFullName.setText(patient.getUser().getFullName());
        tvAddress.setText(patient.getUser().getAddress());
        tvPhoneNumber.setText(patient.getUser().getMobile());
        tvBirthday.setText(patient.getUser().getBirthDay());
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
        if (v.getId() == R.id.tvUpdateUserInfo) {
            Intent mIntent = new Intent(UserInfoActivity.this, UpdateUserInfoActivity.class);
            startActivity(mIntent);
        }
    }
}
