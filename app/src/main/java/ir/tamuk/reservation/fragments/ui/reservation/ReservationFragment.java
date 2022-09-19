package ir.tamuk.reservation.fragments.ui.reservation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import net.time4j.PlainDate;
import net.time4j.calendar.PersianCalendar;
import net.time4j.format.expert.ChronoFormatter;
import net.time4j.format.expert.Iso8601Format;
import net.time4j.format.expert.PatternType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ir.mirrajabi.persiancalendar.PersianCalendarView;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentReservationBinding;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.ReserveAdapterAll;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.RtlGridLayoutManager;
import ir.tamuk.reservation.models.FreeTimeAm;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.TokenManager;
import ir.tamuk.reservation.viewModels.ReservationViewModel;

public class ReservationFragment extends Fragment implements OnSelectedItem {
    //binding
    private FragmentReservationBinding binding;
    //adapter
    private ReserveAdapterAll adapter;

    //selected Date
    private String date_show = "";
    //Id and time Api
    private String idService;
    private String time;
    //context
    private Context context = getContext();
    //ViewModel
    private ReservationViewModel galleryViewModel;
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

    int lastSelectedIndexAm = -1;
    int lastSelectedIndexPm = -1;
    private ArrayList<FreeTimeAm> freeTimeAmArrayList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
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

        //Signing Button
        binding.signingButtonReservation.setOnClickListener(view -> {
//            if (sqlDatabase.getDataId().isEmpty()){
//                snackBarIconError();
//            }else {
            Navigation.findNavController(view).navigate(R.id.action_to_signingFragment);
//            }
        });

        //test
        String a = TokenManager.getAccessToken(getContext());
        String r = TokenManager.getRefreshToken(getContext());
        Log.d(Constants.TAG_KIA, "tokensA: ->"+a );
        Log.d(Constants.TAG_KIA, "tokensR: ->"+r );

