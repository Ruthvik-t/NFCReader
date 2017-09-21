package com.hackathon.apps.nfcreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class AisleActivity extends AppCompatActivity {

    TextView aisleTitle;
    ListView aisleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aisle);

        aisleTitle = (TextView) findViewById(R.id.aisle_title);
        aisleList = (ListView) findViewById(R.id.aislelist);

        OffersAdapter adapter = new OffersAdapter(this, GlobalData.aisles, GlobalData.aisles.size());
        aisleList.setAdapter(adapter);
    }
}
