package com.sh.doctorapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.model.Hospital;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAutoNumberHistoryAdapter extends RecyclerView.Adapter<ListAutoNumberHistoryAdapter.HospitalViewHolder> {
    private final Context mContext;
    private final List<Hospital> hospitals;
    private final OnClickHospitalListener listener;

    public ListAutoNumberHistoryAdapter(Context mContext, List<Hospital> hospitals, OnClickHospitalListener listener) {
        this.mContext = mContext;
        this.hospitals = hospitals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_auto_number_hospital, parent, false);
        return new HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        Hospital model = hospitals.get(position);
        holder.tvName.setText(model.getName());
        Picasso.get().load(model.getLinkImage()).placeholder(R.drawable.ic_thumbnail)
                .error(R.drawable.ic_thumbnail).into(holder.imvImage);
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        if (hospitals != null) {
            return hospitals.size();
        } else {
            return 0;
        }
    }

    public static class HospitalViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName;
        protected ImageView imvImage;
        protected Button btnGetNumberAutoNumber, btnViewNumberAutoNumber;

        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvHospitalNameAutoNumberItem);
            imvImage = itemView.findViewById(R.id.imvImage);
            btnGetNumberAutoNumber = itemView.findViewById(R.id.btnGetNumberAutoNumber);
            btnViewNumberAutoNumber = itemView.findViewById(R.id.btnViewNumberAutoNumber);
        }

        public void bind(final Hospital model, final OnClickHospitalListener listener) {
            btnGetNumberAutoNumber.setOnClickListener(v -> listener.onClickGetNumber(model));
            btnViewNumberAutoNumber.setOnClickListener(v -> listener.onClickViewNumber(model));
        }

    }

    public interface OnClickHospitalListener {
        void onClickGetNumber(Hospital model);

        void onClickViewNumber(Hospital model);
    }

}