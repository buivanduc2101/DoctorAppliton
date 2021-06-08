package com.sh.doctorapplication.ui.benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.adapter.ListKhoaKhamBenhAdapter;
import com.sh.doctorapplication.model.Hospital;
import com.sh.doctorapplication.model.MedicalExam;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.ui.nhombenh.ListDoctorActivity;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListKhoaKhamBenhActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView rcvKhoaKhamBenh;
    private ListKhoaKhamBenhAdapter adapter;
    private List<MedicalExam> khoaKhamBenhModelList;

    private Hospital hospital;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_khoa_kham_benh);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Khoa khám bệnh");
        initData();
        initViews();
        initAdapter();
    }

    private void initData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            hospital = mBundle.getParcelable(Const.BENH_VIEN_SELECTED);
        }

        apiService = RetrofitClient.getClient().create(ApiService.class);

    }

    private void initViews() {
        progressBar = this.findViewById(R.id.progress_bar);

        rcvKhoaKhamBenh = this.findViewById(R.id.rcvKhoaKhamBenh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListKhoaKhamBenhActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvKhoaKhamBenh.setLayoutManager(layoutManager);
    }

    private void initAdapter() {
        khoaKhamBenhModelList = new ArrayList<>();
        adapter = new ListKhoaKhamBenhAdapter(ListKhoaKhamBenhActivity.this, khoaKhamBenhModelList, model -> {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(Const.KHOA_KHAM_BENH_SELECTED, model);
            Intent mIntent = new Intent(ListKhoaKhamBenhActivity.this, ListDoctorActivity.class);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        });
        rcvKhoaKhamBenh.setAdapter(adapter);
        buildAllKhoaKhamBenh();
    }

    private void buildAllKhoaKhamBenh() {
        if (NetworkUtils.haveNetwork(ListKhoaKhamBenhActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            Call<List<MedicalExam>> call = apiService.getAllMedicalExamInHospital(hospital.getId());
            call.enqueue(new Callback<List<MedicalExam>>() {
                @Override
                public void onResponse(Call<List<MedicalExam>> call, Response<List<MedicalExam>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<MedicalExam> models = response.body();
                        if (!models.isEmpty()) {
                            khoaKhamBenhModelList.clear();
                            khoaKhamBenhModelList.addAll(models);
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<MedicalExam>> call, Throwable t) {

                }
            });

        } else {
            Toast.makeText(ListKhoaKhamBenhActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
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

}
