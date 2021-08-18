package com.minesweeper.smart_home_management.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.minesweeper.smart_home_management.R;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.MyViewHolder>{
    private List<String> userList;
    private  ItemClickListener mClickListener;


    public RequestsAdapter(List<String> userArrayList)
    {
        this.userList = userArrayList;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameText;
        private Button approve_btn;
        private Button cancel_btn;

        public MyViewHolder(final View view)
        {
            super(view);
            nameText = view.findViewById(R.id.textView3);
            approve_btn = view.findViewById(R.id.approve_group_btn);
            cancel_btn = view.findViewById(R.id.cancel_group_btn);
            approve_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getBindingAdapterPosition());


                    }

                }
            });

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getBindingAdapterPosition());


                    }

                }
            });
        }
    }


    @NonNull
    @Override
    public RequestsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
       return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.MyViewHolder holder, int position) {
        String name = userList.get(position);
        holder.nameText.setText(name);
    }

    @Override
    public int getItemCount() {
        return userList.size();

    }
}
