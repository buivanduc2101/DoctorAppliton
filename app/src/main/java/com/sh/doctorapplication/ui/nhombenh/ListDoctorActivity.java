package com.sh.doctorapplication.ui.nhombenh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.adapter.ListDoctorAdapter;
import com.sh.doctorapplication.model.Doctor;
import com.sh.doctorapplication.model.GroupDisease;
import com.sh.doctorapplication.model.MedicalExam;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.network.request.SearchDoctorRequest;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDoctorActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    private RecyclerView rcvDoctor;
    private List<Doctor> bacSiModelList;
    private ListDoctorAdapter adapter;

    private GroupDisease groupDisease;
    private MedicalExam medicalExam;
    private SearchDoctorRequest searchDoctorRequest;

    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Danh sách bác sĩ");

        setContentView(R.layout.activity_list_doctor);
        initData();
        initViews();
        initAdapter();
    }


    private void initData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            groupDisease = mBundle.getParcelable(Const.NHOM_BENH_SELECTED);
            medicalExam = mBundle.getParcelable(Const.KHOA_KHAM_BENH_SELECTED);
            searchDoctorRequest = mBundle.getParcelable(Const.SEARCH_DOCTOR_INFO);
        }
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private void initViews() {
        progressDialog = new ProgressDialog(ListDoctorActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        rcvDoctor = this.findViewById(R.id.rcvDoctor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListDoctorActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvDoctor.setItemAnimator(new DefaultItemAnimator());
        rcvDoctor.setLayoutManager(layoutManager);
    }

    private void initAdapter() {
        bacSiModelList = new ArrayList<>();
        adapter = new ListDoctorAdapter(ListDoctorActivity.this, bacSiModelList, model -> {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(Const.DOCTOR_SELECTED, model);
            Intent mIntent = new Intent(ListDoctorActivity.this, DoctorDetailActivity.class);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        });
        rcvDoctor.setAdapter(adapter);
        buildAllDoctors();
    }

    private void buildAllDoctors() {
        if (NetworkUtils.haveNetwork(ListDoctorActivity.this)) {
            showProgressDialog();
            if (groupDisease != null) {
                Call<List<Doctor>> call = apiService.getAllDoctorInGroupDisease(groupDisease.getId());
                call.enqueue(new Callback<List<Doctor>>() {
                    @Override
                    public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Doctor> models = response.body();
                            if (!models.isEmpty()) {
                                bacSiModelList.clear();
                                bacSiModelList.addAll(models);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        hiddenProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<List<Doctor>> call, Throwable t) {
                        hiddenProgressDialog();
                    }
                });
            } else if (medicalExam != null) {
                Call<List<Doctor>> call = apiService.getAllDoctorByMedicalExam(medicalExam.getHospital().getId(), medicalExam.getId());
                call.enqueue(new Callback<List<Doctor>>() {
                    @Override
                    public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Doctor> models = response.body();
                            if (!models.isEmpty()) {
                                bacSiModelList.clear();
                                bacSiModelList.addAll(models);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        hiddenProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<List<Doctor>> call, Throwable t) {
                        hiddenProgressDialog();
                    }
                });
            } else {
                String doctorName = searchDoctorRequest.getDoctorName();
                String hospitalName = searchDoctorRequest.getHospitalName();
                String medicalExamName = searchDoctorRequest.getMedicalExamName();
                Long price = searchDoctorRequest.getPrice();
                String sickName = searchDoctorRequest.getSickName();

                Call<List<Doctor>> call = apiService.getAllDoctorBySearchValue(doctorName, hospitalName, medicalExamName, price, sickName);
                call.enqueue(new Callback<List<Doctor>>() {
                    @Override
                    public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Doctor> models = response.body();
                            if (!models.isEmpty()) {
                                bacSiModelList.clear();
                                bacSiModelList.addAll(models);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        hiddenProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<List<Doctor>> call, Throwable t) {
                        hiddenProgressDialog();
                    }
                });
            }
        } else {
            Toast.makeText(ListDoctorActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
