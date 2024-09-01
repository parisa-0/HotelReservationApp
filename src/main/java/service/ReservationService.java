package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
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
            if(notAvailableRooms.isEmpty()) {
                findRoomsForDates.add(room);
            }
            else {
                for (Reservation reservation : notAvailableRooms.keySet()) {
                    if (!reservation.toString().contains(checkInDate.toString()) && !reservation.toString().contains(checkOutDate.toString())) {
                        findRoomsForDates.add(room);
                    }
                }
            }
        }

        if(findRoomsForDates.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(checkInDate);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            Date dateCheckIn = cal.getTime();
            cal.setTime(checkOutDate);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            Date dateCheckOut = cal.getTime();
            for(IRoom room : allRooms.values()) {
                for (Reservation reservation : notAvailableRooms.keySet()) {
                    //here you need to check to see if the current reservation is before or after the other one
                    if (dateCheckIn.after(reservation.getCheckInDate()) && dateCheckOut.before(reservation.getCheckOutDate())) {
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
            if(reservation.toString().contains(customer.toString())) {
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
