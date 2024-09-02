package model;

import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    String emailRegex = "^(.+)@(.+).(.+)$";
    Pattern pattern = Pattern.compile(emailRegex);
    private final String email;

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
