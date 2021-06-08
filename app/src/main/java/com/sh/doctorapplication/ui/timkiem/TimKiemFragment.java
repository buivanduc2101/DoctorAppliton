package com.sh.doctorapplication.ui.timkiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.network.request.SearchDoctorRequest;
import com.sh.doctorapplication.ui.nhombenh.ListDoctorActivity;
import com.sh.doctorapplication.utils.Const;

public class TimKiemFragment extends Fragment implements View.OnClickListener {
    private View root;
    private Button btnSearch;
    private EditText edtDoctorName, edtHospitalName, edtBenh, edtKhoa, edtMoney;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tim_kiem, container, false);
        initView();
        return root;
    }

    private void initView() {
        edtDoctorName = root.findViewById(R.id.edtDoctorNameSearch);
        edtHospitalName = root.findViewById(R.id.edtHospitalNameSearch);
        edtBenh = root.findViewById(R.id.edtBenhSearch);
        edtKhoa = root.findViewById(R.id.edtKhoaSearch);
        edtMoney = root.findViewById(R.id.edtMoneySearch);
        btnSearch = root.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.btnSearch == v.getId()) {
            onClickSearch();
        }
    }

    private void onClickSearch() {
        String doctorName = edtDoctorName.getText().toString();
        String hospitalName = edtHospitalName.getText().toString();
        String benh = edtBenh.getText().toString();
        String khoa = edtKhoa.getText().toString();
        String money = edtMoney.getText().toString();

        SearchDoctorRequest request = new SearchDoctorRequest();
        request.setDoctorName(doctorName);
        request.setHospitalName(hospitalName);
        request.setMedicalExamName(khoa);
        request.setSickName(benh);
        request.setPrice(money.isEmpty() ? null : Long.valueOf(money));

        Bundle mBundle = new Bundle();
        mBundle.putParcelable(Const.SEARCH_DOCTOR_INFO, request);
        Intent mIntent = new Intent(requireActivity(), ListDoctorActivity.class);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);

        Toast.makeText(requireActivity(), "Đang thực hiện tìm kiếm", Toast.LENGTH_SHORT).show();
    }
}