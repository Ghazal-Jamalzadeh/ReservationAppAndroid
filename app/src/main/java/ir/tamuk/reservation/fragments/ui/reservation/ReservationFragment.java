package ir.tamuk.reservation.fragments.ui.reservation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ir.mirrajabi.persiancalendar.PersianCalendarView;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.models.CalendarEvent;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentReservationBinding;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.ReserveAdapter;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.ReserveModel;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.RtlGridLayoutManager;
import ir.tamuk.reservation.fragments.ui.reservation.database.SqlDatabaseReserve;
import ir.tamuk.reservation.utils.Constants;

public class ReservationFragment extends Fragment {
    //binding
    private FragmentReservationBinding binding;
    //SQLite
    private SqlDatabaseReserve sqlDatabase;
    //adapter
    private ReserveAdapter adapter;
    //selected Date
    private String date_show = "";
    //context
    private Context context = getContext();

    //week of Calender Exchange
    private String[] week = {
            "",
            "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه"
    };
    //month of Calender Exchange
    private String[] month = {
            "",
            "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد",
            "شهریور ", "مهر", "آبان", " آذر", "دی", "بهمن", "اسفند"
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReservationViewModel galleryViewModel =
                new ViewModelProvider(this).get(ReservationViewModel.class);
        binding = FragmentReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //calender Today
        PersianCalendarHandler handler = binding.persianCalendar.getCalendar();
        PersianDate persianDate = handler.getToday();
        binding.daysCardReservation.setText(String.valueOf(persianDate.getDayOfMonth()));
        int i = persianDate.getDayOfWeek();
        int x = persianDate.getMonth();
        binding.weekCardReservation.setText(week[i]);
        binding.monthCardReservation.setText(month[x]);

        //Date of Calender Today
        date_show = String.valueOf(persianDate.getYear()) +"-"+String.valueOf(persianDate.getMonth())+"-"
                +String.valueOf(persianDate.getDayOfMonth());

        //Signing Button
        binding.signingButtonReservation.setOnClickListener(view -> {
            if (sqlDatabase.getDataId().isEmpty()){
                snackBarIconError();
            }else {
                Navigation.findNavController(view).navigate(R.id.action_nav_reservation_to_signingFragment);
            }
        });


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

    //Services Spinner
    public  void Spinnerr(){
        String[] items = new String[]
                {"همه موارد"  , "دست", "پا", "بدن" };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.font_spinner, items);
        binding.spinnerServices.setAdapter(arrayAdapter);

        binding.spinnerServices.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.e(Constants.TAG_KIA, "----------------------------Action down----------------------------");
                    view.setBackgroundResource( R.drawable.spinner_draw_2);
                    break;

                case MotionEvent.ACTION_HOVER_EXIT:
                    Log.e(Constants.TAG_KIA, "----------------------------Cancel----------------------------");
                    view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
                    break;
            }
            return false;
        });


        binding.spinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        Recycler();
                        binding.spinnerServices.setBackgroundResource(R.drawable.spinner_draw);

                        break;
                    case 1:
                        Recycler();
                        binding.spinnerServices.setBackgroundResource(R.drawable.spinner_draw);
                        break;
                    case 2:
                        Recycler();
                        binding.spinnerServices.setBackgroundResource(R.drawable.spinner_draw);

                        break;
                    case 3:
                        Recycler();
                        binding.spinnerServices.setBackgroundResource(R.drawable.spinner_draw);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


    }

    //Calender
    public void Calender(){


        PersianCalendarView calendarView  = binding.persianCalendar;
        PersianCalendarHandler calendarHandler = calendarView.getCalendar();
        ir.mirrajabi.persiancalendar.core.models.PersianDate today = calendarHandler.getToday();

        // Add an event called "Custom event" to this day
        calendarHandler.addLocalEvent(new CalendarEvent(today, "Custom event", false));
// Add an event called "Custom event 2" to 12 days later
        calendarHandler.addLocalEvent(new CalendarEvent(today.clone().rollDay(12,true), "Custom event 2", true));
// Add an event called "Custom event 3" to 1399/1/10 later
        calendarHandler.addLocalEvent(new CalendarEvent(new PersianDate(1401,6,15), "Custom event 2", true));

        calendarHandler.getAllEventsForDay(today);
        calendarHandler.getLocalEvents();
        calendarHandler.getLocalEventsForDay(today);
        calendarHandler.getOfficialEventsForDay(today);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iraniansans);
        calendarHandler.setTypeface(typeface);

        calendarView.setOnDayClickedListener(persianDate -> {

            binding.daysCardReservation.setText(String.valueOf(persianDate.getDayOfMonth()));
            int i = persianDate.getDayOfWeek();
            int x = persianDate.getMonth();
            binding.weekCardReservation.setText(week[i]);
            binding.monthCardReservation.setText(month[x]);
            date_show = String.valueOf(persianDate.getYear()) +"-"+String.valueOf(persianDate.getMonth())+"-"
                    +String.valueOf(persianDate.getDayOfMonth());
            Log.d(Constants.TAG_KIA, "Calender: "+ date_show);
            Recycler();
            sqlDatabase.deleteIdAll();
        });
        calendarView.setOnMonthChangedListener(persianDate -> {
            int x = persianDate.getMonth();
            binding.monthCardReservation.setText(month[x]);
            binding.weekCardReservation.setText("");
            binding.daysCardReservation.setText(String.valueOf(persianDate.getYear()));
        });

    }

    //Reservations Example
    public void Tabale() {
        sqlDatabase = new SqlDatabaseReserve(getContext());
        if (sqlDatabase.getDataId().isEmpty()) {

            sqlDatabase.Insert("1401-6-9T09:00:00.000z", "09:00", 0, "پا");
            sqlDatabase.Insert("1401-6-9T11:00:00.000z", "11:00", 0, "بدن");
            sqlDatabase.Insert("1401-6-9T12:30:00.000z", "12:30", 0, "پا");
            sqlDatabase.Insert("1401-6-9T13:00:00.000z", "14:00", 0, "پا");
            sqlDatabase.Insert("1401-6-9T14:00:00.000z", "15:00", 0, "بدن");
            sqlDatabase.Insert("1401-6-9T15:00:00.000z", "16:00", 0, "پا");
            sqlDatabase.Insert("1401-6-9T16:00:00.000z", "21:00", 0, "دست");

            sqlDatabase.Insert("1401-6-10T11:00:00.000z", "11:00", 0, "بدن");
            sqlDatabase.Insert("1401-6-10T12:30:00.000z", "13:30", 0, "بدن");
            sqlDatabase.Insert("1401-6-10T14:00:00.000z", "16:00", 0, "پا");
            sqlDatabase.Insert("1401-6-10T17:00:00.000z", "17:00", 0, "دست");
        }
    }

    //RecyclerView for Reservations
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
                    Log.d(Constants.TAG_KIA, "Recycler: "+ date_show);
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
//        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(),
//                RecyclerView.HORIZONTAL, false);
//        verticalLayoutManager.setReverseLayout(true);
        int numberOfColumns = 4;
        GridLayoutManager gridLayoutManager = new RtlGridLayoutManager(getContext(), numberOfColumns);
        binding.recyclerViewMorningReserve.setLayoutManager(gridLayoutManager);
//        binding.recyclerViewMorningReserve.setLayoutManager(verticalLayoutManager);
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

    //error snackbar for Verification phone
    private void snackBarIconError() {
        final Snackbar snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_layout, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText("باید یک رزرو انتخاب کنید");
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_baseline_perm_device_information_24);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(getResources().getColor(R.color.red));
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }





}