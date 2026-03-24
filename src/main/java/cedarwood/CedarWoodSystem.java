
//our code


package cedarwood;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

public class CedarWoodSystem {

    // Singleton instance of the system controller
    private static CedarWoodSystem instance;
// Main data collections used by the system
    private List<Accommodation> accommodations;
    private List<Booking> bookings;
    private List<Guest> guests;
    private Map<Integer, TreeMap<LocalDate, CleaningStatus>> cleaningHistory;
// Private constructor to enforce the Singleton pattern
    private CedarWoodSystem() {
    this.accommodations = new ArrayList<>();
    this.bookings = new ArrayList<>();
    this.guests = new ArrayList<>();
    this.cleaningHistory = new HashMap<>();
    

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
        
        for (Accommodation acc : accommodations) {
    TreeMap<LocalDate, CleaningStatus> roomHistory = new TreeMap<>();
    roomHistory.put(LocalDate.now(), acc.getCleaningStatus());
    cleaningHistory.put(acc.getAccommodationNumber(), roomHistory);
}
    }
// Returns the full list of accommodations in the system
    public List<Accommodation> getAllAccommodations() {
        return new ArrayList<>(accommodations);
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
    if (date == null) {
        date = LocalDate.now();
    }

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber && b.isActive()) {
            LocalDate cIn = b.getCheckInDate();
LocalDate cOut = b.getCheckOutDate();

            if (!date.isBefore(cIn) && date.isBefore(cOut)) {
                return b;
            }
        }
    }
    return null;
}
    
public Booking getBookingForDisplayDate(int roomNumber, LocalDate date) {
    if (date == null) {
        date = LocalDate.now();
    }

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() != roomNumber || !b.isActive()) {
            continue;
        }

        LocalDate cIn = b.getCheckInDate();
        LocalDate cOut = b.getCheckOutDate();

        // Normal selected-date range: [check-in, check-out)
        if (!date.isBefore(cIn) && date.isBefore(cOut)) {
            return b;
        }

        // Extra rule for the leaving day:
        // still show the stay only if the guest is truly still in-house
        if (date.equals(cOut)
                && b.isCheckedIn()
                && b.getActualCheckOutDate() == null) {
            return b;
        }
    }

    return null;
}
 public Booking getHistoricalBookingForDate(int roomNumber, LocalDate date) {
    if (date == null) {
        return null;
    }

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber) {
            LocalDate actualIn = b.getActualCheckInDate();
            LocalDate actualOut = b.getActualCheckOutDate();

            if (actualIn == null) {
                continue;
            }

            // Guest has checked in, but not yet checked out
            if (actualOut == null) {
                if (!date.isBefore(actualIn) && !date.isAfter(LocalDate.now())) {
                    return b;
                }
            } else {
                // Historical occupied range is [actualCheckInDate, actualCheckOutDate)
                if (!date.isBefore(actualIn) && date.isBefore(actualOut)) {
                    return b;
                }
            }
        }
    }
    return null;
}

public String getHistoricalOccupancyStringForDate(int roomNumber, LocalDate date) {
    Booking b = getHistoricalBookingForDate(roomNumber, date);
    return (b != null) ? "Occupied" : "Unoccupied";
}

public String getHistoricalGuestCountStringForDate(int roomNumber, LocalDate date) {
    Booking b = getHistoricalBookingForDate(roomNumber, date);
    return (b != null) ? String.valueOf(b.getGuestCount()) : "0";
}

public String getHistoricalBreakfastStringForDate(int roomNumber, LocalDate date) {
    Booking b = getHistoricalBookingForDate(roomNumber, date);
    return (b != null && b.isBreakfastRequired()) ? "Yes" : "No";
}

