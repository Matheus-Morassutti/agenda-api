package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String zipCode;

    // Address fields are resolved from the external ViaCEP API, never from the
    // request body. They are serialized in responses but ignored on input.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String street;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String neighborhood;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String city;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String state;

    public Contact() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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
}
