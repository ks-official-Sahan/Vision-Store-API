package entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import javax.persistence.*;

@Entity()
@Table(name = "category")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Column(name = "created_at")
    private Date createdAt = new Date(System.currentTimeMillis());

    @Column(name = "updated_at")
    private Date updatedAt = new Date(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "parent_category_id", nullable = true)
    private Category parent;
    
    @OneToMany(mappedBy = "category")
    private List<Item> itemList;

    public Category() {
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

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

}
