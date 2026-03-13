package cedarwood;

public class Guest {
    private String firstName;
    private String lastName;
    private String telephone;

    public Guest(String firstName, String lastName, String telephone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTelephone() {
        return telephone;
    }

   // Returns the guest's full name

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
