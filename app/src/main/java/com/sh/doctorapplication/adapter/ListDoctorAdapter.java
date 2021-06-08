package com.sh.doctorapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.model.Doctor;
import com.sh.doctorapplication.utils.StringFormatUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListDoctorAdapter extends RecyclerView.Adapter<ListDoctorAdapter.DoctorViewHolder> {
    private final Context mContext;
    private final List<Doctor> listDoctors;
    private final OnBacSiItemClickListener listener;

    public ListDoctorAdapter(Context mContext, List<Doctor> listDoctors, OnBacSiItemClickListener listener) {
        this.mContext = mContext;
        this.listDoctors = listDoctors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor model = listDoctors.get(position);
        holder.tvName.setText(model.getUser().getFullName());
        holder.tvDonVi.setText(model.getMedicalExam().getHospital().getName());
        holder.tvPrice.setText(StringFormatUtils.convertToStringMoneyVND(model.getPrice()));
        holder.tvStar.setText(String.valueOf(model.getQuantityStar()));
        holder.tvStatus.setText(model.getStatus());
        holder.tvFrequency.setText(model.getFrequency());
        holder.tvQuantityPersonWaiting.setText("Số người chờ: " + model.getQuantityPersonWait());
        Picasso.get().load(model.getLinkImage()).placeholder(R.drawable.ic_thumbnail)
                .error(R.drawable.ic_thumbnail).into(holder.imvImage);
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        if (listDoctors != null) {
            return listDoctors.size();
        } else {
            return 0;
        }
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvDonVi, tvPrice, tvStar, tvStatus, tvFrequency, tvQuantityPersonWaiting;
        protected ImageView imvImage;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameDoctorItem);
            tvStar = itemView.findViewById(R.id.tvStartDoctorItem);
            tvDonVi = itemView.findViewById(R.id.tvDonViDoctorItem);
            tvPrice = itemView.findViewById(R.id.tvPriceDoctorItem);
            tvStatus = itemView.findViewById(R.id.tvStatusDoctorItem);
            imvImage = itemView.findViewById(R.id.imvImageDoctorItem);
            tvFrequency = itemView.findViewById(R.id.tvFrequencyDoctorItem);
            tvQuantityPersonWaiting = itemView.findViewById(R.id.tvSoNguoiChoDoctorItem);
        }

        public void bind(final Doctor model, final OnBacSiItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(model));
        }

    }

    public interface OnBacSiItemClickListener {
        void onClickItem(Doctor model);
    }

}