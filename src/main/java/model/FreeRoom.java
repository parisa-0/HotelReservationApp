package model;

public class FreeRoom extends Room {
    public FreeRoom(Double price) {
        this.price = 0.0;
    }

    @Override
    public String toString() {
        return " Room Price:" + price;
    }
}
