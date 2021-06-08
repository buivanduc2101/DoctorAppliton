package com.sh.doctorapplication.ui.nhombenh;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.sh.doctorapplication.R;
import com.sh.doctorapplication.adapter.NhomBenhAdapter;
import com.sh.doctorapplication.model.GroupDisease;
import com.sh.doctorapplication.network.ApiService;
import com.sh.doctorapplication.network.RetrofitClient;
import com.sh.doctorapplication.utils.Const;
import com.sh.doctorapplication.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NhomBenhFragment extends Fragment {
    private View root;
    private ProgressBar progressBar;
    private ImageSlider imageSlider;
    private RecyclerView rcvNhomBenh;
    private NhomBenhAdapter nhomBenhAdapter;
    private List<GroupDisease> nhomBenhModelList;

    private ApiService apiService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_nhom_benh, container, false);
        initViews();
        initAdapter();
        return root;
    }

    private void initViews() {
        List<SlideModel> listImageSlideModels = new ArrayList<>();
        listImageSlideModels.add(new SlideModel("https://cdn.initial.com/content/local/vn-ini/images/desktop/main_health-and-wellness-hero-banner_desktop.jpg", "Sống vui, sống khỏe mỗi ngày", ScaleTypes.FIT));
        listImageSlideModels.add(new SlideModel("https://www.rafamedicals.com/wp-content/uploads/2018/10/banner-health-care-best-business-template-within-healthcare-it-banner.jpg", "Chăm sóc sức khỏe", ScaleTypes.FIT));
        listImageSlideModels.add(new SlideModel("https://i.ytimg.com/vi/wKHoaOzR1NA/maxresdefault.jpg", "Tạp chí sức khỏe và đời sống", ScaleTypes.FIT));
        listImageSlideModels.add(new SlideModel("https://thaoduocminhnhi.com/wp-content/uploads/2018/06/972013-102416-AM-8232013-100350-AM-banner.jpg", "Thảo dược chăm sóc sức khỏe bạn", ScaleTypes.FIT));

        imageSlider = root.findViewById(R.id.sliderNhomBenh);
        imageSlider.setImageList(listImageSlideModels);

        progressBar = root.findViewById(R.id.progress_bar);

        rcvNhomBenh = root.findViewById(R.id.rcvNhomBenh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvNhomBenh.setItemAnimator(new DefaultItemAnimator());
        rcvNhomBenh.setLayoutManager(layoutManager);

    }

    private void initAdapter() {
        nhomBenhModelList = new ArrayList<>();
        apiService = RetrofitClient.getClient().create(ApiService.class);

        nhomBenhAdapter = new NhomBenhAdapter(requireContext(), nhomBenhModelList, model -> {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(Const.NHOM_BENH_SELECTED, model);
            Intent mIntent = new Intent(requireActivity(), ListDoctorActivity.class);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        });
        rcvNhomBenh.setAdapter(nhomBenhAdapter);
        buildAllNhomBenh();
    }

    private void buildAllNhomBenh() {
        if (NetworkUtils.haveNetwork(requireContext())) {
            progressBar.setVisibility(View.VISIBLE);
            Call<List<GroupDisease>> call = apiService.getAllGroupDiseases();
            call.enqueue(new Callback<List<GroupDisease>>() {
                @Override
                public void onResponse(Call<List<GroupDisease>> call, Response<List<GroupDisease>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<GroupDisease> nhomBenhModels = response.body();
                        if (!nhomBenhModels.isEmpty()) {
                            nhomBenhModelList.clear();
                            nhomBenhModelList.addAll(nhomBenhModels);
                        }
                        nhomBenhAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<GroupDisease>> call, Throwable t) {

                }
            });


        } else {
            Toast.makeText(requireContext(), getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
    }
}