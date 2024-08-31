package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    // Static variable
    private static final String BASE_URL = "http://api.hotelreservation.com";
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    public static void printBaseUrl() {
        System.out.println("Base URL: " + BASE_URL);
    }

    public Customer getCustomer(String email) {
        return getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getRoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        return reservationService.reserveARoom(getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
       return reservationService.getCustomersReservation(getCustomer(customerEmail));
    }

    public Collection<IRoom> findARooms(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn,checkOut);
    }
 }
