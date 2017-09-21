package com.hackathon.apps.nfcreader;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CouponsAdapter extends BaseAdapter implements View.OnClickListener {
    Context context;
    ArrayList<Coupons> coupons;
    int showCount;

    public CouponsAdapter(Context context, ArrayList<Coupons> coupons, int showCount) {
        this.context = context;
        this.coupons = coupons;
        this.showCount = showCount;
    }

    @Override
    public int getCount() {
        return showCount == 2 ? 2 : coupons.size();
    }

    @Override
    public Coupons getItem(int position) {
        return coupons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.coupon_list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.product_title);
        TextView offerDescription = (TextView) convertView.findViewById(R.id.offer_description);
        TextView productImage = (TextView) convertView.findViewById(R.id.coupon_image);
        TextView offerCode = (TextView) convertView.findViewById(R.id.offer_code);
        Button getQrcode = (Button) convertView.findViewById(R.id.locateMe);

        getQrcode.setOnClickListener(this);

        Coupons coupon = getItem(position);
        title.setText(coupon.title);
        offerDescription.setText(coupon.description);
        offerCode.setText(coupon.code);
        productImage.setText(coupon.thumbnail);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        View parentRow = (View)v.getParent();
        ListView listView = (ListView)parentRow.getParent();
        int position = listView.getPositionForView(parentRow);
        Coupons coupon = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.qrcode_layout, null);
        Picasso.with(context).load(coupon.qrCodeUrl).into((ImageView) dialogLayout.findViewById(R.id.qr_code));
        alertDialog.setTitle("Scan the qrcode at the Till to avail offer");
        alertDialog.setView(dialogLayout);
        alertDialog.show();
    }
}
