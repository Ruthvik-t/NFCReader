package com.hackathon.apps.nfcreader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hackathon.apps.nfcreader.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by amarmurari on 21/09/17.
 */

public class GetAisleRecommendationsTask extends AsyncTask<String, Void, ArrayList<Product>>{

    private static final String PRODUCT_URL="https://ci-mango.ngbeta.net/";
    private static final String LOG_TAG = "Network Request";
    private Context context;

    @Override
    protected ArrayList<Product> doInBackground(String... params) {
        URL url = createUrl(PRODUCT_URL);
        String department = params[0];
        ArrayList<Product> products = null;

        try {
            products = makeHttpRequest(url, department);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private ArrayList<Product> makeHttpRequest(URL url, String department) throws IOException {
        ArrayList<Product> products = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection = setHeaderContents(urlConnection);
            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(),"UTF-8");
            Log.e("testing",">>>>> " + loadJSONFromAsset("GetAisleProducts.json"));
            JSONObject jsonObject = addParameters(department);
            writer.write(jsonObject.toString());
            writer.close();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            if(urlConnection.getResponseCode() == 200) {
                products = parseResponse(readFromStream(inputStream));
            }
        } catch (Exception e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return products;
    }

    private ArrayList<Product> parseResponse(String jsonResponse) {
        ArrayList<Product> products = new ArrayList<Product>();

        if(jsonResponse != null) {
            try {
                String offerText = null;
                String price = null;
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject category = data.getJSONObject("category");
                JSONArray productItems = category.getJSONArray("productItems");
                Log.e("productItems", productItems.toString());
                for(int i = 0; i < productItems.length(); i++)
                {
                    JSONObject product = productItems.getJSONObject(i);
                    if (product.has("price")) {
                        JSONObject priceData = product.getJSONObject("price");
                        price = priceData.getString("price");
                    }
                    if (product.has("promotions")) {
                        JSONArray promotions = product.getJSONArray("promotions");
                        if (promotions.length() > 0) {
                            JSONObject promotion = promotions.getJSONObject(0);
                            offerText = promotion.getString("offerText");
                        }
                    }

                    products.add(new Product(
                            product.getString("title"), price, product.getString("defaultImageUrl"), offerText
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return products;
    }

    private JSONObject addParameters(String department) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(loadJSONFromAsset("GetAisleProducts.json"));

            JSONObject variables = jsonObject.getJSONObject("variables");
            variables.put("department", department);
            jsonObject.put("variables",variables);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private HttpURLConnection setHeaderContents(HttpURLConnection urlConnection) {
        urlConnection.setRequestProperty("Accept","*/*");
        urlConnection.setRequestProperty("Content-Type","application/json");
        urlConnection.setRequestProperty("ighs-language","en-US");
        urlConnection.setRequestProperty("region","UK");
        urlConnection.setRequestProperty("X-Status", "Auth");
        return urlConnection;
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

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
