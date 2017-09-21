package com.hackathon.apps.nfcreader;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.hackathon.apps.nfcreader.model.Coupons;
import com.hackathon.apps.nfcreader.model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arunkumar on 21/09/2017.
 */

public class GetCouponsTask extends AsyncTask<Void, Void, ArrayList<Coupons>> {
    private Context context;
    ResponseHandler listener;
    private static final String LOG_TAG = "Network Request";

    @Override
    protected ArrayList<Coupons> doInBackground(Void... params) {
        try {
            JSONObject jsonResponse = new JSONObject(loadJSONFromAsset("coupons.json"));
            return extractFeatureFromJson(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setListener(ResponseHandler listener) {
        this.listener = listener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {

            InputStream is =  context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    @Override
    protected void onPostExecute(ArrayList<Coupons> s) {
        super.onPostExecute(s);
        if(listener != null)
            listener.OnSuccessfullCouponResponse(s);
    }

    /**
     * Return an {@link Product} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private ArrayList<Coupons> extractFeatureFromJson(JSONObject coupons) {
        ArrayList<Coupons> result = new ArrayList<Coupons>();
        try {
            JSONArray couponResponse = coupons.getJSONArray("coupons");
            if (couponResponse.length() > 0) {
                for(int i =0; i< couponResponse.length(); i++){
                    System.out.println(i);
                    JSONObject jsonItem = couponResponse.getJSONObject(i);
                    String code = jsonItem.getString("code");
                    String title = jsonItem.getString("title");
                    String qrCode = jsonItem.getString("qrCode");

                    String description = jsonItem.getString("description");
                    String thumbnail = jsonItem.getString("thumbnail");
                    Coupons coupon = new Coupons(code, title, qrCode, description, thumbnail);
                    result.add(coupon);
                }
                return result;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }
}
