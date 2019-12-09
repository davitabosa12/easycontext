package smd.ufc.br.easycontext.parser.version1_1;

import android.util.Log;

import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import smd.ufc.br.easycontext.fence.DetectedActivityRule;

import static smd.ufc.br.easycontext.parser.version1_0.JSONParser1_0.TAG;

public class DetectedActivityParser {

    public DetectedActivityRule parseDetectedActivityRule(JsonReader jsonReader) throws IOException {
        jsonReader.nextName(); //property 'method'
        String method = jsonReader.nextString();
        DetectedActivityRule rule = null;
        jsonReader.nextName(); //property 'activityTypes', an array
        List<Integer> activityTypes = new ArrayList<>();
        jsonReader.beginArray();
        while(jsonReader.hasNext()){
            String daMethod = jsonReader.nextString();
            if(daMethod.equals(DAMethods.IN_VEHICLE)){
                activityTypes.add(DetectedActivityFence.IN_VEHICLE);
            } else if(daMethod.equals(DAMethods.ON_BICYCLE)){
                activityTypes.add(DetectedActivityFence.ON_BICYCLE);
            } else if(daMethod.equals(DAMethods.ON_FOOT)){
                activityTypes.add(DetectedActivityFence.ON_FOOT);
            } else if(daMethod.equals(DAMethods.RUNNING)){
                activityTypes.add(DetectedActivityFence.RUNNING);
            } else if(daMethod.equals(DAMethods.STILL)){
                activityTypes.add(DetectedActivityFence.STILL);
            } else if(daMethod.equals(DAMethods.TILTING)){
                activityTypes.add(DetectedActivityFence.STILL);
            } else if(daMethod.equals(DAMethods.UNKNOWN)){
                activityTypes.add(DetectedActivityFence.UNKNOWN);
            } else if(daMethod.equals(DAMethods.WALKING)){
                activityTypes.add(DetectedActivityFence.WALKING);
            }
        }
        jsonReader.endArray();
        if(method.equals(DAMethods.STARTING)){
            rule = DetectedActivityRule.during(activityTypes);
        } else if(method.equals(DAMethods.STOPPING)){
            rule = DetectedActivityRule.starting(activityTypes);
        } else if(method.equals(DAMethods.DURING)){
            rule = DetectedActivityRule.stopping(activityTypes);
        } else{
            Log.e(TAG, "parseDetectedActivityRule: ERROR - UNKNOWN METHOD");
        }
        return rule;
    }

    static class DAMethods {
        public static final String STARTING = "DA.STARTING";
        public static final String STOPPING = "DA.STOPPING";
        public static final String DURING = "DA.DURING";
        public static final String IN_VEHICLE =  "DA.IN_VEHICLE";
        public static final String ON_BICYCLE = "DA.ON_BICYCLE";
        public static final String ON_FOOT = "DA.ON_FOOT";
        public static final String RUNNING = "DA.RUNNING";
        public static final String STILL = "DA.STILL";
        public static final String TILTING = "DA.TILTING";
        public static final String UNKNOWN = "DA.UNKNOWN";
        public static final String WALKING = "DA.WALKING";
    }
}
