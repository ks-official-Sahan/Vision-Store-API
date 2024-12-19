package dto;

import entity.Category;
import java.io.Serializable;

public class ItemDTO implements Serializable {

    private int id;

    private String title;

    private String name;

    private double price;

    private String description;

    private String imagePath;

    private int quantity;
    
    private String category;

    public ItemDTO() {
    }

    public ItemDTO(int id, String title, String name, double price, String description, String imagePath, int quantity) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.quantity = quantity;
    }

    public ItemDTO(int id, String title, String name, double price, String description, String imagePath, int quantity, String category) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.quantity = quantity;
        this.category = category;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