public int getHistoricalBreakfastCount(AreaType area, LocalDate date) {
    if (date == null) {
        return 0;
    }

    int count = 0;
    for (Booking b : bookings) {
        if (b.getAccommodation().getArea() == area) {
            LocalDate actualIn = b.getActualCheckInDate();
            LocalDate actualOut = b.getActualCheckOutDate();

            if (actualIn == null) {
                continue;
            }

            boolean inHistoricalRange;

            if (actualOut == null) {
                inHistoricalRange = !date.isBefore(actualIn) && !date.isAfter(LocalDate.now());
            } else {
                inHistoricalRange = !date.isBefore(actualIn) && date.isBefore(actualOut);
            }

            if (inHistoricalRange && b.isBreakfastRequired()) {
                count += b.getGuestCount();
            }
        }
    }

    return count;
}

public String getHistoricalCleaningStatusStringForDate(int roomNumber, LocalDate date) {
    return getCleaningStatusStringForDate(roomNumber, date);
}

public int getHistoricalCleaningCount(AreaType area, LocalDate date) {
    return getCleaningCount(area, date);
}
 
    private boolean hasCheckoutOnDate(int roomNumber, LocalDate targetDate) {
    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber) {
           LocalDate checkoutDate = b.getCheckOutDate();
            if (checkoutDate.equals(targetDate)) {
                return true;
            }
        }
    }
    return false;
}

private CleaningStatus getDerivedCleaningStatusForDate(int roomNumber, LocalDate targetDate) {
    Accommodation acc = getAccommodationByNumber(roomNumber);
    if (acc == null) {
        return null;
    }

    if (targetDate == null) {
        targetDate = LocalDate.now();
    }

    TreeMap<LocalDate, CleaningStatus> roomHistory = cleaningHistory.get(roomNumber);

    if (roomHistory != null) {
        Map.Entry<LocalDate, CleaningStatus> entry = roomHistory.floorEntry(targetDate);
        if (entry != null) {
            return entry.getValue();
        }
    }

    return acc.getCleaningStatus();
}
    
