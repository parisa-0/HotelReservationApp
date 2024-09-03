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
   // Map<Reservation, IRoom> notAvailableRooms = new HashMap<>();



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
      //  notAvailableRooms.put(reservation, room);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Set<IRoom> findRoomsForDates = new HashSet<>();


        for(IRoom room : allRooms.values()) {
            if (isRoomAvailable((Room) room, checkInDate, checkOutDate)) {
                findRoomsForDates.add(room);
            }
        }
            /*
            else {
                for (Reservation reservation : notAvailableRooms.keySet()) {
                    if (!checkInDate.after(reservation.getCheckInDate()) || !checkOutDate.before(reservation.getCheckOutDate())) {
                        findRoomsForDates.add(room);
                    }
                }
            }

        }

        if(findRoomsForDates.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(checkInDate);
            //  cal.add(Calendar.DAY_OF_MONTH, 7);
            cal.add(Calendar.DATE, 7);
            Date dateCheckIn = cal.getTime();
            // cal.add(Calendar.DAY_OF_MONTH, 7);
            cal.add(Calendar.DATE, 7);
            Date dateCheckOut = cal.getTime();
            for(IRoom room : allRooms.values()) {
                for (Reservation reservation : notAvailableRooms.keySet()) {
                    if (!dateCheckIn.after(reservation.getCheckInDate()) || !dateCheckOut.before(reservation.getCheckOutDate())) {
                        findRoomsForDates.add(room);
                    }
                }
            }
            System.out.println("We don't have a room available for the date you selected, please see new check in date and check out date below:");
            System.out.println("Check in date: " + dateCheckIn);
            System.out.println("Check out date: " + dateCheckOut);
        }


 */

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

    public boolean isRoomAvailable(Room room, Date checkIn, Date checkOut) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().equals(room)) {
                Date existingCheckIn = reservation.getCheckInDate();
                Date existingCheckOut = reservation.getCheckOutDate();
                // Check for overlap
                if (checkIn.before(existingCheckOut) && checkOut.after(existingCheckIn)) {
                    return false; // Room is not available
                }
            }
        }
        return true; // Room is available
    }

}
