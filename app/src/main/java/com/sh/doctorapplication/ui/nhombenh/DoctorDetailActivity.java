package com.sh.doctorapplication.ui.nhombenh;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.adapter.RateAdapter;
import com.sh.doctorapplication.model.Doctor;
import com.sh.doctorapplication.model.Rate;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;
import com.sh.doctorapplication.utils.StringFormatUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rcvRate;
    private CircleImageView imvImageDoctor;
    private TextView tv_number_waiting_patient, tv_rate_doctor, tv_name_doctor, tv_name_hospital, tv_price_doctor;
    private TextView tv_medical_register, tv_professinal_doctor, tv_degree_doctor, tv_depart_doctor;
    private TextView tv_hopital_doctor, tv_specialist, tv_description_doctor, tv_birthyear_doctor;
    private TextView tv_study_process_doctor, tv_working_process_doctor, tv_scientific_research_doctor;
    private RateAdapter rateAdapter;
    private List<Rate> rateModelList;

    private int typeView;
    private String dateStrSelected;
    private Doctor doctorModel;

    private ApiService apiService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_doctor);
        initData();
        initViews();
        initAdapter();
        setDataIntoViews();
    }

    private void initData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            doctorModel = mBundle.getParcelable(Const.DOCTOR_SELECTED);
            typeView = mBundle.getInt(Const.DOCTOR_SELECTED_TYPE);
        }
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private void setDataIntoViews() {
        tv_name_doctor.setText(doctorModel.getUser().getFullName());
        tv_name_hospital.setText(doctorModel.getMedicalExam().getHospital().getName());
        tv_price_doctor.setText(StringFormatUtils.convertToStringMoneyVND(doctorModel.getPrice()));
        tv_professinal_doctor.setText(doctorModel.getSpecialize());
        tv_degree_doctor.setText(doctorModel.getDegree());
        tv_depart_doctor.setText(doctorModel.getMedicalExam().getName());
        tv_birthyear_doctor.setText(doctorModel.getYearOfBirth());
        tv_specialist.setText(doctorModel.getSpecialize());
        tv_description_doctor.setText(doctorModel.getSickCanDo());
        tv_rate_doctor.setText(doctorModel.getQuantityStar().toString());
        tv_study_process_doctor.setText(doctorModel.getLearningProcess());
        tv_working_process_doctor.setText(doctorModel.getWorkingProcess());
        tv_scientific_research_doctor.setText(doctorModel.getNghienCuuKhoaHoc());

        Picasso.get().load(doctorModel.getLinkImage()).placeholder(R.drawable.ic_app)
                .error(R.drawable.ic_app).into(imvImageDoctor);

        if (1 == typeView) {
            tv_medical_register.setVisibility(View.GONE);
        } else {
            tv_medical_register.setVisibility(View.VISIBLE);
        }
    }


    private void initViews() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin bác sĩ");

        rcvRate = this.findViewById(R.id.rcvRateDoctor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DoctorDetailActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvRate.setItemAnimator(new DefaultItemAnimator());
        rcvRate.setLayoutManager(layoutManager);

        tv_number_waiting_patient = findViewById(R.id.tv_number_waiting_patient);
        imvImageDoctor = findViewById(R.id.imvImageDoctor);
        tv_rate_doctor = findViewById(R.id.tv_rate_doctor);
        tv_name_doctor = findViewById(R.id.tv_name_doctor);
        tv_name_hospital = findViewById(R.id.tv_name_hospital);
        tv_price_doctor = findViewById(R.id.tv_price_doctor);
        tv_medical_register = findViewById(R.id.tv_medical_register);
        tv_professinal_doctor = findViewById(R.id.tv_professinal_doctor);
        tv_degree_doctor = findViewById(R.id.tv_degree_doctor);
        tv_depart_doctor = findViewById(R.id.tv_depart_doctor);
        tv_hopital_doctor = findViewById(R.id.tv_hopital_doctor);
        tv_birthyear_doctor = findViewById(R.id.tv_birthyear_doctor);
        tv_specialist = findViewById(R.id.tv_specialist);
        tv_description_doctor = findViewById(R.id.tv_description_doctor);
        tv_study_process_doctor = findViewById(R.id.tv_study_process_doctor);
        tv_working_process_doctor = findViewById(R.id.tv_working_process_doctor);
        tv_scientific_research_doctor = findViewById(R.id.tv_scientific_research_doctor);

        tv_medical_register.setOnClickListener(this);
    }

    private void initAdapter() {
        rateModelList = new ArrayList<>();

        rateAdapter = new RateAdapter(this, rateModelList);
        rcvRate.setAdapter(rateAdapter);
        buildAllRate();
    }

    private void buildAllRate() {
        if (NetworkUtils.haveNetwork(DoctorDetailActivity.this)) {
            Call<List<Rate>> call = apiService.getAllRates();
            call.enqueue(new Callback<List<Rate>>() {
                @Override
                public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Rate> models = response.body();
                        if (!models.isEmpty()) {
                            rateModelList.clear();
                            rateModelList.addAll(models);
                        }
                        rateAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Rate>> call, Throwable t) {

                }
            });

        } else {
            Toast.makeText(DoctorDetailActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        if (v.getId() == R.id.tv_medical_register) {
            onClickRegisterMedicalExamDoctor();
        }
    }

    private void onClickRegisterMedicalExamDoctor() {
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(Const.DOCTOR_SELECTED_CONFIRM, doctorModel);
        Intent mIntent = new Intent(DoctorDetailActivity.this, ConfirmRegisterVisitExamActivity.class);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }
}
