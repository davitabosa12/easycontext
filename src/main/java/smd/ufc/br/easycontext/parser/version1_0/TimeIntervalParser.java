package smd.ufc.br.easycontext.parser.version1_0;

import android.util.Log;

import com.google.android.gms.awareness.fence.TimeFence;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.sql.Time;
import java.util.TimeZone;

import smd.ufc.br.easycontext.fence.TimeRule;

import static smd.ufc.br.easycontext.parser.version1_0.JSONParser1_0.TAG;

public class TimeIntervalParser {

    public TimeRule parseTimeIntervalRule(JsonReader jsonReader) throws IOException {

        jsonReader.nextName(); // property 'method'
        String method = jsonReader.nextString();
        if (method.equals(TimeRule.AROUND_TIME_INSTANT)) {
            int timeInstant;
            long startOffsetMillis;
            long stopOffsetMillis;
            jsonReader.nextName(); //property 'timeInstant'
            String strTimeInstant = jsonReader.nextString();
            if (strTimeInstant.equals("TIME_INSTANT_SUNRISE")) {
                timeInstant = TimeFence.TIME_INSTANT_SUNRISE;
            } else {
                timeInstant = TimeFence.TIME_INSTANT_SUNSET;
            }
            jsonReader.nextName(); // property 'startOffsetMillis'
            startOffsetMillis = jsonReader.nextLong();
            jsonReader.nextName(); // property 'stopOffsetMillis'
            stopOffsetMillis = jsonReader.nextLong();
            return TimeRule.aroundTimeInstant(timeInstant, startOffsetMillis, stopOffsetMillis);

        } else if (method.equals(TimeRule.IN_DAILY_INTERVAL)) {
            TimeZone timeZone;
            long startTimeOfDayMillis;
            long stopTimeOfDayMillis;
            jsonReader.nextName(); // property 'timeZone'
            String strTimeZone = jsonReader.nextString();
            if (strTimeZone.equals("Device")) {
                timeZone = null;
            } else {
                timeZone = TimeZone.getTimeZone(strTimeZone);
            }

            jsonReader.nextName(); // property 'startTimeOfDayMillis'
            startTimeOfDayMillis = jsonReader.nextLong();
            jsonReader.nextName(); // property 'stopTimeOfDayMillis'
            stopTimeOfDayMillis = jsonReader.nextLong();
            return TimeRule.inDailyInterval(timeZone, startTimeOfDayMillis, stopTimeOfDayMillis);
        } else if (method.equals(TimeRule.IN_INTERVAL)) {
            long startTimeMillis;
            long stopTimeMillis;
            jsonReader.nextName(); //property 'startTimeMillis'
            startTimeMillis = jsonReader.nextLong();
            jsonReader.nextName(); //property 'stopTimeMillis'
            stopTimeMillis = jsonReader.nextLong();
            return TimeRule.inInterval(startTimeMillis, stopTimeMillis);
        } else if (method.equals(TimeRule.IN_INTERVAL_OF_DAY)) {
            int dayOfWeek;
            TimeZone timeZone;
            long startTimeOfDayMillis;
            long stopTimeOfDayMillis;
            jsonReader.nextName(); // property 'dayOfWeek'
            dayOfWeek = jsonReader.nextInt();
            jsonReader.nextName(); // property 'timeZone'
            String strTimeZone = jsonReader.nextString();
            if (strTimeZone.equals("Device")) {
                timeZone = null;
            } else {
                timeZone = TimeZone.getTimeZone(strTimeZone);
            }
            jsonReader.nextName(); // property 'startTimeOfDayMillis'
            startTimeOfDayMillis = jsonReader.nextLong();
            jsonReader.nextName(); // property 'stopTimeOfDayMillis'
            stopTimeOfDayMillis = jsonReader.nextLong();

            return TimeRule.inIntervalOfDay(dayOfWeek, timeZone, startTimeOfDayMillis, stopTimeOfDayMillis);

        } else if (method.equals(TimeRule.IN_TIME_INTERVAL)) {
            int timeInterval = 0;
            String strTimeInterval;
            jsonReader.nextName(); // property 'timeInterval'
            strTimeInterval = jsonReader.nextString();
            if (strTimeInterval.equals(TimeRule.MORNING)) {
                timeInterval = TimeFence.TIME_INTERVAL_MORNING;
            } else if (strTimeInterval.equals(TimeRule.AFTERNOON)) {
                timeInterval = TimeFence.TIME_INTERVAL_AFTERNOON;
            } else if (strTimeInterval.equals(TimeRule.EVENING)) {
                timeInterval = TimeFence.TIME_INTERVAL_EVENING;
            } else if (strTimeInterval.equals(TimeRule.NIGHT)) {
                timeInterval = TimeFence.TIME_INTERVAL_NIGHT;
            } else if (strTimeInterval.equals(TimeRule.WEEKDAY)) {
                timeInterval = TimeFence.TIME_INTERVAL_WEEKDAY;
            } else if (strTimeInterval.equals(TimeRule.WEEKEND)) {
                timeInterval = TimeFence.TIME_INTERVAL_WEEKEND;
            } else if (strTimeInterval.equals(TimeRule.HOLIDAY)) {
                timeInterval = TimeFence.TIME_INTERVAL_HOLIDAY;
            }
            return TimeRule.inTimeInterval(timeInterval);
        }
        else {
            //error
            Log.e(TAG, "parseTimeIntervalRule: No method found.");
            return null;
        }
    }
}
