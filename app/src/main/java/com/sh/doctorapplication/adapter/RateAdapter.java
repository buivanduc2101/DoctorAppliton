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
import com.sh.doctorapplication.model.Rate;

import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {
    private final Context mContext;
    private final List<Rate> lstGift;

    public RateAdapter(Context mContext, List<Rate> lstGift) {
        this.mContext = mContext;
        this.lstGift = lstGift;
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_rate, parent, false);
        return new RateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder holder, int position) {
        Rate model = lstGift.get(position);
        if (model.getPatient() != null) {
            holder.tvName.setText(model.getPatient().getUser().getFullName());
        } else {
            holder.tvName.setText("Guest");
        }
        if (model.getStar() > 0) {
            for (int i = 1; i <= model.getStar(); i++) {
                if (i == 1) {
                    holder.imvStart1.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    holder.imvStart2.setVisibility(View.VISIBLE);
                }
                if (i == 3) {
                    holder.imvStart3.setVisibility(View.VISIBLE);
                }
                if (i == 4) {
                    holder.imvStart4.setVisibility(View.VISIBLE);
                }
                if (i == 5) {
                    holder.imvStart5.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (lstGift != null) {
            return lstGift.size();
        } else {
            return 0;
        }
    }

    public static class RateViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName;
        protected ImageView imvStart1, imvStart2, imvStart3, imvStart4, imvStart5;

        public RateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameRateItem);
            imvStart1 = itemView.findViewById(R.id.imvStart1RateItem);
            imvStart2 = itemView.findViewById(R.id.imvStart2RateItem);
            imvStart3 = itemView.findViewById(R.id.imvStart3RateItem);
            imvStart4 = itemView.findViewById(R.id.imvStart4RateItem);
            imvStart5 = itemView.findViewById(R.id.imvStart5RateItem);
        }
    }
}
