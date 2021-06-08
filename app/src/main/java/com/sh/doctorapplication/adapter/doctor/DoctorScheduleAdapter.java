package com.sh.doctorapplication.adapter.doctor;

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
import com.sh.doctorapplication.model.VisitExamination;
import com.sh.doctorapplication.utils.StringFormatUtils;

import java.util.List;

public class DoctorScheduleAdapter extends RecyclerView.Adapter<DoctorScheduleAdapter.ItemViewHolder> {
    private final Context mContext;
    private final List<VisitExamination> listAppoint;
    private final DoctorScheduleAdapter.OnAppointItemClickListener listener;

    public interface OnAppointItemClickListener {

        void onClickDone(VisitExamination model);

        void onClickCall(VisitExamination model);

        void onClickCancel(VisitExamination model);
    }

    public DoctorScheduleAdapter(Context mContext, List<VisitExamination> listAppoint, DoctorScheduleAdapter.OnAppointItemClickListener listener) {
        this.mContext = mContext;
        this.listAppoint = listAppoint;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_schedule_doctor, parent, false);
        return new DoctorScheduleAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        VisitExamination model = listAppoint.get(position);
        holder.tvAddress.setText(model.getPatient().getUser().getAddress());
        holder.tvPhonePatient.setText(model.getPatient().getUser().getMobile());
        holder.tvNamePatient.setText(model.getPatient().getUser().getFullName());
        holder.tvDateAppoint.setText("Hẹn khám: " + StringFormatUtils.convertDateToString(model.getExaminationTime()));
        holder.tvBirthYearPatient.setText("Ngày sinh: " + model.getPatient().getUser().getBirthDay());
        holder.tvDescription.setText(model.getDescription() != null ? "Mô tả: " + model.getDescription() : "Không có mô tả");
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        return listAppoint.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvNamePatient, tvPhonePatient, tvAddress, tvBirthYearPatient, tvDateAppoint, tvDescription;
        protected ImageView imvPatient;
        protected Button btn_done, btn_cancel, btnCall;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamePatient = itemView.findViewById(R.id.tv_name_patient_sche);
            tvAddress = itemView.findViewById(R.id.tv_address_patient_sche);
            tvPhonePatient = itemView.findViewById(R.id.tv_phone_patient_sche);
            tvBirthYearPatient = itemView.findViewById(R.id.tv_birthyear_patient_sche);
            tvDateAppoint = itemView.findViewById(R.id.tv_date_appoint_sche);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imvPatient = itemView.findViewById(R.id.imv_patient_sche);
            btn_done = itemView.findViewById(R.id.btn_done);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            btnCall = itemView.findViewById(R.id.btnCall);
        }

        public void bind(final VisitExamination model, final DoctorScheduleAdapter.OnAppointItemClickListener listener) {
            btn_done.setOnClickListener(v -> listener.onClickDone(model));
            btnCall.setOnClickListener(v -> listener.onClickCall(model));
            btn_cancel.setOnClickListener(v -> listener.onClickCancel(model));
        }
    }
}
