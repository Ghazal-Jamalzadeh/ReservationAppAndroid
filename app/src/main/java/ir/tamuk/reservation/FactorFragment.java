package ir.tamuk.reservation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.tamuk.reservation.databinding.FragmentFactorBinding;
import ir.tamuk.reservation.databinding.FragmentSigningBinding;
import ir.tamuk.reservation.fragments.ui.reservation.adapter.ReserveModel;
import ir.tamuk.reservation.fragments.ui.reservation.database.SqlDatabaseReserve;


public class FactorFragment extends Fragment {

    private FragmentFactorBinding binding;

    private SqlDatabaseReserve sqlDatabaseReserve;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFactorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences prefs = getActivity().getSharedPreferences("NumberPhone", Context.MODE_PRIVATE);
        String numb = prefs.getString("number", null);//"No name defined" is the default value.
        binding.phoneTextForm.setText(numb);

        //sql Database
        sqlDatabaseReserve = new SqlDatabaseReserve(getContext());
        //arrays
        ArrayList<ReserveModel> allModels = sqlDatabaseReserve.getData();
        ArrayList<ReserveModel> models = sqlDatabaseReserve.getDataId();
        ArrayList<ReserveModel> result = new ArrayList<>();
        result.clear();
        for (int i = 0; i<allModels.size(); i++){
            if (allModels.get(i).id.contains(models.get(0).id)){
                result.add(allModels.get(i));
            }

        }
        allModels.clear();
        allModels.addAll(result);

        String services = allModels.get(0).service;
        String time = allModels.get(0).id.substring(10,14);

        binding.serviceTextForm.setText(services);
        binding.timeTextForm.setText(time);

        return root;
    }
}