package dto;

import java.io.Serializable;


public class AddressDTO implements Serializable{
    
    private int id;

    private String firstName;

    private String lastName;

    private String mobileNumber;

    private String line1;

    private String line2;

    private String postalCode;

    private int cityId;
    
    public AddressDTO() {
    }

    public AddressDTO(int id, String firstName, String lastName, String mobileNumber, String line1, String line2, String postalCode, int cityId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.line1 = line1;
        this.line2 = line2;
        this.postalCode = postalCode;
        this.cityId = cityId;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

}
