package ir.tamuk.reservation.fragments.ui.home.Adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ir.tamuk.reservation.Interfaces.HomeAdapterInterface;
import ir.tamuk.reservation.databinding.HomeRowBinding;
import ir.tamuk.reservation.fragments.ui.home.Model.OptionList;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.utils.Constants;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

    private ArrayList<Service> serviceArrayList;
    private Activity activity;
    HomeAdapterInterface homeAdapterInterface;


    public OptionAdapter(Activity activity, ArrayList<Service> serviceArrayList, HomeAdapterInterface homeAdapterInterface) {

        this.activity = activity;
        this.serviceArrayList = serviceArrayList;
        this.homeAdapterInterface = homeAdapterInterface;

    }

    @NonNull
    @Override
    public OptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HomeRowBinding v = HomeRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        //Make width of horizontal recycler view items 85% of screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.85);
        v.getRoot().getLayoutParams().width = width;


        return new OptionAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull OptionAdapter.ViewHolder holder, int position) {
        //loop recycler
        Service item = serviceArrayList.get(position % serviceArrayList.size());
        homeAdapterInterface.changeTitle(item.name);
        Glide.with(activity).load(Constants.DOWNLOAD_PHOTO_URL + item.mainPhoto.filename).into(holder.binding.imageView3);
    }

    public Service getItem(int position) {
        //loop recycler
        int positionInList = position % serviceArrayList.size();
        return serviceArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return serviceArrayList == null ? 0 : Integer.MAX_VALUE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public HomeRowBinding binding;

        public ViewHolder(@NonNull HomeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}