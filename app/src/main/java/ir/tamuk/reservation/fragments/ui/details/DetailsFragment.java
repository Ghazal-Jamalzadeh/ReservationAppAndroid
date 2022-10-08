package ir.tamuk.reservation.fragments.ui.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentDetailsBinding;
import ir.tamuk.reservation.fragments.ui.profile.ProfileViewModel;
import ir.tamuk.reservation.models.ServiceData;
import ir.tamuk.reservation.repository.DetailsRepository;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.Keys;
import ir.tamuk.reservation.utils.PriceFormat;
import ir.tamuk.reservation.utils.Tools;

public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private FragmentDetailsBinding binding ;
    private DetailsViewModel viewModel ;
    private String id =  "" ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        binding =  FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //swipe layout attributes
        binding.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorSecondary)
                , getResources().getColor(R.color.colorPrimary));
        binding.swipeRefreshLayout.setRefreshing(true);
        binding.swipeRefreshLayout.setEnabled(true);

        //get bundle
        id = getArguments().getString("id") ;

        //call api
        viewModel.getDetail(getContext() , id  );

        //observers
        viewModel.serviceDetailLiveData.observe(getViewLifecycleOwner(), new Observer<ServiceData>() {
            @Override
            public void onChanged(ServiceData serviceData) {

                showDetail(serviceData) ;
                binding.container.setVisibility(View.VISIBLE);
            }
        });

        viewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                binding.swipeRefreshLayout.setEnabled(aBoolean);
                binding.swipeRefreshLayout.setRefreshing(aBoolean);

            }
        });

        viewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Tools.showToast(getContext() ,  s);
            }
        });

        //close button
        binding.icClose.setOnClickListener(view1 -> {
            Navigation.findNavController(requireView()).popBackStack() ;
        });

        //reserve button
        binding.btnReserve.setOnClickListener(view1 -> {
            if (Tools.checkDestination(requireView() , R.id.detailsFragment)){
                Bundle bundle = new Bundle()  ;
                bundle.putString( Keys.SERVICE_ID , id );
                Navigation.findNavController(requireView()).navigate(R.id.action_to_navReservation , bundle);
            }
        });

        //Text watcher - price format
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                PriceFormat.formatPrice(binding.txtPrice, this, charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        binding.txtPrice.addTextChangedListener(textWatcher);

    }

    private void showDetail(ServiceData data){

        //photo
        if (!data.mainPhoto.filename.equals("")) {
            Glide.with(getContext()).load(Constants.DOWNLOAD_PHOTO_URL + data.mainPhoto.filename)
                    .transition(DrawableTransitionOptions.withCrossFade()) //for loading image smoothly and fastly.
                    .into(binding.mainPhoto);
        }

        //title
        binding.txtTitle.setText(data.name);

        //time
        binding.txtTime.setText(data.time + " دقیقه ");

        //price
        binding.txtPrice.setText(String.valueOf(data.time));

        //description
        binding.description.setText(data.description);


    }
}