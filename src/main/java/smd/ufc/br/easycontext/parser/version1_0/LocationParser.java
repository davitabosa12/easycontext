package smd.ufc.br.easycontext.parser.version1_0;

import android.util.Log;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

import smd.ufc.br.easycontext.fence.LocationRule;

import static smd.ufc.br.easycontext.parser.version1_0.JSONParser1_0.TAG;

public class LocationParser {

    public LocationRule parseLocationRule(JsonReader jsonReader) throws IOException {
        jsonReader.nextName(); // property 'method'
        String method = jsonReader.nextString();
        LocationRule rule = null;
        //get longitude, latitude and radius
        double latitude, longitude, radius;
        jsonReader.nextName(); // property 'latitude'
        latitude = jsonReader.nextDouble();
        jsonReader.nextName(); // property 'longitude'
        longitude = jsonReader.nextDouble();
        jsonReader.nextName(); // property 'latitude'
        radius = jsonReader.nextDouble();
        if(method.equals(LocationMethods.ENTERING)){
            rule = LocationRule.entering(latitude, longitude, radius);
        } else if(method.equals(LocationMethods.EXITING)){
            rule = LocationRule.exiting(latitude, longitude, radius);
        } else if(method.equals(LocationMethods.IN)){
            jsonReader.nextName(); // property 'dwellTimeMillis'
            long dwellTimeMillis = jsonReader.nextLong();
            rule = LocationRule.in(latitude, longitude, radius, dwellTimeMillis);
        } else {
            // error
            Log.e(TAG, "parseLocationRule: ERROR - NO METHOD FOUND");
        }
        return rule;


    }

    static class LocationMethods {
        public static final String ENTERING= "LOCATION.ENTERING";
        public static final String EXITING= "LOCATION.EXITING";
        public static final String IN= "LOCATION.IN";
    }
}
