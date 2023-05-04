package com.example.holidaydestinationsapp;

public class ItemDestination {
    String hotelName;
    String hotelAddress;
    int imageHotel;

    public ItemDestination(String hotelName, String hotelAddress, int imageHotel) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.imageHotel = imageHotel;
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

    public void setHotelAddress(String hotelAdress) {
        this.hotelAddress = hotelAdress;
    }

    public int getImageHotel() {
        return imageHotel;
    }

    public void setImageHotel(int imageHotel) {
        this.imageHotel = imageHotel;
    }
}
