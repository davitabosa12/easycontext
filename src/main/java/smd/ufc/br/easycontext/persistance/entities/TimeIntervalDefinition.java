package smd.ufc.br.easycontext.persistance.entities;

import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.gson.Gson;


import java.util.Arrays;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.math.FloatStatistics;

public class TimeIntervalDefinition implements TimeIntervals, ContextDefinition  {


    //constants
    public static final int TIME_INTERVAL_WEEKDAY = 1;
    public static final int TIME_INTERVAL_WEEKEND = 2;
    public static final int TIME_INTERVAL_HOLIDAY = 3;
    public static final int TIME_INTERVAL_MORNING = 4;
    public static final int TIME_INTERVAL_AFTERNOON = 5;
    public static final int TIME_INTERVAL_EVENING = 6;
    public static final int TIME_INTERVAL_NIGHT = 7;
    public static final int TIME_INTERVAL_ANY = 8;

    private int uid;

    private int[] timeIntervals;


    public TimeIntervalDefinition(int[] timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    /**
     * Default ctor with ANY value
     */
    public TimeIntervalDefinition() {

        this.timeIntervals = new int[0];
    }


    @Override
    public int[] getTimeIntervals() {

        return timeIntervals;
    }

    public TimeIntervalDefinition addTimeInterval(int interval){
        this.timeIntervals = Arrays.copyOf(timeIntervals, timeIntervals.length + 1);
        timeIntervals[timeIntervals.length - 1] = interval;
        return this;
    }

    @Override
    public boolean hasTimeInterval(int i) {
        for (int t: timeIntervals) {
            if(t == i)
                return true;
        }
        return false;
    }

    //GETTERS SETTERS


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setTimeIntervals(int[] timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        if(currentContext == null){
            return 0.0f;
        }
        TimeIntervals currentContextTimeIntervals = currentContext.getTimeIntervals();

        if(currentContextTimeIntervals == null)
            return 0;
        FloatStatistics statistics = new FloatStatistics();


        for(int t : currentContextTimeIntervals.getTimeIntervals()){
            if(this.hasTimeInterval(t)){
                //time intervals match
                statistics.accept(1.0f);
            } else {
                //time intervals dont match
                statistics.accept(0.0f);
            }
        }
        //if defined context doesnt match with current context AND user said that ANY time interval is ok..
        if(statistics.getSum() == 0 && this.hasTimeInterval(TIME_INTERVAL_ANY)){
            //return default value 0.5f
            return 0.5f;
        }
        //else..
        return statistics.getAverage();

    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
