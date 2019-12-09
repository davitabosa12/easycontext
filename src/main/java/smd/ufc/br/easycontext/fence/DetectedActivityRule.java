package smd.ufc.br.easycontext.fence;

import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import smd.ufc.br.easycontext.fence.method.DAMethod;

/**
 * Created by davitabosa on 13/08/2018.
 */

public class DetectedActivityRule implements Rule {
    private List<Integer> activityTypes;
    private DAMethod method;

    public static int IN_VEHICLE = com.google.android.gms.awareness.fence.DetectedActivityFence.IN_VEHICLE;
    public static int RUNNING = com.google.android.gms.awareness.fence.DetectedActivityFence.RUNNING;

    public static int ON_FOOT = com.google.android.gms.awareness.fence.DetectedActivityFence.ON_FOOT;

    public static int ON_BICYCLE = com.google.android.gms.awareness.fence.DetectedActivityFence.ON_BICYCLE;

    public static int STILL = com.google.android.gms.awareness.fence.DetectedActivityFence.STILL;

    public static int WALKING = com.google.android.gms.awareness.fence.DetectedActivityFence.WALKING;

    public static int UNKNOWN = com.google.android.gms.awareness.fence.DetectedActivityFence.UNKNOWN;

    public DetectedActivityRule() {
        activityTypes = new ArrayList<>();
    }

    public void addActivityType(int activityType){
        activityTypes.add(activityType);
    }

    public static DetectedActivityRule during(int ...activityTypes){
        DetectedActivityRule rule = new DetectedActivityRule();
        rule.method = DAMethod.DA_DURING;
        for(int i = 0; i < activityTypes.length; i++){
            rule.addActivityType(activityTypes[i]);
        }
        return rule;
    }
    public static DetectedActivityRule starting(int ...activityTypes){
        DetectedActivityRule rule = new DetectedActivityRule();
        rule.method = DAMethod.DA_STARTING;
        for(int i = 0; i < activityTypes.length; i++){
            rule.addActivityType(activityTypes[i]);
        }
        return rule;
    }
    public static DetectedActivityRule stopping(int ...activityTypes){
        DetectedActivityRule rule = new DetectedActivityRule();
        rule.method = DAMethod.DA_STOPPING;
        for(int i = 0; i < activityTypes.length; i++){
            rule.addActivityType(activityTypes[i]);
        }
        return rule;
    }

    public static DetectedActivityRule during(List<Integer> activityTypes){
        DetectedActivityRule rule = new DetectedActivityRule();
        rule.method = DAMethod.DA_DURING;
        for(int i = 0; i < activityTypes.size(); i++){
            rule.addActivityType(activityTypes.get(i));
        }
        return rule;
    }
    public static DetectedActivityRule starting(List<Integer> activityTypes){
        DetectedActivityRule rule = new DetectedActivityRule();
        rule.method = DAMethod.DA_STARTING;
        for(int i = 0; i < activityTypes.size(); i++){
            rule.addActivityType(activityTypes.get(i));
        }
        return rule;
    }
    public static DetectedActivityRule stopping(List<Integer> activityTypes){
        DetectedActivityRule rule = new DetectedActivityRule();
        rule.method = DAMethod.DA_STOPPING;
        for(int i = 0; i < activityTypes.size(); i++){
            rule.addActivityType(activityTypes.get(i));
        }
        return rule;
    }

    @Override
    public AwarenessFence getAwarenessFence() {
        int size = activityTypes.size();
        int[] types = new int[size];
        for (int i = 0; i < size; i++) {
            types[i] = activityTypes.get(i);
        }
        switch(method){
            case DA_STOPPING:
                return com.google.android.gms.awareness.fence.DetectedActivityFence.stopping(types);
            case DA_STARTING:
                return com.google.android.gms.awareness.fence.DetectedActivityFence.starting(types);
            case DA_DURING:
                return com.google.android.gms.awareness.fence.DetectedActivityFence.during(types);
            default:
                return null;
        }
    }


    @Override
    public String toString() {
        JsonObject rule = new JsonObject();
        rule.addProperty("type", "DetectedActivity");
        rule.addProperty("method", String.valueOf(method));
        JsonArray activityTypes = new JsonArray();
        for(Integer type : this.activityTypes){
            activityTypes.add(type);
        }
        rule.add("activityTypes", activityTypes);
        return rule.toString();
    }
}
