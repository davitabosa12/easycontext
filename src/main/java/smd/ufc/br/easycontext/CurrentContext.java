package smd.ufc.br.easycontext;

import android.location.Location;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.io.Serializable;
import java.util.List;

import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

/**
 * CurrentContext is the current context that the device is at.
 */
public class CurrentContext implements Serializable {


    private Weather weather;
    private TimeIntervals timeIntervals;
    private Location location;
    private PlaceLikelihood placeLikelihood;
    private DetectedActivity mostProbableActivity;
    private List<DetectedActivity> detectedActivities;


    public List<DetectedActivity> getDetectedActivities() {
        return detectedActivities;
    }

    public CurrentContext setDetectedActivities(List<DetectedActivity> detectedActivities) {
        this.detectedActivities = detectedActivities;
        return this;
    }

    CurrentContext setWeather(Weather weather) {
        this.weather = weather;
        return this;
    }

    CurrentContext setTimeIntervals(TimeIntervals timeIntervals) {
        this.timeIntervals = timeIntervals;
        return this;
    }

    CurrentContext setLocation(Location location) {
        this.location = location;
        return this;
    }

    CurrentContext setPlaceLikelihood(PlaceLikelihood placeLikelihood) {
        this.placeLikelihood = placeLikelihood;
        return this;
    }

    CurrentContext setMostProbableActivity(DetectedActivity mostProbableActivity) {
        this.mostProbableActivity = mostProbableActivity;
        return this;
    }

    public Weather getWeather() {
        return weather;
    }

    public TimeIntervals getTimeIntervals() {
        return timeIntervals;
    }

    public Location getLocation() {
        return location;
    }

    public PlaceLikelihood getPlaceLikelihood() {
        return placeLikelihood;
    }

    public DetectedActivity getMostProbableActivity() {
        return mostProbableActivity;
    }

    CurrentContext() {
        weather = null;
        timeIntervals = null;
        location = null;
        placeLikelihood = null;
        mostProbableActivity = null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Weather: ");
        if (weather == null)
            builder.append("null");
        else
            builder.append(weather.toString());

        builder.append("\nTime Intervals: ");

        if (timeIntervals == null) {
            builder.append("null");
        } else {
            builder.append(timeIntervals.toString());
        }
        builder.append("\nLocation: ");

        if (location == null) {
            builder.append("null");
        } else {
            builder.append(location.toString());
        }
        builder.append("\nDetected Activity: ");

        if (mostProbableActivity == null) {
            builder.append("null");
        } else {
            builder.append(mostProbableActivity.toString());
        }
        builder.append("\nPlace Likelihood: ");

        if (placeLikelihood == null) {
            builder.append("null");
        } else {
            builder.append(placeLikelihood.toString());
        }
        return builder.toString();
    }


}