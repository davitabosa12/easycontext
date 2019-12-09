package smd.ufc.br.easycontext.fence;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.awareness.fence.FenceState;

import java.io.Serializable;

public interface FenceAction extends Serializable {

	public abstract void doOperation(Context context, FenceState state, Bundle data);
}
