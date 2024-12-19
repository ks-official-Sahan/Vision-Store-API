package dto.response;

import dto.ItemDTO;
import entity.Item;
import java.io.Serializable;
import java.util.List;

public class SingleItemResponseDTO implements Serializable{
    
    private ItemDTO singleItem;
    
    private List<ItemDTO> similarItems;

    public SingleItemResponseDTO() {
    }

    public ItemDTO getSingleItem() {
        return singleItem;
    }

    public void setSingleItem(ItemDTO singleItem) {
        this.singleItem = singleItem;
    }

    public List<ItemDTO> getSimilarItems() {
        return similarItems;
    }

    public void setSimilarItems(List<ItemDTO> similarItems) {
        this.similarItems = similarItems;
    }

}
