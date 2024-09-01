package service;

import api.HotelResource;
import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class CustomerService {

    private static CustomerService instance = new CustomerService();

    // Step 1: Private constructor
    private CustomerService() {
        // Initialization code here
    }

    // Step 3: Public static method to get the instance
    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }

    static List<Customer> customers = new ArrayList<>();

    public static void addCustomer(String email, String firstName, String lastName) {
        customers.add(new Customer(firstName, lastName, email));
    }

   public Customer getCustomer(String customerEmail) {
        for (Customer customer : customers) {
            if(customer.toString().contains(customerEmail)) {
                return customer;
            }
        }
        return null;
   }

   public static Collection<Customer> getAllCustomers() {
        return customers;
   }
/*
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please provide your first name");
        String firstName = scanner.nextLine();
        System.out.println("Please provide your last name");
        String lastName = scanner.nextLine();
        System.out.println("Please provide your email address");
        String email = scanner.nextLine();
        HotelResource.createACustomer(email, firstName, lastName);
        System.out.println("account created!");
        addCustomer(email, firstName,lastName);
        System.out.println(customers);
        System.out.println(getAllCustomers());
    }

 */
}
