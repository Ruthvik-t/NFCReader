package com.hackathon.apps.nfcreader;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hackathon.apps.nfcreader.fragments.OffersFragment;

public class OffersActivty extends AppCompatActivity {
    OffersFragment offersFragment;
    private static final String OFFERS_TAG = "OffersFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_activty);

        RenderFragments(savedInstanceState);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.offers_list_container, offersFragment, OFFERS_TAG);
        transaction.commit();
    }

    private void RenderFragments(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            offersFragment = OffersFragment.NewInstance(GlobalData.promotions.size(), true);
        }
        else {
            offersFragment = (OffersFragment) getSupportFragmentManager().findFragmentByTag(OFFERS_TAG);
        }
    }
}
