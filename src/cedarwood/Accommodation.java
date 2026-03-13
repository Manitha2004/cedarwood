
package cedarwood;

public abstract class Accommodation {
    private int accommodationNumber;
    private AccommodationType type;
    private AreaType area;
    private double pricePerNight;
    private int maxOccupancy;
    private OccupancyStatus occupancyStatus;
    private CleaningStatus cleaningStatus;

    public Accommodation(int accommodationNumber, AccommodationType type, AreaType area, double pricePerNight, int maxOccupancy) {
        this.accommodationNumber = accommodationNumber;
        this.type = type;
        this.area = area;
        this.pricePerNight = pricePerNight;
        this.maxOccupancy = maxOccupancy;

        // Default states for a newly created accommodation
        this.occupancyStatus = OccupancyStatus.Unoccupied;
        this.cleaningStatus = CleaningStatus.Clean;
    }

    /**
     * Validates if the requested number of guests can fit in this accommodation.
     * Enforces that guests must be at least 1 and cannot exceed maximum capacity.
     */
    public boolean validateCapacity(int numGuests) {
        // This validation ensures that at least 1 guest is required and capacity is not exceeded.
        return numGuests >= 1 && numGuests <= maxOccupancy;
    }

    /**
     * Checks if the accommodation is fully available for a new check-in.
     */
    public boolean isAvailable() {
        return this.occupancyStatus == OccupancyStatus.Unoccupied && this.cleaningStatus == CleaningStatus.Clean;
    }

    /**
     * Updates the current cleaning status of the room.
     */
    public void updateCleaningStatus(CleaningStatus newStatus) {
        this.cleaningStatus = newStatus;
    }

   // Standard getters and setters

    public int getAccommodationNumber() {
        return accommodationNumber;
    }

    public AccommodationType getType() {
        return type;
    }

    public AreaType getArea() {
        return area;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public OccupancyStatus getOccupancyStatus() {
        return occupancyStatus;
    }

    public void setOccupied(OccupancyStatus status) {
        this.occupancyStatus = status;
    }

    public CleaningStatus getCleaningStatus() {
        return cleaningStatus;
    }
}