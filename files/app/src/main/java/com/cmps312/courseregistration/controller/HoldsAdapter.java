package com.cmps312.courseregistration.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.model.Hold;

import java.util.ArrayList;

public class HoldsAdapter extends RecyclerView.Adapter<HoldsAdapter.HoldsViewHolder> {

    private Context context;
    private ArrayList<Hold> holds;
    private HoldsInteraction mListener;

    public HoldsAdapter(Context context, ArrayList<Hold> holds, HoldsInteraction listener) {
        this.context = context;
        this.holds = holds;
        this.mListener = listener;
    }


    @NonNull
    @Override
    public HoldsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holds_item,
                parent, false);
        return new HoldsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoldsViewHolder holder, int position) {
        holder.tvHolds.setText(holds.get(position).getReason());
    }

    @Override
    public int getItemCount() {
        return holds.size();
    }

    public class HoldsViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHolds;

        public HoldsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHolds = itemView.findViewById(R.id.tv_hold);
            final Interactions.ButtonEffectListener bfl = new Interactions.ButtonEffectListener();

            itemView.setOnTouchListener(bfl);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.viewHoldInfo(v, getAdapterPosition());
                }
            });

        }
    }

    public interface HoldsInteraction {
        void viewHoldInfo(View view, int position);
    }
}
