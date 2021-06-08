package com.sh.doctorapplication.ui.activity.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.sh.doctorapplication.adapter.ListAutoNumberHistoryAdapter;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.model.Hospital;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutomaticNumberHospitalActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rcvHospital;
    private List<Hospital> hospitals;
    private ProgressDialog progressDialog;
    private ListAutoNumberHistoryAdapter adapter;


    private View mViewDlgResult;
    private AlertDialog resultDialog;
    private Button btnDoneAutoNumber;
    private TextView tvNameHospital, tvResultAutoNumber;

    private ApiService apiService;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_number_hospital);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lấy số khám tự động");

        initData();
        initViews();
        initAdapter();
        initDialogResult();
    }

    private void initData() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        preferences = new MySharedPreferences(this);
    }

    private void initViews() {
        rcvHospital = this.findViewById(R.id.rcvHospitalAutoNumber);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AutomaticNumberHospitalActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvHospital.setItemAnimator(new DefaultItemAnimator());
        rcvHospital.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(AutomaticNumberHospitalActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initDialogResult() {
        resultDialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog).create();
        mViewDlgResult = getLayoutInflater().inflate(R.layout.dialog_auto_number, null);

        tvNameHospital = mViewDlgResult.findViewById(R.id.tvNameHospital);
        tvResultAutoNumber = mViewDlgResult.findViewById(R.id.tvResultAutoNumber);
        btnDoneAutoNumber = mViewDlgResult.findViewById(R.id.btnDoneAutoNumber);

        resultDialog.setView(mViewDlgResult);
        btnDoneAutoNumber.setOnClickListener(this);
    }

    private void initAdapter() {
        hospitals = new ArrayList<>();
        adapter = new ListAutoNumberHistoryAdapter(AutomaticNumberHospitalActivity.this, hospitals, new ListAutoNumberHistoryAdapter.OnClickHospitalListener() {
            @Override
            public void onClickGetNumber(Hospital model) {
                if (NetworkUtils.haveNetwork(AutomaticNumberHospitalActivity.this)) {
                    showProgressDialog();
                    Call<Hospital> call = apiService.getAutoNumberHospital(model.getId());
                    call.enqueue(new Callback<Hospital>() {
                        @Override
                        public void onResponse(Call<Hospital> call, Response<Hospital> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Hospital hospital = response.body();
                                setDataDialog(hospital);

                                String keyHospital = Const.KEY_SHARE_PREFERENCE.GET_NUMBER_HOSPITAL_PREFIX + hospital.getId();
                                preferences.putHospital(keyHospital, hospital);

                                hiddenProgressDialog();
                                Toast.makeText(AutomaticNumberHospitalActivity.this, "Lấy số thứ tự thành công", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Hospital> call, Throwable t) {
                            hiddenProgressDialog();
                            Toast.makeText(AutomaticNumberHospitalActivity.this, "Có lỗi xảy ra, liên hệ admin", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(AutomaticNumberHospitalActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onClickViewNumber(Hospital model) {
                String keyHospital = Const.KEY_SHARE_PREFERENCE.GET_NUMBER_HOSPITAL_PREFIX + model.getId();
                Hospital hospital = preferences.getHospital(keyHospital);
                if (hospital != null) {
                    setDataDialog(hospital);
                } else {
                    Toast.makeText(AutomaticNumberHospitalActivity.this, "Bạn chưa lấy số trước đó", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rcvHospital.setAdapter(adapter);

        buildDataHospital();
    }

    private void buildDataHospital() {
        if (NetworkUtils.haveNetwork(AutomaticNumberHospitalActivity.this)) {
            showProgressDialog();
            Call<List<Hospital>> call = apiService.getAllHospitals();
            call.enqueue(new Callback<List<Hospital>>() {
                @Override
                public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Hospital> models = response.body();
                        if (!models.isEmpty()) {
                            hospitals.clear();
                            hospitals.addAll(models);
                        }
                        adapter.notifyDataSetChanged();
                        hiddenProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<List<Hospital>> call, Throwable t) {
                    hiddenProgressDialog();
                }
            });

        } else {
            Toast.makeText(AutomaticNumberHospitalActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void clearDialog() {
        tvNameHospital.setText("");
        tvResultAutoNumber.setText("");
    }

    private void setDataDialog(Hospital hospital) {
        tvNameHospital.setText(hospital.getName());
        tvResultAutoNumber.setText(hospital.getAutoNumber() + "");

        resultDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDoneAutoNumber) {
            resultDialog.dismiss();
            hiddenProgressDialog();
            clearDialog();
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