/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto.request;

import dto.OwnerDTO;
import java.io.Serializable;


public class OpenShopDTO implements Serializable{
    
    private OwnerDTO owner;
    
    private GradenDTO graden;
    
    private GardenAddressDTO gradenAddress;

    public OpenShopDTO() {
    }

    public OpenShopDTO(OwnerDTO owner, GradenDTO graden, GardenAddressDTO gradenAddress) {
        this.owner = owner;
        this.graden = graden;
        this.gradenAddress = gradenAddress;
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }

    public GradenDTO getGraden() {
        return graden;
    }

    public void setGraden(GradenDTO graden) {
        this.graden = graden;
    }

    public GardenAddressDTO getGradenAddress() {
        return gradenAddress;
    }

    public void setGradenAddress(GardenAddressDTO gradenAddress) {
        this.gradenAddress = gradenAddress;
    }
    
    
}
