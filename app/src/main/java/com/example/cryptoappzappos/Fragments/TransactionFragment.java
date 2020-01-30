package com.example.cryptoappzappos.Fragments;

import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cryptoappzappos.APICall;
import com.example.cryptoappzappos.App;
import com.example.cryptoappzappos.Model.PriceCheck;
import com.example.cryptoappzappos.Model.Transaction;
import com.example.cryptoappzappos.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment implements OnChartValueSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView textView, currentVal;
    LineChart lineChart;

    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
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
        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        lineChart = v.findViewById(R.id.lineChart);
        currentVal = v.findViewById(R.id.currentVal);

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

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        System.out.println(("Entry selected" + e.toString()));
        System.out.println(("LOWHIGH low: " + lineChart.getLowestVisibleX()
                + ", high: " + lineChart.getHighestVisibleX()));

        System.out.println(("MIN MAX )xmin: " + lineChart.getXChartMin()
                + ", xmax: " + lineChart.getXChartMax()
                + ", ymin: " + lineChart.getYChartMin()
                + ", ymax: " + lineChart.getYChartMax()));
    }

    @Override
    public void onNothingSelected() {

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
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();


        //API call to get transaction data


        Retrofit retrofit = new Retrofit.Builder().baseUrl(App.getContext().getResources().getString(R.string.data_url))
                .addConverterFactory(GsonConverterFactory.create(gson)).build();


        APICall apicall = retrofit.create(APICall.class);
        Call<List<Transaction>> call = apicall.getTransactions();
        Call<PriceCheck> currValCall = apicall.getPriceAlert();

        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (!response.isSuccessful()) {
                    textView.setText("Code: " + response.code());
                    return;
                }

                List<Transaction> transactions = response.body();
                ArrayList<Entry> entries = new ArrayList<>();


                for (Transaction t : transactions) {

                    //Convert date string to date format
                    float x = Float.parseFloat(t.getDate());
                    float y = t.getPrice();
                    entries.add(new Entry(x, y));

                }


                LineDataSet dataSet = new LineDataSet(entries, "Time series");
                dataSet.setDrawValues(false);
                dataSet.setColor(R.color.colorPrimary);
                dataSet.setDrawCircles(false);
                dataSet.setLineWidth(2f);
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setDrawFilled(true);

                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_shade);
                    dataSet.setFillDrawable(drawable);
                } else {
                    dataSet.setFillColor(Color.BLACK);
                }


                LineData data = new LineData(dataSet);

                lineChart.getAxisLeft().setZeroLineWidth(0);
                lineChart.getAxisLeft().setDrawAxisLine(false);
                lineChart.getAxisLeft().setDrawLimitLinesBehindData(false);
                lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);
                lineChart.getAxisLeft().setDrawGridLines(false);

                lineChart.getXAxis().mAxisRange = 100;
                lineChart.getXAxis().setDrawGridLines(false);
                lineChart.getLegend().setEnabled(false);
                lineChart.getDescription().setEnabled(false);
                lineChart.setVisibleXRangeMaximum(75);
                lineChart.getXAxis().setGranularity(2);


                // enable scaling and dragging
                lineChart.setDragEnabled(true);
                lineChart.setScaleEnabled(true);
                lineChart.setTouchEnabled(true);

                lineChart.animateX(800, Easing.EaseInOutBack);
                lineChart.setData(data);
                lineChart.invalidate();

            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                System.out.println(t.getCause().toString());
            }
        });


        currValCall.enqueue(new Callback<PriceCheck>() {
            @Override
            public void onResponse(Call<PriceCheck> call, Response<PriceCheck> response) {
                if (!response.isSuccessful()) {

                    return;
                }


                PriceCheck priceCheck = response.body();
                currentVal.setText(String.valueOf(priceCheck.getLast()));

            }

            @Override
            public void onFailure(Call<PriceCheck> call, Throwable t) {

            }
        });
    }

}
