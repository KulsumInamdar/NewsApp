package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>{

    private  List<String> mData;
    private LayoutInflater mLayoutInflater;
    private  ItemClickListener itemClickListener;
    NewsRecyclerViewAdapter(Context context, List<String> data, ItemClickListener itemClickListener){
        this.mData=data;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.itemClickListener =itemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.news_rcv_row,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = mData.get(position);
        holder.titleTextView.setText(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            titleTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null)
            {
                itemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public interface  ItemClickListener
    {
        void onItemClick(View view,int position);
    }
}
