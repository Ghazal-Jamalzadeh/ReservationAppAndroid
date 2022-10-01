package ir.tamuk.reservation.fragments.ui.reservation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.Objects;

import ir.mirrajabi.persiancalendar.PersianCalendarView;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentReservationBinding;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.ReserveAdapterAll;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.RtlGridLayoutManager;
import ir.tamuk.reservation.models.BodyFactor;
import ir.tamuk.reservation.models.FreeTimeAm;
import ir.tamuk.reservation.models.FreeTimePm;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.models.Services;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.SnackBars;
import ir.tamuk.reservation.utils.TokenManager;
import ir.tamuk.reservation.viewModels.ReservationViewModel;

public class ReservationFragment extends Fragment implements OnSelectedItem {
    //binding
    private FragmentReservationBinding binding;
    //context
    private Context context = getContext();
    //ViewModel
    private ReservationViewModel galleryViewModel;


    //Strings
    //selected Date
    private String date_show = "";
    //Id and time Api
    private String idService;
    private String nameService;
    private String time;
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
    //time of Selected Reserve
    private String selectedTime = "";
    //date of Selected Reserve
    private String selectedDate= "";
    private String selectedDateMiladi= "";

    //Integers
    //lastReservation Select
    private int lastSelectedIndexAm = -1;
    private int lastSelectedIndexPm = -1;

    //ArrayLists
    private ArrayList<FreeTimeAm> freeTimeAmArrayList = new ArrayList<>();
    private ArrayList<FreeTimeAm> freeTimePmArrayList = new ArrayList<>();
    //adapters
    private ReserveAdapterAll adapterAm;
    private ReserveAdapterAll adapterPm;

    private BodyFactor bodyFactor = new BodyFactor();
    private Bundle bundle = new Bundle();
    private SnackBars snackBars = new SnackBars();
    private boolean refreshing = false;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(ReservationViewModel.class);
        binding = FragmentReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ////////////////////----------~~ <calender Today> ~~----------////////////////////
        PersianCalendarHandler handler = binding.persianCalendar.getCalendar();
        PersianDate persianDate = handler.getToday();
        binding.daysCardReservation.setText(String.valueOf(persianDate.getDayOfMonth()));
        int y = persianDate.getDayOfWeek();
        int x = persianDate.getMonth();
        binding.weekCardReservation.setText(week[y]);
        binding.monthCardReservation.setText(month[x]);
        selectedDate = week[y]+" "+ persianDate.getDayOfMonth()+" "+ month[x];
        String m = String.valueOf(persianDate.getMonth());
        String dM = String.valueOf(persianDate.getDayOfMonth());
        if (m.length() == 1){
            m = "0"+persianDate.getMonth();
        }
        if (dM.length() == 1){
            dM = "0"+persianDate.getDayOfMonth();
        }
        String d = persianDate.getYear()+"/"+m+"/"+dM;
        selectedDateMiladi = date(d);

        ////////////////////----------~~ <calender Today> ~~----------////////////////////

        binding.showAllReserves.setVisibility(View.INVISIBLE);

        //test
        String a = TokenManager.getAccessToken(getContext());
        String r = TokenManager.getRefreshToken(getContext());
        Log.d(Constants.TAG_KIA, "tokensA: ->" + a);
        Log.d(Constants.TAG_KIA, "tokensR: ->" + r);




        ////////////////////----------~~ <Swipe> ~~----------////////////////////
        binding.swipeRefreshLayoutReservation.setRefreshing(true);
        binding.swipeRefreshLayoutReservation.setEnabled(true);
        binding.swipeRefreshLayoutReservation.setColorSchemeColors(getResources().getColor(R.color.show_more_text), getResources().getColor(R.color.main));
        binding.swipeRefreshLayoutReservation.setOnRefreshListener(() -> {
//            binding.swipeRefreshLayoutReservation.setRefreshing(false);
            callSearchServicesApi();
        });


        ////////////////////----------~~ <Swipe> ~~----------////////////////////

        //callApi
        Calender();
        Spinnerr();
        callSearchServicesApi();
        Recycler();


        galleryViewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                refreshing = aBoolean;

            }
        });
        galleryViewModel.order.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                bundle.putString("order", s);
                Log.d("JALLAD", "true: ");

            }
        });

        galleryViewModel.isSuccessLiveData.observe(getViewLifecycleOwner(), aBoolean -> {

            if (aBoolean){


                Navigation.findNavController(getView()).navigate(R.id.action_to_factorFragment, bundle);

            }
        });

        galleryViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                snackBarIconError(s, getView());
                Log.d("JALLAD", "err: ");
            }
        });




        //Signing Button
        binding.signingButtonReservation.setOnClickListener(view -> {

            Log.d("KIAOKO", "lastSelectedIndexAm" + lastSelectedIndexAm);
            Log.d("KIAOKO", "lastSelectedIndexPm" + lastSelectedIndexPm);
            if (lastSelectedIndexPm != -1 || lastSelectedIndexAm != -1) {

                if (lastSelectedIndexPm !=-1) {
                    selectedTime = freeTimePmArrayList.get(lastSelectedIndexPm).title;
                    Log.d(Constants.TAG_KIA, "pm: "+selectedTime);
                }
                if (lastSelectedIndexAm != -1){
                    selectedTime = freeTimeAmArrayList.get(lastSelectedIndexAm).title;
                    Log.d(Constants.TAG_KIA, "am: "+selectedTime);
                }
                bundle.putString("serviceId", idService);
                bundle.putString("serviceName", nameService);
                bundle.putString("reserveTime", selectedTime);
                bundle.putString("reserveDate", selectedDate);
                bundle.putString("reserveDateMiladi", selectedDateMiladi);
                bundle.putBoolean("isFactor", true);

                if (TokenManager.hasAccessToken(getContext())){
                    bodyFactor.dateFactor = selectedDateMiladi+"T"+selectedTime;
                    bodyFactor.serviceFactor = idService;

                    galleryViewModel.callFactor(bodyFactor, TokenManager.getAccessToken(getContext()));




                }else {
                    Navigation.findNavController(view).navigate(R.id.action_to_signingFragment, bundle);
                }

            } else {
//                snackBarIconError("باید یک رزرو انتخاب کنید", view);
                snackBars.showErrorSelectReserve(getView(), getContext());
            }

        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ////////////////////----------~~ <ViewModels> ~~----------////////////////////

        galleryViewModel.freeAm.observe(getViewLifecycleOwner(), freeTimeAms -> {
            freeTimeAmArrayList.clear();

            for (int i = 0; i < freeTimeAms.size(); i++) {
                FreeTimeAm item = new FreeTimeAm();
                item.title = freeTimeAms.get(i);
                Log.d(Constants.TAG_KIA, "title: ->" + item.title);
                item.time = "Am";
                item.aBoolean = false;
                freeTimeAmArrayList.add(item);
            }

            if (lastSelectedIndexAm != -1) {
                freeTimeAmArrayList.get(lastSelectedIndexAm).aBoolean = false;
                lastSelectedIndexAm = -1;
            }

            Log.d(Constants.TAG_KIA, "Am: ->" + freeTimeAmArrayList);
            adapterAm.notifyDataSetChanged();

        });

        galleryViewModel.freePm.observe(getViewLifecycleOwner(), freeTimePms -> {
            freeTimePmArrayList.clear();

            for (int i = 0; i < freeTimePms.size(); i++) {
                FreeTimeAm item = new FreeTimeAm();
                item.time = "Pm";
                item.title = freeTimePms.get(i);
                item.aBoolean = false;
                freeTimePmArrayList.add(item);
            }

            if (lastSelectedIndexPm != -1) {
                freeTimePmArrayList.get(lastSelectedIndexPm).aBoolean = false;
                lastSelectedIndexPm = -1;

            }

            Log.d(Constants.TAG_KIA, "callApi: " + adapterPm);
            adapterPm.notifyDataSetChanged();

        });

        galleryViewModel.notRes.observe(getViewLifecycleOwner(), aBoolean -> {

            if (aBoolean) {
                binding.showAllReserves.setVisibility(View.GONE);
                binding.emptyTextReservation.setVisibility(View.VISIBLE);
            } else {
                binding.showAllReserves.setVisibility(View.VISIBLE);
                binding.emptyTextReservation.setVisibility(View.GONE);
            }

        });

        galleryViewModel.errorMessageRes.observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
        });
        galleryViewModel.getServices(1, 20, "");

        ////////////////////----------~~ <ViewModels> ~~----------////////////////////


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //Services Spinner
    public void Spinnerr() {

        galleryViewModel.servicesLiveData.observe(getViewLifecycleOwner(), services -> {
            Log.d("ghazal", "size : " + services.size());

            ArrayList<String> items = new ArrayList<>();
            for (int i = 0; i < services.size(); i++) {
                String result = services.get(i).name;
                items.add(result);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.font_spinner, items);
            binding.spinnerServices.setAdapter(arrayAdapter);

//            binding.spinnerServices.setOnTouchListener((view, motionEvent) -> {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.e(Constants.TAG_KIA, "----------------------------Action down----------------------------");
////                        view.setBackgroundResource(R.drawable.spinner_draw_2);
//                        break;
//                }
//                return false;
//            });

            binding.spinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    idService = services.get(position).id;
                    nameService = services.get(position).name;

                    Log.d("ghazal", "onItemSelected: " + position);

                    String a = adapterView.getAdapter().getItem(position).toString();
                    String b = adapterView.getSelectedItemId() + "";
                    String d = services.get(position).name;

                    Log.d(Constants.TAG_KIA, "onItemSelectedA: ->" + a);
                    Log.d(Constants.TAG_KIA, "onItemSelectedB: ->" + b);
                    Log.d(Constants.TAG_KIA, "onItemSelectedC ->" + idService + d);
                    callSearchServicesApi();
                    adapterPm.notifyDataSetChanged();
                    adapterAm.notifyDataSetChanged();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        });
    }


    //Calender
    public void Calender() {
        PersianCalendarView calendarView = binding.persianCalendar;
        PersianCalendarHandler calendarHandler = calendarView.getCalendar();
        PersianDate today = calendarHandler.getToday();

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        time = formatter.format(todayDate);
        Log.d(Constants.TAG_KIA, "Today: ->" + time);

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

            String mounth;
            String dayOfMonth;
            if (String.valueOf(persianDate.getMonth()).length() == 1) {
                mounth = "0" + String.valueOf(persianDate.getMonth());
            } else {
                mounth = String.valueOf(persianDate.getMonth());
            }
            if (String.valueOf(persianDate.getDayOfMonth()).length() == 1) {
                dayOfMonth = "0" + String.valueOf(persianDate.getDayOfMonth());

            } else {
                dayOfMonth = String.valueOf(persianDate.getDayOfMonth());

            }
            binding.daysCardReservation.setText(String.valueOf(persianDate.getDayOfMonth()));
            int i = persianDate.getDayOfWeek();
            int x = persianDate.getMonth();
            binding.weekCardReservation.setText(week[i]);
            binding.monthCardReservation.setText(month[x]);

            date_show = String.valueOf(persianDate.getYear()) + "/" + mounth + "/"
                    + dayOfMonth;
            Log.d(Constants.TAG_KIA, "Calender: ->" + date_show);
            Log.d(Constants.TAG_KIA, "dateC: ->" + date_show);

            time = date(date_show);
            selectedDate = week[i]+" "+ persianDate.getDayOfMonth()+" "+ month[x];
            String m = String.valueOf(persianDate.getMonth());
            String dM = String.valueOf(persianDate.getDayOfMonth());
            if (m.length() == 1){
                m = "0"+persianDate.getMonth();
            }
            if (dM.length() == 1){
                dM = "0"+persianDate.getDayOfMonth();
            }
            String d = persianDate.getYear()+"/"+m+"/"+dM;
            selectedDateMiladi = date(d);
            callSearchServicesApi();
            adapterPm.notifyDataSetChanged();
            adapterAm.notifyDataSetChanged();

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

        int numberOfColumns = 4;
        GridLayoutManager gridLayoutManager = new RtlGridLayoutManager(getContext(), numberOfColumns);
        GridLayoutManager gridLayoutManager2 = new RtlGridLayoutManager(getContext(), numberOfColumns);
        binding.recyclerViewMorningReserve.setLayoutManager(gridLayoutManager);
        binding.recyclerViewEveningReserve.setLayoutManager(gridLayoutManager2);

        adapterAm = new ReserveAdapterAll(freeTimeAmArrayList, this::onItem);
        binding.recyclerViewMorningReserve.setAdapter(adapterAm);

        adapterPm = new ReserveAdapterAll(freeTimePmArrayList, this::onItem);
        binding.recyclerViewEveningReserve.setAdapter(adapterPm);

    }


    //Check Internet and call api
    private void callSearchServicesApi() {

            if (Connectivity.isConnected(getContext())) {

                callApi();

                Log.d("KIATAG", "callSearchServicesApi: ");

            } else {
                snackBarIconError("اینترنت وصل نیست", requireView());
                binding.emptyTextReservation.setText("اینترنت وصل نیست");
                binding.emptyTextReservation.setVisibility(View.VISIBLE);
                binding.showAllReserves.setVisibility(View.GONE);
                binding.swipeRefreshLayoutReservation.setRefreshing(refreshing);

            }
    }


    //Api
    public void callApi() {

        Log.d(Constants.TAG_KIA, "time?: ->" + time);
        Log.d(Constants.TAG_KIA, "id?: ->" + idService);
        galleryViewModel.getReservations(time, "631869b10bfaf719ef8b76cf");
        Log.d(Constants.TAG_KIA, "sendtime?: ->" + time);
        binding.swipeRefreshLayoutReservation.setEnabled(refreshing);

    }


    //Interface selected item
    @Override
    public void onItem(int position, String key) {

        if (key.equals("Am")) {

            if (lastSelectedIndexAm != -1) {
                freeTimeAmArrayList.get(lastSelectedIndexAm).aBoolean = false;
            }

            lastSelectedIndexAm = position;
            freeTimeAmArrayList.get(lastSelectedIndexAm).aBoolean = true;
            adapterAm.notifyDataSetChanged();

            if (lastSelectedIndexPm != -1) {
                freeTimePmArrayList.get(lastSelectedIndexPm).aBoolean = false;
                lastSelectedIndexPm = -1;
                adapterPm.notifyDataSetChanged();

            }

        }

        if (key.equals("Pm")) {
            if (lastSelectedIndexPm != -1) {
                freeTimePmArrayList.get(lastSelectedIndexPm).aBoolean = false;

            }
            lastSelectedIndexPm = position;
            freeTimePmArrayList.get(lastSelectedIndexPm).aBoolean = true;
            adapterPm.notifyDataSetChanged();

            if (lastSelectedIndexAm != -1) {
                freeTimeAmArrayList.get(lastSelectedIndexAm).aBoolean = false;
                lastSelectedIndexAm = -1;
                adapterAm.notifyDataSetChanged();

            }


        }

        Log.d("RHMN", "keyAll: " + key);
        Log.d("RHMN", "positionAll: " + position);
        Log.d("RHMN", "last am " + lastSelectedIndexAm);
        Log.d("RHMN", "last pm " + lastSelectedIndexPm);

    }


    //error snackbar for Verification phone
    private void snackBarIconError(String s, View v) {
        final Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_layout, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText(s);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_baseline_perm_device_information_24);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(getResources().getColor(R.color.red));
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }


    //Convert Calender Shamsi to Miladi
    public String date(String input) {
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
        Log.d(Constants.TAG_KIA, "date: ->" + iso8601);


        String output = ChronoFormatter
                .ofPattern("yyyy/MM/dd", PatternType.CLDR, Locale.ROOT, PlainDate.axis())
                .format(gregorian);

        return iso8601;
    }


}