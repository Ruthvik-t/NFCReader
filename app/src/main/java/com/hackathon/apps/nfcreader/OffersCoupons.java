package com.hackathon.apps.nfcreader;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.hackathon.apps.nfcreader.fragments.CouponsFragment;
import com.hackathon.apps.nfcreader.fragments.OffersFragment;

public class OffersCoupons extends AppCompatActivity {

    private static final String OFFERS_TAG = "OffersFragment";
    private static final String COUPONS_TAG = "CouponsFragment";
    RelativeLayout offersContainer, couponsContainer;
    OffersFragment offersFragment;
    CouponsFragment couponsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_coupons);

        offersContainer = (RelativeLayout) findViewById(R.id.offers_container);
        couponsContainer = (RelativeLayout) findViewById(R.id.coupons_container);

        RenderFragments(savedInstanceState);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.offers_container, offersFragment, OFFERS_TAG);
        transaction.replace(R.id.coupons_container, couponsFragment, COUPONS_TAG);
        transaction.commit();
    }

    private void RenderFragments(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            offersFragment = OffersFragment.NewInstance(2, true);
            couponsFragment = CouponsFragment.NewInstance(GlobalData.coupons.size(), false);
        }
        else {
            offersFragment = (OffersFragment) getSupportFragmentManager().findFragmentByTag(OFFERS_TAG);
            couponsFragment = (CouponsFragment) getSupportFragmentManager().findFragmentByTag(COUPONS_TAG);
        }
    }
}
