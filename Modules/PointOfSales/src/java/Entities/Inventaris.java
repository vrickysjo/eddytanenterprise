/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author valentinus.r.sjofyan
 */
public class Inventaris {
    private String invName;
    private int stock;
    private int price;
    private String invType;
    private List<InventarisHist> invHist = new ArrayList<InventarisHist>();

    public String getInvName() {
        return invName;
    }

    public void setInvName(String invName) {
        this.invName = invName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public List<InventarisHist> getInvHist() {
        return invHist;
    }

    public void setInvHist(List<InventarisHist> invHist) {
        this.invHist = invHist;
    }
    
    
}
