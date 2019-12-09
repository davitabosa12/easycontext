package smd.ufc.br.easycontext.fence;

import android.annotation.SuppressLint;

import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.gson.JsonObject;

import smd.ufc.br.easycontext.fence.method.LocationMethod;

/**
 * Created by davitabosa on 13/08/2018.
 */

public class LocationRule implements Rule{
    private LocationMethod method;

    private double latitude;
    private double longitude;
    private double radius;
    private long dwellTimeMillis;

    public LocationRule() {

    }

    public static LocationRule entering(double latitude, double longitude, double radius){
        LocationRule locationRule = new LocationRule();
        locationRule.setLatitude(latitude);
        locationRule.setLongitude(longitude);
        locationRule.setRadius(radius);
        locationRule.method = LocationMethod.LOCATION_ENTERING;
        return locationRule;
    }
    public static LocationRule exiting(double latitude, double longitude, double radius){
        LocationRule locationRule = new LocationRule();
        locationRule.setLatitude(latitude);
        locationRule.setLongitude(longitude);
        locationRule.setRadius(radius);
        locationRule.method = LocationMethod.LOCATION_EXITING;
        return locationRule;

    }
    public static LocationRule in(double latitude, double longitude, double radius, long dwell){
        LocationRule locationRule = new LocationRule();
        locationRule.setLatitude(latitude);
        locationRule.setLongitude(longitude);
        locationRule.setRadius(radius);
        locationRule.setDwellTimeMillis(dwell);
        locationRule.method = LocationMethod.LOCATION_IN;
        return locationRule;
    }

    @SuppressLint("MissingPermission")
    @Override
    public AwarenessFence getAwarenessFence() {
        switch(method){
            case LOCATION_ENTERING:
                return com.google.android.gms.awareness.fence.LocationFence.entering(latitude,longitude,radius);
            case LOCATION_EXITING:
                return com.google.android.gms.awareness.fence.LocationFence.exiting(latitude,longitude,radius);
            case LOCATION_IN:
                return com.google.android.gms.awareness.fence.LocationFence.in(latitude,longitude,radius,dwellTimeMillis);
            default:
                return null;
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    public long getDwellTimeMillis() {
        return dwellTimeMillis;
    }
    public void setDwellTimeMillis(long dwellTimeMillis) {
        this.dwellTimeMillis = dwellTimeMillis;
    }

    @Override
    public String toString() {
        JsonObject rule = new JsonObject();
        rule.addProperty("type", "Location");
        rule.addProperty("method", String.valueOf(method));

        rule.addProperty("latitude", latitude);
        rule.addProperty("longitude", longitude);
        rule.addProperty("radius", radius);
        rule.addProperty("dwellTimeMillis", dwellTimeMillis);

        return rule.toString();
    }
}
