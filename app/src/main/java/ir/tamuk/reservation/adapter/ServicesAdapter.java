package ir.tamuk.reservation.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ir.tamuk.reservation.Interfaces.ServiceDatailInterface;
import ir.tamuk.reservation.databinding.RowServicesBinding;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.utils.Constants;


/* از  RecyclerView.Adapter ارث بیر کن و یک viewHolder یهش پاس بده*/
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private ArrayList<Service> services ;
    private Context context ;
    private ServiceDatailInterface serviceDatailInterface;

    //constructor
    //هرچیزی از بیرون این کلاس نیاز داری اینجا پاس بده
    public ServicesAdapter(Context context , ArrayList<Service> services, ServiceDatailInterface serviceDatailInterface) {
        this.context = context ;
       this.services = services ;
       this.serviceDatailInterface = serviceDatailInterface;
//        Log.d("mansour", "ServicesAdapter: "+serviceDatailInterface);
    }

    @NonNull
    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.context = parent.getContext() ;
        RowServicesBinding v = RowServicesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ServicesAdapter.ViewHolder(v);

    }

    // هر عملیاتی میخوای انجام بدی اینجا انجام میدی
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service item = services.get(position)  ;
        int x=position;
        holder.binding.constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.clear();
//                bundle.putInt("id", x);
//                bundle.putString("serviceId", item.id);
//                Log.d("MAN", "onClick: "+x);
                serviceDatailInterface.ShowDetail(item.id);
                Log.d("MANSOOR", "ShoeDetail: "+item);


//                Navigation.findNavController(view).navigate(R.id.action_to_serviceFragment,bundle );

            }
        });
        holder.binding.text1.setText(item.name);
        Log.d("kia", "onBindViewHolder: " + item.name);
        holder.binding.text2.setText(item.description);
        Glide.with(context).load(Constants.DOWNLOAD_PHOTO_URL + item.mainPhoto.filename).into(holder.binding.img);
//        Picasso.get().load(String.valueOf(item.mainPhoto.filename)).into(holder.binding.img);

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RowServicesBinding binding;

        public ViewHolder(@NonNull RowServicesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}