package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {
    Map<String, IRoom> allRooms = new HashMap<>();
    Set<Reservation> reservations = new HashSet<>();
    int numberOfDaysToAddForReservation = 7;

    private static ReservationService instance = new ReservationService();

    private ReservationService() {
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public void addRoom(IRoom room) {
        allRooms.put(room.getRoomNumber(), room);
    }
    public IRoom getRoom(String roomId) {
        return allRooms.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        return allRooms.values().stream()
                .filter(room -> isRoomAvailable((Room) room, checkInDate, checkOutDate))
                .collect(Collectors.toSet());
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        return reservations.stream()
                .filter(res -> res.toString().contains(customer.toString()))
                .collect(Collectors.toSet());
    }

    public Collection<IRoom> getAllRooms() {
        return allRooms.values();
    }

    public void printAllReservation() {
        reservations.forEach(System.out::println);
    }

    public boolean isRoomAvailable(Room room, Date checkIn, Date checkOut) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().equals(room)) {
                Date existingCheckIn = reservation.getCheckInDate();
                Date existingCheckOut = reservation.getCheckOutDate();
                if ((checkIn.before(existingCheckOut) && checkOut.after(existingCheckIn)) ||
                        checkIn.equals(existingCheckIn) || checkOut.equals(existingCheckOut)
                        || checkIn.equals(existingCheckOut) || checkOut.equals(existingCheckIn)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Collection<IRoom> findRecommendedRooms(Date checkInDate, Date checkOutDate) {
        return findRooms(addDays(checkInDate),addDays(checkOutDate));
    }

    public Date addDays(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDaysToAddForReservation);
        return calendar.getTime();
    }

}
