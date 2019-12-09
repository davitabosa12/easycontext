package smd.ufc.br.easycontext.fence;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.FenceClient;
import com.google.android.gms.awareness.fence.FenceQueryRequest;
import com.google.android.gms.awareness.fence.FenceQueryResponse;
import com.google.android.gms.awareness.fence.FenceQueryResult;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import smd.ufc.br.easycontext.GeneralReceiver;

/**
 * Created by davitabosa on 08/08/2018.
 */

public class FenceManager {
    private static FenceManager instance;
    private FenceClient client;
    private Context context;


    private FenceManager(Context ctx){
        this.context = ctx;
        this.client = Awareness.getFenceClient(context);

    }

    public static FenceManager getInstance(Context ctx){
        if(instance == null){
            instance = new FenceManager(ctx);
        }
        return instance;
    }

    public Task registerFence(Fence fence, @Nullable Bundle extras){
        Intent i = new Intent(context, GeneralReceiver.class);
        String actionName = fence.getAction().getClass().getName();
        i.putExtra("actionName", actionName);
        i.putExtra("user_provided", extras);
        //pending intent to trigger GeneralReceiver
        PendingIntent pi = PendingIntent.getBroadcast(context, new Random().nextInt(), i , PendingIntent.FLAG_CANCEL_CURRENT);
        //register info to Awareness API
        return client.updateFences(new FenceUpdateRequest.Builder().addFence(fence.getName(), fence.getRule().getAwarenessFence(), pi).build());
    }


    @Nullable
    public Task unregisterFence(final Fence fence){
            return unregisterFence(fence.getName());
    }

    public Task<Void> unregisterFence(final String fenceString){
        final Task<Void> t = client.updateFences(new FenceUpdateRequest.Builder().removeFence(fenceString).build());

        t.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void o) {
                Log.d("AwarenessLib", "Fence removal successful: " + fenceString);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AwarenessLib", "Fence removal failed for " + fenceString + ": " + t.getException().getMessage());
            }
        });
        return t;
    }
    public  Task<FenceQueryResponse> getRegisteredFences(){
        Task<FenceQueryResponse> task = client.queryFences(FenceQueryRequest.all());
        return task;
    }

    public void unregisterAll(){
        Task<FenceQueryResponse> t =  getRegisteredFences();
        t.addOnSuccessListener(new OnSuccessListener<FenceQueryResponse>() {
            @Override
            public void onSuccess(FenceQueryResponse fenceQueryResponse) {
                Set<String> keys = fenceQueryResponse.getFenceStateMap().getFenceKeys();
                for (final String key : keys){
                    final Task<Void> t = unregisterFence(key);
                    t.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("AwarenessLib", "Error removing fence " + key + ". " + t.getException().getMessage());
                        }
                    });
                }
            }
        });
    }
}
