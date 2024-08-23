package br.com.henrique.JWT.models.dto;

import java.io.Serializable;

import br.com.henrique.JWT.models.User;
import org.springframework.http.ResponseEntity;

public class AddressDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private UserDto userId;
    private String publicPlace;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public AddressDto() {
    }

    public AddressDto(Long id, UserDto userId, String publicPlace, String number, String neighborhood, String city, String state, String zipCode, String country) {
        this.id = id;
        this.userId = userId;
        this.publicPlace = publicPlace;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUserId() {
        return userId;
    }

    public void setUserId(UserDto userId) {
        this.userId = userId;
    }

    public String getPublicPlace() {
        return publicPlace;
    }

    public void setPublicPlace(String publicPlace) {
        this.publicPlace = publicPlace;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
