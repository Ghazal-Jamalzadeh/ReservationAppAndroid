package ir.tamuk.reservation.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.SnackbarLayoutBinding;

public class SnackBars {

    public void show(Context context , View v, String string, int drawable, int color){
        final Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT);
        //inflate view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View custom_view = inflater.inflate(R.layout.snackbar_layout, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText(string);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(drawable);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(color);
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

    public void showErrorInternet(View v, Context context){
        final Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT);
        //inflate view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View custom_view = inflater.inflate(R.layout.snackbar_layout, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText(R.string.internet_not_connected_error);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_baseline_perm_scan_wifi_24);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundResource(R.color.colorSecondary);
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

    public void showErrorSelectReserve(View v, Context context){
        View parent_view = v.getRootView().findViewById(android.R.id.content);
//        final Snackbar snackbar = Snackbar.make(parent_view, "", Snackbar.LENGTH_SHORT);
//        //inflate view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//        View custom_view = inflater.inflate(R.layout.snackbar_layout, null);
//
//        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
//        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
//        snackBarView.setPadding(0, 0, 0, 0);
//
//        ((TextView) custom_view.findViewById(R.id.message)).setText(R.string.not_selection_reserve);
//        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_baseline_perm_device_information_24);
//        (custom_view.findViewById(R.id.parent_view)).setBackgroundResource(R.color.red);
//
//        snackBarView.addView(custom_view, 0);
//        snackbar.show();

        //inflate view
        View custom_view = inflater.inflate(R.layout.snackbar_icon_text, null);
        final Snackbar snackbar = Snackbar.make(parent_view, "", Snackbar.LENGTH_SHORT);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);
        

        ((TextView) custom_view.findViewById(R.id.message)).setText("این یک پیغام خطاست");
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundResource(R.color.colorSecondary);
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

}
