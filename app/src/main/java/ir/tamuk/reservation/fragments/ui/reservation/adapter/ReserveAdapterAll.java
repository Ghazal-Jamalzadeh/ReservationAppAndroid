package ir.tamuk.reservation.fragments.ui.reservation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.ReservedRowBinding;
import ir.tamuk.reservation.fragments.ui.reservation.OnSelectedItem;
import ir.tamuk.reservation.models.FreeTimeAm;

public class ReserveAdapterAll extends RecyclerView.Adapter<ReserveAdapterAll.ViewHolder>{

    private ArrayList<FreeTimeAm> freeTimeAms;
    private Context context;

    private boolean isClicked;
    private int clickPosition;
    private OnSelectedItem onSelectedItem;





    public ReserveAdapterAll(ArrayList<FreeTimeAm> freeTimeAms, OnSelectedItem onSelectedItem){
        this.freeTimeAms=freeTimeAms;
        this.onSelectedItem= onSelectedItem;
    }

    @NonNull
    @Override
    public ReserveAdapterAll.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ReservedRowBinding v = ReservedRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReserveAdapterAll.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveAdapterAll.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FreeTimeAm item = freeTimeAms.get(position);
        holder.binding.timeReserve.setText(item.title);

        if (item.aBoolean){
            //color
            Log.d("RHMN", "color: ");

            holder.binding.relativeLayoutClick.setBackground(context.getDrawable(R.drawable.row_maincolor_reserve));
            holder.binding.timeReserve.setTextColor(Color.WHITE);
        }else {
            //white
            Log.d("RHMN", "white: ");

            holder.binding.relativeLayoutClick.setBackground(context.getDrawable(R.drawable.row_transparent_reserve));
            holder.binding.timeReserve.setTextColor(Color.BLACK);
        }

//        if (isClicked) {
//
//            if (clickPosition == position) {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    holder.binding.relativeLayoutClick.setBackground(context.getDrawable(R.drawable.row_maincolor_reserve));
//                    holder.binding.timeReserve.setTextColor(Color.WHITE);
//
//
//
//                }
//
//            } else {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    holder.binding.relativeLayoutClick.setBackground(context.getDrawable(R.drawable.row_transparent_reserve));
//                    holder.binding.timeReserve.setTextColor(Color.BLACK);
//                }
//
//            }
//        }

        holder.itemView.setOnClickListener(view -> {

            String key = item.time;
            boolean a = item.aBoolean;

            onSelectedItem.onItem(position, key);
            Log.d("RHMN", "setOnClick: ");

//            isClicked = true;
//            clickPosition = position;
//            notifyDataSetChanged();
//                    activity.selectFinalPlan(plans.get(position));

        });


        }

    @Override
    public int getItemCount() {

        return freeTimeAms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ReservedRowBinding binding;

        public ViewHolder(@NonNull ReservedRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

