


package cedarwood;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CedarWoodSystem {

    // Singleton instance of the system controller
    private static CedarWoodSystem instance;
// Main data collections used by the system
    private List<Accommodation> accommodations;
    private List<Booking> bookings;
    private List<Guest> guests;
// Private constructor to enforce the Singleton pattern
    private CedarWoodSystem() {
        this.accommodations = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.guests = new ArrayList<>();

        initializeSystem();
    }
// Returns the single shared instance of the system
    public static CedarWoodSystem getInstance() {
        if (instance == null) {
            instance = new CedarWoodSystem();
        }
        return instance;
    }
// Creates the default set of 16 accommodations across the four site areas
    private void initializeSystem() {
        accommodations.add(new Cabin(1, AreaType.Hilltop));
        accommodations.add(new Cabin(2, AreaType.Hilltop));
        accommodations.add(new Cabin(3, AreaType.Hilltop));
        accommodations.add(new Cabin(4, AreaType.Hilltop));

        accommodations.add(new Yurt(5, AreaType.Meadow));
        accommodations.add(new Yurt(6, AreaType.Meadow));
        accommodations.add(new Yurt(7, AreaType.Meadow));
        accommodations.add(new Yurt(8, AreaType.Meadow));

        accommodations.add(new GeodesicDome(9, AreaType.Woodland));
        accommodations.add(new GeodesicDome(10, AreaType.Woodland));
        accommodations.add(new GeodesicDome(11, AreaType.Woodland));
        accommodations.add(new GeodesicDome(12, AreaType.Woodland));

        accommodations.add(new Airstream(13, AreaType.LakeView));
        accommodations.add(new Airstream(14, AreaType.LakeView));
        accommodations.add(new Airstream(15, AreaType.LakeView));
        accommodations.add(new Airstream(16, AreaType.LakeView));
    }
// Returns the full list of accommodations in the system
    public List<Accommodation> getAllAccommodations() {
        return accommodations;
    }
// Returns only the accommodations that belong to the selected area
    public List<Accommodation> getAccommodationsByArea(AreaType area) {
        List<Accommodation> filteredList = new ArrayList<>();
        for (Accommodation a : accommodations) {
            if (a.getArea() == area) {
                filteredList.add(a);
            }
        }
        return filteredList;
    }
// Finds an accommodation using its unique room number
    private Accommodation getAccommodationByNumber(int roomNumber) {
        for (Accommodation a : accommodations) {
            if (a.getAccommodationNumber() == roomNumber) {
                return a;
            }
        }
        return null;
    }
// Returns the active booking for a room on a given date, if one exists
    public Booking getBookingForDate(int roomNumber, LocalDate date) {
        for (Booking b : bookings) {
            if (b.getAccommodation().getAccommodationNumber() == roomNumber && b.isActive()) {
                LocalDate cIn = b.getCheckInDate();
                LocalDate cOut = cIn.plusDays(b.getNumberOfNights());

                if (!date.isBefore(cIn) && date.isBefore(cOut)) {
                    return b;
                }
            }
        }
        return null;
    }
// Counts how many accommodations in the selected area currently require cleaning
    public int getCleaningCount(AreaType area) {
        int count = 0;
        for (Accommodation acc : getAccommodationsByArea(area)) {
            if (acc.getCleaningStatus() == CleaningStatus.Dirty) {
                count++;
            }
        }
        return count;
    }
// Counts the total number of breakfasts required in the selected area for the given date
    public int getBreakfastCount(AreaType area, LocalDate targetDate) {
        int count = 0;
        for (Booking b : bookings) {
            if (b.getAccommodation().getArea() == area && b.isBreakfastRequired() && b.isActive()) {
                LocalDate checkIn = b.getCheckInDate();
                LocalDate checkOut = checkIn.plusDays(b.getNumberOfNights());

                if (!targetDate.isBefore(checkIn) && targetDate.isBefore(checkOut)) {
                    count += b.getGuestCount();
                }
            }
        }
        return count;
    }

    
// Helper methods used by the GUI to display date-based room information
    public String getOccupancyStringForDate(int roomNumber, LocalDate targetDate) {
        Booking b = getBookingForDate(roomNumber, targetDate);
        return (b != null) ? "Occupied" : "Unoccupied";
    }

    public String getAvailabilityStringForDate(int roomNumber, LocalDate targetDate) {
        Booking b = getBookingForDate(roomNumber, targetDate);

        if (targetDate.equals(LocalDate.now())) {
            Accommodation acc = getAccommodationByNumber(roomNumber);
            boolean isClean = (acc != null && acc.getCleaningStatus() == CleaningStatus.Clean);
            return (b == null && isClean) ? "Available" : "Unavailable";
        } else {
            return (b == null) ? "Available" : "Unavailable";
        }
    }

    public String getCleaningStatusStringForDate(int roomNumber, LocalDate targetDate) {
        if (targetDate.equals(LocalDate.now())) {
            Accommodation acc = getAccommodationByNumber(roomNumber);
            return (acc != null) ? acc.getCleaningStatus().toString() : "N/A";
        } else {
            return "N/A";
        }
    }

    public String getGuestCountStringForDate(int roomNumber, LocalDate targetDate) {
        Booking b = getBookingForDate(roomNumber, targetDate);
        return (b != null) ? String.valueOf(b.getGuestCount()) : "0";
    }

    public String getBreakfastStringForDate(int roomNumber, LocalDate targetDate) {
        Booking b = getBookingForDate(roomNumber, targetDate);
        return (b != null && b.isBreakfastRequired()) ? "Yes" : "No";
    }
    
    
// Updates the cleaning status of a room while enforcing valid status rules
    public String updateCleaningStatus(int roomNumber, CleaningStatus newStatus) {
        Accommodation selected = getAccommodationByNumber(roomNumber);

        if (selected == null) return "ERROR: Room not found.";

        CleaningStatus currentStatus = selected.getCleaningStatus();
        if (currentStatus == newStatus) return "SUCCESS: No status change needed.";

        if (currentStatus == CleaningStatus.Maintenance && newStatus == CleaningStatus.Clean) {
            return "ERROR: Maintenance must be set to 'Dirty' first for thorough cleaning.";
        }

        selected.updateCleaningStatus(newStatus);
        return "SUCCESS! Status updated to " + newStatus + ".";
    }
// Validates input and creates a new booking for the selected accommodation
    public String checkInGuest(int roomNumber, String fName, String lName, String phone, String guestsStr, String nightsStr, boolean breakfast, LocalDate checkInDate) {
        if (fName == null || fName.trim().isEmpty() || lName == null || lName.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            return "ERROR: First Name, Last Name, and Telephone cannot be empty.";
        }
        if (checkInDate == null) return "ERROR: Please select a valid Check-In Date.";
        if (checkInDate.isBefore(LocalDate.now())) {
    return "ERROR: Check-in date cannot be in the past.";
}
        int numGuests;
        int nights;
        try {
            numGuests = Integer.parseInt(guestsStr.trim());
            nights = Integer.parseInt(nightsStr.trim());
        } catch (NumberFormatException e) {
            return "ERROR: Please enter valid numbers for Guests and Nights.";
        }

        if (numGuests <= 0 || nights <= 0) return "ERROR: Guests and Nights must be at least 1.";

        Accommodation selected = getAccommodationByNumber(roomNumber);

        if (selected == null) return "ERROR: Room not found.";
        
        // Only today’s booking is blocked by the room’s current dirty/maintenance state.
        if (checkInDate.equals(LocalDate.now()) &&
    (selected.getCleaningStatus() == CleaningStatus.Dirty || selected.getCleaningStatus() == CleaningStatus.Maintenance)) {
    return "ERROR: Room is Dirty or in Maintenance!";
}
        if (!selected.validateCapacity(numGuests)) {
            return "ERROR: Exceeds maximum capacity of " + selected.getMaxOccupancy() + " guests.";
        }

        LocalDate newCheckIn = checkInDate;
        LocalDate newCheckOut = newCheckIn.plusDays(nights);

        for (Booking b : bookings) {
            if (b.getAccommodation().getAccommodationNumber() == roomNumber && b.isActive()) {
                LocalDate existIn = b.getCheckInDate();
                LocalDate existOut = existIn.plusDays(b.getNumberOfNights());

                if (newCheckIn.isBefore(existOut) && newCheckOut.isAfter(existIn)) {
                    return "ERROR: Room is already booked from " + existIn + " to " + existOut + ".";
                }
            }
        }

        Guest newGuest = new Guest(fName.trim(), lName.trim(), phone.trim());
        Booking newBooking = new Booking(newGuest, selected, checkInDate, nights, breakfast, numGuests);

        bookings.add(newBooking);
        guests.add(newGuest); 
// This makes the room become occupied only when the booking starts today.
        if (checkInDate.equals(LocalDate.now())) {
    selected.setOccupied(OccupancyStatus.Occupied);
}

        return "SUCCESS! Guest Checked In. Booking ID: " + newBooking.getBookingId() + " | Total: £" + newBooking.getTotalCost();
    }

// Completes today's active booking for the selected room and marks the room as dirty
   public String checkOutGuest(int roomNumber, LocalDate checkoutDate) {
    if (!checkoutDate.equals(LocalDate.now())) {
        return "ERROR: Check-out can only be completed for today's active stay.";
    }

    Accommodation selected = getAccommodationByNumber(roomNumber);
    if (selected == null) return "ERROR: Room not found.";

    Booking currentBooking = getBookingForDate(roomNumber, LocalDate.now());

    // Check-out is only allowed when an active booking exists for today
    if (currentBooking != null) {
        currentBooking.completeBooking();
        selected.setOccupied(OccupancyStatus.Unoccupied);
        selected.updateCleaningStatus(CleaningStatus.Dirty);
        return "SUCCESS! Guest checked out. Room marked as Dirty.";
    } else {
        // If no active booking exists for that date, return an error message
        return "ERROR: No active booking found for this room today.";
    }
}
}