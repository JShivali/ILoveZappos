package com.example.cryptoappzappos.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cryptoappzappos.Model.Order;
import com.example.cryptoappzappos.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


/**
 * This is the adapter class to bind order book data to the recyclerviews.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private ArrayList<Order> orderList;

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView amount, value, sum, bid;

        public OrderViewHolder(View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            value = itemView.findViewById(R.id.value);
            sum = itemView.findViewById(R.id.sum);
            bid = itemView.findViewById(R.id.bid);

        }
    }

    public OrderAdapter(ArrayList<Order> exampleList) {
        orderList = exampleList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_book_row, parent, false);
        OrderViewHolder evh = new OrderViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order currentItem = orderList.get(position);

        boolean type = currentItem.isType();

        holder.amount.setText(String.valueOf(BigDecimal.valueOf(currentItem.getAmount()).setScale(4, RoundingMode.CEILING)));
        holder.value.setText(String.valueOf(BigDecimal.valueOf(currentItem.getValue()).setScale(2, RoundingMode.CEILING)));
        holder.sum.setText(String.valueOf(BigDecimal.valueOf(currentItem.getSum()).setScale(2, RoundingMode.CEILING)));
        holder.bid.setText(String.valueOf(BigDecimal.valueOf(currentItem.getBid()).setScale(2, RoundingMode.CEILING)));

        if (type) {
            holder.bid.setTextColor(Color.GREEN);
        } else {
            holder.bid.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}