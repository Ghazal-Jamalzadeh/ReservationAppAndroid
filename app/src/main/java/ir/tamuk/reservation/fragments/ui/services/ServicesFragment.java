package ir.tamuk.reservation.fragments.ui.services;

import static android.os.Build.VERSION_CODES.S;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import ir.tamuk.reservation.Interfaces.ServiceDatailInterface;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.adapter.ServicesAdapter;
import ir.tamuk.reservation.databinding.FragmentServicesBinding;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.models.ServiceData;
import ir.tamuk.reservation.models.Services;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.viewModels.ServicesViewModel;


@SuppressWarnings("ALL")
public class ServicesFragment extends Fragment implements ServiceDatailInterface {

    ArrayList<Service> services = new ArrayList<>() ;
    private FragmentServicesBinding binding;
    Services ser = new Services();
    Services ser2 = new Services();
    private ServicesAdapter adapter ;
    private ServicesViewModel servicesViewModel;
    private boolean isPopUp = false;



    public View onCreateView(@NonNull LayoutInflater  inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        servicesViewModel = new ViewModelProvider(this).get(ServicesViewModel.class);
        binding = FragmentServicesBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        onBackPress();
         binding.recycler.setVisibility(View.VISIBLE);
         binding.linearService.setVisibility(View.INVISIBLE);
         binding.relativeService.setVisibility(View.INVISIBLE);
         binding.linearTop.setClickable(false);
         binding.linearService.setClickable(false);

         binding.linearTop.setClickable(false);
        servicesViewModel.loading.observe(getViewLifecycleOwner(),aBoolean -> {
            if (aBoolean){
                binding.progressbar.setVisibility(View.INVISIBLE);
            }else {

                binding.progressbar.setVisibility(View.VISIBLE);
            }

        });

        binding.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
                Navigation.findNavController(view).navigate(R.id.nav_reservation);
                Log.d("MANSOUR", "onClick: "+"button");

            }
        });
        binding.image.setOnClickListener(view -> {
            toggle(binding.relativeService);
            binding.linearService.setVisibility(View.INVISIBLE);
            binding.linearTop.setVisibility(View.INVISIBLE);
            binding.linearTop.setClickable(false);
            isPopUp = false;
//                Navigation.findNavController(view).popBackStack()

        });
        binding.linearTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle(binding.relativeService);
                binding.linearTop.setVisibility(View.INVISIBLE);
                binding.linearService.setVisibility(View.INVISIBLE);
                binding.linearTop.setClickable(false);
                isPopUp = false;
            }
        });

//        Handler h = new Handler();
//        Runnable r = () -> {
//            callGetServicesApi();
//
//        };h.postDelayed(r, 1000);


//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//                Navigation.findNavController(getView()).popBackStack(R.id.nav_reservation, false);
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        String a;

//        int id = getArguments().getInt("id");
//        Log.d("MANSOUR", "onCreateView: "+ id);


        // Inflate the layout for this fragment

//        binding.linear3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            toggle(binding.linearService);
//            binding.linearService.setVisibility(View.VISIBLE);
//
//            }
//        });


        binding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.show_more_text),getResources().getColor(R.color.main));
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.swipeRefreshLayout.setRefreshing(false);
                        callSearchServicesApi();


                    }
                }, 2000);
            }
        });
        //call api
        callSearchServicesApi();
        servicesViewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                if (b){
                    binding.swipeRefreshLayout.setRefreshing(!b);
                    binding.serviceContainer.setVisibility(View.VISIBLE);
                    binding.linearService.setVisibility(View.INVISIBLE);
                }else{
                    binding.swipeRefreshLayout.setRefreshing(b);


                }
//                binding.swipeRefreshLayout.setRefreshing(b);

            }
        });

//        ser.text1 = "salam";
//        ser.text2 = "hello";
//
//        ser2.text1 = "salam";
//        ser2.text2 = "hello";
//        services.clear();
//        services.add(ser);
//        services.add(ser2);



//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

       servicesViewModel.serviceLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<ServiceData>>() {
           @Override
           public void onChanged(ArrayList<ServiceData> serviceData) {
               Log.d("mansour", "onChanged: serviceLiveData");
               binding.linearTop.setClickable(true);
              toggle(binding.relativeService);
              binding.linearService.setVisibility(View.VISIBLE);
              binding.linearTop.setVisibility(View.VISIBLE);
              isPopUp = true;
           }
       });
