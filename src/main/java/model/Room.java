package model;

public class Room implements IRoom {
    private final String roomNumber;
    Double price;
    private final RoomType enumeration;

    public Room(String roomNumber, Double price, RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }

    @Override
    public final String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public final Double getRoomPrice() {
        return price;
    }

    @Override
    public final RoomType getRoomType() {
        return enumeration;
    }

    @Override
    public final boolean isFree() {
        return false;
    }

    @Override
    public String toString() {
        return "Room Number:" + roomNumber + ", Room Price:" + price + ", Room Type: " + enumeration + ", is Free: false";
    }
}
