package com.meass.cash_capital;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FundAdapter extends RecyclerView.Adapter<FundAdapter.myview> {
    public List<Income22> data;
    FirebaseFirestore firebaseFirestore;
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    public FundAdapter(List<Income22> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income, parent, false);
        return new myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, final int position) {
        double dd=Double.parseDouble(data.get(position).getFee())+Double.parseDouble(data.get(position).getPayment());
        holder.customer_name.setText("Balance Received");
        holder.customer_number.setText("trxid ID : " + data.get(position).getTrixid());
        holder.customer_area1.setText("Total :  "+dd+"\nFee : " + data.get(position).getFee()+"\nAmount Received:  "+data.get(position).getPayment());
        holder.logout.setText(data.get(position).getDate());
        holder.logout1.setText("Balance receive from : "+data.get(position).getUsername());
        holder.logout2.setText(" " + data.get(position).getPayment());
        topAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.splash_end_animation);
        holder.card_view8.setAnimation(endAnimation);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myview extends RecyclerView.ViewHolder {
        TextView logout2, logout1, customer_name, customer_number, customer_area, logout, customer_area1, customer_area111, customer_area11;
        CardView card_view8;

        public myview(@NonNull View itemView) {
            super(itemView);
            customer_name = itemView.findViewById(R.id.customer_name);
            customer_number = itemView.findViewById(R.id.customer_number);
            logout = itemView.findViewById(R.id.logout);

            customer_area11 = itemView.findViewById(R.id.customer_area11);
            customer_area111 = itemView.findViewById(R.id.customer_area111);
            logout2 = itemView.findViewById(R.id.logout2);
            logout1 = itemView.findViewById(R.id.logout1);
            card_view8=itemView.findViewById(R.id.card_view8);
            customer_area1 = itemView.findViewById(R.id.customer_area1);

        }
    }
}
