/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto.response;

import dto.CategoryDTO;
import dto.ItemStatusDTO;
import entity.City;
import java.io.Serializable;
import java.util.List;


public class FeaturesResponseDTO implements  Serializable{
    
    private List<CategoryDTO> categoryList;
    
    private List<ItemStatusDTO> itemStatusList;
    
    private List<City> cityList;

    public FeaturesResponseDTO() {
    }

    public List<CategoryDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }

    public List<ItemStatusDTO> getItemStatusList() {
        return itemStatusList;
    }

    public void setItemStatusList(List<ItemStatusDTO> itemStatusList) {
        this.itemStatusList = itemStatusList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public List<City> getCityList() {
        return cityList;
    }
    
    
    
}
