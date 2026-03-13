package cedarwood;

public class Yurt extends Accommodation {
    public Yurt(int accommodationNumber, AreaType area) {
        // id, type, area, price (£110), max capacity (2 guests)
        super(accommodationNumber, AccommodationType.Yurt, area, 110.0, 2);
    }
}