package ir.tamuk.reservation.fragments.ui.reservation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.activities.MainActivity;
import ir.tamuk.reservation.fragments.ui.reservation.ReservationFragment;
import ir.tamuk.reservation.fragments.ui.reservation.database.SqlDatabaseReserve;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.MyView>{

    private static final String TAG = "kia";
    private ArrayList<ReserveModel> rs;
    private Context context;
    private int clickPosition;
    private boolean isClicked;
    private ReservationFragment fragment;
    private SqlDatabaseReserve sqlDatabaseReserve;

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
        sqlDatabaseReserve = new SqlDatabaseReserve(context);
//        if (position % 2 == 0){
//            holder.layout.setBackgroundResource(R.drawable.spinner2);
//        }else {
//            holder.layout.setBackgroundResource(R.drawable.spinner_radious);
//        }
        holder.time.setText(rs.get(position).time);
        if (rs.get(position).reserved == 0){
//            holder.checkBox.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
            if (sqlDatabaseReserve.getByIdO(rs.get(position).id)){
                holder.layout.setBackgroundResource(R.drawable.row_maincolor_reserve);
                holder.time.setTextColor(Color.WHITE);

            }

            if (isClicked) {

                if (clickPosition == position) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.layout.setBackground(context.getDrawable(R.drawable.row_maincolor_reserve));
                        holder.time.setTextColor(Color.WHITE);
                        sqlDatabaseReserve.Insert(rs.get(position).id);
                    }

                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.layout.setBackground(context.getDrawable(R.drawable.row_transparent_reserve));
                        holder.time.setTextColor(Color.BLACK);
                        sqlDatabaseReserve.deleteId(sqlDatabaseReserve.getById(rs.get(position).id));
                    }

                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isClicked = true;
                    clickPosition = position;
                    notifyDataSetChanged();
//                    activity.selectFinalPlan(plans.get(position));

                }
            });

//            holder.layout.setOnClickListener(view -> {
//                Log.d(TAG, "item clicked : " + position);
//                for(int i = 0 ; i<rs.size(); i++){
//                    Log.d(TAG, "i = " + i);
//                    if (position == i){
//                        Log.d(TAG, "position == i: ");
//                        sqlDatabaseReserve.Insert(rs.get(position).id);
//                        holder.layout.setBackgroundResource(R.drawable.row_maincolor_reserve);
//                        holder.time.setTextColor(Color.WHITE);
//                    }if (position != i){
//                        Log.d(TAG, "----: ");
//                        sqlDatabaseReserve.deleteId(rs.get(i).id);
//                        holder.layout.setBackgroundResource(R.drawable.row_transparent_reserve);
//                        holder.time.setTextColor(Color.BLACK);
//                    }
//                }
//            });
//            holder.layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    if (flag){
//                        if (sqlDatabaseReserve.getDataId().isEmpty()){
//                            sqlDatabaseReserve.Insert(rs.get(position).id);
//                            holder.layout.setBackgroundResource(R.drawable.row_maincolor_reserve);
//                            holder.time.setTextColor(Color.WHITE);
//
//
//                        }else {
//                            sqlDatabaseReserve.deleteId(rs.get(position).id);
//                            holder.layout.setBackgroundResource(R.drawable.row_transparent_reserve);
//                            holder.time.setTextColor(Color.BLACK);
//
//
//                            if (!sqlDatabaseReserve.getDataId().isEmpty()) {
//                                Toast.makeText(context, "قبلا انتخاب کردبد!!!", Toast.LENGTH_SHORT);
//                            }
//                        }
////                        flag=false;
////                    }else {
//
////                        if (position % 2 == 0){
////                            holder.layout.setBackgroundResource(R.drawable.spinner2);
////                        }else {
////                            holder.layout.setBackgroundResource(R.drawable.spinner_radious);
////                        }
////                        flag=true;
//
////                    }
//                }
//            });
//            if (sqlDatabaseReserve.getByIdO(rs.get(position).id)){
//                holder.checkBox.setVisibility(View.INVISIBLE);
//                holder.checkBox2.setVisibility(View.VISIBLE);
//            }else {
//                holder.checkBox2.setVisibility(View.INVISIBLE);
//                holder.checkBox.setVisibility(View.VISIBLE);
//            }
//
//            holder.checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (sqlDatabaseReserve.getDataId().isEmpty()){
//                        sqlDatabaseReserve.Insert(rs.get(position).id);
//                        holder.checkBox.setVisibility(View.INVISIBLE);
//                        holder.checkBox2.setVisibility(View.VISIBLE);
//                    }else{
//                        Toast.makeText(context, "شما قبلا انتخاب کرده اید", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//            holder.checkBox2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    sqlDatabaseReserve.deleteId(rs.get(position).id);
//                    holder.checkBox.setVisibility(View.VISIBLE);
//                    holder.checkBox2.setVisibility(View.INVISIBLE);
//                }
//            });

        }else {
            holder.layout.setBackgroundResource(R.drawable.row_reserved);
            holder.time.setTextColor(Color.WHITE);

        }
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

