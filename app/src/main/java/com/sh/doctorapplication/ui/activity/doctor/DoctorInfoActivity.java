package com.sh.doctorapplication.ui.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.config.MySharedPreferences;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.ui.nhombenh.DoctorDetailActivity;
import com.sh.doctorapplication.utils.Const;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView imvAvatar;
    private TextView tv_name, tv_hospital;
    private TextView tv_xemThongTinCaNhan, tv_capnhatThongTinCaNhan, tvViTienDoctor;

    private UserDetail userDetail;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin cá nhân");

        initView();
        initData();
    }

    private void initView() {
        imvAvatar = this.findViewById(R.id.imv_avatar_doctor_info);
        tv_name = this.findViewById(R.id.tv_name_doctor_info);
        tv_hospital = this.findViewById(R.id.tv_name_hospital_info);
        tv_xemThongTinCaNhan = this.findViewById(R.id.tv_xemThongTinCaNhan);
        tv_capnhatThongTinCaNhan = this.findViewById(R.id.tv_capnhatThongTinCaNhan);
        tvViTienDoctor = this.findViewById(R.id.tvViTienDoctor);

        tv_xemThongTinCaNhan.setOnClickListener(this);
        tv_capnhatThongTinCaNhan.setOnClickListener(this);
        tvViTienDoctor.setOnClickListener(this);
    }

    private void initData() {
        preferences = new MySharedPreferences(this);
        userDetail = preferences.getUserDetailLogin(Const.KEY_SHARE_PREFERENCE.USER_LOGIN);

        tv_name.setText(userDetail.getDoctor().getUser().getFullName());
        tv_hospital.setText(userDetail.getDoctor().getMedicalExam().getHospital().getName());
        Picasso.get().load(userDetail.getDoctor().getLinkImage()).placeholder(R.drawable.ic_app)
                .error(R.drawable.ic_app).into(imvAvatar);
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
        switch (v.getId()) {
            case R.id.tv_xemThongTinCaNhan: {
                onClickViewDoctorInfo();
                break;
            }
            case R.id.tv_capnhatThongTinCaNhan: {
                onClickUpdateDoctorInfo();
                break;
            }
            case R.id.tvViTienDoctor: {
                onClickViTien();
                break;
            }
        }
    }

    private void onClickViTien() {
        Intent mIntent = new Intent(DoctorInfoActivity.this, MoneyAccountDoctorActivity.class);
        startActivity(mIntent);
    }

    private void onClickViewDoctorInfo() {
        Bundle mBundle = new Bundle();
        mBundle.putInt(Const.DOCTOR_SELECTED_TYPE, 1);
        mBundle.putParcelable(Const.DOCTOR_SELECTED, userDetail.getDoctor());
        Intent mIntent = new Intent(DoctorInfoActivity.this, DoctorDetailActivity.class);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    private void onClickUpdateDoctorInfo() {
        Intent mIntent = new Intent(DoctorInfoActivity.this, UpdateDoctorInfoActivity.class);
        startActivity(mIntent);
    }
}
