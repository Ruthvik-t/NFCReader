package com.hackathon.apps.nfcreader;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.hackathon.apps.nfcreader.fragments.OffersFragment;

public class OffersCoupons extends AppCompatActivity {

    private static final String OFFERS_TAG = "OffersFragment";
    RelativeLayout offersContainer, couponsContainer;
    OffersFragment offersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_coupons);

        offersContainer = (RelativeLayout) findViewById(R.id.offers_container);
        couponsContainer = (RelativeLayout) findViewById(R.id.coupons_container);

        RenderFragments(savedInstanceState);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.offers_container, offersFragment, OFFERS_TAG);
        transaction.commit();
    }

    private void RenderFragments(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            offersFragment = OffersFragment.NewInstance(2);
        }
        else {
            offersFragment = (OffersFragment) getSupportFragmentManager().findFragmentByTag(OFFERS_TAG);
        }
    }
}
