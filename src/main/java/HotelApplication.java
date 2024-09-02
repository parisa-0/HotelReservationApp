import api.AdminResource;
import api.HotelResource;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
reserving a room as a different customer for the same dates room should not be available + 7 days if still not avail
then not available - THIS IS FAILING
Fix your find and reserve a room:
The app should show the list of available rooms so the user can choose one of them.
Currently, the app does not show the available rooms: Please make sure to only show available rooms for the user.
When no room is available for the requested date range the app should suggest a new date range with available rooms as stated in the project instructions:
Currently, the app is creating multiple reservations for the same room when the same dates are inputted:
Tip: A user should not be able to book a single room twice for the same date range.
*/

public class HotelApplication {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final AdminResource adminResource = AdminResource.getInstance();


        private static void mainMenuSelection() throws ParseException {
            MainMenu.printMainMenu();
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            try {
                if (Integer.parseInt(line) > 0 && Integer.parseInt(line) < 6) {
                    switch (line) {
                        case "1" -> findAndReserveARoom();
                        case "2" -> seeMyReservations();
                        case "3" -> createAnAccount();
                        case "4" -> adminMenu();
                        case "5" -> scanner.close();
                    }
                } else {
                    System.out.println("invalid selection");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("please enter a valid selection");
                mainMenuSelection();
            }

        }

    private static void adminMenu() throws ParseException {
        AdminMenu.printAdminMenu();
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            if (Integer.parseInt(line) > 0 && Integer.parseInt(line) < 6) {
                switch (line) {
                    case "1" -> seeAllCustomers();
                    case "2" -> seeAllRooms();
                    case "3" -> seeAllReservations();
                    case "4" -> addARoom();
                    case "5" -> mainMenuSelection();
                    default -> adminMenu();
                }
            } else {
                System.out.println("invalid selection");
            }
        }
        catch (NumberFormatException e) {
            System.out.println("please enter a valid selection");
            adminMenu();
        }
    }

    private static void addARoom() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        List<IRoom> rooms = new ArrayList<>();

        System.out.println("Would you like to add a room now? (Y/N)");
        while(scanner.nextLine().equalsIgnoreCase("y")) {
            rooms.add(roomDetailsToAdd());
            AdminResource.addRoom(rooms);
            System.out.println("Would you like to add another room now? (Y/N)");
        }
        AdminResource.addRoom(rooms);
        mainMenuSelection();
    }

    public static Room roomDetailsToAdd() {
        Scanner scanner = new Scanner(System.in);
        String roomNumber = null;
        while(roomNumber == null) {
            System.out.println("Please Enter the room number");
            roomNumber = scanner.next();
            if(hotelResource.getRoom(roomNumber) != null) {
                System.out.println("Room already exists");
                System.out.println("Please enter another room number");
                roomNumber = null;
            }
        }

        System.out.println("Please enter the price");
        Double price = scanner.nextDouble();
        RoomType type = null;
        while(type == null) {
            try {
                System.out.println("Please enter room type as SINGLE or DOUBLE");
                type = RoomType.valueOf(scanner.next().toUpperCase());
            }
            catch(Exception e) {
                System.out.println("invalid room type, please enter SINGLE or DOUBLE");
                roomDetailsToAdd();
            }
        }
        return new Room(roomNumber, price, type);
    }


    private static void seeAllReservations() throws ParseException {
        adminResource.displayAllReservations();
        adminMenu();
    }

    private static void seeAllRooms() throws ParseException {
        adminResource.getAllRooms().forEach(System.out::println);
        adminMenu();
    }

    private static void seeAllCustomers() throws ParseException {
        adminResource.getAllCustomers().forEach(System.out::println);
        adminMenu();
    }

    private static void createAnAccount() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        String firstName = "";
        String lastName = "";
        System.out.println("Please provide your first name");
        firstName = scanner.next();
        System.out.println("Please provide your last name");
        lastName = scanner.next();
        System.out.println("Please provide your email address");
        String email = scanner.next();
        try {
            HotelResource.createACustomer(email, firstName, lastName);
            System.out.println("account created!");
            mainMenuSelection();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
            createAnAccount();
        }
        System.out.println("account created!");
    }

    private static void seeMyReservations() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter your email");
        String email = scanner.nextLine();
        System.out.println(HotelResource.getCustomersReservations(email));
        mainMenuSelection();
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
            mainMenuSelection();
        }
        else {
            System.out.println("please enter your email");
            email = scanner.next();

            if(HotelResource.getCustomer(email) == null || email == null || email.isEmpty()) {
                String firstName = "";
                String lastName = "";
                System.out.println("Please provide your first name");
                firstName = scanner.next();
                System.out.println("Please provide your last name");
                lastName = scanner.next();
                HotelResource.createACustomer(email, firstName, lastName);
            }

            IRoom room = availableRooms.iterator().next();
            HotelResource.bookARoom(email, room, checkInDate, checkOutDate);
            mainMenuSelection();
        }
    }
    public static void main(String[] args) throws ParseException {
        mainMenuSelection();
    }
}
