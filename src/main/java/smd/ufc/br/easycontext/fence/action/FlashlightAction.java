package smd.ufc.br.easycontext.fence.action;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import smd.ufc.br.easycontext.fence.FenceAction;

public class FlashlightAction implements FenceAction {
    private static final String TAG = "FlashlightAction";
    @Override
    public void doOperation(Context context, FenceState state, Bundle data) {
        //check if has permission to use camera
        //check if LED exists
        boolean ledExists = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(ledExists){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                try {
                    assert cameraManager != null;
                    String cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, true);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            } else {
                //old way
                Log.e(TAG, "doOperation: Device does not have a flashlight.");

            }
        } else {
            Log.e(TAG, "doOperation: Device does not have a flashlight.");
        }

    }
}
