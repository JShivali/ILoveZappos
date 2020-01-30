package com.example.cryptoappzappos;

import com.example.cryptoappzappos.Model.OrderBook;
import com.example.cryptoappzappos.Model.PriceCheck;
import com.example.cryptoappzappos.Model.Transaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * This interface has methods to retrieve data from API and get pojo objects (using retrofit)
 */


public interface APICall {

   @GET("transactions/btcusd")
    Call<List<Transaction>> getTransactions();

    @GET("order_book/btcusd")
    Call<OrderBook> getOrderBook();

    @GET("ticker_hour/btcusd")
    Call<PriceCheck> getPriceAlert();

}
