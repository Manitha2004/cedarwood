package cedarwood;

public class Airstream extends Accommodation {
    public Airstream(int accommodationNumber, AreaType area) {
        // id, type, area, price (£180), max capacity (4 guests)
        super(accommodationNumber, AccommodationType.Airstream, area, 180.0, 4);
    }
}