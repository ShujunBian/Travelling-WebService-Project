/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.tongji.cdi.soul.domain;

/**
 *
 * @author bianshujun
 */
public class Hotel {

    private String hotelName;
    private String longtitude;
    private String latitude;
    private String cityName;
    private String regionName;
    private String hotelAddress;
    private String hotelCtripId;
    private String hotelHotwireId;
    private String hotelDZDPId;
    private String hotelTCId;
    private int searchingType;

    public Hotel() {
    }

    public Hotel(String hotelName, String cityName, String hotelAddress) {
        this.hotelName = hotelName;
        this.cityName = cityName;
        this.hotelAddress = hotelAddress;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getHotelCtripId() {
        return hotelCtripId;
    }

    public void setHotelCtripId(String hotelCtripId) {
        this.hotelCtripId = hotelCtripId;
    }

    public String getHotelHotwireId() {
        return hotelHotwireId;
    }

    public void setHotelHotwireId(String hotelHotwireId) {
        this.hotelHotwireId = hotelHotwireId;
    }

    public String getHotelDZDPId() {
        return hotelDZDPId;
    }

    public void setHotelDZDPId(String hotelDZDPId) {
        this.hotelDZDPId = hotelDZDPId;
    }

    public String getHotelTCId() {
        return hotelTCId;
    }

    public void setHotelTCId(String hotelTCId) {
        this.hotelTCId = hotelTCId;
    }

    public int getSearchingType() {
        return searchingType;
    }

    public void setSearchingType(int searchingType) {
        this.searchingType = searchingType;
    }
}
