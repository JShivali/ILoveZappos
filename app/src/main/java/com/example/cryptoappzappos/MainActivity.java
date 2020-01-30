package com.example.cryptoappzappos;


import android.net.Uri;
import android.os.Bundle;


import com.example.cryptoappzappos.Fragments.OrderBookFragment;
import com.example.cryptoappzappos.Fragments.PriceAlertFragment;
import com.example.cryptoappzappos.Fragments.TransactionFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptoappzappos.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements TransactionFragment.OnFragmentInteractionListener, OrderBookFragment.OnFragmentInteractionListener, PriceAlertFragment.OnFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}