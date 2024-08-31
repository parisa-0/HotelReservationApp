package service;

import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    List<Customer> customers = new ArrayList<>();

    public void addCustomer(String email, String firstName, String lastName) {
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

   public Collection<Customer> getAllCustomers() {
        return customers;
   }
}