// Counts how many accommodations in the selected area currently require cleaning
   public int getCleaningCount(AreaType area, LocalDate targetDate) {
    int count = 0;
    for (Accommodation acc : getAccommodationsByArea(area)) {
        CleaningStatus status = getDerivedCleaningStatusForDate(
                acc.getAccommodationNumber(), targetDate
        );
        if (status == CleaningStatus.Dirty) {
            count++;
        }
    }
    return count;
} 
// Counts the total number of breakfasts required in the selected area for the given date
    public int getBreakfastCount(AreaType area, LocalDate targetDate) {
    if (targetDate == null) {
        targetDate = LocalDate.now();
    }

    int count = 0;
    for (Booking b : bookings) {
        if (b.getAccommodation().getArea() == area
                && b.isActive()
                && b.isBreakfastRequired()) {

            LocalDate checkIn = b.getCheckInDate();
            LocalDate checkOut = b.getCheckOutDate();

            if (!targetDate.isBefore(checkIn) && targetDate.isBefore(checkOut)) {
                count += b.getGuestCount();
            }
        }
    }
    return count;
}

    
// Helper methods used by the GUI to display date-based room information
    public String getOccupancyStringForDate(int roomNumber, LocalDate targetDate) {
    Booking b = getBookingForDisplayDate(roomNumber, targetDate);
    return (b != null) ? "Occupied" : "Unoccupied";
}
    public boolean isRoomOccupiedOnDate(int roomNumber, LocalDate targetDate) {
    Booking b = getBookingForDisplayDate(roomNumber, targetDate);
    return b != null && b.isCheckedIn();
}

   public String getAvailabilityStringForDate(int roomNumber, LocalDate targetDate) {
    Booking b = getBookingForDisplayDate(roomNumber, targetDate);
    CleaningStatus status = getDerivedCleaningStatusForDate(roomNumber, targetDate);

    return (b == null && status == CleaningStatus.Clean) ? "Available" : "Unavailable";
}

    public String getCleaningStatusStringForDate(int roomNumber, LocalDate targetDate) {
    CleaningStatus status = getDerivedCleaningStatusForDate(roomNumber, targetDate);
    return (status != null) ? status.toString() : "N/A";
}

   public String getGuestCountStringForDate(int roomNumber, LocalDate targetDate) {
    Booking b = getBookingForDisplayDate(roomNumber, targetDate);
    return (b != null && b.isActive()) ? String.valueOf(b.getGuestCount()) : "0";
}
   
    public CleaningStatus getCleaningStatusForDate(int roomNumber, LocalDate targetDate) {
    return getDerivedCleaningStatusForDate(roomNumber, targetDate);
}
    private CleaningStatus getCurrentCleaningStatus(int roomNumber) {
    return getDerivedCleaningStatusForDate(roomNumber, LocalDate.now());
}

   public String getBreakfastStringForDate(int roomNumber, LocalDate targetDate) {
    Booking b = getBookingForDisplayDate(roomNumber, targetDate);
    return (b != null && b.isActive() && b.isBreakfastRequired()) ? "Yes" : "No";
}
    
    private void recordCleaningStatus(int roomNumber, LocalDate date, CleaningStatus status) {
    if (date == null) {
        date = LocalDate.now();
    }
    
    

    TreeMap<LocalDate, CleaningStatus> roomHistory = cleaningHistory.get(roomNumber);
    if (roomHistory == null) {
        roomHistory = new TreeMap<>();
        cleaningHistory.put(roomNumber, roomHistory);
    }

    roomHistory.put(date, status);
}
     private boolean hasCleaningRecordOnDate(int roomNumber, LocalDate targetDate) {
    if (targetDate == null) {
        return false;
    }

    TreeMap<LocalDate, CleaningStatus> roomHistory = cleaningHistory.get(roomNumber);
    return roomHistory != null && roomHistory.containsKey(targetDate);
}
    
  
    
    
// Updates the cleaning status of a room while enforcing valid status rules
   public String updateCleaningStatus(int roomNumber, CleaningStatus newStatus, LocalDate statusDate) {
    Accommodation selected = getAccommodationByNumber(roomNumber);

    if (selected == null) return "ERROR: Room not found.";
    if (newStatus == null) return "ERROR: Invalid cleaning status.";
    if (statusDate == null) statusDate = LocalDate.now();

    Booking bookingOnThatDate = getBookingForDisplayDate(roomNumber, statusDate);
if (bookingOnThatDate != null && bookingOnThatDate.isCheckedIn()) {
    return "ERROR: Cannot change cleaning status for a date when the room is occupied.";
}

    CleaningStatus currentStatus = getDerivedCleaningStatusForDate(roomNumber, statusDate);
    if (currentStatus == newStatus) return "SUCCESS: No status change needed.";

    if (currentStatus == CleaningStatus.Maintenance && newStatus == CleaningStatus.Clean) {
        return "ERROR: Maintenance must be set to 'Dirty' first for thorough cleaning.";
    }

    recordCleaningStatus(roomNumber, statusDate, newStatus);

    if (statusDate.equals(LocalDate.now())) {
        selected.updateCleaningStatus(newStatus);
    }

    return "SUCCESS! Status updated to " + newStatus + ".";
}
// Validates input and creates a new booking for the selected accommodation
   public String createBooking(int roomNumber, String fName, String lName, String phone,
                            String guestsStr, String nightsStr, boolean breakfast,
                            LocalDate checkInDate) {

    if (fName == null || fName.trim().isEmpty() ||
        lName == null || lName.trim().isEmpty() ||
        phone == null || phone.trim().isEmpty()) {
        return "ERROR: First Name, Last Name, and Telephone cannot be empty.";
    }

    if (checkInDate == null) {
        return "ERROR: Please select a valid Check-In Date.";
    }

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

    if (numGuests <= 0 || nights <= 0) {
        return "ERROR: Guests and Nights must be at least 1.";
    }

    Accommodation selected = getAccommodationByNumber(roomNumber);
    if (selected == null) {
        return "ERROR: Room not found.";
    }

    if (!selected.validateCapacity(numGuests)) {
        return "ERROR: Exceeds maximum capacity of " + selected.getMaxOccupancy() + " guests.";
    }

    LocalDate newCheckIn = checkInDate;
    LocalDate newCheckOut = newCheckIn.plusDays(nights);

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber && b.isActive()) {
            LocalDate existIn = b.getCheckInDate();
LocalDate existOut = b.getCheckOutDate();

            if (newCheckIn.isBefore(existOut) && newCheckOut.isAfter(existIn)) {
                return "ERROR: Room is already booked from " + existIn + " to " + existOut + ".";
            }
        }
    }

    // For same-day booking, make sure the room is actually available now
    // Validate the room's cleaning state on the requested check-in date
