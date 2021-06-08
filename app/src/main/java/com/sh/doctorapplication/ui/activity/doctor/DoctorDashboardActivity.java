package com.sh.doctorapplication.ui.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.ui.activity.LoginActivity;

public class DoctorDashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSchedule, btnLogoutDoctor, btnInfo, btnStatistic;

    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_doctor);
        initViews();
        initData();
    }

    private void initData() {
        preferences = new MySharedPreferences(this);
    }

    private void initViews() {
        btnSchedule = this.findViewById(R.id.btnMyScheduleDoctor);
        btnLogoutDoctor = this.findViewById(R.id.btnLogoutDoctor);
        btnStatistic = this.findViewById(R.id.btnStatisticDoctor);
        btnInfo = this.findViewById(R.id.btnMyInfoDoctor);

        btnSchedule.setTransformationMethod(null);
        btnLogoutDoctor.setTransformationMethod(null);
        btnStatistic.setTransformationMethod(null);
        btnInfo.setTransformationMethod(null);

        btnSchedule.setOnClickListener(this);
        btnLogoutDoctor.setOnClickListener(this);
        btnStatistic.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyScheduleDoctor: {
                Intent mIntent = new Intent(DoctorDashboardActivity.this, DoctorScheduleActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.btnStatisticDoctor: {
                Intent mIntent = new Intent(DoctorDashboardActivity.this, DoctorStatisticActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.btnMyInfoDoctor: {
                Intent mIntent = new Intent(DoctorDashboardActivity.this, DoctorInfoActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.btnLogoutDoctor: {
                preferences.clearAllData();
                Intent mIntent = new Intent(DoctorDashboardActivity.this, LoginActivity.class);
                startActivity(mIntent);
                finish();
            }
        }
    }
}
