package ir.tamuk.reservation.fragments.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import ir.tamuk.reservation.models.Category;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.Tools;
import ir.tamuk.reservation.viewModels.HomeViewModel;

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

    HomeViewModel homeViewModel;


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

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.swipeRefreshLayout.setEnabled(true);

        //call api
        callGetCategories();

        homeViewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                binding.swipeRefreshLayout.setRefreshing(b);
                if (!b) {
                    binding.container.setVisibility(View.VISIBLE);
                }

            }
        });

        homeViewModel.categoriesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                binding.swipeRefreshLayout.setEnabled(false);

                buildTabLayout(categories);

            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callGetCategories();
            }
        });


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
        initLoopRecyclerView(binding.optionRecycler, optionAdapter, layoutManager);




        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        //loop recycler
        runAutoScrollBanner(binding.optionRecycler);
    }

    @Override
    public void onPause() {
        super.onPause();
        //loop recycler
        stopAutoScrollBanner(layoutManager);
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
                Tools.scrollToPosition(binding.scrollView, binding.recyclerTitle1.getRoot());
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
        binding.recyclerTitle1.textView3.setText(string);
    }

    //ScapHelper
    public void newSnapHelper(View view) {
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView((RecyclerView) view);
    }

    public void initLoopRecyclerView(RecyclerView view, OptionAdapter optionAdapter, LinearLayoutManager manager) {
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

    private void buildTabLayout(ArrayList<Category> categories) {
        //TabLayout
        binding.tabLayout.removeAllTabs();

        for (Category category:categories) {
            TabLayout.Tab tab = binding.tabLayout.newTab();
            tab.setText(category.name);
            binding.tabLayout.addTab(tab, 0);
        }
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
                        binding.tabLayout.getTabAt(categories.size() -1).select();
                    }
                }, 1000);

    }

    private void callGetCategories() {
        if (Connectivity.isConnected(getContext())) {
            homeViewModel.getAllCategories();
        } else {
            Toast.makeText(getContext(), "اینترنت وصل نیس ", Toast.LENGTH_SHORT).show();
        }
    }
}