package com.example.julian.matthew.tamim.massivepackage.Model;

import java.util.List;

/**
 * Created by julian.moses on 07/12/15.
 */
public class PropertyModel {
    private String listing_id;
    private String status;
    private String num_floors;
    private String num_bedrooms;
    private String agent_name;
    private Double latitude;
    private Double longitude;
    private String agent_address;
    private String property_type;
    private String country;
    private String first_published_date;
    private String displayable_address;
    private String num_bathrooms;
    private String image_url;
    private String description;
    private String post_town;
    private String agent_logo;
    private String agent_phone;
    private String county;
    private String price;
    private List<String> floor_plan;

    @Override
    public String toString() {
        return "PropertyModel{" +
                "listing_id='" + listing_id + '\'' +
                ", status='" + status + '\'' +
                ", num_floors='" + num_floors + '\'' +
                ", num_bedrooms='" + num_bedrooms + '\'' +
                ", agent_name='" + agent_name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", agent_address='" + agent_address + '\'' +
                ", property_type='" + property_type + '\'' +
                ", country='" + country + '\'' +
                ", first_published_date='" + first_published_date + '\'' +
                ", displayable_address='" + displayable_address + '\'' +
                ", num_bathrooms='" + num_bathrooms + '\'' +
                ", image_url='" + image_url + '\'' +
                ", description='" + description + '\'' +
                ", post_town='" + post_town + '\'' +
                ", agent_logo='" + agent_logo + '\'' +
                ", agent_phone='" + agent_phone + '\'' +
                ", county='" + county + '\'' +
                ", price='" + price + '\'' +
                ", floor_plan=" + floor_plan +
                '}';
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNum_floors() {
        return num_floors;
    }

    public void setNum_floors(String num_floors) {
        this.num_floors = num_floors;
    }

    public String getNum_bedrooms() {
        return num_bedrooms;
    }

    public void setNum_bedrooms(String num_bedrooms) {
        this.num_bedrooms = num_bedrooms;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAgent_address() {
        return agent_address;
    }

    public void setAgent_address(String agent_address) {
        this.agent_address = agent_address;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirst_published_date() {
        return first_published_date;
    }

    public void setFirst_published_date(String first_published_date) {
        this.first_published_date = first_published_date;
    }

    public String getDisplayable_address() {
        return displayable_address;
    }

    public void setDisplayable_address(String displayable_address) {
        this.displayable_address = displayable_address;
    }

    public String getNum_bathrooms() {
        return num_bathrooms;
    }

    public void setNum_bathrooms(String num_bathrooms) {
        this.num_bathrooms = num_bathrooms;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPost_town() {
        return post_town;
    }

    public void setPost_town(String post_town) {
        this.post_town = post_town;
    }

    public String getAgent_logo() {
        return agent_logo;
    }

    public void setAgent_logo(String agent_logo) {
        this.agent_logo = agent_logo;
    }

    public String getAgent_phone() {
        return agent_phone;
    }

    public void setAgent_phone(String agent_phone) {
        this.agent_phone = agent_phone;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getFloor_plan() {
        return floor_plan;
    }

    public void setFloor_plan(List<String> floor_plan) {
        this.floor_plan = floor_plan;
    }

    /*public static class FloorPlan{
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }*/
}
