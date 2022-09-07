package ir.tamuk.reservation.fragments.ui.home.Adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.tamuk.reservation.Interfaces.HomeAdapterInterface;
import ir.tamuk.reservation.databinding.HomeRowBinding;
import ir.tamuk.reservation.fragments.ui.home.Model.OptionList;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

    private ArrayList<OptionList.Option> optionArrayList;
    private Activity activity;
    private int flag;
    HomeAdapterInterface homeAdapterInterface;


    public OptionAdapter(Activity activity, ArrayList<OptionList.Option> optionArrayList, HomeAdapterInterface homeAdapterInterface, int flag) {

        this.activity = activity;
        this.optionArrayList = optionArrayList;
        this.homeAdapterInterface = homeAdapterInterface;
        this.flag = flag;

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
        OptionList.Option item = optionArrayList.get(position % optionArrayList.size());
        homeAdapterInterface.changeTitle(item.getTitle(), flag);
        holder.binding.imageView3.setImageResource(item.getId());

//        Log.d("testLog", "onBindViewHolder: " + moviesList.get(position));

    }

    public OptionList.Option getItem(int position) {
        //loop recycler
        int positionInList = position % optionArrayList.size();
        return optionArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return optionArrayList == null ? 0 : Integer.MAX_VALUE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public HomeRowBinding binding;

        public ViewHolder(@NonNull HomeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}