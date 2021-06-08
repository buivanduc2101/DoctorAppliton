package com.sh.doctorapplication.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.doctorapplication.R;
import com.sh.doctorapplication.model.VisitExamination;
import com.sh.doctorapplication.utils.StringFormatUtils;

import java.util.List;


public class HistoryUserAdapter extends RecyclerView.Adapter<HistoryUserAdapter.ItemViewHolder> {
    private final Context mContext;
    private final List<VisitExamination> listAppoint;
    private final HistoryUserAdapter.OnAppointItemClickListener listener;

    public interface OnAppointItemClickListener {
        void onClickItem(VisitExamination model);
    }

    public HistoryUserAdapter(Context mContext, List<VisitExamination> listAppoint, HistoryUserAdapter.OnAppointItemClickListener listener) {
        this.mContext = mContext;
        this.listAppoint = listAppoint;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryUserAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_history_user, parent, false);
        return new HistoryUserAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryUserAdapter.ItemViewHolder holder, int position) {
        VisitExamination model = listAppoint.get(position);
        holder.tvSickName.setText(model.getSickName() != null ? "Bệnh: " + model.getSickName() : "Đang đợi tư vấn");
        holder.tvDoctorName.setText("Bác sĩ: " + model.getDoctor().getUser().getFullName());
        holder.tvHospital.setText(model.getDoctor().getMedicalExam().getHospital().getName());
        holder.tvPrice.setText("Chi phí: " + StringFormatUtils.convertToStringMoneyVND(model.getPriceExam()));
        holder.tvStatus.setText("Trạng thái: " + StringFormatUtils.generateStatusVisitExam(model.getStatus()));
        holder.tvStatusVisitHistoryItem.setText(StringFormatUtils.generateStatusVisitExam(model.getStatus()));
        holder.tvDateHistoryItem.setText(StringFormatUtils.convertDateToString(model.getExaminationTime()));
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        return listAppoint.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvSickName, tvStatus, tvDoctorName, tvHospital, tvPrice;
        protected TextView tvStatusVisitHistoryItem, tvDateHistoryItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateHistoryItem = itemView.findViewById(R.id.tvDateHistoryItem);
            tvSickName = itemView.findViewById(R.id.tvSickNameHistoryItem);
            tvStatusVisitHistoryItem = itemView.findViewById(R.id.tvStatusVisitHistoryItem);
            tvStatus = itemView.findViewById(R.id.tvStatusHistoryItem);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorNameHistoryItem);
            tvHospital = itemView.findViewById(R.id.tvHospitalHistoryItem);
            tvPrice = itemView.findViewById(R.id.tvPriceHistoryItem);
        }

        public void bind(final VisitExamination model, final HistoryUserAdapter.OnAppointItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(model));
        }
    }
}
