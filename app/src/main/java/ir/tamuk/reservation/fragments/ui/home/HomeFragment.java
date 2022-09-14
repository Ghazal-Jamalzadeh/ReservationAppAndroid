package ir.tamuk.reservation.fragments.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentHomeBinding;
import ir.tamuk.reservation.fragments.ui.home.Adapter.ServicesByCategoryAdapter;
import ir.tamuk.reservation.models.Category;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.Tools;

public class HomeFragment extends Fragment {
    private static final String TAG = "ghazal";
    //binding
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    //context
    private final Context context = getContext();
    //list
    private ArrayList<Service> services = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    //adapter
    private ServicesByCategoryAdapter adapter;
    //other
    private int tabIndex = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");

        //On Back Pressed
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

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        Log.d(TAG, "onCreateView: ");

        return binding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: ");

        //swipe layout attributes
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.show_more_text), getResources().getColor(R.color.main));
        binding.refreshLayout.setRefreshing(true);
        binding.refreshLayout.setEnabled(true);

        binding.bannerImg.setImageResource(R.drawable.test);

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeViewModel.getAllCategories();
            }
        });


        //--------------------------------Observers------------------------------------------------>
        homeViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> list) {

                Log.d(TAG, "onChanged: get all categories");

                categories.clear();
                categories.addAll(list);
                buildTabLayout(categories);

            }
        });

        homeViewModel.servicesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Service>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Service> serviceArrayList) {

                Log.d(TAG, "onChanged:  services live data ");

                services.clear();
                services.addAll(serviceArrayList);

                if (services.size() > 0) {

                    buildRecyclerView(services);
                    binding.txtServiceName.setText(services.get(0).name);
                    binding.container.setVisibility(View.VISIBLE);
                    binding.contentLay.setVisibility(View.VISIBLE);
                    binding.emptyLay.setVisibility(View.GONE);
                } else {
                    binding.contentLay.setVisibility(View.GONE);
                    binding.container.setVisibility(View.VISIBLE);
                    binding.emptyLay.setVisibility(View.VISIBLE);
                    binding.refreshLayout.setRefreshing(false);
                    binding.refreshLayout.setEnabled(false);
                }

            }
        });

        homeViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {

                Log.d(TAG, "onChanged: error live data ");
                binding.refreshLayout.setRefreshing(false);
                binding.refreshLayout.setEnabled(true);
                Tools.showToast(getContext(), errorMessage);
            }
        });

        //--------------------------------Observers-----------------------------------------------//
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
//        homeViewModel.errorMessageLiveData.removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    //--------------------------RecyclerView------------------------------------------------------->
    private void buildRecyclerView(ArrayList<Service> items) {
//        if (adapter == null ){
        adapter = new ServicesByCategoryAdapter(getActivity(), items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, true);
        binding.optionRecycler.setAdapter(adapter);
        binding.optionRecycler.setLayoutManager(layoutManager);
        binding.optionRecycler.setHasFixedSize(true);

        binding.txtServiceName.setText(items.get(0).name);

        SnapHelper snapHelper = new LinearSnapHelper();
        binding.optionRecycler.setOnFlingListener(null);
        snapHelper.attachToRecyclerView((RecyclerView) binding.optionRecycler);

        binding.optionRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(layoutManager);
                    if (centerView != null) {
                        int pos = layoutManager.getPosition(centerView);
                        binding.txtServiceName.setText(items.get(pos).name);

                    }
                }
            }
        });
//        }else{
//            adapter.notifyDataSetChanged();
//        }

        binding.refreshLayout.setRefreshing(false);
        binding.refreshLayout.setEnabled(false);
    }
    //--------------------------RecyclerView------------------------------------------------------->

    //----------------------------TabLayout-------------------------------------------------------->
    private void buildTabLayout(ArrayList<Category> categories) {
        //don't build tab layout twice if it already exists
        Log.d(TAG, "buildTabLayout: last pos : " + homeViewModel.tabLastPos + " tab counts " + binding.tabLayout.getTabCount());
        if (binding.tabLayout.getTabCount() == 0) {

            binding.tabLayout.removeAllTabs();

            for (Category category : categories) {
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
            if ( homeViewModel.tabLastPos >categories.size()  ) {
                tabIndex = categories.size() - 1;
            } else {
                tabIndex = homeViewModel.tabLastPos;
            }
        } else {
            binding.container.setVisibility(View.VISIBLE);
            tabIndex = homeViewModel.tabLastPos;

        }

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        binding.tabLayout.getTabAt(tabIndex).select();
                        binding.container.setVisibility(View.VISIBLE);
                        binding.refreshLayout.setRefreshing(false);
                        binding.refreshLayout.setEnabled(false);
                    }
                }, 100);
    }

    private void selectTab(int position) {

        Log.d(TAG, "selectTab: ");
        homeViewModel.tabLastPos = position;
        binding.refreshLayout.setRefreshing(true);
        binding.refreshLayout.setEnabled(true);
        homeViewModel.getAllServices(1, 20, categories.get(categories.size() - position - 1).id);


    }
    //----------------------------TabLayout-------------------------------------------------------//


}