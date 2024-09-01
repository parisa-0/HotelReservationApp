import api.AdminResource;
import api.HotelResource;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HotelApplication {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void main(String[] args) throws ParseException {

        MainMenu.printMainMenu();
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if(Integer.parseInt(line) > 0 && Integer.parseInt(line) < 6) {
            switch (line) {
                case "1" -> findAndReserveARoom();
                case "2" -> seeMyReservations();
                case "3" -> createAnAccount();
                case "4" -> adminMenu();
                case "5" -> scanner.close();
            }
        }
        else {
            System.out.println("invalid selection");
        }

    }

    private static void adminMenu() {
        AdminMenu.printAdminMenu();
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if(Integer.parseInt(line) > 0 && Integer.parseInt(line) < 6) {
            switch (line) {
                case "1" -> seeAllCustomers();
                case "2" -> seeAllRooms();
                case "3" -> seeAllReservations();
                case "4" -> addARoom();
                case "5" -> MainMenu.printMainMenu();
            }
        }
        else {
            System.out.println("invalid selection");
        }
    }

    private static void addARoom() {
        Scanner scanner = new Scanner(System.in);
        List<IRoom> rooms = new ArrayList<>();

        System.out.println("Would you like to add a room now? (Y/N)");
        while(scanner.nextLine().equalsIgnoreCase("y")) {
            rooms.add(roomDetailsToAdd());
            System.out.println("Would you like to add another room now? (Y/N)");
        }
        AdminResource.addRoom(rooms);
    }

    public static Room roomDetailsToAdd() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter the room number");
        String roomNumber = scanner.nextLine();
        System.out.println("Please enter the price");
        Double price = scanner.nextDouble();
        System.out.println("Please enter room type");
        RoomType type = RoomType.valueOf(scanner.next().toUpperCase());
        return new Room(roomNumber, price, type);
    }


    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void seeAllRooms() {
        adminResource.getAllRooms().forEach(System.out::println);
    }

    private static void seeAllCustomers() {
        adminResource.getAllCustomers().forEach(System.out::println);
    }

    private static void createAnAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please provide your first name");
        String firstName = scanner.nextLine();
        System.out.println("Please provide your last name");
        String lastName = scanner.nextLine();
        System.out.println("Please provide your email address");
        String email = scanner.nextLine();
        HotelResource.createACustomer(email, firstName, lastName);
    }

    private static void seeMyReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter your email");
        String email = scanner.nextLine();
        HotelResource.getCustomersReservations(email);
    }

    public static void findAndReserveARoom() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Enter Check in date (dd/mm/yyyy)");
        String checkIn = scanner.next();
        System.out.println("Enter Check out date (dd/mm/yyyy)");
        String checkOut =  scanner.next();

        Date checkInDate = sdf.parse(checkIn);
        Date checkOutDate = sdf.parse(checkOut);
        Collection<IRoom> availableRooms = hotelResource.findARooms(checkInDate, checkOutDate);
        String email = "";
        if(availableRooms.isEmpty()) {
            System.out.println("unfortunately there are no available rooms for this date");
            MainMenu.printMainMenu();
        }
        else {
            System.out.println("please enter your email");
            email = scanner.nextLine();

            if(AdminResource.getCustomer(email) == null) {
                createAnAccount();
            }

            IRoom room = availableRooms.iterator().next();
            HotelResource.bookARoom(email, room, checkInDate, checkOutDate);
        }
    }
}
