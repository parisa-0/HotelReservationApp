import api.AdminResource;
import api.HotelResource;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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
        } catch (NumberFormatException e) {
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
        } catch (NumberFormatException e) {
            System.out.println("please enter a valid selection");
            adminMenu();
        }
    }

    private static void addARoom() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        List<IRoom> rooms = new ArrayList<>();

        System.out.println("Would you like to add a room now? (Y/N)");
        while (scanner.nextLine().equalsIgnoreCase("y")) {
            rooms.add(roomDetailsToAdd());
            AdminResource.addRoom(rooms);
            System.out.println("Would you like to add another room now? (Y/N)");
        }
        AdminResource.addRoom(rooms);
        mainMenuSelection();
    }

    public static Room roomDetailsToAdd() throws ParseException {

        int roomNumber = -1;
        while (roomNumber < 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please Enter the room number");
            try {
                roomNumber = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer for the room number.");
                roomNumber = -1;
            }
            if (hotelResource.getRoom(String.valueOf(roomNumber)) != null) {
                System.out.println("Room already exists");
                System.out.println("Please enter another room number");
                roomNumber = -1;
            }
        }

        Double price = -1.0;
        while (price < 0.0) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the price");
            try {
                price = scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number for price.");
                roomNumber = -1;
            }
        }

        RoomType type = null;
        while (type == null) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter room type as SINGLE or DOUBLE");
                type = RoomType.valueOf(scanner.next().toUpperCase());
            } catch (Exception e) {
                System.out.println("invalid room type, please enter SINGLE or DOUBLE");
                roomDetailsToAdd();
            }
        }
        return new Room(String.valueOf(roomNumber), price, type);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            LocalDate checkIn = null;
            boolean validDateCheckIn = false;

            while (!validDateCheckIn) {
                System.out.print("Enter check-in date (yyyy-MM-dd): ");
                String dateInput = scanner.next();

                try {
                    checkIn = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);

                    if (checkIn.isBefore(LocalDate.now())) {
                        System.out.println("Check-in date cannot be in the past. Please enter a valid date.");
                    } else {
                        validDateCheckIn = true;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
                }
            }

        LocalDate checkOut = null;
        boolean validDateCheckOut = false;

        while (!validDateCheckOut) {
            System.out.print("Enter check-in date (yyyy-MM-dd): ");
            String dateInput = scanner.next();

            try {
                checkOut = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);

                if (checkOut.isBefore(LocalDate.now()) || checkOut.isBefore(checkIn)) {
                    System.out.println("Check-out date cannot be in the past or before Check-in date. Please enter a valid date.");
                } else {
                    validDateCheckOut = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }

        Date checkInDate = sdf.parse(String.valueOf(checkIn));
        Date checkOutDate = sdf.parse(String.valueOf(checkOut));
        Collection<IRoom> availableRooms = hotelResource.findARooms(checkInDate, checkOutDate);
        String email = "";

        if (availableRooms.isEmpty()) {
            Collection<IRoom> alternativeRooms = hotelResource.findRecommendedRooms(checkInDate, checkOutDate);
            if (alternativeRooms.isEmpty()) {
                System.out.println("unfortunately there are no available rooms for this date");
                mainMenuSelection();
            } else {
                System.out.println("We don't have a room available for the date you selected, please see new check in date and check out date below:");
                System.out.println("Check in date: " + hotelResource.addDays(checkInDate));
                System.out.println("Check out date: " + hotelResource.addDays(checkOutDate));

                for (IRoom alternativeRoom : alternativeRooms) {
                    System.out.println(alternativeRoom);
                }

                System.out.println("Please select the room number you require");
                String roomNumber = scanner.next();
                while(!preventDoubleBooking(roomNumber, alternativeRooms)) {
                    System.out.println("Here are a list of available rooms (please book one of these rooms):");
                    for (IRoom availableRoom : availableRooms) {
                        System.out.println(availableRoom);
                    }
                    roomNumber = scanner.next();
                }

                System.out.println("please enter your email");
                email = scanner.next();
                getCustomerDetails(email);

                HotelResource.bookARoom(email, hotelResource.getRoom(roomNumber), hotelResource.addDays(checkInDate), hotelResource.addDays(checkOutDate));
                availableRooms.remove(hotelResource.getRoom(roomNumber));
                mainMenuSelection();
            }
        } else {
            System.out.println("Here are a list of available rooms:");
            for (IRoom availableRoom : availableRooms) {
                System.out.println(availableRoom);
            }
            System.out.println("Please select the room number you require");
            String roomNumber = scanner.next();
            while(!preventDoubleBooking(roomNumber, availableRooms)) {
                System.out.println("Here are a list of available rooms (please book one of these rooms):");
                for (IRoom availableRoom : availableRooms) {
                    System.out.println(availableRoom);
                }
                roomNumber = scanner.next();
            }
            System.out.println("please enter your email");
            email = scanner.next();
            getCustomerDetails(email);
            HotelResource.bookARoom(email, hotelResource.getRoom(roomNumber), checkInDate, checkOutDate);
            mainMenuSelection();
        }
    }

    public static void main(String[] args) throws ParseException {
        mainMenuSelection();
    }

    public static void getCustomerDetails(String email) throws ParseException {
        if (HotelResource.getCustomer(email) == null || email == null || email.isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            String firstName = "";
            String lastName = "";
            System.out.println("Please provide your first name");
            firstName = scanner.next();
            System.out.println("Please provide your last name");
            lastName = scanner.next();
            HotelResource.createACustomer(email, firstName, lastName);
        }
    }

    public static boolean preventDoubleBooking(String roomNumber, Collection<IRoom> availableRooms) {
        if (!availableRooms.toString().contains("Room Number:" + roomNumber + ",")) {
            System.out.println("Room " + roomNumber + " does not exist or is already booked.");
            return false;
        }
        return true;
    }
}
