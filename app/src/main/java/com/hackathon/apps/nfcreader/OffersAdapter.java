package com.hackathon.apps.nfcreader;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hackathon.apps.nfcreader.model.Coupons;
import com.hackathon.apps.nfcreader.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ruthvik on 21/09/2017.
 */

public class OffersAdapter extends BaseAdapter implements View.OnClickListener {
    Context context;
    ArrayList<Product> products;
    int showCount;

    public OffersAdapter(Context context, ArrayList<Product> products, int showCount) {
        this.context = context;
        this.products = products;
        this.showCount = showCount;
    }

    @Override
    public int getCount() {
        return showCount == 2 ? 2 : products.size();
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
        Button locate = (Button) convertView.findViewById(R.id.locateMe);

        locate.setOnClickListener(this);

        title.setText(getItem(position).title);
        price.setText("£" + getItem(position).price);
        offerDescription.setText(getItem(position).offerText);
        Picasso.with(context).load(getItem(position).defaultImageUrl).into(productImage);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        View parentRow = (View)v.getParent();
        ListView listView = (ListView)parentRow.getParent();
        int position = listView.getPositionForView(parentRow);
        String location = getItem(position).location;
        alertDialog.setTitle("Locate Me")
                .setMessage(location)
                .show();

    }
}
