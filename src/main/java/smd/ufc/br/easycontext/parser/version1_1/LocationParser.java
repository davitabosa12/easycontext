package smd.ufc.br.easycontext.parser.version1_1;

import android.util.Log;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import smd.ufc.br.easycontext.fence.AggregateRule;
import smd.ufc.br.easycontext.fence.LocationRule;
import smd.ufc.br.easycontext.fence.Rule;
import smd.ufc.br.easycontext.getters.LocationGetter;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;

import static smd.ufc.br.easycontext.parser.version1_0.JSONParser1_0.TAG;

public class LocationParser {

    public Rule parseLocationRule(JsonReader jsonReader) throws IOException {
        jsonReader.nextName(); // property 'method'
        String method = jsonReader.nextString();
        jsonReader.nextName(); //property 'fromDataset'
        boolean fromDataset = jsonReader.nextBoolean();
        if(fromDataset){
            return fromDataset(jsonReader, method);
        }
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

    private Rule fromDataset(JsonReader jsonReader, String method) throws IOException {
        jsonReader.nextName(); //property 'getter'
        String getterClass = jsonReader.nextString();

        LocationGetter getter = null;
        try {

            getter = (LocationGetter) Class.forName(getterClass).newInstance();
        } catch (InstantiationException e) {
            Log.e(TAG, "fromDataset: Class " + getterClass + " cannot be instantiated", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "fromDataset: Class " + getterClass + " is private or protected.", e);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "fromDataset: Class " + getterClass + " cannot be found", e);
        } catch (ClassCastException e){
            Log.e(TAG, "fromDataset: Class " + getterClass + " is not an instance of LocationGetter.", e);
        }
        if (getter == null) {
            return null;
        }

        List<Rule> rules = new ArrayList<>();
        for(LocationDefinition location : getter.getLocations() ){
            if(method.equals(LocationMethods.ENTERING)){
                rules.add(LocationRule.entering(location.getLatitude(), location.getLongitude(), location.getRadius()));
            } else if(method.equals(LocationMethods.EXITING)){
                rules.add(LocationRule.exiting(location.getLatitude(), location.getLongitude(), location.getRadius()));
            } else if(method.equals(LocationMethods.IN)){
                jsonReader.nextName(); // property 'dwellTimeMillis'
                long dwellTimeMillis = jsonReader.nextLong();
                rules.add(LocationRule.in(location.getLatitude(), location.getLongitude(), location.getRadius(), location.getDwellTimeMillis()));
            } else {
                // error
                Log.e(TAG, "parseLocationRule: ERROR - NO METHOD FOUND");
            }

        }
        return AggregateRule.or(rules);

    }

    static class LocationMethods {
        public static final String ENTERING= "LOCATION.ENTERING";
        public static final String EXITING= "LOCATION.EXITING";
        public static final String IN= "LOCATION.IN";
    }
}
