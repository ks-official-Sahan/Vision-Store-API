package dto.request;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class UserDTO implements Serializable {

    @Expose
    private String name;

    @Expose
    private String email;

    @Expose(deserialize = true, serialize = false)
    private String password;

    @Expose
    private String role;
 
    public UserDTO() {
    }

    public UserDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
       
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    

}
