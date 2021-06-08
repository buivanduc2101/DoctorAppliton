package com.sh.doctorapplication.ui.benhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.adapter.BenhVienAdapter;
import com.sh.doctorapplication.model.Hospital;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BenhVienFragment extends Fragment {
    private View root;
    private ProgressBar progressBar;
    private RecyclerView rcvBenhVien;
    private BenhVienAdapter adapter;
    private List<Hospital> benhVienModelList;


    private ApiService apiService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_benh_vien, container, false);
        initViews();
        initAdapter();
        return root;
    }

    private void initViews() {
        progressBar = root.findViewById(R.id.progress_bar);
        rcvBenhVien = root.findViewById(R.id.rcvBenhVien);
        GridLayoutManager manager = new GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false);
        rcvBenhVien.setLayoutManager(manager);
    }

    private void initAdapter() {
        benhVienModelList = new ArrayList<>();
        apiService = RetrofitClient.getClient().create(ApiService.class);

        adapter = new BenhVienAdapter(requireContext(), benhVienModelList, model -> {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(Const.BENH_VIEN_SELECTED, model);
            Intent mIntent = new Intent(requireActivity(), ListKhoaKhamBenhActivity.class);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        });
        rcvBenhVien.setAdapter(adapter);
        buildAllBenhViens();
    }

    private void buildAllBenhViens() {
        if (NetworkUtils.haveNetwork(requireContext())) {
            progressBar.setVisibility(View.VISIBLE);
            Call<List<Hospital>> call = apiService.getAllHospitals();
            call.enqueue(new Callback<List<Hospital>>() {
                @Override
                public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Hospital> models = response.body();
                        if (!models.isEmpty()) {
                            benhVienModelList.clear();
                            benhVienModelList.addAll(models);
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<Hospital>> call, Throwable t) {

                }
            });

        } else {
            Toast.makeText(requireContext(), getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
    }
}