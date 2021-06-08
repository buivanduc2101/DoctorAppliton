package com.sh.doctorapplication.ui.activity.doctor;

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
import com.sh.doctorapplication.network.request.DoctorUpdateRequest;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDoctorInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnUpdateDoctorInfo;
    private ProgressDialog progressDialog;
    private EditText edtPrice, edtDegree, edtExperience, edtSpecialize, edtLearningProcess;
    private EditText edtWorkingProcess, edtSickCanDo, edtNghienCuuKhoaHoc, edtGiangDayHuongDan;

    private ApiService apiService;
    private UserDetail userDetail;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doctor_info);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập nhật thông tin");

        initViews();
        initData();
        setDataToView();
    }

    private void initViews() {
        edtPrice = this.findViewById(R.id.edtPrice);
        edtDegree = this.findViewById(R.id.edtDegree);
        edtExperience = this.findViewById(R.id.edtExperience);
        edtSpecialize = this.findViewById(R.id.edtSpecialize);
        edtLearningProcess = this.findViewById(R.id.edtLearningProcess);
        edtWorkingProcess = this.findViewById(R.id.edtWorkingProcess);
        edtSickCanDo = this.findViewById(R.id.edtSickCanDo);
        edtNghienCuuKhoaHoc = this.findViewById(R.id.edtNghienCuuKhoaHoc);
        edtGiangDayHuongDan = this.findViewById(R.id.edtGiangDayHuongDan);
        btnUpdateDoctorInfo = this.findViewById(R.id.btnUpdateDoctorInfo);

        progressDialog = new ProgressDialog(UpdateDoctorInfoActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnUpdateDoctorInfo.setOnClickListener(this);
    }

    private void initData() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        preferences = new MySharedPreferences(this);
        userDetail = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);

    }

    private void setDataToView() {
        edtPrice.setText(userDetail.getDoctor().getPrice() != null ? userDetail.getDoctor().getPrice().toString() : "");
        edtDegree.setText(userDetail.getDoctor().getDegree());
        edtExperience.setText(userDetail.getDoctor().getExperience() != null ? userDetail.getDoctor().getExperience().toString() : "");
        edtSpecialize.setText(userDetail.getDoctor().getSpecialize());
        edtLearningProcess.setText(userDetail.getDoctor().getLearningProcess());
        edtWorkingProcess.setText(userDetail.getDoctor().getWorkingProcess());
        edtSickCanDo.setText(userDetail.getDoctor().getSickCanDo());
        edtNghienCuuKhoaHoc.setText(userDetail.getDoctor().getNghienCuuKhoaHoc());
        edtGiangDayHuongDan.setText(userDetail.getDoctor().getGiangDayHuongDan());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdateDoctorInfo) {
            onClickConfirmUpdate();
        }
    }

    private void onClickConfirmUpdate() {
        String price = edtPrice.getText().toString();
        String degree = edtDegree.getText().toString();
        String experience = edtExperience.getText().toString();
        String specialize = edtSpecialize.getText().toString();
        String learningProcess = edtLearningProcess.getText().toString();
        String workingProcess = edtWorkingProcess.getText().toString();
        String sickCanDo = edtSickCanDo.getText().toString();
        String nghienCuuKhoaHoc = edtNghienCuuKhoaHoc.getText().toString();
        String giangDayHuongDan = edtGiangDayHuongDan.getText().toString();

        if (price.isEmpty() || degree.isEmpty() || experience.isEmpty() || specialize.isEmpty() || learningProcess.isEmpty()
                || workingProcess.isEmpty() || sickCanDo.isEmpty() || nghienCuuKhoaHoc.isEmpty() || giangDayHuongDan.isEmpty()) {
            Toast.makeText(this, "Cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (NetworkUtils.haveNetwork(UpdateDoctorInfoActivity.this)) {
            showProgressDialog();

            DoctorUpdateRequest request = DoctorUpdateRequest.builder()
                    .price(Long.valueOf(price))
                    .degree(degree)
                    .experience(Long.valueOf(experience))
                    .specialize(specialize)
                    .workingProcess(workingProcess)
                    .learningProcess(learningProcess)
                    .sickCanDo(sickCanDo)
                    .nghienCuuKhoaHoc(nghienCuuKhoaHoc)
                    .giangDayHuongDan(giangDayHuongDan)
                    .build();

            Call<UserDetail> callLogin = apiService.updateDoctor(userDetail.getDoctor().getUser().getId(), request);
            callLogin.enqueue(new Callback<UserDetail>() {
                @Override
                public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        preferences.putUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN, response.body());
                        Toast.makeText(UpdateDoctorInfoActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                        Intent mIntent = new Intent(UpdateDoctorInfoActivity.this, DoctorDashboardActivity.class);
                        startActivity(mIntent);
                        finish();

                    } else {
                        Toast.makeText(UpdateDoctorInfoActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onFailure(Call<UserDetail> call, Throwable t) {
                    Toast.makeText(UpdateDoctorInfoActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(UpdateDoctorInfoActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
            hiddenProgressDialog();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
