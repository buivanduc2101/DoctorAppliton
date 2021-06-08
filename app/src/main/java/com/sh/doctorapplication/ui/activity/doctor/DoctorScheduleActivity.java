package com.sh.doctorapplication.ui.activity.doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.adapter.doctor.DoctorScheduleAdapter;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.model.VisitExamination;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.network.request.VisitExamUpdateRequest;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private RecyclerView rcvSchedule;
    private ProgressDialog progressDialog;
    private List<VisitExamination> listModel;
    private DoctorScheduleAdapter adapter;

    private View mViewDlgResult;
    private AlertDialog resultDialog;
    private TextView tv_name, tv_birthyear;
    private EditText edt_result, edt_sick_name;
    private Button btn_save, btn_cancel;

    private UserDetail userDetail;
    private ApiService apiService;
    private MySharedPreferences preferences;

    private VisitExamination modelSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedule);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lịch hẹn khám");

        initView();
        initData();
        initDialogResult();
        initAdapter();
    }

    private void initView() {
        progressDialog = new ProgressDialog(DoctorScheduleActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        rcvSchedule = this.findViewById(R.id.rcv_schedules);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DoctorScheduleActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvSchedule.setItemAnimator(new DefaultItemAnimator());
        rcvSchedule.setLayoutManager(layoutManager);
    }

    private void initAdapter() {
        listModel = new ArrayList<>();
        adapter = new DoctorScheduleAdapter(this, listModel, new DoctorScheduleAdapter.OnAppointItemClickListener() {

            @Override
            public void onClickDone(VisitExamination model) {
                modelSelected = model;
                setDataDialog();
                resultDialog.show();
            }

            @Override
            public void onClickCall(VisitExamination model) {
//                String phone = model.getPatient().getUser().getMobile();
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
//                startActivity(intent);

                String link = "http://zalo.me/" + model.getPatient().getUser().getMobile();
                Uri uri = Uri.parse(link); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

            @Override
            public void onClickCancel(VisitExamination model) {
                modelSelected = model;
                new AlertDialog.Builder(DoctorScheduleActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xác nhận").setMessage("Bạn chắc chắn hủy lịch khám này?")
                        .setPositiveButton("Đồng ý", (dialog, which) -> {
                            onClickSaveResult("Hủy bỏ lịch khám", "Bác sĩ hủy bỏ lịch khám", Const.VisitExamStatus.DONE, "Hủy bỏ thành công");
                            finish();
                        }).setNegativeButton("Hủy bỏ", null).show();
            }
        });
        rcvSchedule.setAdapter(adapter);

        buildAllSchedule();
    }

    private void buildAllSchedule() {
        if (NetworkUtils.haveNetwork(DoctorScheduleActivity.this)) {
            showProgressDialog();

            Call<List<VisitExamination>> callLogin = apiService.getAllVisitExamRegisterOfDoctor(userDetail.getDoctor().getUser().getId());
            callLogin.enqueue(new Callback<List<VisitExamination>>() {
                @Override
                public void onResponse(Call<List<VisitExamination>> call, Response<List<VisitExamination>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<VisitExamination> models = response.body();
                        if (!models.isEmpty()) {
                            listModel.clear();
                            listModel.addAll(models);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DoctorScheduleActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onFailure(Call<List<VisitExamination>> call, Throwable t) {
                    Toast.makeText(DoctorScheduleActivity.this, getResources().getString(R.string.dang_nhap_thai_bai), Toast.LENGTH_SHORT).show();
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(DoctorScheduleActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
            hiddenProgressDialog();
        }
    }

    private void initData() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        preferences = new MySharedPreferences(this);
        userDetail = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);
    }

    private void initDialogResult() {
        resultDialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog).create();
        mViewDlgResult = getLayoutInflater().inflate(R.layout.dialog_result, null);

        tv_name = mViewDlgResult.findViewById(R.id.tv_name_patient_re);
        tv_birthyear = mViewDlgResult.findViewById(R.id.tv_birthyear_patient_re);
        edt_result = mViewDlgResult.findViewById(R.id.edt_result);
        btn_save = mViewDlgResult.findViewById(R.id.btn_save_re);
        edt_sick_name = mViewDlgResult.findViewById(R.id.edt_sick_name);
        btn_cancel = mViewDlgResult.findViewById(R.id.btn_cancel_re);

        resultDialog.setView(mViewDlgResult);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    private void setDataDialog() {
        tv_name.setText("Bệnh nhân: " + modelSelected.getPatient().getUser().getFullName());
        tv_birthyear.setText("Ngày sinh: " + modelSelected.getPatient().getUser().getBirthDay());
    }

    private void clearDataDialog() {
        tv_name.setText("");
        tv_birthyear.setText("");
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
        switch (v.getId()) {
            case R.id.btn_save_re: {
                String result = edt_result.getText().toString();
                String sickName = edt_sick_name.getText().toString();

                if (result.isEmpty() || sickName.isEmpty()) {
                    Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                onClickSaveResult(result, sickName, Const.VisitExamStatus.DONE, "Kết luận bệnh thành công");
                resultDialog.dismiss();
                clearDataDialog();
                break;
            }
            case R.id.btn_cancel_re: {
                clearDataDialog();
                resultDialog.dismiss();
                break;
            }
        }
    }

    private void onClickSaveResult(String result, String sickName, String status, String message) {
        if (NetworkUtils.haveNetwork(DoctorScheduleActivity.this)) {
            showProgressDialog();

            VisitExamUpdateRequest request = VisitExamUpdateRequest.builder()
                    .sickName(sickName.trim())
                    .sickStatus(result.trim())
                    .status(status)
                    .build();

            Call<VisitExamination> callLogin = apiService.updateVisitExam(modelSelected.getId(), request);
            callLogin.enqueue(new Callback<VisitExamination>() {
                @Override
                public void onResponse(Call<VisitExamination> call, Response<VisitExamination> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(DoctorScheduleActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent mIntent = new Intent(DoctorScheduleActivity.this, DoctorDashboardActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        Toast.makeText(DoctorScheduleActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onFailure(Call<VisitExamination> call, Throwable t) {
                    Toast.makeText(DoctorScheduleActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(DoctorScheduleActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
            hiddenProgressDialog();
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