CleaningStatus checkInStatus = getCleaningStatusForDate(roomNumber, checkInDate);

if (checkInStatus == CleaningStatus.Dirty ||
    checkInStatus == CleaningStatus.Maintenance) {
    return "ERROR: Room is not available on the selected check-in date because it is "
            + checkInStatus + ".";
}

// For same-day check-in, also make sure the room is not currently occupied now
if (checkInDate.equals(LocalDate.now())) {
    if (selected.getOccupancyStatus() == OccupancyStatus.Occupied) {
        return "ERROR: Room is already occupied.";
    }
}

    Guest newGuest = new Guest(fName.trim(), lName.trim(), phone.trim());
    Booking newBooking = new Booking(newGuest, selected, checkInDate, nights, breakfast, numGuests);

    bookings.add(newBooking);
guests.add(newGuest);



return "SUCCESS! Booking created. Booking ID: " + newBooking.getBookingId()
        + " | Total: £" + newBooking.getTotalCost();
}
   
   
   
   public String checkInGuest(int roomNumber, LocalDate checkInDate) {
    if (checkInDate == null) {
        return "ERROR: Please select a valid Check-In Date.";
    }

    if (!checkInDate.equals(LocalDate.now())) {
        return "ERROR: Guest can only be checked in on today's date.";
    }

    Accommodation selected = getAccommodationByNumber(roomNumber);
    if (selected == null) {
        return "ERROR: Room not found.";
    }

    CleaningStatus todayStatus = getCurrentCleaningStatus(roomNumber);

if (todayStatus == CleaningStatus.Dirty ||
    todayStatus == CleaningStatus.Maintenance) {
    return "ERROR: Room is Dirty or in Maintenance!";
}

    if (selected.getOccupancyStatus() == OccupancyStatus.Occupied) {
        return "ERROR: Room is already occupied.";
    }

    Booking todayBooking = getBookingForDate(roomNumber, checkInDate);
if (todayBooking == null) {
    return "ERROR: No booking found for this room today.";
}

todayBooking.setCheckedIn(true);
todayBooking.setActualCheckInDate(checkInDate);
selected.setOccupied(OccupancyStatus.Occupied);

return "SUCCESS! Guest checked in. Booking ID: " + todayBooking.getBookingId()
        + " | Total: £" + todayBooking.getTotalCost();
   }
   
// Completes today's active booking for the selected room and marks the room as dirty
  public String checkOutGuest(int roomNumber, LocalDate checkoutDate) {
    if (checkoutDate == null) {
        return "ERROR: Please select a valid check-out date.";
    }

    if (!checkoutDate.equals(LocalDate.now())) {
        return "ERROR: Guests can only be checked out on today's date.";
    }

    Accommodation selected = getAccommodationByNumber(roomNumber);
    if (selected == null) {
        return "ERROR: Room not found.";
    }

    if (selected.getOccupancyStatus() != OccupancyStatus.Occupied) {
        return "ERROR: Room is not currently occupied.";
    }

    Booking currentBooking = null;

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber
                && b.isActive()
                && b.isCheckedIn()
                && b.getCheckOutDate().equals(checkoutDate)) {
            currentBooking = b;
            break;
        }
    }

    if (currentBooking == null) {
        return "ERROR: No checked-in guest found checking out today.";
    }

    currentBooking.setCheckedIn(false);
    currentBooking.setActualCheckOutDate(checkoutDate);
    currentBooking.completeBooking();

    selected.setOccupied(OccupancyStatus.Unoccupied);
    selected.updateCleaningStatus(CleaningStatus.Dirty);
    recordCleaningStatus(roomNumber, checkoutDate, CleaningStatus.Dirty);

    return "SUCCESS! Guest checked out. Room marked as Dirty.";
}
   public String undoTodayCheckIn(int roomNumber) {
    Accommodation selected = getAccommodationByNumber(roomNumber);
    if (selected == null) {
        return "ERROR: Room not found.";
    }

    LocalDate today = LocalDate.now();
    Booking bookingToUndo = null;

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber
                && b.isActive()
                && b.getCheckInDate().equals(today)) {
            bookingToUndo = b;
            break;
        }
    }

    if (bookingToUndo == null) {
        return "ERROR: No active check-in found for today.";
    }

    if (selected.getOccupancyStatus() != OccupancyStatus.Occupied) {
        return "ERROR: Room is not currently checked in.";
    }
