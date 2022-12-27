package till;

public class Tuple<T, K> implements tupleInterface<T, K>{

    T first;
    K second;

    Tuple(T first, K second){
        this.first = first;
        this.second = second;
    }

    Tuple(){
        this.first = null;
        this.second = null;
    }

    Tuple(Tuple<T, K> other){
        this.first = other.first;
        this.second = other.second;
    }

    public void setTuple(T first, K second){
        this.first = first;
        this.second = second;
    }

    @Override
    public T getFirst(){
        return this.first;
    }

    @Override
    public K getSecond(){
        return this.second;
    }

    @Override
    public void setFirst(T first){
        this.first = first;
    }

    @Override
    public void setSecond(K second){
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }
}
