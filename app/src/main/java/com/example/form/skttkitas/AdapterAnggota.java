package com.example.form.skttkitas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.x.R;

import java.util.List;

public class AdapterAnggota extends RecyclerView.Adapter<AdapterAnggota.MyViewHolder> implements Filterable {
    private List<ActivityFormulirKitas.ModelData> modelDataList;
    private List<ActivityFormulirKitas.ModelData> items;
    private final OnItemClickListener listener;
    Context context;

    public AdapterAnggota(List<ActivityFormulirKitas.ModelData> items, OnItemClickListener listener, Context con) {
        this.items = items;
        this.modelDataList = items;
        this.listener = listener;
        this.context = con;
    }

    public interface OnItemClickListener {
        void onItemClick(ActivityFormulirKitas.ModelData mdata);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kitas_anggota, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
        final ActivityFormulirKitas.ModelData contact = items.get(position);

        holder.tvnama.setText(contact.getNama());

        holder.ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvnama;
        ImageView ivdelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvnama = itemView.findViewById(R.id.tvnama);
            ivdelete = itemView.findViewById(R.id.ivdelete);
        }

        public void bind(final ActivityFormulirKitas.ModelData item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}