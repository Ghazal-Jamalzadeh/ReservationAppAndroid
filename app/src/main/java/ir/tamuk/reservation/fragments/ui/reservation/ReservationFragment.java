package ir.tamuk.reservation.fragments.ui.reservation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentReservationBinding;

public class ReservationFragment extends Fragment {

    private FragmentReservationBinding binding;

    public Context context = getContext();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReservationViewModel galleryViewModel =
                new ViewModelProvider(this).get(ReservationViewModel.class);

        binding = FragmentReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinnerr();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public  void Spinnerr(){

        String[] items = new String[]
                {"همه موارد"  , "دست", "پا", "بدن" };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.font_spinner, items);
        binding.spinnerServices.setAdapter(arrayAdapter);
        binding.spinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context, "لطفا یکی از خدمات رو انتخاب کنید", Toast.LENGTH_SHORT).show();
            }
        });

    }
}