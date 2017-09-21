package com.hackathon.apps.nfcreader.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackathon.apps.nfcreader.OffersAdapter;
import com.hackathon.apps.nfcreader.R;
import com.hackathon.apps.nfcreader.model.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.hackathon.apps.nfcreader.R.layout.offers_fragment;

/**
 * Created by ruthvik on 21/09/2017.
 */

public class OffersFragment extends Fragment implements View.OnClickListener {

    TextView title;
    ListView offersList;
    Button showMore;
    int showCount;
    OffersAdapter adapter;

    private static final String LIST_COUNT = "ListCount";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(LIST_COUNT))
                showCount = savedInstanceState.getInt(LIST_COUNT);
        }
        else if(getArguments() != null){
            showCount = getArguments().getInt(LIST_COUNT, 2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.offers_fragment, container, false);
        title = (TextView) view.findViewById(R.id.fragment_title);
        offersList = (ListView) view.findViewById(R.id.mylist);
        showMore = (Button) view.findViewById(R.id.load_more);

        if(showCount > 2)
            showMore.setVisibility(GONE);

        ArrayList<Product> products = new ArrayList<Product>();
        products.add(new Product("Apples","0.45","kg","asdasfdfdf"));
        products.add(new Product("Mangoes","0.45","kg","asdasfdfdf"));

        adapter = new OffersAdapter(getContext(), products);
        offersList.setAdapter(adapter);

        showMore.setOnClickListener(this);
        return view;
    }

    public static OffersFragment NewInstance(int showCount){
        OffersFragment offersFragment = new OffersFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LIST_COUNT, showCount);
        offersFragment.setArguments(bundle);
        return  offersFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.load_more:
                Toast.makeText(getContext(), "load more clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
