package smd.ufc.br.easycontext;

import android.location.Location;

import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;

/**
 * Created by davitabosa on 08/08/2018.
 */

class JSONSnapshotSerializer extends SnapshotSerializer {
    Gson gson;
    JSONSnapshotSerializer(){
        gson = new Gson();
    }

    @Override
    public String serialize(Places places) {
        return places.toString();
    }

    @Override
    public String serialize(Weather weather) {
        return gson.toJson(weather);
    }

    @Override
    public String serialize(DetectedActivity detectedActivity) {
        return gson.toJson(detectedActivity);
    }

    @Override
    public String serialize(TimeIntervals timeIntervals) {
        return gson.toJson(timeIntervals);
    }

    @Override
    public String serialize(HeadphoneState headphoneState) {
        return gson.toJson(headphoneState);
    }

    @Override
    public String serialize(Location location) {
        return gson.toJson(location);
    }
}
