package com.example.givetake.model;

import com.google.android.gms.maps.model.LatLng;

public class MyAddress {
    private String addressLine;
    private LatLng latLng;

    public MyAddress(String addresLine, LatLng latLng) {
        this.addressLine = addresLine;
        this.latLng = latLng;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
