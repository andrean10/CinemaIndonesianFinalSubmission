package com.dicoding.cinemalistfavorite.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.cinemalistfavorite.BuildConfig;
import com.dicoding.cinemalistfavorite.R;
import com.dicoding.cinemalistfavorite.entity.Database;
import com.dicoding.cinemalistfavorite.view.detail.DetailTvShowActivity;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowHolder> {

    private static final String URL_IMAGE = BuildConfig.TMDB_URL_IMG;
    private ArrayList<Database> mData = new ArrayList<>();
    private OnItemClickCallBack onItemClickCallBack;
    private final Context context;

    public TvShowAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Database> getData() {
        return mData;
    }

    public void setData(ArrayList<Database> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallBack(TvShowAdapter.OnItemClickCallBack onItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack;
    }

    @NonNull
    @Override
    public TvShowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new TvShowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvShowHolder holder, final int position) {
        Glide.with(holder.itemView.getContext())
                .load(URL_IMAGE + mData.get(position).getImage())
                .apply(new RequestOptions()).override(100, 150)
                .into(holder.imgPhoto);
        holder.tvTitle.setText(mData.get(position).getTitle());
        holder.tvRelease.setText(mData.get(position).getDate());
        holder.tvDescription.setText(mData.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallBack.onItemClicked(mData.get(holder.getAdapterPosition()));

                Intent intent = new Intent(v.getContext(), DetailTvShowActivity.class);
                intent.putExtra(DetailTvShowActivity.EXTRA_NAME, mData.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class TvShowHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvTitle, tvRelease, tvDescription;

        TvShowHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvRelease = itemView.findViewById(R.id.tv_date);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }

    public interface OnItemClickCallBack {
        void onItemClicked(Database database);
    }
}
