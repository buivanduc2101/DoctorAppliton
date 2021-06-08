package com.sh.doctorapplication.ui.activity.user;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.adapter.user.HistoryUserAdapter;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.model.VisitExamination;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;
import com.sh.doctorapplication.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryStatisticUserActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSearch;
    private ProgressDialog progressDialog;
    private RelativeLayout rllFromDateSearch, rllToDateSearch;
    private TextView tvFromDate, tvToDate, tvTotalAppoint, tvTotalCancelAppoint, tvTotalPrice;

    private RecyclerView rcvHistory;
    private HistoryUserAdapter adapter;
    private List<VisitExamination> listModel;

    private String fromDateStr, toDateStr;

    private UserDetail userDetail;
    private ApiService apiService;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichsu_tuvan_user);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lịch sử tư vấn");

        initViews();
        initData();
        initAdapter();
    }

    private void initData() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        preferences = new MySharedPreferences(HistoryStatisticUserActivity.this);
        userDetail = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);

        Calendar calendar = Calendar.getInstance();   // this takes current date
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        fromDateStr = StringFormatUtils.convertDateToString(calendar.getTime());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        toDateStr = StringFormatUtils.convertDateToString(cal.getTime());

        tvFromDate.setText(fromDateStr);
        tvToDate.setText(toDateStr);
    }

    private void initAdapter() {
        listModel = new ArrayList<>();
        adapter = new HistoryUserAdapter(this, listModel, model -> {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(Const.VISIT_EXAM_HISTORY_SELECTED, model);
            Intent mIntent = new Intent(HistoryStatisticUserActivity.this, DetailHistoryUserActivity.class);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        });

        rcvHistory.setAdapter(adapter);
        buildAllHistory(fromDateStr, toDateStr);
    }

    private void initViews() {
        tvToDate = this.findViewById(R.id.tvToDateStatistic);
        btnSearch = this.findViewById(R.id.btnSearchStatistic);
        tvFromDate = this.findViewById(R.id.tvFromDateStatistic);
        tvTotalCancelAppoint = this.findViewById(R.id.tv_total_cancel_appoint);
        tvTotalAppoint = this.findViewById(R.id.tv_total_appoint);
        tvTotalPrice = this.findViewById(R.id.tv_total_price);
        rllToDateSearch = this.findViewById(R.id.rllToDateStatistic);
        rllFromDateSearch = this.findViewById(R.id.rllFromDateStatistic);
        rcvHistory = this.findViewById(R.id.rcv_history);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvHistory.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(HistoryStatisticUserActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        rllFromDateSearch.setOnClickListener(this);
        rllToDateSearch.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    private void buildAllHistory(String startDate, String endDate) {
        if (NetworkUtils.haveNetwork(HistoryStatisticUserActivity.this)) {
            showProgressDialog();

            Call<List<VisitExamination>> callLogin = apiService.getAllVisitExamHistoryOfPatient(userDetail.getPatient().getUser().getId(), startDate, endDate, null);
            callLogin.enqueue(new Callback<List<VisitExamination>>() {
                @Override
                public void onResponse(Call<List<VisitExamination>> call, Response<List<VisitExamination>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<VisitExamination> data = response.body();
                        if (!data.isEmpty()) {
                            listModel.clear();
                            listModel.addAll(data);

                            Long totalPrice = 0L;
                            int cntCancel = 0;
                            int cntTotal = 0;
                            for (VisitExamination exam : listModel) {

                                if (exam.getStatus().equals(Const.VisitExamStatus.CANCELED)) {
                                    cntCancel++;
                                } else {
                                    totalPrice += exam.getPriceExam();
                                    cntTotal++;
                                }
                            }
                            tvTotalPrice.setText(totalPrice + "");
                            tvTotalCancelAppoint.setText(cntCancel + "");
                            tvTotalAppoint.setText(cntTotal + "");
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(HistoryStatisticUserActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onFailure(Call<List<VisitExamination>> call, Throwable t) {
                    Toast.makeText(HistoryStatisticUserActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(HistoryStatisticUserActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
            hiddenProgressDialog();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rllFromDateStatistic: {
                onClickFromDateStatistic();
                break;
            }
            case R.id.rllToDateStatistic: {
                onClickToDateStatistic();
                break;
            }
            case R.id.btnSearchStatistic: {
                onClickSearchStatistic();
                break;
            }
        }
    }

    private void onClickFromDateStatistic() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    String date = (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth)
                            + "/" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "/" + year1;
                    tvFromDate.setText(date);
                    fromDateStr = date;
                }, year, month, day);
        datePickerDialog.show();
    }

    private void onClickToDateStatistic() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    String date = (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth)
                            + "/" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "/" + year1;
                    tvToDate.setText(date);
                    toDateStr = date;
                }, year, month, day);
        datePickerDialog.show();
    }

    private void onClickSearchStatistic() {
        buildAllHistory(fromDateStr, toDateStr);
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
