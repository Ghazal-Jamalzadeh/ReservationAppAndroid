package ir.tamuk.reservation.fragments.ui.myReserves;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import ir.tamuk.reservation.Interfaces.MyReservesAdapterInterface;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.ItemMyReservesBinding;
import ir.tamuk.reservation.models.Reserve;
import ir.tamuk.reservation.utils.DateTime;

public class MyReservesAdapter extends RecyclerView.Adapter<MyReservesAdapter.ViewHolder> {

    private ArrayList<Reserve> myReserves ;
    private Context context ;

    MyReservesAdapterInterface myReservesAdapterInterface ;

    public MyReservesAdapter(MyReservesAdapterInterface myReservesAdapterInterface ,  ArrayList<Reserve> myReserves ) {
        this.myReservesAdapterInterface = myReservesAdapterInterface ;
        this.myReserves = myReserves ;

    }

    @NonNull
    @Override
    public MyReservesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.context = parent.getContext() ;
        ItemMyReservesBinding v = ItemMyReservesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyReservesAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyReservesAdapter.ViewHolder holder, int position) {

        Reserve item = myReserves.get(position) ;

        holder.itemView.setOnClickListener(view -> {
            myReservesAdapterInterface.getReserve("");
        });

        holder.binding.txtTime.setText(DateTime.getTime(item.date));
        holder.binding.txtDate.setText(DateTime.getPersianDate(item.date));
        holder.binding.txtTitle.setText(item.service.name);

    }

    @Override
    public int getItemViewType(int position) {
        return position ;
    }

    @Override
    public int getItemCount() {
        return myReserves.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemMyReservesBinding binding;

        public ViewHolder(@NonNull ItemMyReservesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}