//
//        servicesViewModel.servicesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Service>>() {
//
//            @Override
//            public void onChanged(ArrayList<Service> services) {
//                Log.d("mansour", "onChanged:servicesLiveData ");
//                recycler(services);
//                adapter.notifyDataSetChanged();
//
//            }
//        });
        binding.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
                Navigation.findNavController(view).navigate(R.id.nav_reservation);
                Log.d("MANSOUR", "onClick: "+"button");

            }
        });

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (isPopUp){
                    toggle(binding.relativeService);
                    binding.linearTop.setClickable(false);
                    binding.linearTop.setVisibility(View.INVISIBLE);
                    binding.linearService.setVisibility(View.INVISIBLE);
                    isPopUp = false;
                }else {
                    Navigation.findNavController(getView()).popBackStack();

                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return root;


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
    }

    private void callSearchServicesApi(){
        Log.d("mansour", "callSearchServicesApi: ");
        if (Connectivity.isConnected(getContext())) {
            servicesViewModel.searchServices(1,20,"");
            servicesViewModel.servicesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Service>>() {

                @Override
                public void onChanged(ArrayList<Service> services) {
                    Log.d("mansour", "onChanged:servicesLiveData ");
                    recycler(services);
                    adapter.notifyDataSetChanged();

                }
            });

        } else {
            Toast.makeText(getContext(), "اینترنت وصل نیس ", Toast.LENGTH_SHORT).show();
        }
//        Log.d("mansour", "callSearchServicesApi: ");
    }

    @Override
    public void onPause() {
        super.onPause();
            Log.d("mansour", "isPopUp: "+isPopUp);
        if (isPopUp){
            binding.linearService.setVisibility(View.INVISIBLE);
              binding.relativeService.setVisibility(View.INVISIBLE);
            binding.linear3.setVisibility(View.INVISIBLE);
            binding.relativeService.setVisibility(View.INVISIBLE);
            binding.relativeRecycler.setVisibility(View.INVISIBLE);
            binding.recycler.setVisibility(View.INVISIBLE);
            binding.serviceContainer.setVisibility(View.INVISIBLE);
            binding.swipeRefreshLayout.setVisibility(View.INVISIBLE);
            Log.d("mansour", "onPause: ");
            getViewModelStore().clear();
            isPopUp=false;
        }
        Log.d("mansour", "isPopUp2: "+isPopUp);


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("mansour", "onStop: ");  if (isPopUp){
            binding.linearService.setVisibility(View.INVISIBLE);
            binding.relativeService.setVisibility(View.INVISIBLE);
            binding.linearTop.setVisibility(View.INVISIBLE);
            binding.linear3.setVisibility(View.INVISIBLE);
            binding.relativeService.setVisibility(View.INVISIBLE);
            binding.relativeRecycler.setVisibility(View.INVISIBLE);
            binding.recycler.setVisibility(View.INVISIBLE);
            binding.serviceContainer.setVisibility(View.INVISIBLE);
            binding.swipeRefreshLayout.setVisibility(View.INVISIBLE);
            getViewModelStore().clear();
            Log.d("mansour", "onPause: ");
            isPopUp=false;
        }


    }

    private void toggle(View view) {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        transition.addTarget(view);

        TransitionManager.beginDelayedTransition((ViewGroup) view,transition);
        if (view.getVisibility() ==View.VISIBLE){
            view.setVisibility(View.INVISIBLE);
           view.setClickable(false);
        }else {
            view.setVisibility(View.VISIBLE);
            view.setClickable(true);
        }
    }

    @Override
    public void ShowDetail(String id) {
        callGetServicesApi(id);
    }

    private void callGetServicesApi(String id){

        if (Connectivity.isConnected(getContext())) {
//            String idService= getArguments().getString("serviceId");
            servicesViewModel.getService(id);
            Log.d("mansour", "callGetServicesApi: "+id);

            servicesViewModel.serviceLiveData.observe(getViewLifecycleOwner(), serviceData -> {
                binding.titleServicesInfo.setText(serviceData.get(0).name);
                binding.descriptionServicesInfo.setText(serviceData.get(0).description);

            });

        } else {
            Toast.makeText(getContext(), "اینترنت وصل نیس ", Toast.LENGTH_SHORT).show();
        }

    }
    public void onBackPress(){


        //BackPress Action (for back to HomeFragment)
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//                getViewModelStore().clear();
//                Navigation.findNavController(getView()).popBackStack();
//                Navigation.findNavController(getView()).popBackStack(R.id.nav_reservation, true);
//
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()

    }

    public void recycler(ArrayList<Service> services){
        adapter = new ServicesAdapter(getContext() ,services, ServicesFragment.this::ShowDetail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(layoutManager);
        binding.recycler.setHasFixedSize(true);
    }

}