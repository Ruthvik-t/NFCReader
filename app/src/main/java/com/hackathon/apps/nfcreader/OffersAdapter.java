package com.hackathon.apps.nfcreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathon.apps.nfcreader.model.Product;

import java.util.ArrayList;

/**
 * Created by ruthvik on 21/09/2017.
 */

public class OffersAdapter extends BaseAdapter {
    Context context;
    ArrayList<Product> products;

    public OffersAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.product_title);
        TextView offerDescription = (TextView) convertView.findViewById(R.id.offer_description);
        ImageView productImage = (ImageView) convertView.findViewById(R.id.item_image);
        TextView price = (TextView) convertView.findViewById(R.id.product_price);

        title.setText(getItem(position).title);
        price.setText("£" + getItem(position).price);

        return convertView;
    }
}
