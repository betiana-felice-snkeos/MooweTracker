package tracker.moowe.de.moowe;

public class Geolocation {
    public double latitude;
    public double longitude;
    public long timestamp;
    public String mot;

    public Geolocation(double latitude, double longitude, long timestamp, String mot){
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.mot = mot;

    }
}
