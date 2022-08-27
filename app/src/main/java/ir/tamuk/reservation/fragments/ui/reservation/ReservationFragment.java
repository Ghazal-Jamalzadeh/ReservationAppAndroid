package ir.tamuk.reservation.fragments.ui.reservation;

import static androidx.navigation.Navigation.findNavController;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dariushm2.PersianCaldroid.caldroidfragment.PersianCaldroidFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import calendar.PersianDate;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentReservationBinding;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.ReserveAdapter;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.ReserveModel;
import ir.tamuk.reservation.fragments.ui.reservation.database.SqlDatabaseReserve;

public class ReservationFragment extends Fragment {
    //binding
    private FragmentReservationBinding binding;
    //adapter
    public ReserveAdapter adapter;
    //selected Date
    public String date_show = "";
    //context
    public Context context = getContext();
    //week of Calender
    public String[] week = {
            "",
            "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه"
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReservationViewModel galleryViewModel =
                new ViewModelProvider(this).get(ReservationViewModel.class);
        binding = FragmentReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //today Date
        binding.turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(view).navigate(R.id.action_nav_reservation_to_signingFragment);
            }
        });
        PersianDate persianDate = new PersianDate();
        date_show = String.valueOf(persianDate.getYear())+"-"+String.valueOf(persianDate.getMonth())+"-"
                +String.valueOf(persianDate.getDayOfMonth());




        //recycler
        Recycler();
        //database Samples
        Tabale();
        //Calender
        Calender();
        //Spinner
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
                        Recycler();
                        break;
                    case 1:
                        Recycler();
                        break;
                    case 2:
                        Recycler();
                        break;
                    case 3:
                        Recycler();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context, "لطفا یکی از خدمات رو انتخاب کنید", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void Calender(){
        PersianCaldroidFragment persianCaldroidFragment = new PersianCaldroidFragment();
        // Set font
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iraniansans);
        persianCaldroidFragment.setTypeface(typeface);
        persianCaldroidFragment.setOnDateClickListener(new PersianCaldroidFragment.OnDateClickListener() {
            @Override
            public void onDateClick(PersianDate persianDate) {

                // Do something when a date is clicked
                //month Name
                binding.monthCardReservation.setText(persianDate.getMonthName());
                //day Number
                binding.daysCardReservation.setText(String.valueOf(persianDate.getDayOfMonth()));
                //week Name
                int i = persianDate.getDayOfWeek();
                binding.weekCardReservation.setText(week[i]);
                //get Date
                date_show = String.valueOf(persianDate.getYear())+"-"+String.valueOf(persianDate.getMonth())+"-"
                        +String.valueOf(persianDate.getDayOfMonth());
                Log.d("CALENDER DATE", "Calender: "+date_show);
                //Recycler
                Recycler();


            }
        });

        persianCaldroidFragment.setOnChangeMonthListener(new PersianCaldroidFragment.OnChangeMonthListener() {
            @Override
            public void onChangeMonth() {

                // Do something when user switches to previous or next month
            }
        });

        /* Add dates with a specified colors, you want to be circled on calendar */
        //calender customize
        HashMap<PersianDate, Integer> backgroundForDatesMap = new HashMap<>();
        backgroundForDatesMap.put(new PersianDate(), R.color.main);
        persianCaldroidFragment.setBackgroundResourceForDates(backgroundForDatesMap);
        PersianDate persianDate = new PersianDate();
        binding.daysCardReservation.setText(String.valueOf(persianDate.getDayOfMonth()));
        binding.monthCardReservation.setText(persianDate.getMonthName());
        int i = persianDate.getDayOfWeek();
        binding.weekCardReservation.setText(week[i]);

        //calender Show
        requireActivity().getSupportFragmentManager().beginTransaction().replace(
                R.id.persianCaldroid,
                persianCaldroidFragment,
                PersianCaldroidFragment.class.getName()).commit();
    }

    public void Tabale() {
        SqlDatabaseReserve sqlDatabase = new SqlDatabaseReserve(getContext());

        sqlDatabase.Insert("1401-5-25T09:00:00.000z", "09:00", 1, "پا");
        sqlDatabase.Insert("1401-5-25T11:00:00.000z", "11:00", 1, "بدن");
        sqlDatabase.Insert("1401-5-25T12:30:00.000z", "12:30", 1, "پا");
        sqlDatabase.Insert("1401-5-25T13:00:00.000z", "14:00", 1, "پا");
        sqlDatabase.Insert("1401-5-25T14:00:00.000z", "15:00", 1, "بدن");
        sqlDatabase.Insert("1401-5-25T15:00:00.000z", "16:00", 1, "پا");
        sqlDatabase.Insert("1401-5-25T16:00:00.000z", "21:00", 1, "دست");

        sqlDatabase.Insert("1401-5-26T11:00:00.000z", "11:00", 0, "بدن");
        sqlDatabase.Insert("1401-5-26T12:30:00.000z", "13:30", 0, "بدن");
        sqlDatabase.Insert("1401-5-26T14:00:00.000z", "16:00", 0, "پا");
        sqlDatabase.Insert("1401-5-26T17:00:00.000z", "17:00", 0 , "دست");
        sqlDatabase.Insert("1401-5-26T19:00:00.000z", "20:00", 0, "بدن");
    }

    public void Recycler() {
        //sql Database
        SqlDatabaseReserve sqlDatabaseReserve = new SqlDatabaseReserve(getContext());
        //arrays
        ArrayList<ReserveModel> reserveModels = sqlDatabaseReserve.getData();
        ArrayList<ReserveModel> result = new ArrayList<>();
        //for loop for filters
        for ( int i = 0; i<reserveModels.size(); i++) {
            if (binding.spinnerServices.getSelectedItemPosition() == 0) {
                if (reserveModels.get(i).id.contains(date_show)) {
                    Log.d("DATEA", "Recycler: "+ date_show);
                    result.add(reserveModels.get(i));
                }
            }else if (reserveModels.get(i).id.contains(String.valueOf(date_show))
                    && reserveModels.get(i).service.equals(binding.spinnerServices.getSelectedItem())) {
                result.add(reserveModels.get(i));
            }
        }
        reserveModels.clear();
        reserveModels.addAll(result);

        //horizontal Morning recycler
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false);
        binding.recyclerViewMorningReserve.setLayoutManager(verticalLayoutManager);
        adapter = new ReserveAdapter( reserveModels);
        binding.recyclerViewMorningReserve.setAdapter(adapter);
        if (result.isEmpty()){
            binding.emptyTextReservation.setVisibility(View.VISIBLE);
        }else {
            binding.emptyTextReservation.setVisibility(View.INVISIBLE);
        }
        //refresh
        adapter.notifyDataSetChanged();


    }

}