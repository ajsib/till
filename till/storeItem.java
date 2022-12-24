package till;

public abstract class storeItem {
    double cost;
    int code;
    double weight;

    storeItem(){
        this.cost = 0.0;
        this.code = 0;
        this.weight = 0.0;
    }

    storeItem(double cost, int code) {
        this.cost = cost;
        this.code = code;
        this.weight = Double.NaN;

    }

    storeItem(double cost, int code, double weight) {
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

    public void setWeight(double weight){
        this.weight = weight;
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
}
