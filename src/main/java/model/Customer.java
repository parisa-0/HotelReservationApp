package model;

import java.util.regex.Pattern;

public class Customer {
    String firstName;
    String lastName;
    String emailRegex = "^(.+)@(.+).(.+)$";
    Pattern pattern = Pattern.compile(emailRegex);
    String email;

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email");
        }
        else {
            this.email = email;
        }
    }


    @Override
    public String toString() {
        return "First Name:" + firstName + ", Last Name:" + lastName + ", Email: " + email;
    }
}
