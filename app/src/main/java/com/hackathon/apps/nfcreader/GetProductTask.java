package com.hackathon.apps.nfcreader;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.hackathon.apps.nfcreader.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by ruthvik on 21/09/2017.
 */

public class GetProductTask extends AsyncTask<Void, Void, Product> {
    private static final String PRODUCT_URL="https://ci-mango.ngbeta.net/";
    private static final String LOG_TAG = "Network Request";
    private static final String query="{\"query\": \"query GetProduct($storeid: ID $tpnc: String){product(tpnc: $tpnc storeid: $storeid){title price {unitPrice unitOfMeasure}}}\",\"variables\":{\"storeid\":6440,\"tpnc\":272043262},\"operationName\": \"GetProduct\"}";
    private Context context;
    @Override
    protected Product doInBackground(Void... params) {
        URL url = createUrl(PRODUCT_URL);

        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
            return extractFeatureFromJson(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Product product = extractFeatureFromJson(jsonResponse);
        return null;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Returns new URL object from the given string URL.
     */
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

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection = setHeaderContents(urlConnection);
            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(),"UTF-8");
            Log.e("testing",">>>>> " + loadJSONFromAsset("GetProduct.json"));
            JSONObject jsonObject = addParameters();
            writer.write(jsonObject.toString());
            writer.close();
            urlConnection.connect();

            int code = urlConnection.getResponseCode();
            inputStream = urlConnection.getInputStream();
            if(urlConnection.getResponseCode() == 200) {
                jsonResponse = readFromStream(inputStream);
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
        return jsonResponse;
    }

    private JSONObject addParameters() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(loadJSONFromAsset("GetProduct.json"));

            JSONObject variables = jsonObject.getJSONObject("variables");
            variables.put("tpnc","284475550");
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

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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

    /**
     * Return an {@link Product} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private Product extractFeatureFromJson(String input) {
        if(TextUtils.isEmpty(input))
            return null;
        try {
            JSONObject baseJsonResponse = new JSONObject(input);
            JSONObject data = baseJsonResponse.getJSONObject("data");
            JSONObject product = data.getJSONObject("product");
            String title = product.getString("title");
            String defaultImageUrl = product.getString("defaultImageUrl");
            String price = null;
            String offerText = null;
            if(product.has("price")) {
                price = product.getJSONObject("price").getString("price");
            }
            if(product.has("promotions")) {
                JSONArray promotions = product.getJSONArray("promotions");
                if(promotions.length() > 0) {
                    offerText = promotions.getJSONObject(0).getString("offerText");
                }
            }

            return new Product(title, price, defaultImageUrl, offerText );

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }
}
