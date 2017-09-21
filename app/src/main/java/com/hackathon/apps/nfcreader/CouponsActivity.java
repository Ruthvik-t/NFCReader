package com.hackathon.apps.nfcreader;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hackathon.apps.nfcreader.fragments.CouponsFragment;
import com.hackathon.apps.nfcreader.fragments.OffersFragment;

public class CouponsActivity extends AppCompatActivity {
    CouponsFragment couponsFragment;
    private static final String COUPONS_TAG = "CouponsFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        RenderFragments(savedInstanceState);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.coupons_list_container, couponsFragment, COUPONS_TAG);
        transaction.commit();
    }

    private void RenderFragments(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            couponsFragment = CouponsFragment.NewInstance(GlobalData.coupons.size(), true);
        }
        else {
            couponsFragment = (CouponsFragment) getSupportFragmentManager().findFragmentByTag(COUPONS_TAG);
        }
    }
}
