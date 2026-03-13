

package cedarwood;

import java.time.LocalDate;

public class Booking {
    // Static counter to generate unique booking IDs automatically
    private static int idCounter = 1;

    private int bookingId;
    private Guest guest;
    private Accommodation accommodation;
    private LocalDate checkInDate;
    private int numberOfNights;
    private boolean breakfastRequired;
    private int guestCount;
    private double totalCost;

     // Additional field used to track whether the booking is currently active
    private boolean isActive;

    /**
     * Constructor to create a new booking.
     * It assigns a unique ID and triggers the cost calculation.
     */
    public Booking(Guest guest, Accommodation accommodation, LocalDate checkInDate, int numberOfNights, boolean breakfastRequired, int guestCount) {
        this.bookingId = idCounter++;
        this.guest = guest;
        this.accommodation = accommodation;
        this.checkInDate = checkInDate;
        this.numberOfNights = numberOfNights;
        this.breakfastRequired = breakfastRequired;
        this.guestCount = guestCount;

        // A newly created booking is active by default until the guest checks out
        this.isActive = true;

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

    public LocalDate getCheckInDate() {
        return checkInDate;
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


    public int getNumberOfNights() {
        return numberOfNights;
    }
}