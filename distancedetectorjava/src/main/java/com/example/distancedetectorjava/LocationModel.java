package com.example.distancedetectorjava;

public class LocationModel {

    double lat = 0;
    double lng = 0;
    double cLat = 0;
    double cLng = 0;
    double pLat = 0;
    double pLng = 0;
    double distance = 0;

    public LocationModel(){}

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LocationModel(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getcLat() {
        return cLat;
    }

    public void setcLat(double cLat) {
        this.cLat = cLat;
    }

    public double getcLng() {
        return cLng;
    }

    public void setcLng(double cLng) {
        this.cLng = cLng;
    }

    public double getpLat() {
        return pLat;
    }

    public void setpLat(double pLat) {
        this.pLat = pLat;
    }

    public double getpLng() {
        return pLng;
    }

    public void setpLng(double pLng) {
        this.pLng = pLng;
    }


}
