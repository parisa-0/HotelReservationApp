package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.time.LocalDate;
import java.util.*;

public class ReservationService {
    Map<String, IRoom> allRooms = new HashMap<>();
    Set<Reservation> reservations = new HashSet<>();
    Map<Reservation, IRoom> notAvailableRooms = new HashMap<>();

    private static ReservationService instance = new ReservationService();

    // Step 1: Private constructor
    private ReservationService() {
        // Initialization code here
    }

    // Step 3: Public static method to get the instance
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
        notAvailableRooms.put(reservation, room);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Set<IRoom> findRoomsForDates = new HashSet<>();
        for(IRoom room : allRooms.values()) {
            for (Reservation reservation : notAvailableRooms.keySet()) {
                if (!reservation.toString().equals(checkInDate.toString()) && !reservation.toString().equals(checkOutDate.toString())) {
                    findRoomsForDates.add(room);
                }
            }
        }

        if(findRoomsForDates.isEmpty()) {
            LocalDate dateCheckIn = LocalDate.from(checkInDate.toInstant());
            LocalDate dateCheckOut = LocalDate.from(checkOutDate.toInstant());
            dateCheckIn = dateCheckIn.plusDays(7);
            dateCheckOut = dateCheckOut.plusDays(7);
            for(IRoom room : allRooms.values()) {
                for (Reservation reservation : notAvailableRooms.keySet()) {
                    if (!reservation.toString().equals(dateCheckIn.toString()) && !reservation.toString().equals(dateCheckOut.toString())) {
                        findRoomsForDates.add(room);
                    }
                }
            }
        }
        return findRoomsForDates;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Set<Reservation> customersReservation = new HashSet<>();
        for (Reservation reservation : reservations) {
            if(reservation.toString().equals(customer.toString())) {
                customersReservation.add(reservation);
            }
        }
        return customersReservation;
    }

    public Collection<IRoom> getAllRooms() {
        return allRooms.values();
    }

    public void printAllReservation() {
        for(Reservation reservation : reservations) {
            System.out.println(reservation.toString());
        }
    }
}
