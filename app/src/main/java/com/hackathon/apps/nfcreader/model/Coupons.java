package com.hackathon.apps.nfcreader.model;

/**
 * Created by ruthvik on 21/09/2017.
 */

public class Coupons {

    public String code;


    public String  qrCodeUrl, title;

    public String description, thumbnail;

    public Coupons(String code, String title, String qrCodeUrl, String description, String thumbnail) {
        this.code = code;
        this.title = title;
        this.qrCodeUrl = qrCodeUrl;
        this.description = description;
        this.thumbnail = thumbnail;

    }

}
