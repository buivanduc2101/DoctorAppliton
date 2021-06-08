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
import com.sh.doctorapplication.model.Hospital;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BenhVienAdapter extends RecyclerView.Adapter<BenhVienAdapter.BenhVienViewHolder> {
    private final Context mContext;
    private final List<Hospital> listBenhViens;
    private final OnBenhVienItemClickListener listener;

    public BenhVienAdapter(Context mContext, List<Hospital> listBenhViens, OnBenhVienItemClickListener listener) {
        this.mContext = mContext;
        this.listBenhViens = listBenhViens;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BenhVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_benhvien, parent, false);
        return new BenhVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BenhVienViewHolder holder, int position) {
        Hospital model = listBenhViens.get(position);
        holder.tvName.setText(model.getName());
        holder.tvAddress.setText(model.getAddress());
        holder.tvPhone.setText(model.getPhoneNumber());
        Picasso.get().load(model.getLinkImage()).placeholder(R.drawable.ic_thumbnail)
                .error(R.drawable.ic_thumbnail).into(holder.imvImage);
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        if (listBenhViens != null) {
            return listBenhViens.size();
        } else {
            return 0;
        }
    }

    public static class BenhVienViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvPhone, tvAddress;
        protected ImageView imvImage;

        public BenhVienViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameBenhVienItem);
            imvImage = itemView.findViewById(R.id.imvImageBenhVienItem);
            tvAddress = itemView.findViewById(R.id.tvAddressBenhVienItem);
            tvPhone = itemView.findViewById(R.id.tvPhoneNumberBenhVienItem);
        }

        public void bind(final Hospital model, final OnBenhVienItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(model));
        }

    }

    public interface OnBenhVienItemClickListener {
        void onClickItem(Hospital model);
    }

}