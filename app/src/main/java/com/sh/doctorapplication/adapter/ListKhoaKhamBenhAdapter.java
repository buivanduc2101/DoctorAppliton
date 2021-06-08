package com.sh.doctorapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.model.MedicalExam;

import java.util.List;

public class ListKhoaKhamBenhAdapter extends RecyclerView.Adapter<ListKhoaKhamBenhAdapter.KhoaKhamBenhViewHolder> {
    private final Context mContext;
    private final List<MedicalExam> listKhoaKhamBenhs;
    private final OnKhoaKhamBenhItemClickListener listener;

    public ListKhoaKhamBenhAdapter(Context mContext, List<MedicalExam> listKhoaKhamBenhs, OnKhoaKhamBenhItemClickListener listener) {
        this.mContext = mContext;
        this.listKhoaKhamBenhs = listKhoaKhamBenhs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KhoaKhamBenhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_khoa_kham_benh, parent, false);
        return new KhoaKhamBenhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhoaKhamBenhViewHolder holder, int position) {
        MedicalExam model = listKhoaKhamBenhs.get(position);
        holder.tvName.setText(model.getName());
        holder.tvStartTime.setText(model.getStartTime());
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        if (listKhoaKhamBenhs != null) {
            return listKhoaKhamBenhs.size();
        } else {
            return 0;
        }
    }

    public static class KhoaKhamBenhViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvStartTime;

        public KhoaKhamBenhViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameKhoaKhamBenhItem);
            tvStartTime = itemView.findViewById(R.id.tvThoiGianKhoaKhamBenhItem);
        }

        public void bind(final MedicalExam model, final OnKhoaKhamBenhItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(model));
        }

    }

    public interface OnKhoaKhamBenhItemClickListener {
        void onClickItem(MedicalExam model);
    }

}
