package till;

public class storeItem implements Comparable<storeItem> {

    String itemName;
    double cost;
    int code;
    double weight;
    boolean taxed;

    storeItem(){
        this.itemName = "";
        this.taxed = true;
        this.cost = -1;
        this.code = -1;
        this.weight = Double.NaN;
    }

    storeItem(double cost, int code) {
        this.taxed = true;
        this.cost = cost;
        this.code = code;
        this.weight = Double.NaN;

    }

    storeItem(double cost, int code, double weight) {
        this.taxed = true;
        this.cost = cost;
        this.code = code;
        this.weight = weight;
    }

    public double getCost(){
        return this.cost;
    }

    public int getBarCode(){
        return this.code;
    }

    public void setBarCode(int code){
        this.code = code;
    }

    public void setCost(double cost){
        this.cost = cost;
    }

    public boolean isTaxed(){
        return this.taxed;
    }

    public void removeTax(){
        this.taxed = false;
    }

    public String getItemName(int code){
        return "";
    }

    @Override 
    public int compareTo(storeItem other){
        if (this.cost == other.cost){
            return 0;
        }
        else if (this.cost > other.cost){
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
        return (this.code == c.code);
    }

    @Override
    public int hashCode(){
        return 31 * this.code;
    }
}

 

