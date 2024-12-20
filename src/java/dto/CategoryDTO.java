package dto;

import java.io.Serializable;


public class CategoryDTO implements Serializable{
    
    private int id;
    private String name;
    private String parent;
    private String media;

    public CategoryDTO() {
    }

    public CategoryDTO(int id, String name, String parent, String media) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.media = media;
    }

    public CategoryDTO(int id, String name, String parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }
    
    public CategoryDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
    
    
}
