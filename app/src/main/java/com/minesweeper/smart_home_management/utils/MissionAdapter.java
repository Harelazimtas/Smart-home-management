package com.minesweeper.smart_home_management.utils;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.minesweeper.smart_home_management.R;
import com.minesweeper.smart_home_management.model.Mission;

import java.util.ArrayList;
import java.util.List;

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MyViewHolder>{
    private List<Mission> missions;
    private  ItemClickListener mClickListener;
    private final int CARD_SIZE=500;

    public MissionAdapter()
    {
        this.missions = new ArrayList<>();
    }


    public MissionAdapter(List<Mission> userArrayList)
    {
        this.missions = userArrayList;
    }

    public void setMissions(List<Mission> missions) {
        this.missions = missions;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameMission,description,dueDate;
        private Button moreDetail;

        public MyViewHolder(final View view)
        {
            super(view);
            dueDate = view.findViewById(R.id.due_date_card);
            nameMission = view.findViewById(R.id.title_card);
            description= view.findViewById(R.id.description_card);
            moreDetail = view.findViewById(R.id.edit_mission_card);
            moreDetail.setOnClickListener(new View.OnClickListener() {
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
    public MissionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mission_card, parent, false);
        itemView.getLayoutParams().height= CARD_SIZE;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MissionAdapter.MyViewHolder holder, int position) {
        Mission mission = missions.get(position);
        holder.description.setText(mission.getDescription()+" ");
        holder.nameMission.setText(mission.getName()+" ");
        holder.dueDate.setText(mission.getDueDate()+" ");
    }

    @Override
    public int getItemCount() {
        return missions.size();

    }


}
