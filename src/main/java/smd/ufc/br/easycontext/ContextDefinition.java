package smd.ufc.br.easycontext;

import android.util.Log;

import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.io.Serializable;

/**
 * ContextDefinition
 */
public interface ContextDefinition extends Serializable {



    /**Compares this ContextDefinition to the current context and returns a value between 0 and 1,
     * indicating how similar this ContextDefinition is to the current context.
     *
     * @param currentContext The current context obtained via Snapshot
     *
     * @return a value between 0 and 1. 0 when they are different, 1 when they are identical.
     */

    public abstract float calculateConfidence(CurrentContext currentContext);

    public static class Maths{

        private static final String TAG = "Maths";

        /**
         * Clamps some float to a ceiling
         * @param value the value to clamp
         * @param ceil the maximum value
         * @return ceil if value > ceil. Otherwise simply returns value.
         */
        public static float clamp(float value, float ceil){
            Float f = value;
            if(f.isNaN() || f.isInfinite()){
                return ceil;
            }
            if(value > ceil)
                return ceil;
            else
                return value;
        }

        /**
         * Normalizes a number from another range into a value between 0 and 1.
         * <b>Values outside of this range are clamped accordingly</b>
         * @param value the value to be remapped
         * @param rangeMin minimum value of the range
         * @param rangeMax maximum value of the range
         * @return value remapped to 0 - 1 range.
         */
        public static float normalize(float value, float rangeMin, float rangeMax){
            return map(value, rangeMin, rangeMax, 0, 1);
        }

        public static float map(float value, float range1Min, float range1Max, float range2Min, float range2Max){
            float newValue = range2Min + (range2Max - range2Min) * ((value - range1Min) / (range1Max - range1Min));

            if(newValue == Float.NaN){
                Log.w(TAG, "map: the result is not a number!", new IllegalArgumentException());
            }else{
                if(Float.isInfinite(newValue)){
                    Log.w(TAG, "map: the result is infinite!", new IllegalArgumentException());
                }
            }
            return newValue;
        }
    }
}
