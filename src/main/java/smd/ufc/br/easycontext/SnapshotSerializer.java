package smd.ufc.br.easycontext;

import android.location.Location;

import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.Places;

/**
 * Created by davitabosa on 08/08/2018.
 */

public abstract class SnapshotSerializer {
    public static final String JSON = "json";
    public static final String XML = "xml";
    /**
     * Serializes only in JSON
     */
    public static SnapshotSerializer getSerializer(String type){
        return new JSONSnapshotSerializer();
    }
    private String serializationMode;

    public abstract String serialize(Places places);
    public abstract String serialize(Weather weather);
    public abstract String serialize(DetectedActivity detectedActivity);
    public abstract String serialize(TimeIntervals timeIntervals);
    public abstract String serialize(HeadphoneState headphoneState);
    public abstract String serialize(Location location);
}
