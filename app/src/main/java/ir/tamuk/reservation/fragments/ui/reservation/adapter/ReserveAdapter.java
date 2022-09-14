package ir.tamuk.reservation.fragments.ui.reservation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.fragments.ui.reservation.ReservationFragment;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.MyView>{

    private static final String TAG = "kia";
    private ArrayList<ReserveModel> rs;
    private Context context;
    private int clickPosition;
    private boolean isClicked;
    private ReservationFragment fragment;
//    private SqlDatabaseReserve sqlDatabaseReserve;

//    boolean flag = true;


    public ReserveAdapter(ArrayList<ReserveModel> rs){
        this.rs=rs;
    }

    @NonNull
    @Override
    public ReserveAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserved_row, parent, false);
        return new ReserveAdapter.MyView(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveAdapter.MyView holder, @SuppressLint("RecyclerView") int position) {
//
        }

    @Override
    public int getItemCount() {

        return rs.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        public TextView id;
        public TextView time;
        public TextView reserved;
        public RelativeLayout layout;
//        public ImageView checkBox2;

        public MyView(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time_reserve);
            layout = itemView.findViewById(R.id.relative_layout_click);
//            checkBox2 = itemView.findViewById(R.id.checkboxRes2);


        }
    }

}

