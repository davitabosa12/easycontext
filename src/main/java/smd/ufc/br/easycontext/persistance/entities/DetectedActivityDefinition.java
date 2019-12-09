package smd.ufc.br.easycontext.persistance.entities;
import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.math.FloatStatistics;

public class DetectedActivityDefinition implements ContextDefinition {

    //constants
    
    public static final int IN_VEHICLE = 0;
    
    public static final int ON_BICYCLE = 1;
    
    public static final int ON_FOOT = 2;
    
    public static final int STILL = 3;
    
    public static final int UNKNOWN = 4;
    
    public static final int TILTING = 5;
    
    public static final int WALKING = 7;
    
    public static final int RUNNING = 8;
    
    public static final int ANY = 9;



    private int uid;

    private int[] activityTypes;



    public DetectedActivityDefinition(int[] activityTypes) {
        this.activityTypes = activityTypes;
    }

    public DetectedActivityDefinition() {
        activityTypes = new int[0];
    }

    //GETTERS SETTERS
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int[] getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(int[] activityTypes) {
        this.activityTypes = activityTypes;
    }

    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        //
        // DetectedActivity other = currentContext.getMostProbableActivity();
        List<DetectedActivity> currentActivities = currentContext.getDetectedActivities();


        //if there are no current context..
        if(currentActivities == null)
            return 0;


        FloatStatistics statistics = new FloatStatistics();
        //for each defined context
        for(int activityType : activityTypes){
            //if defined context is ANY
            if(activityType == ANY){
                //push standard value 0.5
                statistics.accept(0.5f);
                continue;
            }
            for(DetectedActivity da : currentActivities){
                //if defined values match
                if(da.getType() == activityType){
                    //push confidence value to calculator
                    //confidence value domain is 0 - 100, resizing to 0 - 1
                    statistics.accept(da.getConfidence() / 100.0f);
                }
            }
        }
        //returns the average sum of confidences
        return statistics.getAverage();
    }


    public void addActivityTypes(int i) {
        int[] list = Arrays.copyOf(activityTypes, activityTypes.length + 1);
        list[list.length - 1] = i;
        activityTypes = list;

    }

    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }
}
