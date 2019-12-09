package smd.ufc.br.easycontext.persistance.entities;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;


public class LocationDefinition implements ContextDefinition {

    private Location location;

    private float latitude;

    private float longitude;

    private float radius;
    private long dwellTimeMillis;



    /**
     * Maximum distance in meters
     */

    public LocationDefinition(float latitude, float longitude, float radius){
        this.location = new Location("user-defined");
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.dwellTimeMillis = 1;
    }

    public LocationDefinition(float latitude, float longitude, float radius, long dwellTimeMillis) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.dwellTimeMillis = dwellTimeMillis;
    }

    public long getDwellTimeMillis() {
        return dwellTimeMillis;
    }

    public void setDwellTimeMillis(long dwellTimeMillis) {
        if(dwellTimeMillis > 0)
            this.dwellTimeMillis = dwellTimeMillis;
    }

    /**
     * Default constructor with invalid coordinates. "wildcard value"
     */
    public LocationDefinition(){
        this.location = new Location("user-defined");
        this.latitude = -200.0f;
        this.longitude = -200.0f;
        this.radius = Float.MIN_VALUE;
    }

    public Location getLocation() {
        if(!isLatitudeValid(latitude) || !isLongitudeValid(longitude))
            return null;

        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public LocationDefinition setLocation(Location location) {
        this.location = location;
        return this;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;

    }
    //GETTERS SETTERS

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }


    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        if(isLatitudeValid(this.latitude) || isLongitudeValid(longitude) || this.radius < 0){
            return 0.5f; //the default value for any location.
        }
        Location other = currentContext.getLocation();
        if(other == null)
            return 0;
        float distance = getLocation().distanceTo(other);
        if(distance > radius)
            return 0;
        return Maths.normalize(distance, 0, radius);
    }

    private boolean isLatitudeValid(float latitude){
        return -90 <= latitude && latitude <= 90;
    }
    private boolean isLongitudeValid(float longitude){
        return -180 <= longitude && longitude <= 180;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
