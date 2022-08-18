package ir.tamuk.reservation.activities.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.api.ApiClient;
import ir.tamuk.reservation.api.ApiServices;
import ir.tamuk.reservation.databinding.FragmentHomeBinding;
import ir.tamuk.reservation.models.MoviesList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    //api
    private ApiServices apiServices ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //client
        apiServices = ApiClient.getClient().create(ApiServices.class);

        //call api
        Call<MoviesList> call1 = apiServices.getMovies(1);

        //response
        call1.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {

                for (int i = 0; i <response.body().movies.size() ; i++) {

                    Log.d("ghazal", "movie: " + response.body().movies.get(i).poster);
                }


//                moviesAdapter = new MoviesAdapter(getContext() , response.body());
//                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
//                binding.recyclerView.setAdapter(moviesAdapter);
//                binding.recyclerView.setLayoutManager(layoutManager);
//                binding.recyclerView.setHasFixedSize(true);


            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {

            }
        });





        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btn.setOnClickListener(view1 -> {

            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_testFragment);

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}