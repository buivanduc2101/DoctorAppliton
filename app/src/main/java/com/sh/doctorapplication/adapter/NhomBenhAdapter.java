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
import com.sh.doctorapplication.model.GroupDisease;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NhomBenhAdapter extends RecyclerView.Adapter<NhomBenhAdapter.NhomBenhViewHolder> {
    private final Context mContext;
    private final List<GroupDisease> listNhomBenhs;
    private final OnNhomBenhItemClickListener listener;

    public NhomBenhAdapter(Context mContext, List<GroupDisease> listNhomBenhs, OnNhomBenhItemClickListener listener) {
        this.mContext = mContext;
        this.listNhomBenhs = listNhomBenhs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NhomBenhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_nhombenh, parent, false);
        return new NhomBenhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhomBenhViewHolder holder, int position) {
        GroupDisease model = listNhomBenhs.get(position);
        holder.tvName.setText(model.getName());
        holder.tvDescription.setText(model.getDescription());
        Picasso.get().load(model.getLinkImage()).placeholder(R.drawable.ic_thumbnail)
                .error(R.drawable.ic_thumbnail).into(holder.imvImage);
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        if (listNhomBenhs != null) {
            return listNhomBenhs.size();
        } else {
            return 0;
        }
    }

    public static class NhomBenhViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvDescription;
        protected ImageView imvImage;

        public NhomBenhViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameNhomBenhItem);
            imvImage = itemView.findViewById(R.id.imvImageNhomBenhItem);
            tvDescription = itemView.findViewById(R.id.tvDescNhomBenhItem);
        }

        public void bind(final GroupDisease model, final OnNhomBenhItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(model));
        }

    }

    public interface OnNhomBenhItemClickListener {
        void onClickItem(GroupDisease model);
    }

}