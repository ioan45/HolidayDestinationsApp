package com.example.holidaydestinationsapp;

public class ItemDestination {
    String hotelName;
    String hotelAddress;
    int imageHotel;
    double lat, lgt;

    public ItemDestination(String hotelName, String hotelAddress, int imageHotel, double lat, double lgt) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.imageHotel = imageHotel;
        this.lat = lat;
        this.lgt = lgt;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public int getImageHotel() {
        return imageHotel;
    }

    public void setImageHotel(int imageHotel) {
        this.imageHotel = imageHotel;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLgt() {
        return lgt;
    }

    public void setLgt(double lgt) {
        this.lgt = lgt;
    }
}
