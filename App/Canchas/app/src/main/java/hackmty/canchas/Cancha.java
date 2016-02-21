package hackmty.canchas;

import com.google.android.gms.maps.model.LatLng;

/**
 * baumann
 * 2/20/16
 */
public class Cancha {

    private String id;
    private String name;

    private String lat;
    private String lng;

    private double dist;

    public Cancha(String id, String name, String lng, String lat) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getId() {
        return id;
    }

    public LatLng getLatLng(){
        return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public double getDist() {
        return dist;
    }
}