bookingToUndo.setCheckedIn(false);
bookingToUndo.setActualCheckInDate(null);

selected.setOccupied(OccupancyStatus.Unoccupied);
selected.updateCleaningStatus(CleaningStatus.Clean);
recordCleaningStatus(roomNumber, today, CleaningStatus.Clean);

return "SUCCESS! Today's check-in has been undone. Booking is still kept for today.";
}
   public String cancelFutureBooking(int roomNumber, LocalDate checkInDate) {
    if (checkInDate == null) {
        return "ERROR: Please select a valid booking date.";
    }

    if (!checkInDate.isAfter(LocalDate.now())) {
        return "ERROR: Only future bookings can be cancelled.";
    }

    Booking bookingToRemove = null;

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber
                && b.getCheckInDate().equals(checkInDate)
                && b.isActive()) {
            bookingToRemove = b;
            break;
        }
    }

    if (bookingToRemove == null) {
        return "ERROR: No future booking found for this room on that date.";
    }

   System.out.println("Guests before cancel = " + guests.size());

bookings.remove(bookingToRemove);
guests.remove(bookingToRemove.getGuest());

System.out.println("Guests after cancel = " + guests.size());

return "SUCCESS! Future booking cancelled.";
}
   
   public String changeAccommodation(int oldRoomNumber, int newRoomNumber, LocalDate targetDate) {
    if (targetDate == null) {
        return "ERROR: Please select a valid booking date.";
    }

    if (oldRoomNumber == newRoomNumber) {
        return "ERROR: New room must be different from the current room.";
    }

    Accommodation oldRoom = getAccommodationByNumber(oldRoomNumber);
    Accommodation newRoom = getAccommodationByNumber(newRoomNumber);

    if (oldRoom == null) {
        return "ERROR: Current room not found.";
    }

    if (newRoom == null) {
        return "ERROR: New room not found.";
    }

    Booking bookingToMove = getBookingForDate(oldRoomNumber, targetDate);

    if (bookingToMove == null) {
        return "ERROR: No active booking found for the selected room and date.";
    }

    // Clean first version:
    // only allow moving bookings that have NOT been checked in yet
    if (bookingToMove.isCheckedIn()) {
        return "ERROR: Checked-in guest transfer requires a separate transfer workflow.";
    }

    if (!newRoom.validateCapacity(bookingToMove.getGuestCount())) {
        return "ERROR: New room does not have enough capacity for "
                + bookingToMove.getGuestCount() + " guest(s).";
    }

    LocalDate bookingCheckIn = bookingToMove.getCheckInDate();
    LocalDate bookingCheckOut = bookingToMove.getCheckOutDate();

    // New room must be clean/available on the booking check-in date
    CleaningStatus newRoomStatus = getCleaningStatusForDate(newRoomNumber, bookingCheckIn);
    if (newRoomStatus == CleaningStatus.Dirty || newRoomStatus == CleaningStatus.Maintenance) {
        return "ERROR: New room is not available on the booking check-in date because it is "
                + newRoomStatus + ".";
    }

    // Check overlap against other active bookings in the new room
    for (Booking b : bookings) {
        if (b != bookingToMove
                && b.isActive()
                && b.getAccommodation().getAccommodationNumber() == newRoomNumber) {

            LocalDate existIn = b.getCheckInDate();
            LocalDate existOut = b.getCheckOutDate();

            if (bookingCheckIn.isBefore(existOut) && bookingCheckOut.isAfter(existIn)) {
                return "ERROR: New room is already booked from " + existIn + " to " + existOut + ".";
            }
        }
    }

    bookingToMove.setAccommodation(newRoom);

    return "SUCCESS! Booking moved from room " + oldRoomNumber
            + " to room " + newRoomNumber
            + ". New total: £" + bookingToMove.getTotalCost();
}
   
   
   public void resetSystem() {
    this.accommodations = new ArrayList<>();
    this.bookings = new ArrayList<>();
    this.guests = new ArrayList<>();
    this.cleaningHistory = new HashMap<>();
Booking.resetIdCounter();
    initializeSystem();
    
    
}
   //enable Check Out only if that room has an active booking checking out on the selected top date
   public boolean canCheckOutOnDate(int roomNumber, LocalDate checkoutDate) {
    if (checkoutDate == null) {
        return false;
    }

    if (!checkoutDate.equals(LocalDate.now())) {
        return false;
    }

    Accommodation selected = getAccommodationByNumber(roomNumber);
    if (selected == null || selected.getOccupancyStatus() != OccupancyStatus.Occupied) {
        return false;
    }

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber
                && b.isActive()
                && b.isCheckedIn()
                && b.getCheckOutDate().equals(checkoutDate)) {
            return true;
        }
    }

    return false;
}
   
   public boolean canChangeAccommodation(int roomNumber, LocalDate targetDate) {
    if (targetDate == null) {
        return false;
    }

    Booking booking = getBookingForDate(roomNumber, targetDate);
    if (booking == null) {
        return false;
    }

    return !booking.isCheckedIn();
}
//enable Check Out only if that room has an active booking checking out on the selected top date
public boolean canUndoTodayCheckIn(int roomNumber) {
    LocalDate today = LocalDate.now();

    Accommodation selected = getAccommodationByNumber(roomNumber);
    if (selected == null || selected.getOccupancyStatus() != OccupancyStatus.Occupied) {
        return false;
    }

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber
                && b.isActive()
                && b.getCheckInDate().equals(today)) {
            return true;
        }
    }
    return false;
}

