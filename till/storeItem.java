package till;

import java.lang.Math;

public class storeItem implements Comparable<storeItem> {

    String productName;
    String brand;
    double price;
    String category;
    String subCategory;
    long barcode;
    double taxRate;

    public storeItem(String productName, String brand, double price, String category, String subCategory, long barcode) {
        this.productName = productName;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
        this.barcode = barcode;
        this.taxRate = 1;
    }

    public storeItem(storeItem other){
        this.productName = other.productName;
        this.brand = other.brand;
        this.price = other.price;
        this.category = other.category;
        this.subCategory = other.subCategory;
        this.barcode = other.barcode;
        this.taxRate = other.taxRate;
    }

    public void applyDiscount(double discountRate) {
        this.price = discountRate * price;
    }

    public double taxRate() {
        return this.taxRate;
    }

    public void setTaxRate(double rate){
        this.taxRate *= rate;
    }

    @Override 
    public int compareTo(storeItem other){
        if (this.price == other.price){
            return 0;
        }
        else if (this.price > other.price){
            return 1;
        }
        else{
            return -1;
        }
    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof storeItem)){
            return false;
        }
        storeItem c = (storeItem) other;
        return (this.barcode == c.barcode);
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(barcode);
    }

    @Override
    public String toString() {
        String s = new String();
        s += this.productName + "|" +  this.brand + " ----  $" +  this.price;
        return s;
    }
}

 

