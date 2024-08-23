package br.com.henrique.JWT.models;

import java.io.Serializable;

import br.com.henrique.JWT.models.dto.AddressWithoutUserDto;
import br.com.henrique.JWT.models.dto.AddressWithUserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Addresses")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "public_place")
    private String publicPlace;

    @Column(name = "house_number")
    private String number;

    private String neighborhood;

    private String city;

    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    private String country;

    public Address() {
    }

    public Address(Address address) {
        this.id = address.getId();
        this.userId = address.getUserId();
        this.publicPlace = address.getPublicPlace();
        this.number = address.getNumber();
        this.neighborhood = address.getNeighborhood();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getZipCode();
        this.country = address.getCountry();
    }

    public Address(AddressWithUserDto addressWithUserDto, User user) {
        this.userId = user;
        this.publicPlace = addressWithUserDto.getPublicPlace();
        this.number = addressWithUserDto.getNumber();
        this.neighborhood = addressWithUserDto.getNeighborhood();
        this.city = addressWithUserDto.getCity();
        this.state = addressWithUserDto.getState();
        this.zipCode = addressWithUserDto.getZipCode();
        this.country = addressWithUserDto.getCountry();
    }

    public Address(User userId, String publicPlace, String number, String neighborhood, String city, String state, String zipCode, String country) {
        this.userId = userId;
        this.publicPlace = publicPlace;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Address(Long id, User userId, String publicPlace, String number, String neighborhood, String city, String state, String zipCode, String country) {
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

    public User getUserId() {
        return userId;
    }

    public void setUserId(User user) {
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
