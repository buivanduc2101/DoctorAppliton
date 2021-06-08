package com.sh.doctorapplication.ui.nhombenh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.model.Doctor;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.model.VisitExamination;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.network.request.VisitExamRegisterRequest;
import com.sh.doctorapplication.ui.activity.MainActivity;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmRegisterVisitExamActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtDescription;
    private Button btnRegisterMedicalExam;
    private ProgressDialog progressDialog;
    private CircleImageView imvDoctorImage;
    private TextView tvDateRegister, tvTimeRegister;
    private TextView tvDoctorName, tvDoctorHospital, tvDoctorMedical;
    private ImageView imvDate, imvTime;

    private Doctor doctorModel;
    private UserDetail userDetail;
    private ApiService apiService;
    private MySharedPreferences preferences;

    private String dateStr;
    private String timeStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Xác nhận đăng ký");
        setContentView(R.layout.activity_confirm_register_medical_exam);
        initViews();
        initData();
    }

    private void initViews() {
        edtDescription = this.findViewById(R.id.edtDescriptionConfirmRegister);
        imvDoctorImage = this.findViewById(R.id.imvDoctorImage);
        tvDoctorName = this.findViewById(R.id.tvDoctorName);
        tvDoctorHospital = this.findViewById(R.id.tvDoctorHospital);
        tvDoctorMedical = this.findViewById(R.id.tvDoctorMedical);
        tvDateRegister = this.findViewById(R.id.tvDateRegister);
        tvTimeRegister = this.findViewById(R.id.tvTimeRegister);
        btnRegisterMedicalExam = this.findViewById(R.id.btnRegisterMedicalExam);
        imvDate = this.findViewById(R.id.imvDateCalendar);
        imvTime = this.findViewById(R.id.imvTimeCalendar);

        progressDialog = new ProgressDialog(ConfirmRegisterVisitExamActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        imvDate.setOnClickListener(this);
        imvTime.setOnClickListener(this);
        tvDateRegister.setOnClickListener(this);
        tvTimeRegister.setOnClickListener(this);
        btnRegisterMedicalExam.setOnClickListener(this);
    }

    private void initData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            doctorModel = mBundle.getParcelable(Const.DOCTOR_SELECTED_CONFIRM);

            tvDoctorName.setText(doctorModel.getUser().getFullName());
            tvDoctorHospital.setText(doctorModel.getMedicalExam().getHospital().getName());
            tvDoctorMedical.setText(doctorModel.getMedicalExam().getName());
        }
        apiService = RetrofitClient.getClient().create(ApiService.class);
        preferences = new MySharedPreferences(this);
        userDetail = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date mDate = new Date();

        dateStr = dateFormat.format(mDate);
        timeStr = "09:00";

        tvDateRegister.setText(dateStr);
        tvTimeRegister.setText(timeStr);

        Picasso.get().load(doctorModel.getLinkImage()).placeholder(R.drawable.ic_app)
                .error(R.drawable.ic_app).into(imvDoctorImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvDateCalendar: {
                onClickDateRegister();
                break;
            }
            case R.id.imvTimeCalendar: {
                onClickTimeRegister();
                break;
            }
            case R.id.tvDateRegister: {
                onClickDateRegister();
                break;
            }

            case R.id.tvTimeRegister: {
                onClickTimeRegister();
                break;
            }

            case R.id.btnRegisterMedicalExam: {
                onClickConfirm();
                break;
            }
        }
    }

    private void onClickDateRegister() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    dateStr = (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth)
                            + "/" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "/" + year;
                    tvDateRegister.setText(dateStr);
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    private void onClickTimeRegister() {
        Calendar mCalendar = Calendar.getInstance();

        TimePickerDialog timeDialog = TimePickerDialog.newInstance(
                (view, hourOfDay, minute, second) -> {
                    String time = (hourOfDay < 10 ? ("0" + hourOfDay) : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute);
                    tvTimeRegister.setText(time);
                    timeStr = tvTimeRegister.getText().toString();
                },
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                true
        );
        timeDialog.show(getSupportFragmentManager(), "Timepickerdialog");
    }

    @SneakyThrows
    private void onClickConfirm() {

        if (NetworkUtils.haveNetwork(ConfirmRegisterVisitExamActivity.this)) {
            showProgressDialog();

            String description = edtDescription.getText().toString();
            String createdDateStr = dateStr + " " + timeStr;

            VisitExamRegisterRequest request = VisitExamRegisterRequest.builder()
                    .doctorId(doctorModel.getUser().getId())
                    .patientId(userDetail.getPatient().getUser().getId())
                    .status(Const.VisitExamStatus.REGISTER)
                    .examinationTime(createdDateStr)
                    .description(description)
                    .build();

            Call<VisitExamination> callLogin = apiService.createVisitExam(request);
            callLogin.enqueue(new Callback<VisitExamination>() {
                @Override
                public void onResponse(Call<VisitExamination> call, Response<VisitExamination> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(ConfirmRegisterVisitExamActivity.this, "Đăng ký khám bệnh thành công", Toast.LENGTH_SHORT).show();
                        Intent mIntent = new Intent(ConfirmRegisterVisitExamActivity.this, MainActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        Toast.makeText(ConfirmRegisterVisitExamActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onFailure(Call<VisitExamination> call, Throwable t) {
                    Toast.makeText(ConfirmRegisterVisitExamActivity.this, getResources().getString(R.string.dang_nhap_thai_bai), Toast.LENGTH_SHORT).show();
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(ConfirmRegisterVisitExamActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
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
