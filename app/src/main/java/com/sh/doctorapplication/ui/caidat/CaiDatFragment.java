package com.sh.doctorapplication.ui.caidat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.ui.activity.LoginActivity;
import com.sh.doctorapplication.ui.activity.user.AutomaticNumberHospitalActivity;
import com.sh.doctorapplication.ui.activity.user.ChangePasswordActivity;
import com.sh.doctorapplication.ui.activity.user.HistoryStatisticUserActivity;
import com.sh.doctorapplication.ui.activity.user.IntroduceActivity;
import com.sh.doctorapplication.ui.activity.user.MoneyAccountActivity;
import com.sh.doctorapplication.ui.activity.user.UserInfoActivity;

public class CaiDatFragment extends Fragment implements View.OnClickListener {
    private View root;
    private TextView tvUserInfo, tvHistory, tvIntroduce, tvChangePassword, tvLogout, tvMoney, tvGetIndexHospital;

    private MySharedPreferences preferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cai_dat, container, false);
        initViews();
        initData();
        return root;
    }

    private void initData() {
        preferences = new MySharedPreferences(requireActivity());
    }

    private void initViews() {
        tvUserInfo = root.findViewById(R.id.tvUserInfo);
        tvHistory = root.findViewById(R.id.tvHistory);
        tvIntroduce = root.findViewById(R.id.tvIntroduce);
        tvChangePassword = root.findViewById(R.id.tvChangePassword);
        tvLogout = root.findViewById(R.id.tvLogout);
        tvMoney = root.findViewById(R.id.tvMoney);
        tvGetIndexHospital = root.findViewById(R.id.tvGetIndexHospital);

        tvUserInfo.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
        tvIntroduce.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvMoney.setOnClickListener(this);
        tvGetIndexHospital.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUserInfo: {
                Intent mIntent = new Intent(requireActivity(), UserInfoActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.tvHistory: {
                Intent mIntent = new Intent(requireActivity(), HistoryStatisticUserActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.tvIntroduce: {
                Intent mIntent = new Intent(requireActivity(), IntroduceActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.tvChangePassword: {
                Intent mIntent = new Intent(requireActivity(), ChangePasswordActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.tvLogout: {
                preferences.clearAllData();
                Intent mIntent = new Intent(requireActivity(), LoginActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.tvMoney: {
                Intent mIntent = new Intent(requireActivity(), MoneyAccountActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.tvGetIndexHospital: {
                Intent mIntent = new Intent(requireActivity(), AutomaticNumberHospitalActivity.class);
                startActivity(mIntent);
                break;
            }
        }
    }
}