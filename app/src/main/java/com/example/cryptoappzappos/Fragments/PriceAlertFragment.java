package com.example.cryptoappzappos.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cryptoappzappos.JobWorker;
import com.example.cryptoappzappos.R;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import static com.example.cryptoappzappos.App.CHANNEL_ID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PriceAlertFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriceAlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceAlertFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FirebaseJobDispatcher firebaseJobDispatcher;
    private Button setLimit,cancel;
    private EditText thresholdValue;

    private DatabaseReference databaseReference;


    public PriceAlertFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static PriceAlertFragment newInstance(String param1, String param2) {
        PriceAlertFragment fragment = new PriceAlertFragment();
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
        firebaseJobDispatcher= new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));
        View v= inflater.inflate(R.layout.fragment_price_alert, container, false);
        setLimit=v.findViewById(R.id.setThreshold);
        cancel=v.findViewById(R.id.removeThreshold);
        thresholdValue=v.findViewById(R.id.threshold);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Threshold");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot ds: dataSnapshot.getChildren()){
                   if(!ds.getValue().toString().equals("-1"))
                   thresholdValue.setText(ds.getValue().toString());
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        setLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startJob(v);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopJob(v);
            }
        });
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

    public void startJob(View view){
        String threshold=thresholdValue.getText().toString().trim();

        //
        WorkManager.getInstance().cancelAllWorkByTag(getString(R.string.unique_work_name));

        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(JobWorker.class, 1, TimeUnit.HOURS).addTag(getString(R.string.unique_work_name))
                .build();
        //WorkManager.getInstance().enqueueUniquePeriodicWork(getString(R.string.unique_work_name), ExistingPeriodicWorkPolicy.REPLACE, periodicWork);
        if(threshold!=null && threshold.matches(".*[0-9].*")){
            Map<String,Object> limit = new HashMap<>();
            limit.put("threshold",Double.parseDouble(threshold));
            databaseReference.updateChildren(limit);
            Toast.makeText(getActivity(), R.string.threshold_set_success,
                    Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), R.string.valid_threshold,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void stopJob(View view){

        if(thresholdValue.getText().equals(R.string.enter_threshold)){
            Toast.makeText(getActivity(), R.string.no_alerts,
                    Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), R.string.stop_alerts,
                    Toast.LENGTH_LONG).show();
            Map<String,Object> limit = new HashMap<>();
            limit.put("threshold","-1");
            databaseReference.updateChildren(limit);
            thresholdValue.setText("");
            thresholdValue.setHint(getString(R.string.enter_threshold));
        }

        WorkManager.getInstance().cancelAllWorkByTag(getString(R.string.unique_work_name));


    }



}
