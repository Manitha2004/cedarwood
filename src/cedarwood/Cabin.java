package cedarwood;

public class Cabin extends Accommodation {
    public Cabin(int accommodationNumber, AreaType area) {
        // id, type, area, price (£160), max capacity (4 guests)
        super(accommodationNumber, AccommodationType.Cabin, area, 160.0, 4);
    }
}