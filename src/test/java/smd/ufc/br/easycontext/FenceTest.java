package smd.ufc.br.easycontext;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import org.junit.Test;

import smd.ufc.br.easycontext.fence.Fence;
import smd.ufc.br.easycontext.fence.FenceAction;
import smd.ufc.br.easycontext.fence.TimeRule;

import static android.content.ContentValues.TAG;

public class FenceTest {

    @Test
    public void test(){
        Fence fence = new Fence("fence_name", TimeRule.inTimeInterval(1), new FenceAction() {
            @Override
            public void doOperation(Context context, FenceState state, Bundle data) {
                System.out.println("o.o");
            }
        });
        System.out.println(fence.serialize());
        assert true;
    }
}
