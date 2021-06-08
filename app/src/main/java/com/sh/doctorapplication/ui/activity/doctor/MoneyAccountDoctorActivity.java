package com.sh.doctorapplication.ui.activity.doctor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.model.StatisticMoneyAccount;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;
import com.sh.doctorapplication.utils.StringFormatUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoneyAccountDoctorActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextView tvUsername, tvFullName, tvSoDu, tvQuantityExam, tvTotalMoneyExam;

    private UserDetail userLogin;
    private ApiService apiService;
    private MySharedPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surplus_money_user);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Số dư và nạp tiền");
        initViews();
        initData();
        setDataToView();
    }


    private void initViews() {
        tvUsername = this.findViewById(R.id.tvUsernameMoneyUser);
        tvFullName = this.findViewById(R.id.tvFullNameMoneyUser);
        tvSoDu = this.findViewById(R.id.tvSoDuMoneyUser);
        tvQuantityExam = this.findViewById(R.id.tvQuantityExamMoneyUser);
        tvTotalMoneyExam = this.findViewById(R.id.tvTotalMoneyExam);

        progressDialog = new ProgressDialog(MoneyAccountDoctorActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);
    }


    private void initData() {
        preferences = new MySharedPreferences(this);
        userLogin = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);

        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private void setDataToView() {
        tvUsername.setText(userLogin.getDoctor().getUser().getUsername());
        tvFullName.setText(userLogin.getDoctor().getUser().getFullName());

        if (NetworkUtils.haveNetwork(MoneyAccountDoctorActivity.this)) {
            showProgressDialog();

            Call<StatisticMoneyAccount> callLogin = apiService.getStatisticMoneyDoctor(userLogin.getDoctor().getUser().getId());
            callLogin.enqueue(new Callback<StatisticMoneyAccount>() {
                @Override
                public void onResponse(Call<StatisticMoneyAccount> call, Response<StatisticMoneyAccount> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        StatisticMoneyAccount statisticMoneyAccount = response.body();
                        tvQuantityExam.setText(statisticMoneyAccount.getQuantityExam() + "");
                        tvTotalMoneyExam.setText(StringFormatUtils.convertToStringMoneyVND(statisticMoneyAccount.getTotalMoneySpend()));
                        tvSoDu.setText(StringFormatUtils.convertToStringMoneyVND(statisticMoneyAccount.getAccount().getTotalMoney()));
                    } else {
                        Toast.makeText(MoneyAccountDoctorActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onFailure(Call<StatisticMoneyAccount> call, Throwable t) {
                    Toast.makeText(MoneyAccountDoctorActivity.this, getResources().getString(R.string.co_loi_xay_ra), Toast.LENGTH_SHORT).show();
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(MoneyAccountDoctorActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
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