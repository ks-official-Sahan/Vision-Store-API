package dto.request;

import java.io.Serializable;


public class ProductSearchDTO implements Serializable{

    private String categoryName;
    private double priceRangeStart = 0;
    private double priceRangeEnd = 0;
    private String sortText;
    private int first;
    private String searchText;

    public ProductSearchDTO() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getPriceRangeStart() {
        return priceRangeStart;
    }

    public void setPriceRangeStart(double priceRangeStart) {
        this.priceRangeStart = priceRangeStart;
    }

    public double getPriceRangeEnd() {
        return priceRangeEnd;
    }

    public void setPriceRangeEnd(double priceRangeEnd) {
        this.priceRangeEnd = priceRangeEnd;
    }

    public String getSortText() {
        return sortText;
    }

    public void setSortText(String sortText) {
        this.sortText = sortText;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    
    
    
}
