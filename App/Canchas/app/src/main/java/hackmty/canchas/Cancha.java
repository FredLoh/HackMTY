package hackmty.canchas;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;

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

    private int start, end;

    JSONArray timeslots;

    public Cancha(String id, String name, String lng, String lat) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
    }

    public void setTimeslots(JSONArray arr){

        timeslots = arr;

    }

    public JSONArray getTimeslots() {
        return timeslots;
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

    public void setTimes(int s, int e){

        start = s;
        end = e;

    }

    public String getTimes(){



        String s_suff = "AM", e_suff = "AM";

        if(start >= 13){

            if(start > 12){
                s_suff = "PM";
            }

            start %= 12;


        }

        end += 1;

        if(end >= 13){

            if(start > 12){
                e_suff = "PM";
            }

            end %= 12;
        }


        return "de " + start + ":00 " + s_suff + " a " + end + ":00 " + e_suff;


    }
}
