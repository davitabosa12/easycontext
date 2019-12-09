package smd.ufc.br.easycontext.fence;

import android.os.Parcelable;

import com.google.android.gms.awareness.fence.AwarenessFence;

import java.io.Serializable;

public interface Rule extends Serializable {
    abstract AwarenessFence getAwarenessFence();


}