public boolean canUseCheckInButton(int roomNumber, LocalDate checkInDate) {
    if (checkInDate == null) {
        return false;
    }

    if (checkInDate.isBefore(LocalDate.now())) {
        return false;
    }

    Accommodation selected = getAccommodationByNumber(roomNumber);
    if (selected == null) {
        return false;
    }

    CleaningStatus status = getCleaningStatusForDate(roomNumber, checkInDate);
    if (status == CleaningStatus.Dirty || status == CleaningStatus.Maintenance) {
        return false;
    }

    Booking bookingOnDate = getBookingForDate(roomNumber, checkInDate);

    // Today: allow if there is an unchecked-in booking, or if the room is not occupied yet
    if (checkInDate.equals(LocalDate.now())) {
        if (bookingOnDate != null) {
            return !bookingOnDate.isCheckedIn();
        }
        return !isRoomOccupiedOnDate(roomNumber, checkInDate);
    }

    // Future: allow only if there is no booking already on that date
    return bookingOnDate == null;
}
//enable Undo only if that room has a today booking and is currently occupied
public boolean hasFutureBooking(int roomNumber, LocalDate checkInDate) {
    if (checkInDate == null || !checkInDate.isAfter(LocalDate.now())) {
        return false;
    }

    for (Booking b : bookings) {
        if (b.getAccommodation().getAccommodationNumber() == roomNumber
                && b.isActive()
                && b.getCheckInDate().equals(checkInDate)) {
            return true;
        }
    }
    return false;
}
}