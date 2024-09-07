package service;

import api.HotelResource;
import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class CustomerService {

    private static CustomerService instance = new CustomerService();

    private CustomerService() {
    }

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

}