        //recycler
        callSearchServicesApi();
        Calender();
        Spinnerr();
        callApi();
        Recycler();






        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Services Spinner
    public  void Spinnerr(){
        galleryViewModel.servicesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Service>>() {
            @Override
            public void onChanged(ArrayList<Service> services) {
                Log.d("ghazal", "size : " + services.size());
                ArrayList<String> items = new ArrayList<>();
                for (int i = 0; i<services.size(); i++ ){
                    String result = services.get(i).name;
                    items.add(result);
                }

                idService = services.get(0).id;

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.font_spinner, items );
                Log.d(Constants.TAG_KIA, "onChanged: "+items);

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
//                        for (int i = 0; i<services.size(); i++){
//                            position = i;
//                        }
                        idService = services.get(position).id;
                        callApi();
                        Recycler();

                        String a = adapterView.getAdapter().getItem(position).toString();
                        String b = adapterView.getSelectedItemId()+"";
                        String d = services.get(position).name;

                        Log.d(Constants.TAG_KIA, "onItemSelectedA: ->"+a);
                        Log.d(Constants.TAG_KIA, "onItemSelectedB: ->"+b);
                        Log.d(Constants.TAG_KIA, "onItemSelectedC ->"+idService+d);
                        Recycler();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        adapterView.getEmptyView().setBackgroundResource(R.drawable.spinner_draw);

                    }
                });

            }
        });


    }

    //Calender
    public void Calender(){
        PersianCalendarView calendarView  = binding.persianCalendar;
        PersianCalendarHandler calendarHandler = calendarView.getCalendar();
        PersianDate today = calendarHandler.getToday();

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        time = formatter.format(todayDate);
        Log.d(Constants.TAG_KIA, "Today: ->"+ time);

//        // Add an event called "Custom event" to this day
//        calendarHandler.addLocalEvent(new CalendarEvent(today, "Custom event", false));
//// Add an event called "Custom event 2" to 12 days later
//        calendarHandler.addLocalEvent(new CalendarEvent(today.clone().rollDay(12,true), "Custom event 2", true));
//// Add an event called "Custom event 3" to 1399/1/10 later
//        calendarHandler.addLocalEvent(new CalendarEvent(new PersianDate(1401,6,15), "Custom event 2", true));

        calendarHandler.getAllEventsForDay(today);
        calendarHandler.getLocalEvents();
        calendarHandler.getLocalEventsForDay(today);
        calendarHandler.getOfficialEventsForDay(today);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iraniansans);
        calendarHandler.setTypeface(typeface);


        calendarView.setOnDayClickedListener(persianDate -> {

            String mounth ;
            String dayOfMonth;
            if (String.valueOf(persianDate.getMonth()).length() == 1){
                mounth = "0"+String.valueOf(persianDate.getMonth());
            }else {
                mounth = String.valueOf(persianDate.getMonth());
            }
            if (String.valueOf(persianDate.getDayOfMonth()).length() ==1){
                dayOfMonth = "0"+String.valueOf(persianDate.getDayOfMonth());

            }else {
                dayOfMonth = String.valueOf(persianDate.getDayOfMonth());

            }
            binding.daysCardReservation.setText(String.valueOf(persianDate.getDayOfMonth()));
            int i = persianDate.getDayOfWeek();
            int x = persianDate.getMonth();
            binding.weekCardReservation.setText(week[i]);
            binding.monthCardReservation.setText(month[x]);
            date_show = String.valueOf(persianDate.getYear()) +"/"+mounth+"/"
                    +dayOfMonth;
            Log.d(Constants.TAG_KIA, "Calender: ->"+ date_show);
            Log.d(Constants.TAG_KIA, "dateC: ->"+ date_show);

            Recycler();
            time = date(date_show);
            callApi();

        });
        calendarView.setOnMonthChangedListener(persianDate -> {
            int x = persianDate.getMonth();
            binding.monthCardReservation.setText(month[x]);
            binding.weekCardReservation.setText("");
            binding.daysCardReservation.setText(String.valueOf(persianDate.getYear()));
        });

    }


    //RecyclerView for Reservations
    public void Recycler() {



    }

    private void callSearchServicesApi(){
        if (Connectivity.isConnected(getContext())) {
            galleryViewModel.getServices(1,20,"");
        } else {
            Toast.makeText(getContext(), "اینترنت وصل نیس ", Toast.LENGTH_SHORT).show();
        }
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


    public String date(String input){
        PersianCalendar jalali =
                null;
        try {
            jalali = ChronoFormatter
                    .ofPattern("yyyy/MM/dd", PatternType.CLDR, Locale.ROOT, PersianCalendar.axis())
                    .parse(input);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        PlainDate gregorian = jalali.transform(PlainDate.axis().getChronoType());
        String iso8601 = Iso8601Format.EXTENDED_DATE.format(gregorian);
        System.out.println(iso8601); // 2018-04-03
        Log.d(Constants.TAG_KIA, "date: ->"+iso8601);


        String output = ChronoFormatter
                .ofPattern("yyyy/MM/dd", PatternType.CLDR, Locale.ROOT, PlainDate.axis())
                .format(gregorian);

        return iso8601;
    }

    public void callApi(){
        Log.d(Constants.TAG_KIA, "time?: ->"+time);
        Log.d(Constants.TAG_KIA, "id?: ->"+idService);
        galleryViewModel.getReservations(time,"631869b10bfaf719ef8b76cf" );
        Log.d(Constants.TAG_KIA, "sendtime?: ->"+time);
        galleryViewModel.freeAm.observe(getViewLifecycleOwner(), freeTimeAms -> {
            freeTimeAmArrayList = new ArrayList<>();
            FreeTimeAm freeTimeAm = new FreeTimeAm();
            freeTimeAm.time = "Am";
            for (int i = 0; i<freeTimeAms.size(); i++){
                freeTimeAm.title = freeTimeAms.get(i);
                freeTimeAm.aBoolean = false;
            }
            freeTimeAmArrayList.add(freeTimeAm);
            int numberOfColumns = 4;
            GridLayoutManager gridLayoutManager = new RtlGridLayoutManager(getContext(), numberOfColumns);
            binding.recyclerViewMorningReserve.setLayoutManager(gridLayoutManager);
            adapter = new ReserveAdapterAll(freeTimeAmArrayList, this::onItem);
            binding.recyclerViewMorningReserve.setAdapter(adapter);
            Log.d(Constants.TAG_KIA, "callApi: "+adapter);
            adapter.notifyDataSetChanged();

        });

        galleryViewModel.freePm.observe(getViewLifecycleOwner(), freeTimePms -> {

            int numberOfColumns = 4;
            GridLayoutManager gridLayoutManager = new RtlGridLayoutManager(getContext(), numberOfColumns);
            binding.recyclerViewEveningReserve.setLayoutManager(gridLayoutManager);
            freeTimeAmArrayList = new ArrayList<>();
            FreeTimeAm freeTimeAm = new FreeTimeAm();
            freeTimeAm.time = "Am";
            for (int i = 0; i<freeTimePms.size(); i++){
                freeTimeAm.title = freeTimePms.get(i);
                freeTimeAm.aBoolean = false;
            }
            freeTimeAmArrayList.add(freeTimeAm);
            adapter = new ReserveAdapterAll(freeTimeAmArrayList, this::onItem);
            binding.recyclerViewEveningReserve.setAdapter(adapter);
            Log.d(Constants.TAG_KIA, "callApi: "+adapter);
            adapter.notifyDataSetChanged();


        });

        galleryViewModel.notRes.observe(getViewLifecycleOwner(), aBoolean -> {

            if (aBoolean){
                binding.showAllReserves.setVisibility(View.GONE);
                binding.emptyTextReservation.setVisibility(View.VISIBLE);
            }else{

                binding.showAllReserves.setVisibility(View.VISIBLE);
                binding.emptyTextReservation.setVisibility(View.GONE);
            }

        });

        galleryViewModel.errorMessageRes.observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
        });
    }


    @Override
    public void onItem(int position, boolean click, String key) {
        if (lastSelectedIndexAm != -1){
            freeTimeAmArrayList.get(position).aBoolean = true;


        }
        if (lastSelectedIndexPm !=-1){

        }
    }
}