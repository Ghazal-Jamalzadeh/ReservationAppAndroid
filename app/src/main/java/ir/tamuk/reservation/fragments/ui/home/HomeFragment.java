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
import java.util.Timer;
import java.util.TimerTask;

import ir.tamuk.reservation.Interfaces.HomeAdapterInterface;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.api.ApiServices;
import ir.tamuk.reservation.databinding.FragmentHomeBinding;
import ir.tamuk.reservation.fragments.ui.home.Adapter.OptionAdapter;
import ir.tamuk.reservation.fragments.ui.home.Model.OptionList;
import ir.tamuk.reservation.utils.Tools;

public class HomeFragment extends Fragment implements HomeAdapterInterface {

    private FragmentHomeBinding binding;
    //adapter
    private OptionAdapter optionAdapter;
    private OptionAdapter optionAdapter2;
    //api
    private ApiServices apiServices;
    private Timer timer;
    private TimerTask timerTask;

    private ArrayList<OptionList.Option> options;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager2;


    int position;

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

        //loop recycler
        //set smooth scroll to 2 for using chance app
        if (options != null) {
            position = Integer.MAX_VALUE / 2;
            binding.optionRecycler.smoothScrollToPosition(position);
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.imageView2.setImageResource(R.drawable.test);
        options = new ArrayList<>();
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


        //intitialize RecyclerViews
        optionAdapter = new OptionAdapter(getActivity(), options, this::changeTitle, 1);
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        initLoopRecyclerView(binding.optionRecycler,optionAdapter,layoutManager);

        optionAdapter2 = new OptionAdapter(getActivity(), options, this::changeTitle, 2);
        layoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        initLoopRecyclerView(binding.optionRecycler2,optionAdapter2,layoutManager2);

        //TabLayout
        binding.tabLayout.removeAllTabs();

        TabLayout.Tab tab1 = binding.tabLayout.newTab();
        tab1.setText("فول بادی کاربردی");
        binding.tabLayout.addTab(tab1, 0);

        TabLayout.Tab tab2 = binding.tabLayout.newTab();
        tab2.setText("لیزر صورت");
        binding.tabLayout.addTab(tab2, 1);

        TabLayout.Tab tab3 = binding.tabLayout.newTab();
        tab3.setText("جوانسازی پوست");
        binding.tabLayout.addTab(tab3, 2);

        TabLayout.Tab tab4 = binding.tabLayout.newTab();
        tab4.setText("فول بادی");
        binding.tabLayout.addTab(tab4, 3);

        TabLayout.Tab tab5 = binding.tabLayout.newTab();
        tab5.setText("فول بادی");
        binding.tabLayout.addTab(tab5, 4);

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

        //tab scroll to right
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        binding.tabLayout.getTabAt(4).select();
                    }
                }, 1000);


        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        //loop recycler
        runAutoScrollBanner(binding.optionRecycler);
        runAutoScrollBanner(binding.optionRecycler2);
    }

    @Override
    public void onPause() {
        super.onPause();
        //loop recycler
        stopAutoScrollBanner(layoutManager);
        stopAutoScrollBanner(layoutManager2);
    }

    //loop recycler
    private void stopAutoScrollBanner(LinearLayoutManager manager) {
        if (timer != null && timerTask != null) {
            timerTask.cancel();
            timer.cancel();
            timer = null;
            timerTask = null;
            position = manager.findFirstCompletelyVisibleItemPosition();
        }
    }

    //loop recycler
    private void runAutoScrollBanner(RecyclerView recyclerView) {
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (position == Integer.MAX_VALUE) {
                        position = Integer.MAX_VALUE / 2;
                        recyclerView.scrollToPosition(position);
                        recyclerView.smoothScrollBy(5, 0);
                    } else {
                        position++;
                        recyclerView.smoothScrollToPosition(position);

                    }
                }
            };
            timer.schedule(timerTask, 4000, 4000);
        }
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    @Override
    public void changeTitle(String string, int flag) {
        switch (flag) {
            case 1:
                binding.recyclerTitle1.textView3.setText(string);
                break;
            case 2:
                binding.recyclerTitle2.textView3.setText(string);
                break;
        }


    }

    //ScapHelper
    public void newSnapHelper(View view) {
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView((RecyclerView) view);
    }

    public void initLoopRecyclerView(RecyclerView view,OptionAdapter optionAdapter,LinearLayoutManager manager ) {
        view.setLayoutManager(manager);
        view.setAdapter(optionAdapter);
        view.setHasFixedSize(true);
        view.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);

        // on below line we are creating a new variable for
        // our snap helper class and initializing it for our Linear Snap Helper.
        newSnapHelper(view);

        //loop recycler
        view.smoothScrollBy(5, 0);

        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == 1) {
                    stopAutoScrollBanner(manager);
                } else if (newState == 0) {
                    position = manager.findFirstCompletelyVisibleItemPosition();
                    runAutoScrollBanner(view);
                }
            }
        });

    }
}