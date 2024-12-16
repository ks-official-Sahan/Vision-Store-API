/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto.response;

import entity.Address;
import entity.Cart;
import entity.City;
import java.io.Serializable;
import java.util.List;


public class GetCheckoutDTO implements Serializable{
    
    private Address address;
    
    private List<Cart> cartList;
    
    private List<City> cityList;

    public GetCheckoutDTO() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
    
    
    
}
