package smd.ufc.br.easycontext;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Snapshot {

    public static final int WEATHER = 978;
    public static final int TIME_INTERVAL = 111;
    public static final int LOCATION = 688;
    public static final int PLACE_LIKELIHOOD = 668;
    public static final int DETECTED_ACTIVITY = 452;
    public static final int[] ALL_PROVIDERS = {WEATHER, TIME_INTERVAL, LOCATION, PLACE_LIKELIHOOD, DETECTED_ACTIVITY};
    private static final String TAG = "Snapshot";
    private SnapshotClient client;
    private static Snapshot instance;
    private CurrentContext currentContext;
    private OnContextUpdate callback;
    private Context context;

    /** RUNNABLES **/
    private Runnable weatherUpdater = new Runnable() {
        @Override
        public void run() {
            updateWeather();
        }
    };

    private Runnable timeIntervalsUpdater = new Runnable() {
        @Override
        public void run() {
            updateTimeIntervals();
        }
    };

    private Runnable activityUpdater = new Runnable() {
        @Override
        public void run() {
            updateDetectedActivity();
        }
    };



    private Snapshot(Context context){
        this.context = context;
        this.client = Awareness.getSnapshotClient(context);
        this.currentContext = new CurrentContext();

    }

    private Snapshot(Activity activity){
        this.client = Awareness.getSnapshotClient(activity);
    }

    public static Snapshot getInstance(Context context){
        if(instance == null){
            instance = new Snapshot(context);
        }
        return instance;
    }

    public CurrentContext getCurrentContext(){
        return currentContext;
    }
    /**************************************************************************************************/
    public void setCallback(OnContextUpdate callback){

        this.callback = callback;
    }
    private Task<WeatherResponse> updateWeather() {
        @SuppressLint("MissingPermission")
        Task<WeatherResponse> weatherTask = client.getWeather();
        return weatherTask;
       
    }

    /**************************************************************************************************/
    private Task<TimeIntervalsResponse> updateTimeIntervals() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            throw new SecurityException("Missing ACCESS_FINE_LOCATION permission");
        }
        Task<TimeIntervalsResponse> timeTask = client.getTimeIntervals();
        return timeTask;
    }
    /**************************************************************************************************/
    private Task<DetectedActivityResponse> updateDetectedActivity() {
        Task<DetectedActivityResponse> detectedActivityTask = client.getDetectedActivity();
        return detectedActivityTask;
    }

    /**
     * Tries to update context with a timeout of 10 seconds
     * @param providers context-aware providers to update
     * @throws InterruptedException if times out
     */
    public void updateContext(int ...providers){
        final List< Task<?> > tasks = new ArrayList<>();
        for(int p : providers){
            switch (p){
                case WEATHER:
                    Task weatherTask = updateWeather();
                    tasks.add(weatherTask);
                    break;
                case TIME_INTERVAL:
                    Task timeIntervalTask = updateTimeIntervals();
                    tasks.add(timeIntervalTask);
                    break;
                case LOCATION:

                    break;
                case DETECTED_ACTIVITY:
                    Task daTask = updateDetectedActivity();

                    tasks.add(daTask);
                    break;
                case PLACE_LIKELIHOOD:
                    break;
            }
        }

        //wait for all task to finish.
        Collection< Task<?> > c = tasks;
        Tasks.whenAllComplete(c).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Task is successful");
                } else{

                    Log.e(TAG, "onComplete: task not successful", task.getException());
                }
                for (Task t :
                        task.getResult()) {
                    if(!t.isSuccessful()){
                        Log.e(TAG, "onComplete: task failed!", t.getException());
                        continue;
                    }
                    if(t.getResult() instanceof WeatherResponse){
                        WeatherResponse response = (WeatherResponse) t.getResult();
                        currentContext.setWeather(response.getWeather());
                    } else if(t.getResult() instanceof TimeIntervalsResponse){
                        TimeIntervalsResponse response = (TimeIntervalsResponse) t.getResult();
                        currentContext.setTimeIntervals(response.getTimeIntervals());
                    } else if(t.getResult() instanceof LocationResponse){
                        LocationResponse response = (LocationResponse) t.getResult();
                        currentContext.setLocation(response.getLocation());
                    } else if(t.getResult() instanceof DetectedActivityResponse){
                        DetectedActivityResponse response = (DetectedActivityResponse) t.getResult();
                        currentContext.setMostProbableActivity(response.getActivityRecognitionResult().getMostProbableActivity());
                        currentContext.setDetectedActivities(response.getActivityRecognitionResult().getProbableActivities());
                    }
                }
                checkForInconsistencies();
                callback.onContextUpdate(currentContext);
            }


        });

        if (callback == null) {
            Log.d(TAG, "updateContext: CALLBACK NOT SET! CONTEXT WILL NOT UPDATE!");
        }
        if (currentContext != null) {

            //callback.onContextUpdate(currentContext);
        }

    }

    private void checkForInconsistencies() {

    }

    /**************************************************************************************************/
    public interface OnContextUpdate{
        void onContextUpdate(CurrentContext currentContext);
    }
}
