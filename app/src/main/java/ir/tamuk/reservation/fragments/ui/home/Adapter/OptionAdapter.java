package ir.tamuk.reservation.fragments.ui.home.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.tamuk.reservation.databinding.HomeRowBinding;
import ir.tamuk.reservation.fragments.ui.home.Model.OptionList;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

    private ArrayList<OptionList.Option> optionArrayList ;
    private Activity activity;

    public OptionAdapter(Activity activity, ArrayList<OptionList.Option> optionArrayList) {

        this.activity=activity ;
        this.optionArrayList = optionArrayList ;

    }

    @NonNull
    @Override
    public OptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HomeRowBinding v = HomeRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        //Make width of horizontal recycler view items 85% of screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int)(displayMetrics.widthPixels * 0.85);
        v.getRoot().getLayoutParams().width = width ;


        return new OptionAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull OptionAdapter.ViewHolder holder, int position) {

        holder.binding.imageView3.setImageResource(optionArrayList.get(position).getId());

//        Log.d("testLog", "onBindViewHolder: " + moviesList.get(position));

    }

    @Override
    public int getItemCount() {
        return optionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public HomeRowBinding binding;

        public ViewHolder(@NonNull HomeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}