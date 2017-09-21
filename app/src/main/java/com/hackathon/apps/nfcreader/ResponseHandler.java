package com.hackathon.apps.nfcreader;

import com.hackathon.apps.nfcreader.model.Product;

import java.util.ArrayList;

/**
 * Created by ruthvik on 21/09/2017.
 */

public interface ResponseHandler{
    public void OnSuccessfullResponse(String result);
    public void OnSuccessfullResponse(ArrayList<Product> products);
}
