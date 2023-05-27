package com.meass.cash_capital;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PackageInAdapter extends RecyclerView.Adapter<PackageInAdapter.myview> {
    public List<Package> data;
    FirebaseFirestore firebaseFirestore;

    public PackageInAdapter(List<Package> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.papa, parent, false);
        return new myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, final int position) {
        holder.customer_name.setText("User ID : " + data.get(position).getUsername());
        holder.customer_number.setText("Package  : " + data.get(position).getPackage_name()+"\nDeposit Amount : "+data.get(position).getPackage_price()+" BDT\n" +
                "Date : "+data.get(position).getDate());
        holder.customer_area.setText(data.get(position).getDate());
        holder.logout.setText(data.get(position).getStatus());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myview extends RecyclerView.ViewHolder {
        TextView customer_name, customer_number, customer_area, logout;

        public myview(@NonNull View itemView) {
            super(itemView);
            customer_name = itemView.findViewById(R.id.customer_name);
            customer_number = itemView.findViewById(R.id.customer_number);
            customer_area = itemView.findViewById(R.id.customer_area);
            logout = itemView.findViewById(R.id.logout);
        }
    }
}