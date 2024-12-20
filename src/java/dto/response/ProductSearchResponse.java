/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto.response;

import dto.ItemDTO;
import entity.Item;
import java.io.Serializable;
import java.util.List;

public class ProductSearchResponse implements Serializable {

    private List<Item> items;
    private List<ItemDTO> itemList;

    private int allItemCount;

    public ProductSearchResponse() {
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getAllItemCount() {
        return allItemCount;
    }

    public void setAllItemCount(int allItemCount) {
        this.allItemCount = allItemCount;
    }

    public List<ItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemDTO> itemList) {
        this.itemList = itemList;
    }

}
