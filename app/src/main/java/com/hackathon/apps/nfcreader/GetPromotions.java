package com.hackathon.apps.nfcreader;
        import android.content.Context;
        import android.os.AsyncTask;
        import android.text.TextUtils;
        import android.util.Log;
        import android.widget.TextView;

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


public class GetPromotions extends AsyncTask<Void, Void, ArrayList<Product>> {
    private static final String PRODUCT_URL="https://ci-mango.ngbeta.net/";
    private static final String LOG_TAG = "Network Request";
    private  String offerType;
    private Context context;
    ResponseHandler listener;
    @Override
    protected ArrayList<Product> doInBackground(Void... params) {
        URL url = createUrl(PRODUCT_URL);

        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
            return extractFeatureFromJson(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //Product product = extractFeatureFromJson(jsonResponse);

    }

    public void setListener(ResponseHandler listener) {
        this.listener = listener;
    }

    GetPromotions(String offerType, TextView nfcData){
        this.offerType = offerType;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private JSONObject addParameters(String offersType) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(loadJSONFromAsset("GetPromotions.json"));

            JSONObject variables = jsonObject.getJSONObject("variables");
            variables.put("promotion", offersType );
            jsonObject.put("variables",variables);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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
            Log.e("testing",">>>>> " + loadJSONFromAsset("GetPromotions.json"));
            JSONObject jsonObject = addParameters(this.offerType);
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

    @Override
    protected void onPostExecute(ArrayList<Product> s) {
        super.onPostExecute(s);
        if(listener != null)
            listener.OnSuccessfullResponse(s);
    }

    /**
     * Return an {@link Product} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private ArrayList<Product> extractFeatureFromJson(String OfferResult) {
        if(TextUtils.isEmpty(OfferResult))
            return null;
        try {
            JSONObject baseJsonResponse = new JSONObject(OfferResult);
            JSONObject data = baseJsonResponse.getJSONObject("data");
            JSONObject promotions = data.getJSONObject("promotions");
            JSONArray offers = promotions.getJSONArray("productItems");
            if (offers.length() > 0) {
                ArrayList<Product> result = new ArrayList<Product>();
                for(int i =0; i< offers.length(); i++){
                    System.out.println(i);
                    JSONObject jsonItem = offers.getJSONObject(i);
                    String title = jsonItem.getString("title");
                    String defaultImageUrl = jsonItem.getString("defaultImageUrl");
                    String price = jsonItem.getJSONObject("price").getString("price");
                    String offerText = jsonItem.getJSONArray("promotions").getJSONObject(0).getString("offerText");
                    Product item = new Product(title,price, defaultImageUrl, offerText );
                    result.add(item);

                }
                return result;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }
}
