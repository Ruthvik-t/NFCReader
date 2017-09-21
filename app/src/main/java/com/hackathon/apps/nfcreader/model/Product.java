package com.hackathon.apps.nfcreader.model;

/**
 * Created by ruthvik on 21/09/2017.
 */

public class Product {

    public String title;

    public String  price, offerText;

    public String defaultImageUrl;

    public Product(String title, String price, String defaultImageUrl, String offerText) {
        this.title = title;
        this.defaultImageUrl = defaultImageUrl;
        this.price = price;
        this.offerText = offerText;

    }

}


