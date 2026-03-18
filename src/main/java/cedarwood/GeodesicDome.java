package cedarwood;

public class GeodesicDome extends Accommodation {
    public GeodesicDome(int accommodationNumber, AreaType area) {
        // id, type, area, price (£120), max capacity (2 guests)
        super(accommodationNumber, AccommodationType.GeodesicDome, area, 120.0, 2);
    }
}