//our code

package cedarwood;

import java.time.LocalDate;

public class Booking {
    // Static counter to generate unique booking IDs automatically
    private static int idCounter = 1;

    private final int bookingId;
    private final Guest guest;
    private Accommodation accommodation;
    private final LocalDate checkInDate;
    private final int numberOfNights;
    private final boolean breakfastRequired;
    private final int guestCount;
    private double totalCost;

     // Additional field used to track whether the booking is currently active
    private boolean isActive;
      private boolean checkedIn;
      
    private LocalDate actualCheckInDate;
private LocalDate actualCheckOutDate;

    /**
     * Constructor to create a new booking.
     * It assigns a unique ID and triggers the cost calculation.
     */
    public Booking(Guest guest, Accommodation accommodation, LocalDate checkInDate, int numberOfNights, boolean breakfastRequired, int guestCount) {
        
        if (guest == null) {
    throw new IllegalArgumentException("Guest cannot be null.");
}
if (accommodation == null) {
    throw new IllegalArgumentException("Accommodation cannot be null.");
}
if (checkInDate == null) {
    throw new IllegalArgumentException("Check-in date cannot be null.");
}
if (numberOfNights <= 0) {
    throw new IllegalArgumentException("Number of nights must be greater than 0.");
}
if (guestCount <= 0) {
    throw new IllegalArgumentException("Guest count must be greater than 0.");
}
if (!accommodation.validateCapacity(guestCount)) {
    throw new IllegalArgumentException("Guest count exceeds accommodation capacity.");
}

        this.bookingId = idCounter++;
        this.guest = guest;
        this.accommodation = accommodation;
        this.checkInDate = checkInDate;
        this.numberOfNights = numberOfNights;
        this.breakfastRequired = breakfastRequired;
        this.guestCount = guestCount;

        // A newly created booking is active by default until the guest checks out
        this.isActive = true;
        this.checkedIn = false;
        this.actualCheckInDate = null;
this.actualCheckOutDate = null;

        // Calculate the cost immediately when booking is created
        calculateTotalCost();
    }

    /**
     * Method to calculate total cost based on room price and breakfast.
     * Note: Breakfast is calculated per guest per night.
     */
    private void calculateTotalCost() {
        // Basic room cost: Price per night * total nights
        double roomCost = accommodation.getPricePerNight() * numberOfNights;

        double breakfastCost = 0;
        if (breakfastRequired) {
            // Charging £10 per guest per night for breakfast
            breakfastCost = 10.0 * guestCount * numberOfNights;
        }

        // Summing up for the final total cost
        this.totalCost = roomCost + breakfastCost;
    }

    // Methods used to manage booking status

    public boolean isActive() {
        return isActive;
    }

    public void completeBooking() {
        this.isActive = false; // The booking becomes inactive after check-out
    }
    
    public boolean isCheckedIn() {
    return checkedIn;
}

public void setCheckedIn(boolean checkedIn) {
    this.checkedIn = checkedIn;
}

public LocalDate getActualCheckInDate() {
    return actualCheckInDate;
}

public void setActualCheckInDate(LocalDate actualCheckInDate) {
    this.actualCheckInDate = actualCheckInDate;
}

public LocalDate getActualCheckOutDate() {
    return actualCheckOutDate;
}

public void setActualCheckOutDate(LocalDate actualCheckOutDate) {
    if (actualCheckOutDate != null) {
        if (actualCheckInDate == null) {
            throw new IllegalStateException(
                    "Cannot set actual check-out date before actual check-in date."
            );
        }

        if (actualCheckOutDate.isBefore(actualCheckInDate)) {
            throw new IllegalArgumentException(
                    "Actual check-out date cannot be before actual check-in date."
            );
        }
    }

    this.actualCheckOutDate = actualCheckOutDate;
}

    // Standard Getters for accessing booking information

    public int getBookingId() {
        return bookingId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }
    public void setAccommodation(Accommodation accommodation) {
    if (accommodation == null) {
        throw new IllegalArgumentException("Accommodation cannot be null.");
    }

    if (!accommodation.validateCapacity(guestCount)) {
        throw new IllegalArgumentException(
                "Accommodation capacity is too small for " + guestCount + " guest(s)."
        );
    }

    this.accommodation = accommodation;
    calculateTotalCost();
}

    public LocalDate getCheckInDate() {
       return checkInDate;
}
    public LocalDate getCheckOutDate() {
        return checkInDate.plusDays(numberOfNights);
    }
    public int getNumberOfNights() {
        return numberOfNights;
    }
    public double getTotalCost() {
        return totalCost;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public boolean isBreakfastRequired() {
        return breakfastRequired;
    }


    
    public static void resetIdCounter() {
    idCounter = 1;
}
    
    
  
}