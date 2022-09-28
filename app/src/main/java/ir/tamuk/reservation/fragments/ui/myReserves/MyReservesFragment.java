package ir.tamuk.reservation.fragments.ui.myReserves;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.tamuk.reservation.Interfaces.MyReservesAdapterInterface;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.activities.MainActivity;
import ir.tamuk.reservation.databinding.FragmentMyReservesBinding;
import ir.tamuk.reservation.fragments.ui.home.Adapter.ServicesByCategoryAdapter;
import ir.tamuk.reservation.fragments.ui.profile.ProfileViewModel;
import ir.tamuk.reservation.models.Reserve;
import ir.tamuk.reservation.utils.TokenManager;

public class MyReservesFragment extends Fragment implements MyReservesAdapterInterface {

    private static final String TAG = "MyReservesFragment";
    private FragmentMyReservesBinding binding;
    private MyReservesViewModel viewModel ;
    private MyReservesAdapter adapter ;
    private ArrayList<Reserve> myReserves = new ArrayList<>() ;
    private boolean isOpen = false ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MyReservesViewModel.class);

        binding = FragmentMyReservesBinding.inflate(inflater, container, false);

        return binding.getRoot() ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //swipe layout attributes
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.show_more_text), getResources().getColor(R.color.main));
        binding.refreshLayout.setRefreshing(true);
        binding.refreshLayout.setEnabled(true);

        //titles
        binding.btn1.txt.setText("done");
        binding.btn2.txt.setText("reserved");
        binding.btn3.txt.setText("canceled");

        //recyclerView
        setupRecyclerView(myReserves);

        //onBackPressed
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (isOpen){
                    binding.blurBg.performClick() ;
                }else {
                    Navigation.findNavController(getView()).popBackStack() ;
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //Buttons
        binding.blurBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.blurBg.setVisibility(View.INVISIBLE);
                toggle(binding.detailsLay);
                isOpen = false ;
            }
        });

        binding.btn1.card.setOnClickListener(view1 -> {
        viewModel.callGetAllCategories(getContext() , TokenManager.getAccessToken(getContext()) ,"done" );

        binding.btn1.card.setCardBackgroundColor(ContextCompat.getColor(getContext() , R.color.show_more_text));
        binding.btn2.card.setCardBackgroundColor(ContextCompat.getColor(getContext() , R.color.gray_lighter));
        binding.btn3.card.setCardBackgroundColor(ContextCompat.getColor(getContext() , R.color.gray_lighter));

        binding.btn1.txt.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
        binding.btn2.txt.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
        binding.btn3.txt.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
        });

        binding.btn2.card.setOnClickListener(view1 -> {
        viewModel.callGetAllCategories(getContext() , TokenManager.getAccessToken(getContext()) ,"reserved" );

            binding.btn1.card.setCardBackgroundColor(ContextCompat.getColor(getContext() , R.color.gray_lighter));
            binding.btn2.card.setCardBackgroundColor(ContextCompat.getColor(getContext() , R.color.show_more_text));
            binding.btn3.card.setCardBackgroundColor(ContextCompat.getColor(getContext() , R.color.gray_lighter));

            binding.btn1.txt.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
            binding.btn2.txt.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
            binding.btn3.txt.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
        });

        binding.btn3.card.setOnClickListener(view1 -> {
        viewModel.callGetAllCategories(getContext() , TokenManager.getAccessToken(getContext()) ,"canceled" );

            binding.btn1.card.setCardBackgroundColor(ContextCompat.getColor(getContext() , R.color.gray_lighter));
            binding.btn2.card.setCardBackgroundColor(ContextCompat.getColor(getContext() , R.color.gray_lighter));
            binding.btn3.card.setCardBackgroundColor(ContextCompat.getColor(getContext() , R.color.show_more_text));

            binding.btn1.txt.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
            binding.btn2.txt.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
            binding.btn3.txt.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
        });

        //observers
        viewModel.myReservesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Reserve>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Reserve> reserves) {
                Log.d(TAG, "onChanged: " + reserves.size());
                myReserves.clear();
                myReserves.addAll(reserves) ;
                if (myReserves.size()>0){
                    adapter.notifyDataSetChanged();
                    binding.myReservesRecycler.setVisibility(View.VISIBLE);
                    binding.emptyLay.setVisibility(View.INVISIBLE);
                }else {
                    adapter.notifyDataSetChanged();
                    binding.myReservesRecycler.setVisibility(View.INVISIBLE);
                    binding.emptyLay.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.refreshLayout.setRefreshing(true);
                    binding.refreshLayout.setEnabled(true);
                }else {
                    binding.refreshLayout.setRefreshing(false);
                    binding.refreshLayout.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.btn1.card.performClick();
    }

    private void setupRecyclerView(ArrayList<Reserve> items){
        adapter = new MyReservesAdapter( this ,  items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.myReservesRecycler.setAdapter(adapter);
        binding.myReservesRecycler.setLayoutManager(layoutManager);
        binding.myReservesRecycler.setHasFixedSize(true);
    }


    private void toggle(View view) {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        transition.addTarget(view);

        TransitionManager.beginDelayedTransition((ViewGroup) view, transition);
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void getReserve(String id) {
        binding.blurBg.setVisibility(View.VISIBLE);
        toggle(binding.detailsLay);
        isOpen = true ;
    }
}