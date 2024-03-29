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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import ir.tamuk.reservation.Interfaces.HomeAdapterInterface;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.activities.MainActivity;
import ir.tamuk.reservation.databinding.FragmentHomeBinding;
import ir.tamuk.reservation.fragments.ui.home.Adapter.ServicesByCategoryAdapter;
import ir.tamuk.reservation.models.Category;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.Tools;

public class HomeFragment extends Fragment implements HomeAdapterInterface {
    private static final String TAG = "HomeFragment";
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
    private int position = 0 ;
    private boolean isFirstTabs = true ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //On Back Pressed
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //swipe layout attributes
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorSecondary)
                , getResources().getColor(R.color.colorPrimary));
        binding.refreshLayout.setRefreshing(true);
        binding.refreshLayout.setEnabled(true);

        binding.bannerImg.setImageResource(R.drawable.test);

        //refresh
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeViewModel.getAllCategories();
            }
        });

        buildRecyclerView(services);

        //Buttons
        binding.txtShowAll.setOnClickListener(view1 -> {

            ((MainActivity)getActivity()).clickBottomNav(R.id.nav_services);

        });


        //--------------------------------Observers------------------------------------------------>
        homeViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> list) {

                categories.clear();
                categories.addAll(list);
                buildTabLayout(categories);

            }
        });

        homeViewModel.servicesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Service>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Service> serviceArrayList) {

                services.clear();
                services.addAll(serviceArrayList);

                binding.refreshLayout.setRefreshing(false);
                binding.refreshLayout.setEnabled(false);
                if (services.size() > 0) {

                    adapter.notifyDataSetChanged();
                    binding.txtServiceName.setText(services.get(position).name);
                    binding.container.setVisibility(View.VISIBLE);
                    binding.contentLay.setVisibility(View.VISIBLE);
                    binding.emptyLay.setVisibility(View.GONE);
                } else {
                    binding.contentLay.setVisibility(View.GONE);
                    binding.container.setVisibility(View.VISIBLE);
                    binding.emptyLay.setVisibility(View.VISIBLE);
                }

            }
        });

        homeViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                binding.refreshLayout.setRefreshing(false);
                binding.refreshLayout.setEnabled(true);
                if (homeViewModel.ignoreMessage){
                    homeViewModel.ignoreMessage = false ;
                }else {
                    Tools.showToast(getContext(), errorMessage);
                }
            }
        });

        //--------------------------------Observers-----------------------------------------------//
    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.ignoreMessage =  false ;
    }

    @Override
    public void onStop() {
        super.onStop();
        homeViewModel.ignoreMessage =  true ;
    }

    //--------------------------RecyclerView------------------------------------------------------->
    private void buildRecyclerView(ArrayList<Service> items) {
        adapter = new ServicesByCategoryAdapter(getActivity(), items , this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, true);
        binding.optionRecycler.setAdapter(adapter);
        binding.optionRecycler.setLayoutManager(layoutManager);
        binding.optionRecycler.setHasFixedSize(true);

        if (services.size() != 0 )
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
                        try {
                            position = layoutManager.getPosition(centerView);
                            binding.txtServiceName.setText(items.get(position).name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }
    //--------------------------RecyclerView------------------------------------------------------->

    //----------------------------TabLayout-------------------------------------------------------->
    private void buildTabLayout(ArrayList<Category> categories) {
        //don't build tab layout twice if it already exists
        if (binding.tabLayout.getTabCount() == 0) {

            binding.tabLayout.removeAllTabs();

            for (Category category : categories) {
                TabLayout.Tab tab = binding.tabLayout.newTab();
                tab.setText(category.name);
                binding.tabLayout.addTab(tab, 0);
            }
//            int lastIndex = categories.size()-1 ;
//            selectTab(lastIndex);

            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    position = 0  ;
                    selectTab(binding.tabLayout.getSelectedTabPosition());
                    Log.d("HomeViewModel", "onTabSelected: " + tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                    if (isFirstTabs){
                    selectTab(binding.tabLayout.getSelectedTabPosition());
                    isFirstTabs = false ;
                    }
                }
            });

        }


        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        if ( homeViewModel.tabLastPos >categories.size() || homeViewModel.tabLastPos == -1) {
                            tabIndex = categories.size() - 1;
                        } else {
                            tabIndex = homeViewModel.tabLastPos;
                        }

                        binding.tabLayout.getTabAt(tabIndex).select();
                        binding.container.setVisibility(View.VISIBLE);
                        binding.refreshLayout.setRefreshing(false);
                        binding.refreshLayout.setEnabled(false);
                    }
                }, 100);
    }

    private void selectTab(int position) {

        homeViewModel.tabLastPos = position;
        binding.refreshLayout.setRefreshing(true);
        binding.refreshLayout.setEnabled(true);
        int index =  categories.size() - position - 1 ;
//        homeViewModel.getAllServices( categories.get(index).id );
        homeViewModel.getServiceByIndex( index , categories.get(index).id );

    }

    @Override
    public void showDetail(String id) {

        Bundle bundle = new Bundle()  ;
        bundle.putString("id" , id );
        if (Tools.checkDestination(requireView(), R.id.nav_home)) {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_home_to_detailsFragment, bundle);
        }

    }
    //----------------------------TabLayout-------------------------------------------------------//


}