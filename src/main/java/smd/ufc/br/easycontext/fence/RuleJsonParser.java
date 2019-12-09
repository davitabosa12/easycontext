package smd.ufc.br.easycontext.fence;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import smd.ufc.br.easycontext.fence.method.AggregateMethod;
import smd.ufc.br.easycontext.fence.method.DAMethod;
import smd.ufc.br.easycontext.fence.method.HeadphoneMethod;
import smd.ufc.br.easycontext.fence.method.LocationMethod;
import smd.ufc.br.easycontext.fence.method.TimeMethod;

public class RuleJsonParser {

    public static Rule fromJson(String json){
        JsonObject obj = new Gson().fromJson(json, JsonObject.class);

        String type = obj.get("type").getAsString();

        if(type.equalsIgnoreCase("Location")){
            return parseLocationRule(obj);
        } else if(type.equalsIgnoreCase("DetectedActivity")){
            return parseDARule(obj);
        } else if(type.equalsIgnoreCase("TimeInterval")){
            return parseTIRule(obj);
        } else if(type.equalsIgnoreCase("Headphone")){
            return parseHeadphoneRule(obj);
        } else if(type.equalsIgnoreCase("Aggregate")){
            return parseAggregateRule(obj);
        }
        return null;
    }

    private static Rule parseAggregateRule(JsonObject obj) {
        AggregateMethod method = AggregateMethod.valueOf(obj.get("method").getAsString());
        JsonArray array = obj.getAsJsonArray("rules");
        List<Rule> rules = new ArrayList<>();
        for (JsonElement element :
                array) {
            rules.add(RuleJsonParser.fromJson(element.toString()));
        }
        switch (method){
            case AGGREGATE_NOT:
                return AggregateRule.not(rules.get(0));
            case AGGREGATE_AND:
                return AggregateRule.and(rules);
            case AGGREGATE_OR:
                return AggregateRule.or(rules);
        }
        return null;
    }

    private static Rule parseHeadphoneRule(JsonObject obj) {
        HeadphoneMethod method = HeadphoneMethod.valueOf(obj.get("method").getAsString());
        switch (method){
            case HEADPHONE_DURING:
                return HeadphoneRule.during(obj.get("headphoneState").getAsInt());
            case HEADPHONE_UNPLUGGING:
                return HeadphoneRule.unplugging();
            case HEADPHONE_PLUGGING_IN:
                return HeadphoneRule.pluggingIn();
        }
        return null;
    }

    private static Rule parseTIRule(JsonObject obj) {
        TimeMethod method = TimeMethod.valueOf(obj.get("method").getAsString());



        long startOffsetMillis = obj.get("startOffsetMillis").getAsLong();
        long stopOffsetMillis = obj.get("stopOffsetMillis").getAsLong();

        TimeZone timeZone = TimeZone.getTimeZone(obj.get("timeZone").getAsString());

        long startTimeOfDayMillis = obj.get("startTimeOfDayMillis").getAsLong();
        long stopTimeOfDayMillis = obj.get("stopTimeOfDayMillis").getAsLong();
        long startTimeMillis = obj.get("startTimeMillis").getAsLong();
        long stopTimeMillis = obj.get("stopTimeMillis").getAsLong();

        int dayOfWeek = obj.get("dayOfWeek").getAsInt();
        int timeInterval = obj.get("timeInterval").getAsInt();
        int timeInstant = obj.get("timeInstant").getAsInt();

        switch (method){
            case AROUND_TIME_INSTANT:
                return TimeRule.aroundTimeInstant(timeInstant, startOffsetMillis, stopOffsetMillis);
            case IN_INTERVAL_OF_DAY:
                return TimeRule.inIntervalOfDay(dayOfWeek, timeZone, startTimeOfDayMillis, stopTimeOfDayMillis);
            case IN_DAILY_INTERVAL:
                return TimeRule.inDailyInterval(timeZone, startTimeOfDayMillis, stopTimeOfDayMillis);
            case IN_TIME_INTERVAL:
                return TimeRule.inTimeInterval(timeInterval);
            case IN_INTERVAL:
                return TimeRule.inInterval(startTimeMillis,stopTimeMillis);
        }
        return null;
    }

    private static Rule parseDARule(JsonObject obj) {
        DAMethod method = DAMethod.valueOf(obj.get("method").getAsString());
        List<Integer> activityTypes = new ArrayList<>();
        JsonArray array = obj.getAsJsonArray("activityTypes");
        for (JsonElement element:
                array) {
            activityTypes.add(element.getAsInt());
        }
        switch (method){
            case DA_DURING:
                return DetectedActivityRule.during(activityTypes);
            case DA_STARTING:
                return DetectedActivityRule.starting(activityTypes);
            case DA_STOPPING:
                return DetectedActivityRule.stopping(activityTypes);
        }
        return null;
    }

    private static Rule parseLocationRule(JsonObject obj) {
        LocationMethod method = LocationMethod.valueOf(obj.get("method").getAsString());
        double latitude, longitude, radius;
        latitude = obj.get("latitude").getAsDouble();
        longitude = obj.get("longitude").getAsDouble();
        radius = obj.get("radius").getAsDouble();
        long dwell = obj.get("dwellTimeMillis").getAsLong();
        switch (method){
            case LOCATION_IN:
                return LocationRule.in(latitude, longitude, radius, dwell);
            case LOCATION_EXITING:
                return LocationRule.exiting(latitude, longitude, radius);
            case LOCATION_ENTERING:
                return LocationRule.entering(latitude, longitude, radius);
        }
        return null;
    }
}
