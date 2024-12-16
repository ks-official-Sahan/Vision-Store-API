/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto.request;

import java.io.Serializable;


public class OwnerLoginDTO implements Serializable{
    private String email;
    private String ownerCode;

    public OwnerLoginDTO() {
    }

    public OwnerLoginDTO(String email, String ownerCode) {
        this.email = email;
        this.ownerCode = ownerCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }
    
    
}
