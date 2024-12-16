/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto.request;

import java.io.Serializable;


public class GradenDTO implements Serializable{
    
    private String gardenName;
    private String description;

    public GradenDTO() {
    }

    public GradenDTO(String gardenName, String description) {
        this.gardenName = gardenName;
        this.description = description;
    }

    public String getGardenName() {
        return gardenName;
    }

    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
