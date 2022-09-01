package ir.tamuk.reservation.fragments.ui.home;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.api.ApiServices;
import ir.tamuk.reservation.databinding.FragmentHomeBinding;
import ir.tamuk.reservation.fragments.ui.home.Adapter.OptionAdapter;
import ir.tamuk.reservation.fragments.ui.home.Model.OptionList;
import ir.tamuk.reservation.utils.Tools;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    //adapter
    private OptionAdapter optionAdapter;
    //api
    private ApiServices apiServices;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
//                Navigation.findNavController(getView()).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        //client
//        apiServices = ApiClient.getClient().create(ApiServices.class);
//
//        //call api
//        Call<MoviesList> call1 = apiServices.getMovies(1);
//
//        //response
//        call1.enqueue(new Callback<MoviesList>() {
//            @Override
//            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
//
//                for (int i = 0; i < response.body().movies.size(); i++) {
//
//                    Log.d("ghazal", "movie: " + response.body().movies.get(i).poster);
//                }
//
//
////                moviesAdapter = new MoviesAdapter(getContext() , response.body());
////                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
////                binding.recyclerView.setAdapter(moviesAdapter);
////                binding.recyclerView.setLayoutManager(layoutManager);
////                binding.recyclerView.setHasFixedSize(true);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<MoviesList> call, Throwable t) {
//
//            }
//        });
        binding.imageView2.setImageResource(R.drawable.test);

        ArrayList<OptionList.Option> options = new ArrayList<>();
        OptionList optionList = new OptionList();

        OptionList.Option option = optionList.new Option();
        option.setId(R.drawable.test2);
        option.setTitle("خط ریش آقایان");
        options.add(option);

        OptionList.Option option1 = optionList.new Option();
        option1.setId(R.drawable.test2);
        option1.setTitle("فول بادی ویژه");
        options.add(option1);

        OptionList.Option option2 = optionList.new Option();
        option2.setId(R.drawable.test2);
        option2.setTitle("لیزر مو های زائد");
        options.add(option2);

        optionAdapter = new OptionAdapter(getActivity(), options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        binding.optionRecycler.setLayoutManager(layoutManager);
        binding.optionRecycler.setAdapter(optionAdapter);
        binding.optionRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        binding.optionRecycler2.setLayoutManager(layoutManager2);
        binding.optionRecycler2.setAdapter(optionAdapter);
        binding.optionRecycler2.setHasFixedSize(true);

        // on below line we are creating a new variable for
        // our snap helper class and initializing it for our Linear Snap Helper.
        SnapHelper snapHelper = new LinearSnapHelper();
        // on below line we are attaching this snap helper to our recycler view.
        snapHelper.attachToRecyclerView(binding.optionRecycler);

        SnapHelper snapHelper1 = new LinearSnapHelper();
        snapHelper1.attachToRecyclerView(binding.optionRecycler2);
        return root;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

///////////////////////////tablayout///////

        binding.tabLayout.removeAllTabs();

        TabLayout.Tab tab4 = binding.tabLayout.newTab();
        tab4.setText("فول بادی کاربردی");
        binding.tabLayout.addTab(tab4, 0);

        TabLayout.Tab tab3 = binding.tabLayout.newTab();
        tab3.setText("لیزر صورت");
        binding.tabLayout.addTab(tab3, 1);

        TabLayout.Tab tab2 = binding.tabLayout.newTab();
        tab2.setText("جوانسازی پوست");
        binding.tabLayout.addTab(tab2, 2);

        TabLayout.Tab tab1 = binding.tabLayout.newTab();
        tab1.setText("فول بادی");
        binding.tabLayout.addTab(tab1, 3);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(binding.tabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                selectTab(binding.tabLayout.getSelectedTabPosition());
            }
        });

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        binding.tabLayout.getTabAt(3).select();
                    }
                }, 0);


////////////////////////////////////////////

//        binding.recyclerTitle1.textView2.setText();
//        binding.btn.setOnClickListener(view1 -> {
//
//            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_testFragment);
//
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void selectTab(int position) {
        switch (position) {
            case 0:
                Tools.scrollToPosition(binding.scrollView, binding.recyclerTitle1.getRoot());
                break;
            case 1:
                Tools.scrollToPosition(binding.scrollView, binding.recyclerTitle2.getRoot());
                break;
            case 2:
                Tools.scrollToPosition(binding.scrollView, binding.recyclerTitle1.getRoot());
                break;
            case 3:
                Tools.scrollToPosition(binding.scrollView, binding.recyclerTitle1.getRoot());
                break;

        }
    }
}