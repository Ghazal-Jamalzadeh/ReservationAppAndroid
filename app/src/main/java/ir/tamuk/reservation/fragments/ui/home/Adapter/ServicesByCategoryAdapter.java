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
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.utils.Constants;

public class ServicesByCategoryAdapter extends RecyclerView.Adapter<ServicesByCategoryAdapter.ViewHolder> {

    private ArrayList<Service> services ;
    private Activity activity  ;
    private HomeAdapterInterface homeAdapterInterface ;

    public ServicesByCategoryAdapter(Activity activity, ArrayList<Service> services , HomeAdapterInterface homeAdapterInterface) {

        this.activity = activity ;
        this.services = services ;
        this.homeAdapterInterface = homeAdapterInterface ;

    }

    @NonNull
    @Override
    public ServicesByCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HomeRowBinding v = HomeRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        //Make width of horizontal recycler view items 85% of screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.85);
        v.getRoot().getLayoutParams().width = width;

        return new ServicesByCategoryAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ServicesByCategoryAdapter.ViewHolder holder, int position) {

        Service item = services.get(position);
        Glide.with(activity).load(Constants.DOWNLOAD_PHOTO_URL + item.mainPhoto.filename).into(holder.binding.imageView3);

        holder.itemView.setOnClickListener(view -> {
            homeAdapterInterface.showDetail(item.id);
        });

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public HomeRowBinding binding;

        public ViewHolder(@NonNull HomeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
