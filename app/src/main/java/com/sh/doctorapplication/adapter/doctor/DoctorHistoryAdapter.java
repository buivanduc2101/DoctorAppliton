package com.sh.doctorapplication.adapter.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.model.VisitExamination;
import com.sh.doctorapplication.utils.StringFormatUtils;

import java.util.List;

public class DoctorHistoryAdapter extends RecyclerView.Adapter<DoctorHistoryAdapter.ItemViewHolder> {
    private final Context mContext;
    private final List<VisitExamination> listAppoint;
    private final DoctorHistoryAdapter.OnAppointItemClickListener listener;

    public interface OnAppointItemClickListener {
        void onClickItem(VisitExamination model);
    }

    public DoctorHistoryAdapter(Context mContext, List<VisitExamination> listAppoint, DoctorHistoryAdapter.OnAppointItemClickListener listener) {
        this.mContext = mContext;
        this.listAppoint = listAppoint;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorHistoryAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_history_doctor, parent, false);
        return new DoctorHistoryAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        VisitExamination model = listAppoint.get(position);
        holder.tvDescription.setText(model.getSickStatus());
        holder.tvResult.setText("Kết luận: " + model.getSickName());
        holder.tvAddress.setText(model.getPatient().getUser().getAddress());
        holder.tvPhonePatient.setText(model.getPatient().getUser().getMobile());
        holder.tvNamePatient.setText(model.getPatient().getUser().getFullName());
        holder.tvStatus.setText(StringFormatUtils.generateStatusVisitExam(model.getStatus()));
        holder.tvPrice.setText("Chi phí: " + StringFormatUtils.convertToStringMoneyVND(model.getPriceExam()));
        holder.tvDateAppoint.setText("Lịch hẹn: " + StringFormatUtils.convertDateToString(model.getExaminationTime()));
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        return listAppoint.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvNamePatient, tvPhonePatient, tvAddress, tvDateAppoint, tvPrice, tvResult, tvStatus, tvDescription;
        ImageView imvPatient;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamePatient = itemView.findViewById(R.id.tv_name_patient);
            tvAddress = itemView.findViewById(R.id.tv_address_patient);
            tvPhonePatient = itemView.findViewById(R.id.tv_phone_patient);
            tvDateAppoint = itemView.findViewById(R.id.tv_date_appoint);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvResult = itemView.findViewById(R.id.tv_result_patient);
            tvStatus = itemView.findViewById(R.id.tv_status);
            imvPatient = itemView.findViewById(R.id.imv_patient);
            tvDescription = itemView.findViewById(R.id.tv_mota_patient);
        }

        public void bind(final VisitExamination model, final DoctorHistoryAdapter.OnAppointItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(model));
        }
    }
}
