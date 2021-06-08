package com.sh.doctorapplication.ui.activity.user;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.model.VisitExamination;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.StringFormatUtils;

import java.util.Objects;

public class DetailHistoryUserActivity extends AppCompatActivity {

    private TextView tvDoctorName, tvHospitalName, tvKhoa, tvStatus, tvPrice;
    private TextView tvMoTa, tvKetLuan, tvBenh;

    private VisitExamination visitExamination;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_exam_user);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết lịch sử");

        initData();
        initViews();
        setDataToView();
    }

    private void setDataToView() {
        tvBenh.setText(visitExamination.getSickName());
        tvMoTa.setText(visitExamination.getDescription());
        tvKetLuan.setText(visitExamination.getSickStatus());
        tvDoctorName.setText(visitExamination.getDoctor().getUser().getFullName());
        tvKhoa.setText(visitExamination.getDoctor().getMedicalExam().getName());
        tvStatus.setText(StringFormatUtils.generateStatusVisitExam(visitExamination.getStatus()));
        tvPrice.setText(StringFormatUtils.convertToStringMoneyVND(visitExamination.getPriceExam()));
        tvHospitalName.setText(visitExamination.getDoctor().getMedicalExam().getHospital().getName());
    }

    private void initData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            visitExamination = mBundle.getParcelable(Const.VISIT_EXAM_HISTORY_SELECTED);
        }
    }

    private void initViews() {
        tvDoctorName = this.findViewById(R.id.tvDoctorNameHistoryDetailUser);
        tvHospitalName = this.findViewById(R.id.tvHospitalNameHistoryDetailUser);
        tvKhoa = this.findViewById(R.id.tvKhoaHistoryDetailUser);
        tvStatus = this.findViewById(R.id.tvStatusHistoryDetailUser);
        tvPrice = this.findViewById(R.id.tvPriceHistoryDetailUser);
        tvKetLuan = this.findViewById(R.id.tvKetLuanHistoryDetailUser);
        tvMoTa = this.findViewById(R.id.tvMoTaHistoryDetailUser);
        tvBenh = this.findViewById(R.id.tvBenhHistoryDetailUser);
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
