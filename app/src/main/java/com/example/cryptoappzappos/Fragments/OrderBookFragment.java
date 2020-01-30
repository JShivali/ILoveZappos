package com.example.cryptoappzappos.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cryptoappzappos.APICall;
import com.example.cryptoappzappos.Adapter.OrderAdapter;
import com.example.cryptoappzappos.App;
import com.example.cryptoappzappos.Model.Order;
import com.example.cryptoappzappos.Model.OrderBook;
import com.example.cryptoappzappos.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderBookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView sum, value, amount, bid;
    private RecyclerView bidRecyclerView;
    private RecyclerView.Adapter bidAdapter;
    private RecyclerView.LayoutManager bidLayoutMAnager;


    private RecyclerView askRecyclerView;
    private RecyclerView.Adapter askAdapter;
    private RecyclerView.LayoutManager askLayoutManager;

    private ArrayList<Order> orderAsksList;
    private ArrayList<Order> orderBidsList;

    public OrderBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
        } else {
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderBookFragment newInstance(String param1, String param2) {
        OrderBookFragment fragment = new OrderBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_book, container, false);

        bidRecyclerView = v.findViewById(R.id.bidsRV);
        orderAsksList = new ArrayList<>();
        orderBidsList = new ArrayList<>();

        askRecyclerView = v.findViewById(R.id.asksRV);


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // API Call to get data for order book
        Retrofit retrofit = new Retrofit.Builder().baseUrl(App.getContext().getResources().getString(R.string.data_url))
                .addConverterFactory(GsonConverterFactory.create()).build();


        APICall apicall = retrofit.create(APICall.class);
        Call<OrderBook> call = apicall.getOrderBook();


        call.enqueue(new Callback<OrderBook>() {
            @Override
            public void onResponse(Call<OrderBook> call, Response<OrderBook> response) {
                if (!response.isSuccessful()) {
                    //textView.setText("Code: "+response.code());
                    return;
                }

                OrderBook orderbook = response.body();
                List<List<Double>> asks = orderbook.getAsks();
                List<List<Double>> bids = orderbook.getBids();
                double sum = 0;

                orderBidsList.clear();
                orderAsksList.clear();

                for (List<Double> a : asks) {
                    double val = a.get(0) * a.get(1);
                    if (a == asks.get(0)) {
                        sum = a.get(1);
                        orderAsksList.add(new Order(sum, val, a.get(1), a.get(0), false));      //sum, val, amount, bid, type
                    } else {
                        sum += a.get(1);
                        orderAsksList.add(new Order(sum, val, a.get(1), a.get(0), false));
                    }
                }

                sum = 0;
                for (List<Double> a : bids) {
                    double val = a.get(0) * a.get(1);
                    if (a == asks.get(0)) {
                        sum = a.get(1);
                        orderBidsList.add(new Order(sum, val, a.get(1), a.get(0), true));
                    } else {
                        sum += a.get(1);
                        orderBidsList.add(new Order(sum, val, a.get(1), a.get(0), true));
                    }
                }

                // Set up the bids recyclerview
                bidLayoutMAnager = new LinearLayoutManager(getActivity());
                bidAdapter = new OrderAdapter(orderBidsList);
                bidRecyclerView.setLayoutManager(bidLayoutMAnager);
                bidRecyclerView.setAdapter(bidAdapter);

                // Set up the asks recyclerview
                askLayoutManager = new LinearLayoutManager(getActivity());
                askAdapter = new OrderAdapter(orderAsksList);
                askRecyclerView.setLayoutManager(askLayoutManager);
                askRecyclerView.setAdapter(askAdapter);

            }

            @Override
            public void onFailure(Call<OrderBook> call, Throwable t) {
                System.out.println("API Error" + t.getMessage());
            }
        });

    }
}
