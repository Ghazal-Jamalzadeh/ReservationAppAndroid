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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ir.tamuk.reservation.Interfaces.MyReservesAdapterInterface;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentMyReservesBinding;
import ir.tamuk.reservation.enums.StatusReserve;
import ir.tamuk.reservation.fragments.ui.profile.ProfileViewModel;
import ir.tamuk.reservation.models.Reserve;
import ir.tamuk.reservation.models.User;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.DateTime;
import ir.tamuk.reservation.utils.PriceFormat;
import ir.tamuk.reservation.utils.TokenManager;

public class MyReservesFragment extends Fragment implements MyReservesAdapterInterface {

    private static final String TAG = "MyReservesFragment";
    private FragmentMyReservesBinding binding;
    private MyReservesViewModel myReservesViewModel;
    private ProfileViewModel profileViewModel;
    private MyReservesAdapter adapter;
    private ArrayList<Reserve> myReserves = new ArrayList<>();
    private boolean isOpen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myReservesViewModel = new ViewModelProvider(this).get(MyReservesViewModel.class);
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);


        binding = FragmentMyReservesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //swipe layout attributes
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorSecondary), getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setRefreshing(true);
        binding.refreshLayout.setEnabled(true);

        //titles
        binding.btnDone.txt.setText(StatusReserve.DONE.labelFa);
        binding.btnReserved.txt.setText(StatusReserve.RESERVED.labelFa);
        binding.btnCanceled.txt.setText(StatusReserve.CANCELED.labelFa);

        //recyclerView
        setupRecyclerView(myReserves);

        //onBackPressed
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (isOpen) {
                    binding.blurBg.performClick();
                } else {
                    Navigation.findNavController(getView()).popBackStack();
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
                isOpen = false;
            }
        });

        binding.btnDone.card.setOnClickListener(view1 -> {

//            if (!binding.refreshLayout.isRefreshing()) {

                myReservesViewModel.getDoneList(getContext(), TokenManager.getAccessToken(getContext()), StatusReserve.DONE.label);

                binding.btnDone.card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                binding.btnReserved.card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_lighter));
                binding.btnCanceled.card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_lighter));

                binding.btnDone.txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.btnReserved.txt.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                binding.btnCanceled.txt.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//            }
        });

        binding.btnReserved.card.setOnClickListener(view1 -> {

//            if (!binding.refreshLayout.isRefreshing()){
                myReservesViewModel.getReservedList(getContext(), TokenManager.getAccessToken(getContext()), StatusReserve.RESERVED.label);

                binding.btnDone.card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_lighter));
                binding.btnReserved.card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                binding.btnCanceled.card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_lighter));

                binding.btnDone.txt.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                binding.btnReserved.txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                binding.btnCanceled.txt.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//            }

        });

        binding.btnCanceled.card.setOnClickListener(view1 -> {

//            if (!binding.refreshLayout.isRefreshing()){

            myReservesViewModel.getCanceledList(getContext(), TokenManager.getAccessToken(getContext()), StatusReserve.CANCELED.label);

            binding.btnDone.card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_lighter));
            binding.btnReserved.card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_lighter));
            binding.btnCanceled.card.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));

            binding.btnDone.txt.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            binding.btnReserved.txt.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            binding.btnCanceled.txt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//            }
        });

        //observers
        profileViewModel.getMyProfile().observe(getViewLifecycleOwner(), new Observer<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(User user) {
                binding.details.nameTextForm.setText(user.firstName + " " + user.lastName);
                binding.details.phoneTextForm.setText(user.mobile);
            }
        });

        myReservesViewModel.myReservesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Reserve>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Reserve> reserves) {

                myReserves.clear();
                myReserves.addAll(reserves);
                if (myReserves.size() > 0) {
                    adapter.notifyDataSetChanged();
                    binding.myReservesRecycler.setVisibility(View.VISIBLE);
                    binding.emptyLay.setVisibility(View.INVISIBLE);
                } else {
                    adapter.notifyDataSetChanged();
                    binding.myReservesRecycler.setVisibility(View.INVISIBLE);
                    binding.emptyLay.setVisibility(View.VISIBLE);
                }
            }
        });

        myReservesViewModel.reserveDetailLiveData.observe(getViewLifecycleOwner(), new Observer<Reserve>() {
            @Override
            public void onChanged(Reserve reserve) {

                showInfo(reserve);
                binding.blurBg.setVisibility(View.VISIBLE);
                toggle(binding.detailsLay);
                isOpen = true;

            }
        });

        myReservesViewModel.isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.refreshLayout.setRefreshing(true);
                    binding.refreshLayout.setEnabled(true);
                } else {
                    binding.refreshLayout.setRefreshing(false);
                    binding.refreshLayout.setEnabled(false);
                }
            }
        });

        //Text watcher - price format
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                PriceFormat.formatPrice(binding.details.priceTextForm, this, charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        binding.details.priceTextForm.addTextChangedListener(textWatcher);

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.btnDone.card.performClick();
    }

    private void setupRecyclerView(ArrayList<Reserve> items) {
        adapter = new MyReservesAdapter(this, items);
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
            binding.blurBg.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
            binding.blurBg.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showInfo(Reserve reserve) {
        //icon
        if(reserve.isPay){

            binding.details.tike.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            binding.details.txetTike.setTextColor(ContextCompat.getColor(getContext() ,  R.color.colorPrimary));

            binding.details.tike.setImageDrawable(ContextCompat.getDrawable(getContext() , R.drawable.ic_check_circle));
            binding.details.txetTike.setText("پرداخت شده");

        }else {
            binding.details.tike.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorSecondary));
            binding.details.txetTike.setTextColor(ContextCompat.getColor(getContext() ,  R.color.colorSecondary));

            binding.details.tike.setImageDrawable(ContextCompat.getDrawable(getContext() , R.drawable.ic_error_circle));
            binding.details.txetTike.setText("پرداخت نشده");
        }
        //date
        binding.details.dateTextForm.setText(DateTime.getPersianDate(reserve.date));
        //time
        binding.details.timeTextForm.setText(DateTime.getTime(reserve.date));
        //service name
        binding.details.serviceTextForm.setText(reserve.service.name);
        //duration
        binding.details.durationTextForm.setText(reserve.service.time + " دقیقه");
        //price
        binding.details.priceTextForm.setText(String.valueOf(reserve.service.price));
    }


    @Override
    public void getReserve(String id) {

//        if (!binding.refreshLayout.isRefreshing()){

        myReservesViewModel.callGetReserveDetail(getContext(), TokenManager.getAccessToken(getContext()), id);
//        }

    }
}