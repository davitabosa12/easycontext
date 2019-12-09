package smd.ufc.br.easycontext.fence;

import android.annotation.SuppressLint;

import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.TimeFence;
import com.google.gson.JsonObject;

import java.util.TimeZone;

import smd.ufc.br.easycontext.fence.method.TimeMethod;

public class TimeRule implements Rule{
    public static final String AROUND_TIME_INSTANT =  "TIME_INTERVAL.AROUND_TIME_INSTANT";
    public static final String IN_DAILY_INTERVAL =  "TIME_INTERVAL.IN_DAILY_INTERVAL";
    public static final String IN_INTERVAL =  "TIME_INTERVAL.IN_INTERVAL";
    public static final String IN_INTERVAL_OF_DAY =  "TIME_INTERVAL.IN_INTERVAL_OF_DAY";
    public static final String IN_TIME_INTERVAL =  "TIME_INTERVAL.IN_TIME_INTERVAL";
    public static final String MORNING = "TIME_INTERVAL.MORNING" ;
    public static final String AFTERNOON = "TIME_INTERVAL.AFTERNOON";
    public static final String EVENING = "TIME_INTERVAL.EVENING";
    public static final String NIGHT = "TIME_INTERVAL.NIGHT";
    public static final String WEEKDAY = "TIME_INTERVAL.WEEKDAY";
    public static final String WEEKEND = "TIME_INTERVAL.WEEKEND";
    public static final String HOLIDAY = "TIME_INTERVAL.HOLIDAY";

    private TimeMethod method;

    private int timeInstant;
    private long startOffsetMillis;
    private long stopOffsetMillis;

    private TimeZone timeZone;
    private long startTimeOfDayMillis;
    private long stopTimeOfDayMillis;
    private long startTimeMillis;
    private long stopTimeMillis;

    private int dayOfWeek;
    private int timeInterval;

    TimeRule() {

    }

    @SuppressLint("MissingPermission")
    @Override
    public AwarenessFence getAwarenessFence() {
        switch(method){
            case IN_INTERVAL:
                return TimeFence.inInterval(startTimeMillis, stopTimeMillis);
            case IN_TIME_INTERVAL:
                return TimeFence.inTimeInterval(timeInterval);
            case IN_DAILY_INTERVAL:
                return TimeFence.inDailyInterval(timeZone, startTimeOfDayMillis, stopTimeOfDayMillis);
            case IN_INTERVAL_OF_DAY:
                return TimeFence.inIntervalOfDay(dayOfWeek, timeZone, startTimeOfDayMillis, stopTimeOfDayMillis);
            case AROUND_TIME_INSTANT:
                return TimeFence.aroundTimeInstant(timeInstant, startOffsetMillis, stopOffsetMillis);
        }
        return null;

    }

    public static TimeRule aroundTimeInstant(int timeInstant, long startOffsetMillis, long stopOffsetMillis){
        TimeRule timeRule = new TimeRule();
        timeRule.timeInstant = timeInstant;
        timeRule.startOffsetMillis = startOffsetMillis;
        timeRule.stopOffsetMillis = stopOffsetMillis;
        timeRule.method = TimeMethod.AROUND_TIME_INSTANT;
        return timeRule;
    }

    public static TimeRule inDailyInterval(TimeZone timeZone, long startTimeOfDayMillis, long stopTimeOfDayMillis){
        TimeRule timeRule =  new TimeRule();
        timeRule.timeZone = timeZone;
        timeRule.startTimeOfDayMillis = startTimeOfDayMillis;
        timeRule.stopTimeOfDayMillis = stopTimeOfDayMillis;
        timeRule.method = TimeMethod.IN_DAILY_INTERVAL;
        return timeRule;
    }

    public static TimeRule inInterval(long startTimeMillis, long stopTimeMillis){
        TimeRule timeRule =  new TimeRule();
        timeRule.startTimeMillis = startTimeMillis;
        timeRule.stopTimeMillis = stopTimeMillis;
        timeRule.method = TimeMethod.IN_INTERVAL;
        return timeRule;
    }

    public static TimeRule inIntervalOfDay(int dayOfWeek, TimeZone timeZone, long startTimeOfDayMillis, long stopTimeOfDayMillis){
        TimeRule timeRule =  new TimeRule();
        timeRule.dayOfWeek = dayOfWeek;
        timeRule.timeZone = timeZone;
        timeRule.startTimeOfDayMillis = startTimeOfDayMillis;
        timeRule.stopTimeOfDayMillis = stopTimeOfDayMillis;
        timeRule.method = TimeMethod.IN_INTERVAL_OF_DAY;
        return timeRule;
    }


    public static TimeRule inTimeInterval(int timeInterval){
        TimeRule timeRule =  new TimeRule();
        timeRule.timeInterval = timeInterval;
        timeRule.method = TimeMethod.IN_TIME_INTERVAL;
        return timeRule;
    }

    @Override
    public String toString() {
        JsonObject rule = new JsonObject();
        rule.addProperty("type", "TimeIntervals");
        rule.addProperty("method", String.valueOf(method));
        if (timeZone != null) {
            rule.addProperty("timeZone", timeZone.getID());
        } else {
            rule.addProperty("timeZone", TimeZone.getDefault().getID());
        }

        rule.addProperty("dayOfWeek", dayOfWeek);
        rule.addProperty("timeInterval", timeInterval);
        rule.addProperty("timeInstant", timeInstant);
        rule.addProperty("startOffsetMillis", startOffsetMillis);
        rule.addProperty("stopOffsetMillis", stopOffsetMillis);
        rule.addProperty("startTimeOfDayMillis", startTimeOfDayMillis);
        rule.addProperty("stopTimeOfDayMillis", stopTimeOfDayMillis);
        rule.addProperty("startTimeMillis", startTimeMillis);
        rule.addProperty("stopTimeMillis", stopTimeMillis);
        return rule.toString();
    }
}
