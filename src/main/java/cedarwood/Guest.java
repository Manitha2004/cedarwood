package cedarwood;

public class Guest {
    private final String firstName;
    private final String lastName;
    private final String telephone;

   public Guest(String firstName, String lastName, String telephone) {
    if (firstName == null || firstName.trim().isEmpty()) {
        throw new IllegalArgumentException("First name cannot be empty.");
    }
    if (lastName == null || lastName.trim().isEmpty()) {
        throw new IllegalArgumentException("Last name cannot be empty.");
    }
    if (telephone == null || telephone.trim().isEmpty()) {
        throw new IllegalArgumentException("Telephone cannot be empty.");
    }

    this.firstName = firstName.trim();
    this.lastName = lastName.trim();
    this.telephone = telephone.trim();
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
//our code