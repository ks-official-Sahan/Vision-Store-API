/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto.request;

import java.io.Serializable;


public class ProductSearchDTO implements Serializable{

    private String categoryName;
    private int freshness;
    private double priceRangeStart;
    private double priceRangeEnd;
    private String sortText;
    private int first;

    public ProductSearchDTO() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getFreshness() {
        return freshness;
    }

    public void setFreshness(int freshness) {
        this.freshness = freshness;
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
    
    
    
}
