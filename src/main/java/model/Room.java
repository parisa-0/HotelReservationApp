package model;

public class Room implements IRoom {
    String roomNumber;
    Double price;
    RoomType enumeration;

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return enumeration;
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public String toString() {
        return "Room Number:" + roomNumber + ", Room Price:" + price + ", Room Type: " + enumeration + ", is Free: false";
    }
}
